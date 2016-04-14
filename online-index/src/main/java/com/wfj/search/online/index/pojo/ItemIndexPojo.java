package com.wfj.search.online.index.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.solr.client.solrj.beans.Field;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 专柜商品索引POJO
 * <br/>create at 15-7-21
 *
 * @author liufl
 * @since 1.0.0
 */
public class ItemIndexPojo {
    @Field("itemId")
    private String itemId; // 商品编码
    @Field("supplierId")
    private String supplierId; // 供应商编码
    @Field("stockMode")
    private Integer stockMode; // 库存方式：自库、虚库、不管库存
    @Field("inventory")
    private Integer inventory; // 库存量
    @Field("originalPrice")
    private Double originalPrice; // 原价
    @Field("currentPrice")
    private Double currentPrice; // 现价
    @Field("discountRate")
    private Double discountRate; // 折扣率
    @Field("channels")
    private List<String> channels = Collections.synchronizedList(Lists.newArrayList());
    @Field("activeId")
    private List<String> activeId = Collections.synchronizedList(Lists.newArrayList()); // 活动标签编码
    @Field("activeName")
    private List<String> activeName = Collections.synchronizedList(Lists.newArrayList()); // 活动名
    @Field("boost_*")
    private Map<String, Float> boost_ = Maps.newConcurrentMap();
    @Field("popSpotWeightBrand_*")
    private Map<String, Integer> popSpotWeightBrand = Maps.newConcurrentMap(); // 品牌坑位权重 popSpotWeightBrand_[channel]
    @Field("popSpotWeightCategory_*")
    private Map<String, Integer> popSpotWeightCategory = Maps
            .newConcurrentMap(); // 分类坑位权重 popSpotWeightCategory_[catId]
    @Field("skuId")
    private String skuId; // SKU编码
    @Field("title")
    private String title; // 长标题
    @Field("subTitle")
    private String subTitle; // 短标题/副标题
    @Field("activeKeywords")
    private List<String> activeKeywords = Collections.synchronizedList(Lists.newArrayList()); // 活动关键字
    @Field("colorId")
    private String colorId; // 色系编码
    @Field("colorName")
    private String colorName; // 色系名称
    @Field("colorAlias")
    private String colorAlias; // 颜色别名
    @Field("pictures")
    private List<String> pictures = Collections
            .synchronizedList(Lists.newArrayList()); // 图片信息, 格式：展示顺序号-是否颜色主图（0/1）-图片地址
    @Field("colorMasterPicture")
    private String colorMasterPicture; // 颜色主图
    @Field("colorMasterPictureOfPix_*")
    private Map<String, String> colorMasterPictureOfPix = Maps.newConcurrentMap(); // 颜色主图（按分辨率）
    @Field("standardId")
    private String standardId; // 规格编码
    @Field("standardName")
    private String standardName; // 规格名称
    @Field("type")
    private int type; // 销售类型， 正常/礼品/赠品
    @Field("upTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8", shape = JsonFormat.Shape.STRING)
    private Date upTime; // SKU上架时间
    @Field("spuId")
    private String spuId; // 所属SPU编码
    @Field("spuName")
    private String spuName; // 所属SPU名称
    @Field("model")
    private String model; //款号
    @Field("activeBit")
    private Integer activeBit; // 所属SPU活动标识位
    @Field("pageDescription")
    private String pageDescription; // SPU页面介绍
    @Field("aliases")
    private List<String> aliases = Collections.synchronizedList(Lists.newArrayList()); // SPU别名
    @Field("onSellSince")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8", shape = JsonFormat.Shape.STRING)
    private Date onSellSince; // 所属SPU上架时间
    @Field("brandId")
    private String brandId; // 品牌编码
    @Field("brandName")
    private String brandName;// 品牌名称
    @Field("brandAliases")
    private List<String> brandAliases = Collections.synchronizedList(Lists.newArrayList());// 品牌别名
    @Field("allLevelCategoryIds_*")
    private Map<String, List<String>> allLevelCategoryIds = Maps.newConcurrentMap();// 各级所有分类编码
    @Field("allLevelCategoryNames_*")
    private Map<String, List<String>> allLevelCategoryNames = Maps.newConcurrentMap();// 各级所有分类名称
    @Field("categoryIdUnderCategory_*")
    private Map<String, List<String>> categoryIdUnderCategory = Maps.newConcurrentMap();// 当前SPU从属的分类路径中的子类关系
    @Field("tagIds")
    private List<String> tagIds = Collections.synchronizedList(Lists.newArrayList());// 标签ID
    @Field("tags")
    private List<String> tags = Collections.synchronizedList(Lists.newArrayList());// 标签值
    @Field("propertyValues_*")
    private Map<String, List<String>> propertyValues = Maps.newConcurrentMap();// 属性值列表（按渠道）
    @Field("propertyIds_*")
    private Map<String, List<String>> propertyIds = Maps.newConcurrentMap();// 属性编码（按渠道）
    @Field("propertyValueIdOfPropertyId_*")
    private Map<String, String> propertyValueIdOfPropertyId = Maps.newConcurrentMap();// 属性属性值关系
    @Field("itemSale")
    private Integer itemSale;
    @Field("skuSale")
    private Integer skuSale;
    @Field("spuSale")
    private Integer spuSale;
    @Field("spuClick")
    private Integer spuClick;
    @Field("operationSid")
    private Long operationSid;// 操作记录号

