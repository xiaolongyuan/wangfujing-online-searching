package com.wfj.search.online.common.constant;

/**
 * Cacheable Value共享常量
 * <p>create at 15-9-21</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public final class CacheableAware {
    private CacheableAware() {
        throw new AssertionError("No com.wfj.search.online.common.constant.CacheableAware instances for you!");
    }

    /**
     * 价格区间规则缓存
     */
    public static final String VALUE_KEY_SEARCH_CONFIG_RANG_RULES = "WFJ_ONLINE_SEARCH_RANG_RULES";
    /**
     * 价格区间规则明细缓存
     */
    public static final String VALUE_KEY_SEARCH_CONFIG_RANG_RULE_DETAILS = "WFJ_ONLINE_SEARCH_RANG_RULE_DETAILS";
    /**
     * 排序规则缓存
     */
    public static final String VALUE_KEY_SEARCH_CONFIG_SORT_RULE = "WFJ_ONLINE_SEARCH_SORT_RULE";
    /**
     * 排序规则列表（已按展示顺序排序）缓存
     */
    public static final String VALUE_KEY_SEARCH_CONFIG_SORT_RULES_IN_ORDER = "WFJ_ONLINE_SEARCH_SORT_RULES_IN_ORDER";
    /**
     * 渠道默认排序规则缓存
     */
    public static final String VALUE_KEY_SEARCH_DEFAULT_SORT_RULE = "WFJ_ONLINE_SEARCH_CHANNEL_DEF_SORT_RULE";
    /**
     * URL序号(用户追踪统计用，与页面展示元素无关)缓存
     */
    public static final String VALUE_KEY_URL_CODE = "WFJ_ONLINE_URL_CODE";
    /**
     * 品牌缓存
     */
    public static final String VALUE_KEY_BRAND = "WFJ_ONLINE_BRAND";
    /**
     * 分类缓存
     */
    public static final String VALUE_KEY_CATEGORY = "WFJ_ONLINE_CATEGORY";
    /**
     * 色系缓存
     */
    public static final String VALUE_KEY_COLOR = "WFJ_ONLINE_COLOR";
    /**
     * 属性缓存
     */
    public static final String VALUE_KEY_PROPERTY = "WFJ_ONLINE_PROPERTY";
    /**
     * 属性值缓存
     */
    public static final String VALUE_KEY_PROPERTY_VALUE = "WFJ_ONLINE_PROPERTY_VALUE";
    /**
     * 规格缓存
     */
    public static final String VALUE_KEY_STANDARD = "WFJ_ONLINE_STANDARD";
    /**
     * 标签缓存
     */
    public static final String VALUE_KEY_TAG = "WFJ_ONLINE_TAG";
    /**
     * 评论缓存
     */
    public static final String VALUE_KEY_COMMENT = "WFJ_ONLINE_COMMENT";
    /**
     * 黑名单列表缓存
     */
    public static final String VALUE_KEY_INDEX_BLACKLIST = "WFJ_ONLINE_INDEX_BLACKLIST";
    /**
     * 默认建议词缓存
     */
    public static final String VALUE_KEY_DEFAULT_SUGGESTION_KEY_WORD = "WFJ_ONLINE_INDEX_DEFAULT_SUGGESTION_KEY_WORD";
    /**
     * 建议词缓存
     */
    public static final String VALUE_KEY_SUGGESTION_KEY_WORD = "WFJ_ONLINE_INDEX_SUGGESTION_KEY_WORD";
    /**
     * WEB单页返回结果数
     */
    public static final String VALUE_KEY_SEARCH_CONFIG_WEB_ROWS = "WFJ_ONLINE_WEB_SEARCH_WEB_ROWS";
    /**
     * WEB搜索service层结果缓存
     */
    public static final String VALUE_KEY_SEARCH_WEB_DO_SEARCH = "WFJ_ONLINE_WEB_SEARCH_DO_SEARCH";
    /**
     * 搜索用户评论缓存
     */
    public static final String VALUE_KEY_SEARCH_COMMENT = "WFJ_ONLINE_WEB_SEARCH_COMMENT";
    /**
     * WEB搜索Html块内容缓存
     */
    public static final String VALUE_KEY_SEARCH_WEB_HTML_COMMON_PART = "WFJ_ONLINE_WEB_SEARCH_HTML_COMMON_PART";
    /**
     * 热词列表缓存
     */
    public static final String VALUE_KEY_HOT_WORDS = "WFJ_ONLINE_HOT_WORDS";
    public static final String VALUE_KEY_HOT_WORDS_PAGES = "WFJ_ONLINE_HOT_WORDS_PAGES";
}
