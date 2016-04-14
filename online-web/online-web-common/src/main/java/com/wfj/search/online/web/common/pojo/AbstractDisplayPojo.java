package com.wfj.search.online.web.common.pojo;

/**
 * 前台展示POJO，提供URL
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public abstract class AbstractDisplayPojo {
    protected String url = "#";
    protected int facetCount;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getFacetCount() {
        return facetCount;
    }

    public void setFacetCount(int facetCount) {
        this.facetCount = facetCount;
    }
}