    /**
     * 商品编码
     *
     * @return 商品编码
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * 商品编码
     *
     * @param itemId 商品编码
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     * 供应商编码
     *
     * @return 供应商编码
     */
    public String getSupplierId() {
        return supplierId;
    }

    /**
     * 供应商编码
     *
     * @param supplierId 供应商编码
     */
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * 库存方式：自库、虚库、不管库存
     *
     * @return 库存方式：自库、虚库、不管库存
     */
    public Integer getStockMode() {
        return stockMode;
    }

    /**
     * 库存方式：自库、虚库、不管库存
     *
     * @param stockMode 库存方式：自库、虚库、不管库存
     */
    public void setStockMode(Integer stockMode) {
        this.stockMode = stockMode;
    }

    /**
     * 库存量
     *
     * @return 库存量
     */
    public Integer getInventory() {
        return inventory;
    }

    /**
     * 库存量
     *
     * @param inventory 库存量
     */
    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    /**
     * 原价
     *
     * @return 原价
     */
    public Double getOriginalPrice() {
        return originalPrice;
    }

    /**
     * 原价
     *
     * @param originalPrice 原价
     */
    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    /**
     * 现价
     *
     * @return 现价
     */
    public Double getCurrentPrice() {
        return currentPrice;
    }

    /**
     * 现价
     *
     * @param currentPrice 现价
     */
    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    /**
     * 折扣率
     *
     * @return 折扣率
     */
    public Double getDiscountRate() {
        return discountRate;
    }

    /**
     * 折扣率
     *
     * @param discountRate 折扣率
     */
    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    /**
     * 可售渠道编码
     *
     * @return 可售渠道编码
     */
    public List<String> getChannels() {
        return channels;
    }

    /**
     * 可售渠道编码
     *
     * @param channels 可售渠道编码
     */
    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    /**
     * 按渠道加权量
     *
     * @return 按渠道加权量
     */
    public Map<String, Float> getBoost_() {
        return boost_;
    }

    /**
     * 按渠道加权量
     *
     * @param boost_ 按渠道加权量
     */
    @SuppressWarnings("unused")
    public void setBoost_(Map<String, Float> boost_) {
        this.boost_ = boost_;
    }

    /**
     * 活动标签编码
     *
     * @return 活动标签编码
     */
    public List<String> getActiveId() {
        return activeId;
    }

    /**
     * 活动标签编码
     *
     * @param activeId 活动标签编码
     */
    public void setActiveId(List<String> activeId) {
        this.activeId = activeId;
    }

    /**
     * 活动名
     *
     * @return 活动名
     */
    @SuppressWarnings("unused")
    public List<String> getActiveName() {
        return activeName;
    }

    /**
     * 活动名
     *
     * @param activeName 活动名
     */
    public void setActiveName(List<String> activeName) {
        this.activeName = activeName;
    }

