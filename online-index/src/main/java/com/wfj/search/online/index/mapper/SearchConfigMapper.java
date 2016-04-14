package com.wfj.search.online.index.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * 搜索配置Mapper
 * <p>create at 15-9-21</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface SearchConfigMapper {
    String get(@Param("name") String name, @Param("channel") String channel);
}
