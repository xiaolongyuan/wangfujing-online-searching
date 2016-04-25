package com.wfj.search.online.web.common.pojo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * SPU
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public class SpuDisplayPojo extends AbstractDisplayPojo {
    private String itemId;
    private String supplierId;
    private Integer stockMode;
    private Integer inventory;
    private Double originalPrice;
    private Double currentPrice;
    private Double discountRate;
    private String skuId;
    private String colorId;
    private String colorName;
    private String colorMasterPicture;
    private Map<String, String> colorMasterPictureOfPix = Maps.newHashMap();
    private Integer type;
    private String spuId;
    private String spuName;
    private String model;
    private Integer activeBit;
    private Date onSellSince;
    private String brandId;
    private String brandName;
    private List<ItemDisplayPojo> colorItems = Collections.synchronizedList(Lists.newArrayList());
    private List<String> activeName = Collections.synchronizedList(Lists.newArrayList());
    private String title;
    private String subTitle;
    private List<String> categoryIds = Collections.synchronizedList(Lists.newArrayList());
    private List<CategoryDisplayPojo> leafCategories = Collections.synchronizedList(Lists.newArrayList());
    private String moreUrl = "#";
    private String longDesc;
    private String shortDesc;

    @SuppressWarnings("unused")
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @SuppressWarnings("unused")
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @SuppressWarnings("unused")
    public Integer getStockMode() {
        return stockMode;
    }

    public void setStockMode(Integer stockMode) {
        this.stockMode = stockMode;
    }

    @SuppressWarnings("unused")
    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    @SuppressWarnings("unused")
    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    @SuppressWarnings("unused")
    public Double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    @SuppressWarnings("unused")
    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    @SuppressWarnings("unused")
    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getColorMasterPicture() {
        return colorMasterPicture;
    }

    public void setColorMasterPicture(String colorMasterPicture) {
        this.colorMasterPicture = colorMasterPicture;
    }

    public Map<String, String> getColorMasterPictureOfPix() {
        return colorMasterPictureOfPix;
    }

    @SuppressWarnings("unused")
    public void setColorMasterPictureOfPix(Map<String, String> colorMasterPictureOfPix) {
        this.colorMasterPictureOfPix = colorMasterPictureOfPix;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    @SuppressWarnings("unused")
    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    @SuppressWarnings("unused")
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @SuppressWarnings("unused")
    public Integer getActiveBit() {
        return activeBit;
    }

    public void setActiveBit(Integer activeBit) {
        this.activeBit = activeBit;
    }

    @SuppressWarnings("unused")
    public Date getOnSellSince() {
        return onSellSince;
    }

    public void setOnSellSince(Date onSellSince) {
        this.onSellSince = onSellSince;
    }

    @SuppressWarnings("unused")
    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    @SuppressWarnings("unused")
    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public List<ItemDisplayPojo> getColorItems() {
        return colorItems;
    }

    public List<String> getActiveName() {
        return activeName;
    }

    @SuppressWarnings("unused")
    public void setActiveName(List<String> activeName) {
        this.activeName = activeName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @SuppressWarnings("unused")
    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<CategoryDisplayPojo> getLeafCategories() {
        return leafCategories;
    }

    @SuppressWarnings("unused")
    public void setLeafCategories(List<CategoryDisplayPojo> leafCategories) {
        this.leafCategories = leafCategories;
    }

    public String getMoreUrl() {
        return moreUrl;
    }

    public void setMoreUrl(String moreUrl) {
        this.moreUrl = moreUrl;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public static SpuDisplayPojo copyOf(ItemDisplayPojo item) {
        SpuDisplayPojo pojo = new SpuDisplayPojo();
        pojo.setItemId(item.getItemId());
        pojo.setSupplierId(item.getSupplierId());
        pojo.setStockMode(item.getStockMode());
        pojo.setInventory(item.getInventory());
        pojo.setOriginalPrice(item.getOriginalPrice());
        pojo.setCurrentPrice(item.getCurrentPrice());
        pojo.setDiscountRate(item.getDiscountRate());
        pojo.setSkuId(item.getSkuId());
        pojo.setColorId(item.getColorId());
        pojo.setColorName(item.getColorName());
        pojo.setColorMasterPicture(item.getColorMasterPicture());
        pojo.getColorMasterPictureOfPix().putAll(item.getColorMasterPictureOfPix());
        pojo.setType(item.getType());
        pojo.setSpuId(item.getSpuId());
        pojo.setSpuName(item.getSpuName());
        pojo.setModel(item.getModel());
        pojo.setActiveBit(item.getActiveBit());
        pojo.setOnSellSince(item.getOnSellSince());
        pojo.setBrandId(item.getBrandId());
        pojo.setBrandName(item.getBrandName());
        pojo.setTitle(item.getTitle());
        pojo.setSubTitle(item.getSubTitle());
        pojo.setCategoryIds(item.getCategoryIds());
        pojo.setLongDesc(item.getLongDesc());
        pojo.setShortDesc(item.getShortDesc());
        return pojo;
    }

    /**
     * 仅用于缓存反序列化，不要手工调用！
     */
    @SuppressWarnings("unused")
    public void setColorItems(List<ItemDisplayPojo> colorItems) {
        this.colorItems = colorItems;
    }
}