    /**
     * 品牌坑位权重 popSpotWeightBrand_[channel]
     *
     * @return 品牌坑位权重 popSpotWeightBrand_[channel]
     */
    public Map<String, Integer> getPopSpotWeightBrand() {
        return popSpotWeightBrand;
    }

    /**
     * 品牌坑位权重 popSpotWeightBrand_[channel]
     *
     * @param popSpotWeightBrand 品牌坑位权重 popSpotWeightBrand_[channel]
     */
    @SuppressWarnings("unused")
    public void setPopSpotWeightBrand(Map<String, Integer> popSpotWeightBrand) {
        this.popSpotWeightBrand = popSpotWeightBrand;
    }

    /**
     * 分类坑位权重 popSpotWeightCategory_[catId]
     *
     * @return 分类坑位权重 popSpotWeightCategory_[catId]
     */
    public Map<String, Integer> getPopSpotWeightCategory() {
        return popSpotWeightCategory;
    }

    /**
     * 分类坑位权重 popSpotWeightCategory_[catId]
     *
     * @param popSpotWeightCategory 分类坑位权重 popSpotWeightCategory_[catId]
     */
    @SuppressWarnings("unused")
    public void setPopSpotWeightCategory(Map<String, Integer> popSpotWeightCategory) {
        this.popSpotWeightCategory = popSpotWeightCategory;
    }

    /**
     * SKU编码
     *
     * @return SKU编码
     */
    public String getSkuId() {
        return skuId;
    }

    /**
     * SKU编码
     *
     * @param skuId SKU编码
     */
    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    /**
     * 长标题
     *
     * @return 长标题
     */
    @SuppressWarnings("unused")
    public String getTitle() {
        return title;
    }

    /**
     * 长标题
     *
     * @param title 长标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 短标题/副标题
     *
     * @return 短标题/副标题
     */
    @SuppressWarnings("unused")
    public String getSubTitle() {
        return subTitle;
    }

    /**
     * 短标题/副标题
     *
     * @param subTitle 短标题/副标题
     */
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    /**
     * 活动关键字
     *
     * @return 活动关键字
     */
    @SuppressWarnings("unused")
    public List<String> getActiveKeywords() {
        return activeKeywords;
    }

    /**
     * 活动关键字
     *
     * @param activeKeywords 活动关键字
     */
    public void setActiveKeywords(List<String> activeKeywords) {
        this.activeKeywords = activeKeywords;
    }

    /**
     * 色系编码
     *
     * @return 色系编码
     */
    @SuppressWarnings("unused")
    public String getColorId() {
        return colorId;
    }

    /**
     * 色系编码
     *
     * @param colorId 色系编码
     */
    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    /**
     * 色系名称
     *
     * @return 色系名称
     */
    @SuppressWarnings("unused")
    public String getColorName() {
        return colorName;
    }

    /**
     * 色系名称
     *
     * @param colorName 色系名称
     */
    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    /**
     * 颜色别名
     *
     * @return 颜色别名
     */
    @SuppressWarnings("unused")
    public String getColorAlias() {
        return colorAlias;
    }

    /**
     * 颜色别名
     *
     * @param colorAlias 颜色别名
     */
    public void setColorAlias(String colorAlias) {
        this.colorAlias = colorAlias;
    }

    /**
     * 图片信息, 格式：展示顺序号-是否颜色主图（0/1）-图片地址
     *
     * @return 图片信息, 格式：展示顺序号-是否颜色主图（0/1）-图片地址
     */
    public List<String> getPictures() {
        return pictures;
    }

    /**
     * 图片信息, 格式：展示顺序号-是否颜色主图（0/1）-图片地址
     *
     * @param pictures 图片信息, 格式：展示顺序号-是否颜色主图（0/1）-图片地址
     */
    @SuppressWarnings("unused")
    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    /**
     * 颜色主图
     *
     * @return 颜色主图
     */
    public String getColorMasterPicture() {
        return colorMasterPicture;
    }

