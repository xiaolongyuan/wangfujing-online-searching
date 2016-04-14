package com.wfj.search.online.common.pojo;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品信息POJO
 * <br/>create at 15-6-30
 *
 * @author liufl
 * @since 1.0.0
 */
public class ItemPojo {
    private String itemId;// 商品编码
    private String skuId;// 所属SKU编码
    private String supplierId;// 供应商编码
    private List<String> channels = Lists.newArrayList();
    private List<ActivityPojo> activities = Lists.newArrayList(); // 活动标签
    private int stockMode; // 库存方式：自库、虚库、不管库存
    private int inventory;// 可售库存量
    private BigDecimal originalPrice;// 原价
    private BigDecimal currentPrice;// 现价
    private double discountRate;// 折扣率

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
     * 所属SKU编码
     *
     * @return 所属SKU编码
     */
    public String getSkuId() {
        return skuId;
    }

    /**
     * 所属SKU编码
     *
     * @param skuId 所属SKU编码
     */
    public void setSkuId(String skuId) {
        this.skuId = skuId;
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

    public List<ActivityPojo> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityPojo> activities) {
        this.activities = activities;
    }

    /**
     * 可售库存量
     *
     * @return 可售库存量
     */
    public int getInventory() {
        return inventory;
    }

    /**
     * 可售库存量
     *
     * @param inventory 可售库存量
     */
    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    /**
     * 原价
     *
     * @return 原价
     */
    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    /**
     * 原价
     *
     * @param originalPrice 原价
     */
    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    /**
     * 现价
     *
     * @return 现价
     */
    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    /**
     * 现价
     *
     * @param currentPrice 现价
     */
    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    /**
     * 折扣率
     *
     * @return 折扣率
     */
    public double getDiscountRate() {
        return discountRate;
    }

    /**
     * 折扣率
     *
     * @param discountRate 折扣率
     */
    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    /**
     * 库存方式：自库、虚库、不管库存
     *
     * @return 库存方式：自库、虚库、不管库存
     */
    public int getStockMode() {
        return stockMode;
    }

    /**
     * 库存方式：自库、虚库、不管库存
     *
     * @param stockMode 库存方式：自库、虚库、不管库存
     */
    public void setStockMode(int stockMode) {
        this.stockMode = stockMode;
    }

    @Override
    public String toString() {
        return "ItemPojo{" +
                "itemId='" + itemId + '\'' +
                ", skuId='" + skuId + '\'' +
                ", supplierId='" + supplierId + '\'' +
                ", channels=" + channels +
                ", activities=" + activities +
                ", stockMode=" + stockMode +
                ", inventory=" + inventory +
                ", originalPrice=" + originalPrice +
                ", currentPrice=" + currentPrice +
                ", discountRate=" + discountRate +
                '}';
    }
}
