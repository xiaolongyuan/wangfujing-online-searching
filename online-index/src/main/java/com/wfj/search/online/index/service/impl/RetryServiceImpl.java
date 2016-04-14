package com.wfj.search.online.index.service.impl;

import com.google.common.collect.Lists;
import com.wfj.platform.util.concurrent.BatchRunnables;
import com.wfj.search.online.common.pojo.CommentPojo;
import com.wfj.search.online.common.pojo.ItemPojo;
import com.wfj.search.online.common.pojo.OnlineRetryNotePojo;
import com.wfj.search.online.index.iao.ICommentRequester;
import com.wfj.search.online.index.iao.IPcmRequester;
import com.wfj.search.online.index.iao.RequestException;
import com.wfj.search.online.index.mapper.RetryNoteMapper;
import com.wfj.search.online.index.pojo.RetryResultCollector;
import com.wfj.search.online.index.pojo.failure.DataType;
import com.wfj.search.online.index.pojo.failure.Failure;
import com.wfj.search.online.index.pojo.failure.FailureType;
import com.wfj.search.online.index.pojo.failure.NoFailure;
import com.wfj.search.online.index.service.*;
import com.wfj.search.online.index.util.ExecutorServiceFactory;
import com.wfj.search.online.index.util.PojoUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.wfj.search.online.common.pojo.OnlineRetryNotePojo.*;
import static com.wfj.search.online.index.pojo.RetryResultCollector.*;

/**
 * <br/>create at 15-11-2
 *
 * @author liuxh
 * @since 1.0.0
 */
@Component("retryService")
public class RetryServiceImpl implements IRetryService {
    private static final Logger logger = LoggerFactory.getLogger(RetryServiceImpl.class);
    @Autowired
    private RetryNoteMapper retryNoteMapper;
    @Autowired
    private IEsService esService;
    @Autowired
    private IIndexService indexService;
    @Autowired
    private ICommentEsService commentEsService;
    @Autowired
    private ICommentIndexService commentIndexService;
    @Autowired
    private IPcmRequester pcmRequester;
    @Autowired
    private ICommentRequester commentRequester;
    @Autowired
    private IIndexConfigService indexConfigService;