    /**
     * 颜色主图
     *
     * @param colorMasterPicture 颜色主图
     */
    public void setColorMasterPicture(String colorMasterPicture) {
        this.colorMasterPicture = colorMasterPicture;
    }

    /**
     * 颜色主图（按分辨率）
     * @return 颜色主图（按分辨率）
     */
    public Map<String, String> getColorMasterPictureOfPix() {
        return colorMasterPictureOfPix;
    }

    /**
     *  颜色主图（按分辨率）
     * @param colorMasterPictureOfPix  颜色主图（按分辨率）
     */
    @SuppressWarnings("unused")
    public void setColorMasterPictureOfPix(Map<String, String> colorMasterPictureOfPix) {
        this.colorMasterPictureOfPix = colorMasterPictureOfPix;
    }

    /**
     * 规格编码
     *
     * @return 规格编码
     */
    @SuppressWarnings("unused")
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
     * 规格名称
     *
     * @return 规格名称
     */
    @SuppressWarnings("unused")
    public String getStandardName() {
        return standardName;
    }

    /**
     * 规格名称
     *
     * @param standardName 规格名称
     */
    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    /**
     * 销售类型， 正常/礼品/赠品
     *
     * @return 销售类型
     */
    public int getType() {
        return type;
    }

    /**
     * 销售类型， 正常/礼品/赠品
     *
     * @param type 销售类型
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * SKU上架时间
     *
     * @return SKU上架时间
     */
    @SuppressWarnings("unused")
    public Date getUpTime() {
        return upTime;
    }

    /**
     * SKU上架时间
     *
     * @param upTime SKU上架时间
     */
    public void setUpTime(Date upTime) {
        this.upTime = upTime;
    }

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
     * 所属SPU名称
     *
     * @return 所属SPU名称
     */
    @SuppressWarnings("unused")
    public String getSpuName() {
        return spuName;
    }

    /**
     * 所属SPU名称
     *
     * @param spuName 所属SPU名称
     */
    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    /**
     * 款号
     *
     * @return 款号
     */
    @SuppressWarnings("unused")
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
     * 所属SPU活动标识位
     *
     * @return 所属SPU活动标识位
     */
    @SuppressWarnings("unused")
    public Integer getActiveBit() {
        return activeBit;
    }

    /**
     * 所属SPU活动标识位
     *
     * @param activeBit 所属SPU活动标识位
     */
    public void setActiveBit(Integer activeBit) {
        this.activeBit = activeBit;
    }

    /**
     * 所属SPU页面介绍
     *
     * @return 所属SPU页面介绍
     */
    @SuppressWarnings("unused")
    public String getPageDescription() {
        return pageDescription;
    }

    /**
     * 所属SPU页面介绍
     *
     * @param pageDescription 所属SPU页面介绍
     */
    public void setPageDescription(String pageDescription) {
        this.pageDescription = pageDescription;
    }

    /**
     * 所属SPU别名
     *
     * @return 所属SPU别名
     */
    @SuppressWarnings("unused")
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * 所属SPU别名
     *
     * @param aliases 所属SPU别名
     */
    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    /**
     * 所属SPU上架时间
     *
     * @return 所属SPU上架时间
     */
    public Date getOnSellSince() {
        return onSellSince;
    }

