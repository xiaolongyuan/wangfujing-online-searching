package com.wfj.search.online.index.mapper;

import com.wfj.search.online.index.pojo.Spot;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>create at 15-12-14</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface BrandTopSpotMapper {
    List<Spot> listSpot(@Param("brandId") String brandId, @Param("channel") String channel);
}
