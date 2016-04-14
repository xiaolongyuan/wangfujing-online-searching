package com.wfj.search.online.management.console.mapper.interval;

import com.wfj.search.online.common.pojo.interval.IntervalDetailPojo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br/>create at 15-8-10
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface IntervalDetailMapper {
    List<IntervalDetailPojo> intervalDetailListWithPage(@Param("intervalDetail") IntervalDetailPojo intervalDetail,
            @Param("start") int start, @Param("limit") int limit);

    long intervalDetailTotal(@Param("intervalDetail") IntervalDetailPojo intervalDetail);

    List<IntervalDetailPojo> listDetailByContentSid(String contentSid);

    int addIntervalDetail(IntervalDetailPojo detail);

    int updateIntervalDetail(IntervalDetailPojo detail);

    int deleteIntervalDetail(String sid);

    void deleteIntervalDetailByContentSid(String contentSid);
}
