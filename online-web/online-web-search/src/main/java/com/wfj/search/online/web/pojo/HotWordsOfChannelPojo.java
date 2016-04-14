package com.wfj.search.online.web.pojo;

import java.util.List;

/**
 * <br/>create at 15-12-22
 *
 * @author liuxh
 * @since 1.0.0
 */
public class HotWordsOfChannelPojo {
    private String siteId;
    private String channelId;
    private List<HotWord> hotWords;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public List<HotWord> getHotWords() {
        return hotWords;
    }

    public void setHotWords(List<HotWord> hotWords) {
        this.hotWords = hotWords;
    }
}
