package com.wfj.search.online.web.pojo;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * <br/>create at 16-1-21
 *
 * @author liufl
 * @since 1.0.0
 */
public class PagedResult<T> {
    private long total = 0;
    private int fetch = 0;
    private List<T> list = Lists.newArrayList();
    private String msg;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getFetch() {
        return fetch;
    }

    public void setFetch(int fetch) {
        this.fetch = fetch;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