    @Override
    public Optional<RetryResultCollector<Failure>> retrySaveItem(long version) {
        List<OnlineRetryNotePojo> retryList = Lists.newArrayList();
        retryList.addAll(retryNoteMapper.listUnresolvedNote(Step.request, Type.item, Action.save));
        retryList.addAll(retryNoteMapper.listUnresolvedNote(Step.es, Type.item, Action.save));
        if (retryList.isEmpty()) {
            return Optional.empty();
        }
        RetryResultCollector<Failure> collector = new RetryResultCollector<>();
        int itemCount = retryList.size();
        collector.setTotal(itemCount);
        int threadCount = indexConfigService.getFetchThreads() * Runtime.getRuntime().availableProcessors();
        if (itemCount < threadCount) {
            threadCount = itemCount;
        }
        final AtomicReference<Throwable> tracker = new AtomicReference<>();
        ExecutorService pool = ExecutorServiceFactory.create("retry-save-item-thread", threadCount,
                Thread.currentThread(), tracker);
        BatchRunnables batchRunnables = new BatchRunnables(pool);
        for (OnlineRetryNotePojo retryNote : retryList) {
            batchRunnables.addRunnable(() -> {
                String itemId = retryNote.getCode();
                logger.info("重试将专柜商品编码[itemId:{}]的数据保存到ES中。。。", itemId);
                try {
                    // 使用单个item重试，可以最大程度保证每个重试单位能够重试，不会因为列表中某一单位出错，使其他单位丧失重试机会
                    Optional<ItemPojo> itemOptional = pcmRequester.getItemsByItemId(itemId);
                    if (!itemOptional.isPresent()) {
                        // pcm请求无效数据
                        collector.getRequestErrorMap().get(RequestError.invalidData).add(new Failure(itemId));
                        this.cleanRetryNode(itemId, "无效数据：无法从PCM查询商品[itemId:" + itemId + "]的数据",
                                Type.item);
                        return;
                    }
                    itemOptional.ifPresent(item -> {
                        logger.debug("需要重试的专柜商品信息:{}", item);
                        // 保存到ES中
                        Optional<Failure> failureOptional = this.esService.buildItem(item, version);
                        failureOptional
                                .ifPresent(failure -> {
                                    logger.error(failure.getMessage(), failure.getThrowable());
                                    collector.getEsErrorMap().get(ESError.save).add(failure);// 保存错误
                                });
                        if (!failureOptional.isPresent()) {
                            // 写ES成功后，尽快返回成功标记，修改数据库。将重建索引任务交到重建索引方法中。
                            if (this.rewriteRetryNote(itemId, false, Step.index, Type.item, Action.save,
                                    retryNote.getVersion()) > 0) {
                                collector.getSuccessList().add(new NoFailure(itemId));
                            } else {
                                collector.getOverdueList().add(new Failure(itemId));
                            }
                        }
                    });
                } catch (RequestException e) {
                    logger.error("重试将专柜商品编码itemCode[{}]的数据保存到ES中失败：从pcm获取数据失败！", itemId, e);
                    collector.getRequestErrorMap().get(RequestError.requestError).add(new Failure(DataType.item,
                            FailureType.requestError, itemId));
                    // 根据itemCode获取item时，从pcm获取数据可能出错，出错记录在重试表中。
                    this.updateUnresolvedRetryNote(itemId, Step.request, Type.item, Action.save);
                } catch (Exception e) {
                    logger.error("重试将专柜商品编码itemCode[{}]的数据保存到ES中失败：向ES写数据异常！", itemId, e);
                    collector.getFailList().add(new Failure(DataType.item, FailureType.save2ES, itemId, null, e));
                    // 根据itemCode获取item时，从pcm获取数据可能出错，出错记录在重试表中。
                    this.updateUnresolvedRetryNote(itemId, Step.es, Type.item, Action.save);
                }
            });
        }
        try {
            batchRunnables.execute();
        } catch (InterruptedException e) {
            String msg = "重建商品意外中断：失败编号：" + retryList.stream()
                    .map(retryNote -> {
                        String itemId = retryNote.getCode();
                        collector.getFailList().add(new Failure(DataType.item, FailureType.unknown, itemId,
                                null, tracker.get()));
                        return itemId;
                    })
                    .collect(Collectors.toSet());
            logger.error(msg, tracker.get());
        }
        return Optional.of(collector);
    }

