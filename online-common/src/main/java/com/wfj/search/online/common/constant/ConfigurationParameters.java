package com.wfj.search.online.common.constant;

import org.apache.solr.common.params.DisMaxParams;

/**
 * <br/>create at 15-11-5
 *
 * @author liuxh
 * @since 1.0.0
 */
// DDD check unused
public final class ConfigurationParameters {
    private ConfigurationParameters() {
        throw new AssertionError("No com.wfj.search.online.common.constant.ConfigurationParameters instances for you!");
    }

    /**
     * 索引过程专柜商品记录抓取分页长度
     */
    public static final String CONFIG_ITEMS_FETCH_SIZE = "itemsFetchSize";
    /**
     * 索引过程专柜商品抓取线程数
     */
    public static final String CONFIG_FETCH_THREADS = "productFetchThreads";
    /**
     * 是否启用搜索规格筛选条件
     */
    public static final String CONFIG_STANDARD_CONDITION_ENABLED = "standardConditionEnabled";
    /**
     * 是否启用搜索颜色筛选条件
     */
    public static final String CONFIG_COLOR_CONDITION_ENABLED = "colorConditionEnabled";
    /**
     * DisMax查询qf项配置
     */
    public static final String CONFIG_QF = DisMaxParams.QF;
    /**
     * DisMax查询mm配置
     */
    public static final String CONFIG_MM = DisMaxParams.MM;
    /**
     * DisMax查询bf配置
     */
    public static final String CONFIG_BF = DisMaxParams.BF;
    public static final String CONFIG_TIE = DisMaxParams.TIE;
    public static final String CONFIG_PF = DisMaxParams.PF;
    public static final String CONFIG_PS = DisMaxParams.PS;
    public static final String CONFIG_QS = DisMaxParams.QS;
    public static final String CONFIG_BQ = DisMaxParams.BQ;
    /**
     * 简单查询返回条数
     */
    public static final String CONFIG_SIMPLE_ROWS = "simpleRows";
    // ########################front config param############################
    public static final String CONFIG_WEB_ROWS = "web_rows";
    public static final String CONFIG_MAY_LIKE_ROWS = "may_like_rows";
    /**
     * 分页组件显示分页数量
     */
    public static final String CONFIG_NUM_DISPLAY_ENTRIES = "num_display_entries";
    /**
     * 内网WWW地址
     */
    public static final String CONFIG_WEB_DEFAULT_SITE = "web_default_site";
    public static final String CONFIG_WEB_DEFAULT_CHANNEL = "web_default_channel";
    public static final String CONFIG_NEW_PRODUCT_DATE_FROM = "new_product_date_from";
}
