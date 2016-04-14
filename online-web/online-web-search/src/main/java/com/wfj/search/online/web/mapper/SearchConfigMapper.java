package com.wfj.search.online.web.mapper;

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

    void save(@Param("name") String name, @Param("value") String value, @Param("channel") String channel);
}
