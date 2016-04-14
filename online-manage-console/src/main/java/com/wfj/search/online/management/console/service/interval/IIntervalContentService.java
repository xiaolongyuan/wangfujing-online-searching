package com.wfj.search.online.management.console.service.interval;

import com.wfj.search.online.common.pojo.interval.IntervalContentPojo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.util.List;

import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_SEARCH_CONFIG_RANG_RULES;
import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_SEARCH_CONFIG_RANG_RULE_DETAILS;

/**
 * <br/>create at 15-8-10
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
public interface IIntervalContentService {
    List<IntervalContentPojo> intervalContentListWithPage(IntervalContentPojo intervalContent, int start, int limit);

    long intervalContentTotal(IntervalContentPojo intervalContent);

    int addIntervalContent(IntervalContentPojo content);

    int updateIntervalContent(IntervalContentPojo content);

    int deleteIntervalContent(String sid);

    int doSelectedInterval(String sid);

    /**
     * 清除价格区间缓存
     */
    @Caching(evict = {
            @CacheEvict(value = VALUE_KEY_SEARCH_CONFIG_RANG_RULES, allEntries = true),
            @CacheEvict(value = VALUE_KEY_SEARCH_CONFIG_RANG_RULE_DETAILS, allEntries = true)
    })
    default void clearCache() {
    }
}
