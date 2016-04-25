package com.wfj.search.online.index.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Spu索引POJO<br/>
 * 线程不安全
 * <p>create at 15-10-30</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class SpuIndexPojo {
    private String spuId; // SPU编码
    private String spuName; // SPU名称
    private String model; //款号
    private String brandId; // 品牌编码
    private Integer activeBit; // 活动标识位
    private Boolean onSell; // 是否在售、上架
    private String pageDescription; // 页面介绍
    private List<String> aliases = Lists.newArrayList(); // SPU别名
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8", shape = JsonFormat.Shape.STRING)
    private Date onSellSince; // 上架时间
    private List<String> categoryIds = Lists.newArrayList(); // 所属的所有叶子分类编码
    private List<String> tagIds = Lists.newArrayList(); // 标签ID
    private List<String> propertyValueIds = Lists.newArrayList(); // 属性值编码
    private String shortDesc;
    private String longDesc;

    private List<String> spuPropertyValues = Lists.newArrayList(); // 属性值
    private Long operationSid; // 操作记录号

    // 属性列表
    private final List<PropertyIndexPojo> propertyIndexPojos = Collections.synchronizedList(Lists.newArrayList());
    // 属性值列表
    private final List<PropertyValueIndexPojo> propertyValueIndexPojos = Collections
            .synchronizedList(Lists.newArrayList());
    // 标签列表
    private final List<TagIndexPojo> tagIndexPojos = Collections.synchronizedList(Lists.newArrayList());

    /**
     * SPU编码
     *
     * @return SPU编码
     */
    public String getSpuId() {
        return spuId;
    }

    /**
     * SPU编码
     *
     * @param spuId SPU编码
     */
    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    /**
     * SPU名称
     *
     * @return SPU名称
     */
    public String getSpuName() {
        return spuName;
    }

    /**
     * SPU名称
     *
     * @param spuName SPU名称
     */
    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    /**
     * 款号
     *
     * @return 款号
     */
    public String getModel() {
        return model;
    }

    /**
     * 款号
     *
     * @param model 款号
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * 品牌编码
     *
     * @return 品牌编码
     */
    public String getBrandId() {
        return brandId;
    }

    /**
     * 品牌编码
     *
     * @param brandId 品牌编码
     */
    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    /**
     * 活动标识位
     *
     * @return 活动标识位
     */
    public Integer getActiveBit() {
        return activeBit;
    }

    /**
     * 活动标识位
     *
     * @param activeBit 活动标识位
     */
    public void setActiveBit(Integer activeBit) {
        this.activeBit = activeBit;
    }

    /**
     * 是否在售、上架
     *
     * @return 是否在售、上架
     */
    @SuppressWarnings("unused")
    public Boolean getOnSell() {
        return onSell;
    }

    /**
     * 是否在售、上架
     *
     * @param onSell 是否在售、上架
     */
    public void setOnSell(Boolean onSell) {
        this.onSell = onSell;
    }

    /**
     * 页面介绍
     *
     * @return 页面介绍
     */
    public String getPageDescription() {
        return pageDescription;
    }

    /**
     * 页面介绍
     *
     * @param pageDescription 页面介绍
     */
    public void setPageDescription(String pageDescription) {
        this.pageDescription = pageDescription;
    }

    /**
     * SPU别名
     *
     * @return SPU别名
     */
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * SPU别名
     *
     * @param aliases SPU别名
     */
    @SuppressWarnings("unused")
    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    /**
     * 上架时间
     *
     * @return 上架时间
     */
    public Date getOnSellSince() {
        return onSellSince;
    }

    /**
     * 上架时间
     *
     * @param onSellSince 上架时间
     */
    public void setOnSellSince(Date onSellSince) {
        this.onSellSince = onSellSince;
    }

    /**
     * 所属的所有叶子分类编码
     *
     * @return 所属的所有叶子分类编码
     */
    public List<String> getCategoryIds() {
        return categoryIds;
    }

    /**
     * 所属的所有叶子分类编码
     *
     * @param categoryIds 所属的所有叶子分类编码
     */
    @SuppressWarnings("unused")
    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    /**
     * 标签ID
     *
     * @return 标签ID
     */
    public List<String> getTagIds() {
        return tagIds;
    }

    /**
     * 标签ID
     *
     * @param tagIds 标签ID
     */
    @SuppressWarnings("unused")
    public void setTagIds(List<String> tagIds) {
        this.tagIds = tagIds;
    }

    /**
     * 属性值编码
     *
     * @return 属性值编码
     */
    public List<String> getPropertyValueIds() {
        return propertyValueIds;
    }

    /**
     * 属性值编码
     *
     * @param propertyValueIds 属性值编码
     */
    @SuppressWarnings("unused")
    public void setPropertyValueIds(List<String> propertyValueIds) {
        this.propertyValueIds = propertyValueIds;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    /**
     * 属性值
     *
     * @return 属性值
     */
    public List<String> getSpuPropertyValues() {
        return spuPropertyValues;
    }

    /**
     * 属性值
     *
     * @param spuPropertyValues 属性值
     */
    @SuppressWarnings("unused")
    public void setSpuPropertyValues(List<String> spuPropertyValues) {
        this.spuPropertyValues = spuPropertyValues;
    }

    /**
     * 操作记录号
     *
     * @return 操作记录号
     */
    @SuppressWarnings("unused")
    public Long getOperationSid() {
        return operationSid;
    }

    /**
     * 操作记录号
     *
     * @param operationSid 操作记录号
     */
    @SuppressWarnings("unused")
    public void setOperationSid(Long operationSid) {
        this.operationSid = operationSid;
    }

    public List<PropertyIndexPojo> getPropertyIndexPojos() {
        return propertyIndexPojos;
    }

    public List<PropertyValueIndexPojo> getPropertyValueIndexPojos() {
        return propertyValueIndexPojos;
    }

    public List<TagIndexPojo> getTagIndexPojos() {
        return tagIndexPojos;
    }
}
