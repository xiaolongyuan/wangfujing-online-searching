package com.wfj.search.online.index.pojo;

import org.apache.solr.client.solrj.beans.Field;

/**
 * 属性值索引POJO
 * <br/>create at 15-7-21
 *
 * @author liufl
 * @since 1.0.0
 */
public class PropertyValueIndexPojo {
    @Field("propertyValueId")
    private String propertyValueId;
    @Field("propertyValue")
    private String propertyValue;
    @Field("propertyId")
    private String propertyId;
    @Field("channel")
    private String channel;
    @Field("operationSid")
    private Long operationSid;// 写入操作记录号

    /**
     * 属性值编码
     *
     * @return 属性值编码
     */
    public String getPropertyValueId() {
        return propertyValueId;
    }

    /**
     * 属性值编码
     *
     * @param propertyValueId 属性值编码
     */
    public void setPropertyValueId(String propertyValueId) {
        this.propertyValueId = propertyValueId;
    }

    /**
     * 属性值
     *
     * @return 属性值
     */
    public String getPropertyValue() {
        return propertyValue;
    }

    /**
     * 属性值
     *
     * @param propertyValue 属性值
     */
    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    /**
     * 属性编码
     *
     * @return 属性编码
     */
    public String getPropertyId() {
        return propertyId;
    }

    /**
     * 属性编码
     *
     * @param propertyId 属性编码
     */
    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * 操作记录号
     *
     * @return 操作记录号
     */
    public Long getOperationSid() {
        return operationSid;
    }

    /**
     * 操作记录号
     *
     * @param operationSid 操作记录号
     */
    public void setOperationSid(Long operationSid) {
        this.operationSid = operationSid;
    }
}
