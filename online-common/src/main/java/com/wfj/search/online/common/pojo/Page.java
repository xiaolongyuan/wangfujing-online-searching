package com.wfj.search.online.common.pojo;

import java.util.List;

/**
 * <br/>create at 15-12-16
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface Page<T> {
    /**
     * 获取总页数
     *
     * @return 总页数
     */
    int getTotalPages();

    /**
     * 获取每页数量
     *
     * @return 每页数量
     */
    int getSize();

    /**
     * 获取数据总数
     *
     * @return 数据总数
     */
    long getTotalElements();

    /**
     * 获取当前页元素列表
     *
     * @return 当前页元素列表
     */
    List<T> getContent();
}
