package com.wfj.search.online.management.console.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import static com.wfj.search.online.common.constant.CacheableAware.*;
import static com.wfj.search.online.common.constant.IndexCacheableAware.*;

/**
 * <p>create at 15-12-21</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public interface ICacheService {
    /**
     * 清除指定类型黑名单缓存
     *
     * @param type 黑名单类型，可用：ITEM/SKU/SPU/BRAND
     */
    @SuppressWarnings("UnusedParameters")
    @CacheEvict(value = VALUE_KEY_INDEX_BLACKLIST)
    default void clearBlacklistCacheOfType(String type) {
    }

    /**
     * 清除手工加权缓存
     */
    @CacheEvict(value = VALUE_KEY_INDEX_VARIABLE_MANUAL_BOOSTS, allEntries = true)
    public default void invalidManualBoostsCache() {
    }

    /**
     * 清除品牌置顶坑位缓存
     */
    @CacheEvict(value = VALUE_KEY_BRAND_TOP_SPOTS, allEntries = true)
    default void clearBrandTopSpotsCache() {
    }

    /**
     * 清除分类置顶坑位缓存
     */
    @CacheEvict(value = VALUE_KEY_CATEGORY_TOP_SPOTS, allEntries = true)
    default void clearCategoryTopSpotsCache() {
    }

    @Caching(evict = {
            @CacheEvict(value = VALUE_KEY_BRAND, allEntries = true),// 品牌实体
            @CacheEvict(value = VALUE_KEY_CATEGORY, allEntries = true),// 分类实体
            @CacheEvict(value = VALUE_KEY_COLOR, allEntries = true),// 颜色实体
            @CacheEvict(value = VALUE_KEY_PROPERTY, allEntries = true),// 属性实体
            @CacheEvict(value = VALUE_KEY_PROPERTY_VALUE, allEntries = true),// 属性值实体
            @CacheEvict(value = VALUE_KEY_SEARCH_CONFIG_RANG_RULES, allEntries = true),// 区间实体
            @CacheEvict(value = VALUE_KEY_SEARCH_CONFIG_RANG_RULE_DETAILS, allEntries = true),// 区间明细实体
            @CacheEvict(value = VALUE_KEY_SEARCH_DEFAULT_SORT_RULE, allEntries = true),// 频道默认排序规则
            @CacheEvict(value = VALUE_KEY_SEARCH_CONFIG_SORT_RULE, allEntries = true),// 排序规则实体
            @CacheEvict(value = VALUE_KEY_SEARCH_CONFIG_SORT_RULES_IN_ORDER, allEntries = true),// 频道排序规则实体
            @CacheEvict(value = VALUE_KEY_STANDARD, allEntries = true),// 规格实体
            @CacheEvict(value = VALUE_KEY_TAG, allEntries = true),// 标签实体
            @CacheEvict(value = VALUE_KEY_DEFAULT_SUGGESTION_KEY_WORD, allEntries = true),// 默认建议词
            @CacheEvict(value = VALUE_KEY_SUGGESTION_KEY_WORD, allEntries = true),// 建议词
            @CacheEvict(value = VALUE_KEY_SEARCH_CONFIG_WEB_ROWS, allEntries = true),//WEB单页返回结果数
            @CacheEvict(value = VALUE_KEY_SEARCH_WEB_DO_SEARCH, allEntries = true), //WEB搜索service层结果缓存
            @CacheEvict(value = VALUE_KEY_SEARCH_WEB_HTML_COMMON_PART, allEntries = true),
            @CacheEvict(value = VALUE_KEY_HOT_WORDS, allEntries = true),// 热词缓存
            @CacheEvict(value = VALUE_KEY_HOT_WORDS_PAGES, allEntries = true),// 热词分页缓存
    })
    default void clearAllSearchingCache() {
    }

    @CacheEvict(value = VALUE_KEY_HOT_WORDS)
    @SuppressWarnings("UnusedParameters")
    default void clearHotWords(String siteId, String channelId) {
    }

    @Caching(evict = {
            @CacheEvict(value = VALUE_KEY_HOT_WORDS_PAGES, allEntries = true),// 热词分页缓存
    })
    default void clearHotWordsPages() {
    }
}
