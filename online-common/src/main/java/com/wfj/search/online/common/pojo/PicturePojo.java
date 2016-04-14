package com.wfj.search.online.common.pojo;

/**
 * 产品图片POJO
 * <br/>create at 15-7-3
 *
 * @author liufl
 * @since 1.0.0
 */
public class PicturePojo {
    private String pictureSid;
    private String picture;
    private int order;
    private String colorId;
    private String colorAlias;
    private boolean colorMaster;
    private String size = "1000x1000";

    /**
     * 图片ID
     *
     * @return 图片ID
     */
    @SuppressWarnings("unused")
    public String getPictureSid() {
        return pictureSid;
    }

    /**
     * 图片ID
     *
     * @param pictureSid 图片ID
     */
    public void setPictureSid(String pictureSid) {
        this.pictureSid = pictureSid;
    }

    /**
     * 图片地址
     *
     * @return 图片地址
     */
    public String getPicture() {
        return picture;
    }

    /**
     * 图片地址
     *
     * @param picture 图片地址
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * 图片展示顺序
     *
     * @return 图片展示顺序
     */
    public int getOrder() {
        return order;
    }

    /**
     * 图片展示顺序
     *
     * @param order 图片展示顺序
     */
    public void setOrder(int order) {
        this.order = order;
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
     * 是否颜色主图
     *
     * @return 是否颜色主图
     */
    public boolean isColorMaster() {
        return colorMaster;
    }

    /**
     * 是否颜色主图
     *
     * @param colorMaster 是否颜色主图
     */
    public void setColorMaster(boolean colorMaster) {
        this.colorMaster = colorMaster;
    }

    /**
     * 图片分辨率
     *
     * @return 图片分辨率
     */
    public String getSize() {
        return size;
    }

    /**
     * 图片分辨率
     *
     * @param size 图片分辨率
     */
    public void setSize(String size) {
        this.size = size;
    }
}
