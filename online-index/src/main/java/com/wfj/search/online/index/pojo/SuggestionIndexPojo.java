package com.wfj.search.online.index.pojo;

import org.apache.solr.client.solrj.beans.Field;

/**
 * <p>create at 16-1-22</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class SuggestionIndexPojo {
    @Field("ck")
    private String ck;
    @Field("keyword")
    private String keyword;
    @Field("frequency")
    private Long frequency;
    @Field("matchCount")
    private Long matchCount;
    @Field("channel")
    private String channel;
    @Field("operationSid")
    private Long operationSid;// 操作记录号

    @SuppressWarnings("unused")
    public String getCk() {
        return ck;
    }

    public void setCk(String ck) {
        this.ck = ck;
    }

    @SuppressWarnings("unused")
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @SuppressWarnings("unused")
    public Long getFrequency() {
        return frequency;
    }

    public void setFrequency(Long frequency) {
        this.frequency = frequency;
    }

    @SuppressWarnings("unused")
    public Long getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(Long matchCount) {
        this.matchCount = matchCount;
    }

    @SuppressWarnings("unused")
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Long getOperationSid() {
        return operationSid;
    }

    public void setOperationSid(Long operationSid) {
        this.operationSid = operationSid;
    }
}
