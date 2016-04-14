package com.wfj.search.online.index.pojo;

import org.apache.solr.client.solrj.beans.Field;

/**
 * 标签索引POJO
 * <br/>create at 15-7-20
 *
 * @author liufl
 * @since 1.0.0
 */
public class TagIndexPojo {
    @Field("tagId")
    private String tagId;
    @Field("tagName")
    private String tagName;
    @Field("operationSid")
    private Long operationSid;

    /**
     * 标签ID
     * @return 标签ID
     */
    public String getTagId() {
        return tagId;
    }

    /**
     * 标签ID
     * @param tagId 标签ID
     */
    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    /**
     * 标签名
     * @return 标签名
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * 标签名
     * @param tagName 标签名
     */
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    /**
     * 操作SID
     * @return 操作SID
     */
    public Long getOperationSid() {
        return operationSid;
    }

    /**
     * 操作SID
     * @param operationSid 操作SID
     */
    public void setOperationSid(Long operationSid) {
        this.operationSid = operationSid;
    }
}
