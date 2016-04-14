package com.wfj.search.online.web.mapper;

import com.wfj.search.online.common.pojo.interval.IntervalContentPojo;
import com.wfj.search.online.common.pojo.interval.IntervalDetailPojo;

import java.util.List;

/**
 * <p>create at 15-11-23</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface RangeRuleMapper {
    List<IntervalContentPojo> listRangeRules(String channel);

    List<IntervalDetailPojo> listRangeRuleDetails(String ruleSid);
}
