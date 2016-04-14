package com.wfj.search.online.management.console.mapper.boost;

import com.wfj.search.online.common.pojo.boost.ManualBoostPojo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br/>create at 15-11-10
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface ManualBoostMapper {
    List<ManualBoostPojo> list(@Param("skuId") String skuId, @Param("start") int start, @Param("limit") int limit);

    int count(@Param("skuId") String skuId);

    int save(@Param("skuId") String skuId, @Param("boost") float boost);

    int delete(@Param("skuId") String skuId);
}
