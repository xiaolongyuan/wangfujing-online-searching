package com.wfj.search.online.web.common.pojo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>create at 15-9-22</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class ItemDisplayPojo extends AbstractDisplayPojo {
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
    private List<String> activeName = Collections.synchronizedList(Lists.newArrayList());
    private String title;
    private String subTitle;
    private List<String> categoryIds = Collections.synchronizedList(Lists.newArrayList());
    private String longDesc;
    private String shortDesc;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getStockMode() {
        return stockMode;
    }

    public void setStockMode(Integer stockMode) {
        this.stockMode = stockMode;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

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

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

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

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getActiveBit() {
        return activeBit;
    }

    public void setActiveBit(Integer activeBit) {
        this.activeBit = activeBit;
    }

    public Date getOnSellSince() {
        return onSellSince;
    }

    public void setOnSellSince(Date onSellSince) {
        this.onSellSince = onSellSince;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setActiveName(List<String> activeName) {
        this.activeName = activeName;
    }

    public List<String> getActiveName() {
        return activeName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
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
}
