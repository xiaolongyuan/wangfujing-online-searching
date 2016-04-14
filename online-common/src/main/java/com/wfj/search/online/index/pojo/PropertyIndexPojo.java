package com.wfj.search.online.index.pojo;

import org.apache.solr.client.solrj.beans.Field;

/**
 * 属性索引POJO
 * <br/>create at 15-7-17
 *
 * @author liufl
 * @since 1.0.0
 */
public class PropertyIndexPojo {
    @Field("propertyId")
    private String propertyId;
    @Field("propertyName")
    private String propertyName;
    @Field("enumProperty")
    private boolean enumProperty;
    @Field("propertyOrder")
    private int propertyOrder;
    @Field("operationSid")
    private Long operationSid;

    /**
     * 属性编码
     * @return 属性编码
     */
    public String getPropertyId() {
        return propertyId;
    }

    /**
     * 属性编码
     * @param propertyId 属性编码
     */
    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    /**
     * 属性名
     * @return 属性名
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * 属性名
     * @param propertyName 属性名
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * 是否枚举属性
     * @return 是否枚举属性
     */
    @SuppressWarnings("unused")
    public boolean isEnumProperty() {
        return enumProperty;
    }

    /**
     * 是否枚举属性
     * @param enumProperty 是否枚举属性
     */
    public void setEnumProperty(boolean enumProperty) {
        this.enumProperty = enumProperty;
    }

    /**
     * 展示排序
     * @return 展示排序
     */
    public int getPropertyOrder() {
        return propertyOrder;
    }

    /**
     * 展示排序
     * @param propertyOrder 展示排序
     */
    public void setPropertyOrder(int propertyOrder) {
        this.propertyOrder = propertyOrder;
    }

    /**
     * 写入操作记录号
     * @return 写入操作记录号
     */
    public Long getOperationSid() {
        return operationSid;
    }

    /**
     * 写入操作记录号
     * @param operationSid 写入操作记录号
     */
    public void setOperationSid(Long operationSid) {
        this.operationSid = operationSid;
    }
}
