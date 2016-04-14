package com.wfj.search.online.management.console.service.interval;

import com.wfj.search.online.common.pojo.interval.IntervalDetailPojo;

import java.util.List;

/**
 * <br/>create at 15-8-10
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface IIntervalDetailService {
    List<IntervalDetailPojo> intervalDetailListWithPage(IntervalDetailPojo intervalDetail, int start, int limit);

    long intervalDetailTotal(IntervalDetailPojo intervalDetail);

    int createIntervalDetail(IntervalDetailPojo detail);

    int updateIntervalDetail(IntervalDetailPojo detail);

    int deleteIntervalDetail(IntervalDetailPojo detail);
}
