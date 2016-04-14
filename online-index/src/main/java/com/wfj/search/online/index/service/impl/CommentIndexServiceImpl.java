package com.wfj.search.online.index.service.impl;

import com.google.common.collect.Lists;
import com.wfj.search.online.index.es.CommentEsIao;
import com.wfj.search.online.index.es.ScrollPage;
import com.wfj.search.online.index.iao.ICommentIAO;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.CommentIndexPojo;
import com.wfj.search.online.index.pojo.failure.DataType;
import com.wfj.search.online.index.pojo.failure.Failure;
import com.wfj.search.online.index.pojo.failure.FailureType;
import com.wfj.search.online.index.pojo.failure.MultiFailure;
import com.wfj.search.online.index.service.ICommentIndexService;
import com.wfj.search.online.index.service.IRetryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.wfj.search.online.common.pojo.OnlineRetryNotePojo.*;

/**
 * <br/>create at 15-11-10
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
@Service("commentIndexService")
public class CommentIndexServiceImpl implements ICommentIndexService {
    private static final Logger logger = LoggerFactory.getLogger(CommentIndexServiceImpl.class);
    @Autowired
    private CommentEsIao commentEsIao;
    @Autowired
    private ICommentIAO commentIAO;
    @Autowired
    private IRetryService retryService;

    @Override
    public Optional<Failure> indexAllFromEs(long versionNo) {
        MultiFailure multiFailure = new MultiFailure();
        ScrollPage<CommentIndexPojo> scrollPage;
        try {
            scrollPage = this.commentEsIao.startScrollOfAll();
        } catch (IndexException e) {
            Failure failure = new Failure(DataType.comment, FailureType.retrieveFromES, "", "scroll评论ES数据失败", e);
            return Optional.of(failure);
        }
        multiFailure.setTotal(scrollPage.getTotal());
        while (true) {
            List<CommentIndexPojo> list = scrollPage.getList();
            if (list == null) {
                break;
            }
            list.forEach(comment -> comment.setOperationSid(versionNo));
            int size = list.size();
            try {
                this.commentIAO.save(list);
                multiFailure.addSuccess(size);
            } catch (IndexException e) {
                multiFailure.addFail(size);
                String msg = "评论编号" + list.stream()
                        .map(comment -> {
                            String commentId = comment.getCommentId();
                            retryService.addUnresolvedRetryNote(commentId, Step.index, Type.comment, Action.save);
                            Failure failure = new Failure(DataType.comment, FailureType.save2Index, commentId);
                            multiFailure.addFailure(failure);
                            return commentId;
                        })
                        .collect(Collectors.toList())
                        + "写入索引失败";
                logger.error(msg, e);
            }
            try {
                scrollPage = this.commentEsIao.scroll(scrollPage.getScrollId(), scrollPage.getScrollIdTTL());
            } catch (IndexException e) {
                Failure failure = new Failure(DataType.comment, FailureType.retrieveFromES, "", "scroll评论ES数据失败", e);
                multiFailure.addFailure(failure);
                return Optional.of(multiFailure);
            }
        }
        try {
            this.commentIAO.removeExpired(versionNo);
        } catch (IndexException e) {
            logger.error("清除过期索引失败", e);
            multiFailure.addFailure(
                    new Failure(DataType.comment, FailureType.deleteFromIndex, "{version:" + versionNo + "}"));
        }
        return Optional.of(multiFailure);
    }

    @Override
    public Optional<Failure> saveFromEs(String commentId, long versionNo) {
        if (StringUtils.isBlank(commentId)) {
            return Optional.empty();
        }
        CommentIndexPojo commentIndexPojo;
        try {
            commentIndexPojo = this.commentEsIao.get(commentId);
        } catch (Exception e) {
            String msg = "从ES查询数据异常, {commentId:" + commentId + "}";
            logger.error(msg, e);
            Failure failure = new Failure(DataType.comment, FailureType.retrieveFromES, commentId);
            failure.setMessage(msg);
            failure.setThrowable(e);
            return Optional.of(failure);
        }
        if (commentIndexPojo != null) {
            commentIndexPojo.setOperationSid(versionNo);
            try {
                commentIAO.save(Lists.newArrayList(commentIndexPojo));
            } catch (IndexException e) {
                String msg = "保存评论索引失败，{commentId:" + commentId + "}";
                logger.error(msg, e);
                Failure failure = new Failure(DataType.comment, FailureType.save2Index, commentId);
                failure.setMessage(msg);
                failure.setThrowable(e);
                return Optional.of(failure);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Failure> remove(String sid) throws IndexException {
        try {
            commentIAO.remove(sid);
            return Optional.empty();
        } catch (Exception e) {
            Failure failure = new Failure(DataType.comment, FailureType.save2Index, sid);
            failure.setMessage("");
            failure.setThrowable(e);
            return Optional.of(failure);
        }
    }

    @Override
    public void commit() throws IndexException {
        commentIAO.commit();
    }
}