    @Override
    public Optional<RetryResultCollector<String>> retryIndexItem(long version) {
        List<OnlineRetryNotePojo> retryList = retryNoteMapper.listUnresolvedNote(Step.index, Type.item, Action.save);
        if (retryList.size() > 0) {
            RetryResultCollector<String> collector = new RetryResultCollector<>();
            int itemCount = retryList.size();
            collector.setTotal(itemCount);
            int threadCount = indexConfigService.getFetchThreads() * Runtime.getRuntime().availableProcessors();
            if (itemCount < threadCount) {
                threadCount = itemCount;
            }
            ExecutorService threadPool = Executors.newFixedThreadPool(threadCount,
                    r -> {
                        Thread thread = new Thread(r);
                        thread.setDaemon(true);
                        return thread;
                    });
            CountDownLatch countDownLatch = new CountDownLatch(retryList.size());
            for (OnlineRetryNotePojo retryNote : retryList) {
                threadPool.execute(() -> {
                    String itemCode = retryNote.getCode();
                    logger.info("重试创建专柜商品编码itemCode[{}]的索引。。。", itemCode);
                    try {
                        // 分条重建专柜商品索引
                        Optional<Failure> failureOptional = indexService.indexItemsFromEs(
                                Lists.newArrayList(itemCode), version);
                        if (failureOptional.isPresent()) {
                            collector.getIndexErrorMap().get(IndexError.save).add(itemCode);
                        } else {
                            if (this.rewriteRetryNote(itemCode, true, Step.index, Type.item, Action.save,
                                    retryNote.getVersion()) > 0) {
                                collector.getSuccessList().add(itemCode);
                            } else {
                                collector.getOverdueList().add(itemCode);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("重试创建专柜商品编码itemCode[{}]的索引失败", itemCode, e);
                        collector.getFailList().add(itemCode);
                        // 在索引过程中失败，因为没有后续过程，不再重写失败记录
                        // this.updateUnresolvedRetryNote(itemCode, index, item);
                    }
                    countDownLatch.countDown();
                });
            }
            try {
                countDownLatch.await();
                threadPool.shutdown();
            } catch (InterruptedException ignored) {
            }
            return Optional.of(collector);
        }
        return Optional.empty();
    }

    @Override
    public Optional<RetryResultCollector<String>> retrySaveComment(long version) {
        List<OnlineRetryNotePojo> retryList = Lists.newArrayList();
        retryList.addAll(retryNoteMapper.listUnresolvedNote(Step.request, Type.comment, Action.save));
        retryList.addAll(retryNoteMapper.listUnresolvedNote(Step.es, Type.comment, Action.save));
        if (retryList.isEmpty()) {
            return Optional.empty();
        }
        RetryResultCollector<String> collector = new RetryResultCollector<>();
        int total = retryList.size();
        collector.setTotal(total);
        int threadCount = indexConfigService.getFetchThreads() * Runtime.getRuntime().availableProcessors();
        if (total < threadCount) {
            threadCount = total;
        }
        final AtomicReference<Throwable> tracker = new AtomicReference<>();
        ExecutorService pool = ExecutorServiceFactory.create("retry-save-comment-thread", threadCount,
                Thread.currentThread(), tracker);
        BatchRunnables batchRunnables = new BatchRunnables(pool);
        for (OnlineRetryNotePojo retryNote : retryList) {
            batchRunnables.addRunnable(() -> {
                String commentId = retryNote.getCode();
                logger.info("重试将评论[commentId:{}]的数据保存到ES中，开始。。。", commentId);
                try {
                    Optional<CommentPojo> commentOptional = commentRequester.getCommentById(commentId);
                    if (!commentOptional.isPresent()) {
                        // 无效数据：清理重试表
                        collector.getRequestErrorMap().get(RequestError.invalidData).add(commentId);
                        this.cleanRetryNode(commentId, "无效数据：无法从账户系统查询评论[commentId:" + commentId + "]的数据",
                                Type.comment);
                    }
                    // 此处使用了JDK1.8的API，写着写着感觉各种反模式
                    commentOptional.ifPresent(comment -> {
                        Optional<Failure> failureOptional = commentEsService
                                .updateComment(PojoUtils.toIndexPojo(comment, version));
                        failureOptional
                                .ifPresent(failure -> logger.error(failure.getMessage(), failure.getThrowable()));
                        if (failureOptional.isPresent()) {
                            collector.getEsErrorMap().get(ESError.save).add(commentId);
                            return;
                        }
                        if (this.addUnresolvedRetryNote(commentId, Step.index, Type.comment, Action.save) > 0) {
                            collector.getSuccessList().add(commentId);
                        } else {
                            collector.getOverdueList().add(commentId);
                        }
                    });
                } catch (RequestException e) {
                    logger.error("重试将评论[commentId:{}]的数据保存到ES中：从账户系统获取数据失败！", commentId, e);
                    collector.getRequestErrorMap().get(RequestError.requestError).add(commentId);
                    this.updateUnresolvedRetryNote(commentId, Step.request, Type.comment, Action.save);
                } catch (Exception e) {
                    logger.error("重试将评论[commentId:{}]的数据保存到ES中：向ES写数据异常！", commentId, e);
                    collector.getFailList().add(commentId);
                    this.updateUnresolvedRetryNote(commentId, Step.es, Type.comment, Action.save);
                }
            });
        }
        try {
            batchRunnables.execute();
        } catch (InterruptedException e) {
            String msg = "重建评论意外中断：失败编号：" + retryList.stream()
                    .map(retryNote -> {
                        String commentId = retryNote.getCode();
                        collector.getFailList().add(commentId);
                        return commentId;
                    })
                    .collect(Collectors.toSet());
            logger.error(msg, tracker.get());
        }
        return Optional.of(collector);
    }

    @Override
    public Optional<RetryResultCollector<String>> retryIndexComment(long version) {
        List<OnlineRetryNotePojo> retryList = retryNoteMapper.listUnresolvedNote(Step.index, Type.comment, Action.save);
        if (retryList.isEmpty()) {
            return Optional.empty();
        }
        RetryResultCollector<String> collector = new RetryResultCollector<>();
        int total = retryList.size();
        collector.setTotal(total);
        int threadCount = indexConfigService.getFetchThreads() * Runtime.getRuntime().availableProcessors();
        if (total < threadCount) {
            threadCount = total;
        }
        final AtomicReference<Throwable> tracker = new AtomicReference<>();
        ExecutorService pool = ExecutorServiceFactory.create("retry-index-comment-thread", threadCount,
                Thread.currentThread(), tracker);
        BatchRunnables batchRunnables = new BatchRunnables(pool);
        for (OnlineRetryNotePojo retryNote : retryList) {
            batchRunnables.addRunnable(() -> {
                String commentId = retryNote.getCode();
                logger.info("重建评论[commentId:{}]的索引，开始。。。", commentId);
                Optional<Failure> failureOptional = commentIndexService.saveFromEs(commentId, version);
                failureOptional.ifPresent(failure -> logger.error(failure.getMessage(), failure.getThrowable()));
                if (failureOptional.isPresent()) {
                    collector.getIndexErrorMap().get(IndexError.save).add(commentId);
                    return;
                }
                if (this.rewriteRetryNote(commentId, true, Step.index, Type.comment, Action.save,
                        retryNote.getVersion()) > 0) {
                    collector.getSuccessList().add(commentId);
                } else {
                    collector.getOverdueList().add(commentId);
                }
            });
        }
        try {
            batchRunnables.execute();
        } catch (InterruptedException e) {
            String msg = "重建评论意外中断：失败编号：" + retryList.stream()
                    .map(retryNote -> {
                        String commentId = retryNote.getCode();
                        collector.getFailList().add(commentId);
                        return commentId;
                    })
                    .collect(Collectors.toSet());
            logger.error(msg, tracker.get());
        }
        return Optional.of(collector);
    }

    @Override
    public int addUnresolvedRetryNote(String code, Step step, Type type, Action action) {
        if (StringUtils.isBlank(code)) {
            return -1;
        }
        OnlineRetryNotePojo retryNote = retryNoteMapper.getRetryNote(code);
        if (retryNote == null) {
            return retryNoteMapper.addRetryNote(code, step, type, action);
        } else {
            return retryNoteMapper.unresolvedRetryNote(code, step, type, action);
        }
    }

    @Override
    public int updateUnresolvedRetryNote(String code, Step step, Type type, Action action) {
        if (StringUtils.isBlank(code)) {
            return -1;
        }
        return retryNoteMapper.unresolvedRetryNote(code, step, type, action);
    }

    @Override
    public int rewriteRetryNote(String code, boolean resolved, Step step, Type type, Action action, long version) {
        if (StringUtils.isBlank(code)) {
            return -1;
        }
        return retryNoteMapper.rewriteRetryNote(code, resolved, step, type, action, version);
    }

    @Override
    public int cleanRetryNode(String code, String comment, Type type) {
        if (StringUtils.isBlank(code)) {
            return -1;
        }
        return retryNoteMapper.cleanRetryNote(code, comment, type);
    }
}
