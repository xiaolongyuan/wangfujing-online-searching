package com.wfj.search.online.web.mapper;

import com.wfj.search.online.common.pojo.rule.SortRulePojo;

import java.util.List;

/**
 * <p>create at 15-11-20</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface SortMapper {
    SortRulePojo findDefaultSortRule(String channel);

    SortRulePojo getSortRuleBySid(long sid);

    List<SortRulePojo> listAllSortOfChannel(String channel);
}
