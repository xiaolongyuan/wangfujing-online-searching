package com.wfj.search.online.common.pojo;

import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;

/**
 * SPU POJO
 * <br/>create at 15-6-29
 *
 * @author liufl
 * @since 1.0.0
 */
public class SpuPojo {
    private String spuId;
    private String spuName;
    private String model;
    private String brandId;
    private int activeBit;
    private boolean onSell;
    private String pageDescription;
    private List<String> aliases = Lists.newArrayList();
    private Date onSellSince;
    private List<String> categoryIds = Lists.newArrayList();
    private List<TagPojo> tags = Lists.newArrayList();
    private List<PropertyValuePojo> propertyValues = Lists.newArrayList();

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
     * 促销活动标识
     *
     * @return 促销活动标识
     */
    public int getActiveBit() {
        return activeBit;
    }

    /**
     * 促销活动标识
     *
     * @param activeBit 促销活动标识
     */
    public void setActiveBit(int activeBit) {
        this.activeBit = activeBit;
    }

    /**
     * 是否在售、上架
     *
     * @return 在售、上架返回 {@code true} ，否则返回 {@code false}
     */
    public boolean isOnSell() {
        return onSell;
    }

    /**
     * 设置是否在售、上架
     *
     * @param onSell {@code true} 在售、上架； {@code false} 已下架
     */
    public void setOnSell(boolean onSell) {
        this.onSell = onSell;
    }

    /**
     * SPU详情页描述
     *
     * @return SPU详情页描述
     */
    public String getPageDescription() {
        return pageDescription;
    }

    /**
     * SPU详情页描述
     *
     * @param pageDescription SPU详情页描述
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
     * 所属分类编码列表
     *
     * @return 所属分类编码列表
     */
    public List<String> getCategoryIds() {
        return categoryIds;
    }

    /**
     * 所属分类编码列表
     *
     * @param categoryIds 所属分类编码列表
     */
    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    /**
     * 标签
     *
     * @return 标签
     */
    public List<TagPojo> getTags() {
        return tags;
    }

    /**
     * 标签
     *
     * @param tags 标签
     */
    public void setTags(List<TagPojo> tags) {
        this.tags = tags;
    }

    /**
     * SPU属性
     *
     * @return SPU属性
     */
    public List<PropertyValuePojo> getPropertyValues() {
        return propertyValues;
    }

    /**
     * SPU属性
     *
     * @param propertyValues SPU属性
     */
    public void setPropertyValues(List<PropertyValuePojo> propertyValues) {
        this.propertyValues = propertyValues;
    }
}
