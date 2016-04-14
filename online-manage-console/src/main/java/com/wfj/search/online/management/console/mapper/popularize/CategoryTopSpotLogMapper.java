package com.wfj.search.online.management.console.mapper.popularize;

import com.wfj.search.online.common.pojo.popularize.CategoryPopularizePositionLogPojo;

import java.util.List;

/**
 * <br/>create at 15-11-2
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface CategoryTopSpotLogMapper {
    int addLog(CategoryPopularizePositionLogPojo log);

    List<CategoryPopularizePositionLogPojo> listLog();
}
