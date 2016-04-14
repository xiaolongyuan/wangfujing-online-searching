package com.wfj.search.online.common.pojo;

/**
 * 属性值POJO
 * <br/>create at 15-7-2
 *
 * @author liufl
 * @since 1.0.0
 */
public class PropertyValuePojo {
    private String categoryId;
    private String channel;
    private String propertyValueId;
    private String propertyValue;
    private PropertyPojo property;

    /**
     * 所属分类编码
     *
     * @return 所属分类编码
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * 所属分类编码
     *
     * @param categoryId 所属分类编码
     */
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 所属渠道编码
     *
     * @return 所属渠道编码
     */
    public String getChannel() {
        return channel;
    }

    /**
     * 所属渠道编码
     *
     * @param channel 所属渠道编码
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

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
     * 属性值字面值
     *
     * @return 属性值字面值
     */
    public String getPropertyValue() {
        return propertyValue;
    }

    /**
     * 属性值字面值
     *
     * @param propertyValue 属性值字面值
     */
    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    /**
     * 属性
     *
     * @return 属性
     */
    public PropertyPojo getProperty() {
        return property;
    }

    /**
     * 属性
     *
     * @param property 属性
     */
    public void setProperty(PropertyPojo property) {
        this.property = property;
    }
}
