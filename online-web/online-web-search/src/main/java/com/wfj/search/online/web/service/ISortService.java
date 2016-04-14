package com.wfj.search.online.web.service;

import com.wfj.search.online.common.pojo.rule.SortRulePojo;

import java.util.List;

/**
 * <p>create at 15-11-20</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface ISortService {
    SortRulePojo findDefaultSortRule(String channel);

    SortRulePojo getSortRule(long sid);

    List<SortRulePojo> listAllSort(String channel);
}
