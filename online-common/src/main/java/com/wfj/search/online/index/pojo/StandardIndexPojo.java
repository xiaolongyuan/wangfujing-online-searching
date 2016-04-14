package com.wfj.search.online.index.pojo;

import org.apache.solr.client.solrj.beans.Field;

/**
 * 规格索引POJO
 * <br/>create at 15-7-20
 *
 * @author liuxh
 * @since 1.0.0
 */
public class StandardIndexPojo {
    @Field("standardId")
    private String standardId;
    @Field("standardName")
    private String standardName;
    @Field("operationSid")
    private Long operationSid;

    /**
     * 规格编码
     *
     * @return 规格编码
     */
    public String getStandardId() {
        return standardId;
    }

    /**
     * 规格编码
     *
     * @param standardId 规格编码
     */
    public void setStandardId(String standardId) {
        this.standardId = standardId;
    }

    /**
     * 规格
     *
     * @return 规格
     */
    public String getStandardName() {
        return standardName;
    }

    /**
     * 规格
     *
     * @param standardName 规格
     */
    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    /**
     * 写入操作记录号
     *
     * @return 写入操作记录号
     */
    public Long getOperationSid() {
        return operationSid;
    }

    /**
     * 写入操作记录号
     *
     * @param operationSid 写入操作记录号
     */
    public void setOperationSid(Long operationSid) {
        this.operationSid = operationSid;
    }
}
