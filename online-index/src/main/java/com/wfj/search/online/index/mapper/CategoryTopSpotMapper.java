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
public interface CategoryTopSpotMapper {
    List<Spot> listSpot(@Param("catId") String catId, @Param("channel") String channel);
}
