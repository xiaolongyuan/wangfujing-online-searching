package com.wfj.search.online.management.console.mapper.config;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <br/>create at 15-11-5
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface SearchConfigMapper {
    List<Map<String, String>> getConfigs();

    void save(@Param("name") String name, @Param("value") String value);
}
