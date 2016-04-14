package com.wfj.search.online.common.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * <br/>create at 15-12-16
 *
 * @author liuxh
 * @since 1.0.0
 */
public class PageImpl<T> implements Page<T> {
    private final long total;
    private final List<T> content = new ArrayList<>();
    private final int pageSize;

    public PageImpl(List<T> content, long total, int pageSize) {
        this.total = total;
        this.pageSize = pageSize;
        this.content.addAll(content);
    }

    @Override
    public int getTotalPages() {
        return (int) ((this.total + this.pageSize - 1) / this.pageSize);
    }

    @Override
    public int getSize() {
        return this.content.size();
    }

    @Override
    public long getTotalElements() {
        return total;
    }

    @Override
    public List<T> getContent() {
        return this.content;
    }
}
