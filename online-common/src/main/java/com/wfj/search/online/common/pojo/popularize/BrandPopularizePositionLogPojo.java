package com.wfj.search.online.common.pojo.popularize;

import com.wfj.search.online.common.pojo.ChangeLogPojo;

/**
 * <br/>create at 15-11-2
 *
 * @author liuxh
 * @since 1.0.0
 */
public class BrandPopularizePositionLogPojo extends ChangeLogPojo {
    private String brandId;// 品牌id
    private String spuId;// spu id
    private int orders;// 顺序

    public BrandPopularizePositionLogPojo() {
    }

    public BrandPopularizePositionLogPojo(String brandId, String spuId, int orders, String modifier,
            ModifyType modifyType) {
        this.brandId = brandId;
        this.spuId = spuId;
        this.orders = orders;
        this.modifier = modifier;
        this.modifyType = modifyType.getType();
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
}
