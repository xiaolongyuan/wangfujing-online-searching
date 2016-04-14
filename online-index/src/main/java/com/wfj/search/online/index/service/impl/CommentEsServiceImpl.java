package com.wfj.search.online.index.service.impl;

import com.wfj.search.online.common.pojo.CommentPojo;
import com.wfj.search.online.index.es.CommentEsIao;
import com.wfj.search.online.index.iao.ICommentRequester;
import com.wfj.search.online.index.iao.RequestException;
import com.wfj.search.online.index.pojo.CommentIndexPojo;
import com.wfj.search.online.index.pojo.failure.DataType;
import com.wfj.search.online.index.pojo.failure.Failure;
import com.wfj.search.online.index.pojo.failure.FailureType;
import com.wfj.search.online.index.pojo.failure.MultiFailure;
import com.wfj.search.online.index.service.ICacheEvictService;
import com.wfj.search.online.index.service.ICommentEsService;
import com.wfj.search.online.index.service.IIndexConfigService;
import com.wfj.search.online.index.service.IRetryService;
import com.wfj.search.online.index.util.ExecutorServiceFactory;
import com.wfj.search.online.index.util.PojoUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.wfj.search.online.common.pojo.OnlineRetryNotePojo.*;

/**
 * <br/>create at 15-12-28
 *
 * @author liuxh
 * @since 1.0.0
 */
@Service("commentESService")
public class CommentEsServiceImpl implements ICommentEsService {
    private static final Logger logger = LoggerFactory.getLogger(CommentEsServiceImpl.class);
    @Autowired
    private ICacheEvictService cacheEvictService;
    @Autowired
    private CommentEsIao commentEsIao;
    @Autowired
    private IRetryService retryService;
    @Autowired
    private IIndexConfigService indexConfigService;
    @Autowired
    private ICommentRequester commentRequester;

    @Override
    public Optional<Failure> updateComment(CommentIndexPojo comment) {
        if (comment == null) {
            return Optional.empty();
        }
        String commentId = comment.getCommentId();
        try {
            commentEsIao.upsert(comment);
            cacheEvictService.removeCommentCache(commentId);
        } catch (Exception e) {
            String msg = "将评论信息[commentId=" + commentId + "]保存到ES中失败";
            retryService.addUnresolvedRetryNote(commentId, Step.es, Type.comment, Action.save);
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.comment, FailureType.save2ES, commentId, msg, e));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Failure> removeComment(String commentId) {
        if (StringUtils.isBlank(commentId)) {
            return Optional.empty();
        }
        try {
            this.commentEsIao.delete(commentId);
            cacheEvictService.removeCommentCache(commentId);
        } catch (Exception e) {
            String msg = "从ES中删除评论[commentId=" + commentId + "]失败";
            retryService.addUnresolvedRetryNote(commentId, Step.es, Type.comment, Action.delete);
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.comment, FailureType.deleteFromES, commentId, msg, e));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Failure> buildComments(long version) {
        int fetchSize = this.indexConfigService.getFetchSize();
        int pageSize;
        int total;
        try {
            total = commentRequester.count();
            pageSize = (total + fetchSize - 1) / fetchSize;
        } catch (RequestException e) {
            String msg = "查询评论总数出错";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.comment, FailureType.requestError, null, msg, e));
        }
        MultiFailure multiFailure = new MultiFailure();
        multiFailure.setTotal(total);
        int threads = this.indexConfigService.getFetchThreads();
        final AtomicReference<Throwable> tracker = new AtomicReference<>();
        ExecutorService threadPool = ExecutorServiceFactory.create("buildComment2ES", threads,
                Thread.currentThread(), tracker);
        CompletionService<Void> completionService = new ExecutorCompletionService<>(threadPool);
        commentRequester.clearCache();
        for (int i = 0; i < pageSize; i++) {
            final int start = i * fetchSize;
            completionService.submit(() -> multiFailure.merge(saveCommentPages(start, fetchSize, version)), null);
        }
        try {
            for (int i = 0; i < pageSize; i++) {
                completionService.take();
            }
        } catch (InterruptedException e) {
            String msg = "向ES保存数据被打断";
            logger.error(msg, e);
            multiFailure.addFailure(new Failure(DataType.comment, FailureType.save2ES, null, msg, tracker.get()));
        }
        try {
            this.commentEsIao.removeExpired(version);
        } catch (Exception e) {
            String msg = "从ES中删除过期数据出错，{version:" + version + "}";
            logger.error(msg, e);
            multiFailure.addFailure(
                    new Failure(DataType.comment, FailureType.deleteFromES, "{version:" + version + "}", msg, e));
        }
        return multiFailure.toOptional();
    }

    private MultiFailure saveCommentPages(int start, int fetch, long version) {
        MultiFailure multiFailure = new MultiFailure();
        List<CommentPojo> comments;
        try {
            comments = commentRequester.listCommentsWithPage(start, fetch);
        } catch (RequestException e) {
            String msg = "分页获取评论列表出错，start=" + start + ", fetch=" + fetch;
            logger.error(msg, e);
            return multiFailure
                    .addFailure(new Failure(DataType.comment, FailureType.requestError,
                            "{start:" + start + ", fetch:" + fetch + "}", msg, e));
        }
        List<CommentIndexPojo> list = comments.stream()
                .map(comment -> PojoUtils.toIndexPojo(comment, version))
                .collect(Collectors.toList());
        int size = list.size();
        try {
            for (CommentIndexPojo commentIndexPojo : list) {
                this.commentEsIao.upsert(commentIndexPojo);
            }
            multiFailure.addSuccess(size);
        } catch (Exception e) {
            multiFailure.addFail(size);
            String msg = "向ES保存数据出错，出错信息：" + list.stream()
                    .map(ci -> {
                        String commentId = ci.getCommentId();
                        retryService.addUnresolvedRetryNote(commentId, Step.es, Type.comment, Action.save);
                        multiFailure
                                .addFailure(new Failure(DataType.comment, FailureType.save2ES, commentId));
                        return commentId;
                    })
                    .collect(Collectors.toList());
            logger.error(msg, e);
        }
        return multiFailure;
    }
}