    /**
     * 所属SPU上架时间
     *
     * @param onSellSince 所属SPU上架时间
     */
    public void setOnSellSince(Date onSellSince) {
        this.onSellSince = onSellSince;
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
     * 品牌名称
     *
     * @return 品牌名称
     */
    @SuppressWarnings("unused")
    public String getBrandName() {
        return brandName;
    }

    /**
     * 品牌名称
     *
     * @param brandName 品牌名称
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    /**
     * 品牌别名
     *
     * @return 品牌别名
     */
    public List<String> getBrandAliases() {
        return brandAliases;
    }

    /**
     * 品牌别名
     *
     * @param brandAliases 品牌别名
     */
    public void setBrandAliases(List<String> brandAliases) {
        this.brandAliases = brandAliases;
    }

    /**
     * 各级所有分类编码（按渠道）
     *
     * @return 各级所有分类编码（按渠道）
     */
    public Map<String, List<String>> getAllLevelCategoryIds() {
        return allLevelCategoryIds;
    }

    /**
     * 各级所有分类编码（按渠道）
     *
     * @param allLevelCategoryIds 各级所有分类编码（按渠道）
     */
    @SuppressWarnings("unused")
    public void setAllLevelCategoryIds(Map<String, List<String>> allLevelCategoryIds) {
        this.allLevelCategoryIds = allLevelCategoryIds;
    }

    /**
     * 各级所有分类名称（按渠道）
     *
     * @return 各级所有分类名称（按渠道）
     */
    public Map<String, List<String>> getAllLevelCategoryNames() {
        return allLevelCategoryNames;
    }

    /**
     * 各级所有分类名称 （按渠道）
     *
     * @param allLevelCategoryNames 各级所有分类名称 （按渠道）
     */
    @SuppressWarnings("unused")
    public void setAllLevelCategoryNames(Map<String, List<String>> allLevelCategoryNames) {
        this.allLevelCategoryNames = allLevelCategoryNames;
    }

    /**
     * 当前SPU从属的分类路径中的子类关系
     *
     * @return 当前SPU从属的分类路径中的子类关系
     */
    public Map<String, List<String>> getCategoryIdUnderCategory() {
        return categoryIdUnderCategory;
    }

    /**
     * 当前SPU从属的分类路径中的子类关系
     *
     * @param categoryIdUnderCategory 当前SPU从属的分类路径中的子类关系
     */
    @SuppressWarnings("unused")
    public void setCategoryIdUnderCategory(Map<String, List<String>> categoryIdUnderCategory) {
        this.categoryIdUnderCategory = categoryIdUnderCategory;
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
     * 标签值
     *
     * @return 标签值
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * 标签值
     *
     * @param tags 标签值
     */
    @SuppressWarnings("unused")
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * 属性值列表（按渠道）
     *
     * @return 属性值列表（按渠道）
     */
    public Map<String, List<String>> getPropertyValues() {
        return propertyValues;
    }

    /**
     * 属性值列表（按渠道）
     *
     * @param propertyValues 属性值列表（按渠道）
     */
    @SuppressWarnings("unused")
    public void setPropertyValues(Map<String, List<String>> propertyValues) {
        this.propertyValues = propertyValues;
    }

    /**
     * 属性编码（按渠道）
     *
     * @return 属性编码（按渠道）
     */
    public Map<String, List<String>> getPropertyIds() {
        return propertyIds;
    }

    /**
     * 属性编码（按渠道）
     *
     * @param propertyIds 属性编码（按渠道）
     */
    @SuppressWarnings("unused")
    public void setPropertyIds(Map<String, List<String>> propertyIds) {
        this.propertyIds = propertyIds;
    }

    /**
     * 属性属性值关系
     *
     * @return 属性属性值关系
     */
    public Map<String, String> getPropertyValueIdOfPropertyId() {
        return propertyValueIdOfPropertyId;
    }

    /**
     * 属性属性值关系
     *
     * @param propertyValueIdOfPropertyId 属性属性值关系
     */
    @SuppressWarnings("unused")
    public void setPropertyValueIdOfPropertyId(Map<String, String> propertyValueIdOfPropertyId) {
        this.propertyValueIdOfPropertyId = propertyValueIdOfPropertyId;
    }

    @SuppressWarnings("unused")
    public Integer getItemSale() {
        return itemSale;
    }

    public void setItemSale(Integer itemSale) {
        this.itemSale = itemSale;
    }

    @SuppressWarnings("unused")
    public Integer getSkuSale() {
        return skuSale;
    }

    public void setSkuSale(Integer skuSale) {
        this.skuSale = skuSale;
    }

    public Integer getSpuSale() {
        return spuSale;
    }

    public void setSpuSale(Integer spuSale) {
        this.spuSale = spuSale;
    }

    public Integer getSpuClick() {
        return spuClick;
    }

    public void setSpuClick(Integer spuClick) {
        this.spuClick = spuClick;
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
    public void setOperationSid(Long operationSid) {
        this.operationSid = operationSid;
    }
}
