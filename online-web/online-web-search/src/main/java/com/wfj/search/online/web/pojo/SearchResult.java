package com.wfj.search.online.web.pojo;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * <br/>create at 16-1-12
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SearchResult<T> {
    private long total = 0;
    private int pageSize = 0;
    private List<T> list = Lists.newArrayList();

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
