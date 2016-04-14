package com.wfj.search.online.web.service.impl;

import com.wfj.search.online.common.pojo.interval.IntervalContentPojo;
import com.wfj.search.online.common.pojo.interval.IntervalDetailPojo;
import com.wfj.search.online.web.mapper.RangeRuleMapper;
import com.wfj.search.online.web.service.IRangeRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_SEARCH_CONFIG_RANG_RULES;
import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_SEARCH_CONFIG_RANG_RULE_DETAILS;

/**
 * <p>create at 15-11-23</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service("rangeRuleService")
public class RangeRuleServiceImpl implements IRangeRuleService {
    @Autowired
    private RangeRuleMapper rangeRuleMapper;

    @Override
    @Cacheable(VALUE_KEY_SEARCH_CONFIG_RANG_RULES)
    public List<IntervalContentPojo> listRangeRules(String channel) {
        return this.rangeRuleMapper.listRangeRules(channel);
    }

    @Override
    @Cacheable(VALUE_KEY_SEARCH_CONFIG_RANG_RULE_DETAILS)
    public List<IntervalDetailPojo> listRangeRuleDetails(String ruleSid) {
        return this.rangeRuleMapper.listRangeRuleDetails(ruleSid);
    }
}
