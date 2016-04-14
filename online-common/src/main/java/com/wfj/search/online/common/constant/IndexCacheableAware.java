package com.wfj.search.online.common.constant;

/**
 * 索引用Cacheable Value共享常量
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public final class IndexCacheableAware {
    private IndexCacheableAware() {
        throw new AssertionError("No com.wfj.search.online.common.constant.IndexCacheableAware instances for you!");
    }

    /**
     * 索引过程记录抓取分页长度
     */
    public static final String VALUE_KEY_INDEX_CONFIG_FETCH_SIZE = "WFJ_ONLINE_INDEX_FETCH_SIZE";
    /**
     * 索引过程抓取线程数
     */
    public static final String VALUE_KEY_INDEX_CONFIG_PRO_FETCH_THREADS = "WFJ_ONLINE_INDEX_FETCH_THREADS";
    /**
     * 手工加权
     */
    public static final String VALUE_KEY_INDEX_VARIABLE_MANUAL_BOOSTS = "WFJ_ONLINE_INDEX_V_MANUAL_BOOSTS";
    /**
     * 专柜商品销量缓存
     */
    public static final String VALUE_KEY_SALE_COUNT_ITEM = "WFJ_ONLINE_INDEX_SALES_ITEM";
    /**
     * sku销量缓存
     */
    public static final String VALUE_KEY_SALE_COUNT_SKU = "WFJ_ONLINE_INDEX_SALES_SKU";
    /**
     * spu销量缓存
     */
    public static final String VALUE_KEY_SALE_COUNT_SPU = "WFJ_ONLINE_INDEX_SALES_SPU";
    /**
     * 品牌坑位缓存
     */
    public static final String VALUE_KEY_BRAND_TOP_SPOTS = "WFJ_ONLINE_INDEX_BRAND_TOP_SPOTS";
    /**
     * 分类坑位缓存
     */
    public static final String VALUE_KEY_CATEGORY_TOP_SPOTS = "WFJ_ONLINE_INDEX_CATEGORY_TOP_SPOTS";
}
