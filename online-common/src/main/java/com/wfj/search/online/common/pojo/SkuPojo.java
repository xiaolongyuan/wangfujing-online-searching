package com.wfj.search.online.common.pojo;

import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;

/**
 * SKU POJO
 * <p>create at 15-9-7</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class SkuPojo {
    private String skuId;
    private String spuId;
    private int type;
    private boolean onSell;// 线上可售状态
    private String title;
    private String subTitle;
    private List<String> activeKeywords = Lists.newArrayList();
    private String colorId;
    private String colorName;
    private String colorAlias;
    private StandardPojo standardPojo;
    private List<PicturePojo> pictures = Lists.newArrayList();
    private Date upTime;

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
     * 销售类型， 正常/礼品/赠品
     *
     * @return 销售类型
     */
    public int getType() {
        return type;
    }

    /**
     * 销售类型，正常/礼品/赠品
     *
     * @param type 销售类型
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * 线上可售状态
     *
     * @return 线上可售状态
     */
    public boolean isOnSell() {
        return onSell;
    }

    /**
     * 线上可售状态
     *
     * @param onSell 线上可售状态
     */
    public void setOnSell(boolean onSell) {
        this.onSell = onSell;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public List<String> getActiveKeywords() {
        return activeKeywords;
    }

    @SuppressWarnings("unused")
    public void setActiveKeywords(List<String> activeKeywords) {
        this.activeKeywords = activeKeywords;
    }

    /**
     * 色系编码
     *
     * @return 色系编码
     */
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
     * 规格信息
     *
     * @return 规格信息
     */
    public StandardPojo getStandardPojo() {
        return standardPojo;
    }

    /**
     * 规格信息
     *
     * @param standardPojo 规格信息
     */
    public void setStandardPojo(StandardPojo standardPojo) {
        this.standardPojo = standardPojo;
    }

    /**
     * 图片信息
     *
     * @return 图片信息
     */
    public List<PicturePojo> getPictures() {
        return pictures;
    }

    /**
     * 上架时间
     *
     * @return 上架时间
     */
    public Date getUpTime() {
        return upTime;
    }

    /**
     * 上架时间
     *
     * @param upTime 上架时间
     */
    public void setUpTime(Date upTime) {
        this.upTime = upTime;
    }
}
