package com.wfj.search.online.common.pojo;

/**
 * 属性POJO
 * <br/>create at 15-7-1
 *
 * @author liufl
 * @since 1.0.0
 */
public class PropertyPojo {
    private String propertyId;
    private String propertyName; // PTODO 属性名通常就是停用词
    private boolean enumProperty;
    private int propertyOrder;

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

    /**
     * 属性名
     *
     * @return 属性名
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * 属性名
     *
     * @param propertyName 属性名
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * 是否枚举属性
     *
     * @return 是否枚举属性
     */
    public boolean isEnumProperty() {
        return enumProperty;
    }

    /**
     * 是否枚举属性
     *
     * @param enumProperty 是否枚举属性
     */
    public void setEnumProperty(boolean enumProperty) {
        this.enumProperty = enumProperty;
    }

    /**
     * 展示排序
     *
     * @return 展示排序
     */
    public int getPropertyOrder() {
        return propertyOrder;
    }

    /**
     * 展示排序
     *
     * @param propertyOrder 展示排序
     */
    public void setPropertyOrder(int propertyOrder) {
        this.propertyOrder = propertyOrder;
    }
}
