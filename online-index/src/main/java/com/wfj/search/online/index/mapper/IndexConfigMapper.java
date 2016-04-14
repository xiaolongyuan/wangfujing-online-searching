package com.wfj.search.online.index.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * 索引配置Mapper
 * <br/>create at 15-7-24
 *
 * @author liufl
 * @since 1.0.0
 */
public interface IndexConfigMapper {
    String get(String name);

    void save(@Param("name") String name, @Param("value") String value);
}
