package com.wfj.search.online.index.iao;

import com.wfj.search.online.index.pojo.CommentIndexPojo;

import java.util.Collection;

/**
 * <br/>create at 15-11-10
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface ICommentIAO extends CommitableIAO {
    /**
     * 写入评论数据。如果已存在对应记录将会覆盖
     *
     * @param array 新记录
     * @throws IndexException
     */
    void save(Collection<CommentIndexPojo> array) throws IndexException;

    /**
     * 根据主键编号删除索引
     *
     * @param sid 主键编号
     * @throws IndexException
     */
    void remove(String sid) throws IndexException;

    /**
     * 删除版本号小于给定版本号的评论索引
     *
     * @param versionNo 版本号
     * @throws IndexException
     */
    void removeExpired(long versionNo) throws IndexException;
}
