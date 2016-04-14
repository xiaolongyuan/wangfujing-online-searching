package com.wfj.search.online.management.console.mapper.interval;

import com.wfj.search.online.common.pojo.interval.IntervalContentPojo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br/>create at 15-8-10
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface IntervalContentMapper {
    List<IntervalContentPojo> intervalContentListWithPage(@Param("intervalContent") IntervalContentPojo intervalContent,
            @Param("start") int start, @Param("limit") int limit);

    long intervalContentTotal(@Param("intervalContent") IntervalContentPojo intervalContent);

    IntervalContentPojo getIntervalContentBySid(String sid);

    List<IntervalContentPojo> getIntervalContentList(IntervalContentPojo content);

    int addIntervalContent(IntervalContentPojo content);

    int updateIntervalContent(IntervalContentPojo content);

    int deleteIntervalContent(String sid);

    int doSelected(@Param("sid") String sid);

    int unselected(@Param("sid") String sid);
}
