package com.wfj.search.online.common.pojo.boost;

/**
 * 手工加权
 * <p>create at 15-11-10</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class ManualBoostPojo {
    private String skuId;
    private float boost = 1;

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public float getBoost() {
        return boost;
    }

    public void setBoost(float boost) {
        this.boost = boost;
    }
}
