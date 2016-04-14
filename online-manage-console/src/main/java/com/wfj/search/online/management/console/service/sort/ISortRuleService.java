package com.wfj.search.online.management.console.service.sort;

import com.wfj.search.online.common.pojo.rule.SortRulePojo;

import java.util.List;

/**
 * <br/>create at 15-11-20
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface ISortRuleService {
    List<SortRulePojo> listWithPages(String channel, int start, int limit);

    int count(String channel);

    int add(SortRulePojo pojo);

    int update(SortRulePojo pojo);

    int delete(String channel, String orderField);

    int changeDefault(String channel, String orderField);
}
