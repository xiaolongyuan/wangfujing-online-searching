package com.wfj.search.online.common.pojo.popularize;

/**
 * <br/>create at 15-8-21
 *
 * @author liuxh
 * @since 1.0.0
 */
public class BrandPopularizePositionPojo {
    private String sid;// 编号
    private String brandId;// 品牌id
    private String spuId;// spu id
    private int orders;// 顺序
    private String createTime;// 创建时间
    private String createOperator;// 创建操作人

    public BrandPopularizePositionPojo() {
    }

    public BrandPopularizePositionPojo(String sid, String brandId, String spuId, int orders) {
        this.sid = sid;
        this.brandId = brandId;
        this.spuId = spuId;
        this.orders = orders;
    }

    public BrandPopularizePositionPojo(String brandId, String spuId, int orders, String createOperator) {
        this.brandId = brandId;
        this.spuId = spuId;
        this.orders = orders;
        this.createOperator = createOperator;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateOperator() {
        return createOperator;
    }

    public void setCreateOperator(String createOperator) {
        this.createOperator = createOperator;
    }
}
