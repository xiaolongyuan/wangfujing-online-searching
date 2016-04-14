package com.wfj.search.online.index.es;

import java.util.List;

/**
 * <p>create at 16-3-27</p>
 *
 * @author liufl
 * @since 1.0.19
 */
public class ScrollPage<T> {
    private String scrollId;
    private Long scrollIdTTL;
    private Long total;
    private List<T> list;

    public String getScrollId() {
        return scrollId;
    }

    public void setScrollId(String scrollId) {
        this.scrollId = scrollId;
    }

    public Long getScrollIdTTL() {
        return scrollIdTTL;
    }

    public void setScrollIdTTL(Long scrollIdTTL) {
        this.scrollIdTTL = scrollIdTTL;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
