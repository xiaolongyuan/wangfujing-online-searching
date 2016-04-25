package com.wfj.search.online.web.common.constant;

/**
 * Cacheable Value共享常量
 * <p>create at 15-9-21</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
// DDD check unused
public final class CacheableAware {
    private CacheableAware() {
        throw new AssertionError("No com.wfj.search.online.common.constant.CacheableAware instances for you!");
    }

    /**
     * DisMax查询tie项配置
     */
    public static final String VALUE_KEY_SEARCH_CONFIG_TIE = "WFJ_ONLINE_WEB_SEARCH_TIE";
    /**
     * DisMax查询qf项配置
     */
    public static final String VALUE_KEY_SEARCH_CONFIG_QF = "WFJ_ONLINE_WEB_SEARCH_QF";
    /**
     * DisMax查询qs项配置
     */
    public static final String VALUE_KEY_SEARCH_CONFIG_QS = "WFJ_ONLINE_WEB_SEARCH_QS";
    /**
     * DisMax查询mm配置
     */
    public static final String VALUE_KEY_SEARCH_CONFIG_MM = "WFJ_ONLINE_WEB_SEARCH_MM";
    /**
     * DisMax查询bq配置
     */
    public static final String VALUE_KEY_SEARCH_CONFIG_BQ = "WFJ_ONLINE_WEB_SEARCH_BQ";
    /**
     * 简单查询返回条数
     */
    public static final String VALUE_KEY_SEARCH_CONFIG_SIMPLE_ROWS = "WFJ_ONLINE_WEB_SEARCH_SIMPLE_ROWS";
    // VALUE_KEY_URL_CODE 使用online-common中的常量
    /**
     * 搜索页面分页组件显示数量
     */
    public static final String VALUE_KEY_SEARCH_PAGINATION_NUM_DISPLAY_ENTRIES = "WFJ_ONLINE_WEB_SEARCH_NUM_DISPLAY_ENTRIES";
    public static final String VALUE_KEY_SEARCH_CONFIG_MAY_LIKE_ROWS = "WFJ_ONLINE_WEB_SEARCH_MAY_LIKE_ROWS";
    /**
     * 内网WWW 地址
     */
    public static final String VALUE_KEY_SEARCH_CONFIG_DEFAULT_WEB_SITE = "WFJ_ONLINE_WEB_SEARCH_DEFAULT_WEB_SITE";
    public static final String VALUE_KEY_SEARCH_CONFIG_DEFAULT_WEB_CHANNEL = "WFJ_ONLINE_WEB_SEARCH_DEFAULT_WEB_CHANNEL";
    public static final String VALUE_KEY_SEARCH_CONFIG_NEW_PRODUCT_DATE_FROM = "WFJ_ONLINE_WEB_SEARCH_NEW_PRODUCT_DATE_FROM";
}
