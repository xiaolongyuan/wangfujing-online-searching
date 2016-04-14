package com.wfj.search.online.management.console.service.sort.impl;

import com.wfj.search.online.common.pojo.rule.SortRulePojo;
import com.wfj.search.online.management.console.mapper.sort.SortRuleMapper;
import com.wfj.search.online.management.console.service.sort.ISortRuleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * <br/>create at 15-11-20
 *
 * @author liuxh
 * @since 1.0.0
 */
@Service("sortRuleService")
public class SortRuleServiceImpl implements ISortRuleService {
    @Autowired
    private SortRuleMapper sortRuleMapper;

    @Override
    public List<SortRulePojo> listWithPages(String channel, int start, int limit) {
        if (start < 0) {
            start = 0;
        }
        if (limit <= 0) {
            return Collections.emptyList();
        }
        return sortRuleMapper.listWithPages(channel, start, limit);
    }

    @Override
    public int count(String channel) {
        return sortRuleMapper.count(channel);
    }

    @Override
    public int add(SortRulePojo pojo) {
        if (pojo == null
                || StringUtils.isBlank(pojo.getChannel())
                || StringUtils.isBlank(pojo.getOrderField())
                || StringUtils.isBlank(pojo.getShowText())
                || StringUtils.isBlank(pojo.getDefaultOrderBy())) {
            return -1;
        }
        SortRulePojo other = sortRuleMapper.get(pojo.getChannel(), pojo.getOrderField());
        if (other == null) {
            return sortRuleMapper.add(pojo);
        }
        return -2;
    }

    @Override
    public int update(SortRulePojo pojo) {
        if (pojo == null
                || StringUtils.isBlank(pojo.getChannel())
                || StringUtils.isBlank(pojo.getOrderField())
                || StringUtils.isBlank(pojo.getShowText())
                || StringUtils.isBlank(pojo.getDefaultOrderBy())) {
            return -1;
        }
        SortRulePojo other = sortRuleMapper.get(pojo.getChannel(), pojo.getOrderField());
        if (other == null) {
            return -2;
        }
        return sortRuleMapper.update(pojo);
    }

    @Override
    public int delete(String channel, String orderField) {
        if (StringUtils.isBlank(channel) || StringUtils.isBlank(orderField)) {
            return -1;
        }
        return sortRuleMapper.delete(channel, orderField);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeDefault(String channel, String orderField) {
        if (StringUtils.isBlank(channel)) {
            return -1;
        }
        sortRuleMapper.unDefault(channel);
        SortRulePojo other = sortRuleMapper.get(channel, orderField);
        if (other == null) {
            return -2;
        }
        return sortRuleMapper.defaulted(channel, orderField);
    }
}
