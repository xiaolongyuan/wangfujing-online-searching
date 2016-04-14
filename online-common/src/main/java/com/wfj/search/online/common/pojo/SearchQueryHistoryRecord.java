package com.wfj.search.online.common.pojo;

/**
 * <p>create at 16-1-21</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class SearchQueryHistoryRecord {
    private String query;
    private double count;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }
}
