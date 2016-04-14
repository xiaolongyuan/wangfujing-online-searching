package com.wfj.search.online.web.service.impl;

import com.wfj.search.online.common.pojo.rule.SortRulePojo;
import com.wfj.search.online.web.mapper.SortMapper;
import com.wfj.search.online.web.service.ISortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.wfj.search.online.common.constant.CacheableAware.*;

/**
 * <p>create at 15-11-20</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service("sortService")
public class SortServiceImpl implements ISortService {
    @Autowired
    private SortMapper sortMapper;

    @Override
    @Cacheable(VALUE_KEY_SEARCH_DEFAULT_SORT_RULE)
    public SortRulePojo findDefaultSortRule(String channel) {
        return this.sortMapper.findDefaultSortRule(channel);
    }

    @Override
    @Cacheable(VALUE_KEY_SEARCH_CONFIG_SORT_RULE)
    public SortRulePojo getSortRule(long sid) {
        return this.sortMapper.getSortRuleBySid(sid);
    }

    @Override
    @Cacheable(VALUE_KEY_SEARCH_CONFIG_SORT_RULES_IN_ORDER)
    public List<SortRulePojo> listAllSort(String channel) {
        return this.sortMapper.listAllSortOfChannel(channel);
    }
}
