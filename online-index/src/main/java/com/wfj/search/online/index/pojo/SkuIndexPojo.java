package com.wfj.search.online.index.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * SKU索引POJO
 * <p>create at 15-10-30</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class SkuIndexPojo {
    private String skuId; // SKU编码
    private String spuId; // 所属SPU编码
    private Integer type; // 商品类型
    private Boolean onSell; // 是否在售、上架
    private String title;
    private String subTitle;
    private List<String> activeKeywords;
    private String colorId; // 色系编码
    private String colorName; // 色系名称
    private String colorAlias; // 颜色别名，色
    private String standardId; // 规格编码
    private String standardName; // 规格名称
    private List<String> pictures; // 图片信息, 格式：展示顺序号-是否颜色主图（0/1）-图片地址
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8", shape = JsonFormat.Shape.STRING)
    private Date upTime; // 上架时间
    private Long operationSid; // 操作记录号

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
     * 所属SPU编码
     *
     * @return 所属SPU编码
     */
    public String getSpuId() {
        return spuId;
    }

    /**
     * 所属SPU编码
     *
     * @param spuId 所属SPU编码
     */
    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    /**
     * 商品类型
     *
     * @return 商品类型
     */
    public Integer getType() {
        return type;
    }

    /**
     * 商品类型
     *
     * @param type 商品类型
     */
    public void setType(Integer type) {
        this.type = type;
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
     * 颜色别名，色码
     *
     * @return 颜色别名，色码
     */
    public String getColorAlias() {
        return colorAlias;
    }

    /**
     * 颜色别名，色码
     *
     * @param colorAlias 颜色别名，色码
     */
    public void setColorAlias(String colorAlias) {
        this.colorAlias = colorAlias;
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
    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    /**
     * 上架时间
     *
     * @return 上架时间
     */
    @SuppressWarnings("unused")
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
}
