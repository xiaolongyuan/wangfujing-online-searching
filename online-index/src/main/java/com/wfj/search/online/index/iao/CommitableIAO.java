package com.wfj.search.online.index.iao;

/**
 * <br/>create at 15-7-8
 *
 * @author liufl
 * @since 1.0.0
 */
public interface CommitableIAO {
    /**
     * 提交索引
     * @throws IndexException
     */
    void commit() throws IndexException;
}
