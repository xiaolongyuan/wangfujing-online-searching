package com.wfj.search.online.common.pojo.popularize;

import com.wfj.search.online.common.pojo.ChangeLogPojo;

/**
 * <br/>create at 15-11-2
 *
 * @author liuxh
 * @since 1.0.0
 */
public class CategoryPopularizePositionLogPojo extends ChangeLogPojo {
    private String categoryId;// 品牌id
    private String spuId;// spu id
    private int orders;// 顺序

    public CategoryPopularizePositionLogPojo() {
    }

    public CategoryPopularizePositionLogPojo(String categoryId, String spuId) {
        this.categoryId = categoryId;
        this.spuId = spuId;
    }

    public CategoryPopularizePositionLogPojo(String categoryId, String spuId, int orders, String modifier,
            ModifyType modifyType) {
        this.categoryId = categoryId;
        this.spuId = spuId;
        this.orders = orders;
        this.modifier = modifier;
        this.modifyType = modifyType.getType();
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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
