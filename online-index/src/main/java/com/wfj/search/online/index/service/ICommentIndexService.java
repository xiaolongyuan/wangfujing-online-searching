package com.wfj.search.online.index.service;

import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.failure.Failure;

import java.util.Optional;

/**
 * <br/>create at 15-11-10
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface ICommentIndexService {
    /**
     * 将ES中所有评论信息写入索引
     *
     * @param versionNo 版本号
     * @return 错误记录
     */
    Optional<Failure> indexAllFromEs(long versionNo);

    /**
     * 根据评论id将ES中数据写入索引
     *
     * @param commentId 评论id
     * @param versionNo 版本号
     * @return 错误记录
     */
    Optional<Failure> saveFromEs(String commentId, long versionNo);

    /**
     * 删除评论索引
     *
     * @param sid 专柜商品编码
     * @throws IndexException
     */
    Optional<Failure> remove(String sid) throws IndexException;

    /**
     * 提交评论
     *
     * @throws IndexException
     */
    void commit() throws IndexException;
}
