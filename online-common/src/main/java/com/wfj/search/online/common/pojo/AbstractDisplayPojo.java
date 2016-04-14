package com.wfj.search.online.common.pojo;

/**
 * <br/>create at 15-11-16
 *
 * @author liuxh
 * @since 1.0.0
 */
public abstract class AbstractDisplayPojo {
    public static final String DEFAULT_URL = "";
    protected String url = DEFAULT_URL;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return ", url='" + url + '\'';
    }
}
