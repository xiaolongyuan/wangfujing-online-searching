package com.wfj.search.online.management.console.mapper.sort;

import com.wfj.search.online.common.pojo.rule.SortRulePojo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br/>create at 15-11-20
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface SortRuleMapper {
    List<SortRulePojo> listWithPages(@Param("channel") String channel, @Param("start") int start,
            @Param("limit") int limit);

    int count(@Param("channel") String channel);

    int add(SortRulePojo pojo);

    int update(SortRulePojo pojo);

    int delete(@Param("channel") String channel, @Param("orderField") String orderField);

    int unDefault(@Param("channel") String channel);

    int defaulted(@Param("channel") String channel, @Param("orderField") String orderField);

    SortRulePojo get(@Param("channel") String channel, @Param("orderField") String orderField);
}
