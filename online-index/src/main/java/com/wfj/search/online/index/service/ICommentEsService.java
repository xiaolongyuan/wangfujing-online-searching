package com.wfj.search.online.index.service;

import com.wfj.search.online.index.pojo.CommentIndexPojo;
import com.wfj.search.online.index.pojo.failure.Failure;

import java.util.Optional;

/**
 * <br/>create at 15-12-28
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface ICommentEsService {
    /**
     * 更新评论信息
     *
     * @param comment 评论信息
     * @return 错误信息
     */
    Optional<Failure> updateComment(CommentIndexPojo comment);

    /**
     * 删除评论信息
     *
     * @param commentId 评论id
     * @return 错误信息
     */
    Optional<Failure> removeComment(String commentId);

    /**
     * 从评论系统获取所有评论数据，保存到ES
     *
     * @param version 版本号
     * @return 错误信息
     */
    Optional<Failure> buildComments(long version);
}
