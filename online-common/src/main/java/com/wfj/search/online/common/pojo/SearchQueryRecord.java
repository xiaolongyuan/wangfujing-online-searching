package com.wfj.search.online.common.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.Set;

/**
 * <p>create at 16-1-21</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class SearchQueryRecord {
    private String uuid;
    private String trackId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8", shape = JsonFormat.Shape.STRING)
    private Date queryTime;
    private String query;
    private Set<String> queryPinyin;
    private Set<String> queryAbbre;
    private String channel;
    private boolean removed = false;

    @SuppressWarnings("unused")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @SuppressWarnings("unused")
    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    @SuppressWarnings("unused")
    public Date getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(Date queryTime) {
        this.queryTime = queryTime;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @SuppressWarnings("unused")
    public Set<String> getQueryPinyin() {
        return queryPinyin;
    }

    public void setQueryPinyin(Set<String> queryPinyin) {
        this.queryPinyin = queryPinyin;
    }

    @SuppressWarnings("unused")
    public Set<String> getQueryAbbre() {
        return queryAbbre;
    }

    public void setQueryAbbre(Set<String> queryAbbre) {
        this.queryAbbre = queryAbbre;
    }

    @SuppressWarnings("unused")
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @SuppressWarnings("unused")
    public boolean isRemoved() {
        return removed;
    }

    @SuppressWarnings("unused")
    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
}
