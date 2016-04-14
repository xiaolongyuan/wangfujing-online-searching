package com.wfj.search.online.management.console.mapper.blacklist;

import com.wfj.search.online.common.pojo.blacklist.BlacklistPojo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br/>create at 15-10-28
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface BlackListMapper {
    /**
     * 向黑名单添加记录
     *
     * @param id   编号
     * @param type 类型
     * @return 添加数量
     */
    int add(@Param("id") String id, @Param("type") String type, @Param("creator") String creator);

    /**
     * 从黑名单删除记录
     *
     * @param id   编号
     * @param type 类型
     * @return 删除记录数量
     */
    int del(@Param("id") String id, @Param("type") String type);

    /**
     * 查询黑名单
     *
     * @param id    编号
     * @param type  类型
     * @param start 分页数据：开始下标
     * @param limit 分页数据：返回条数
     * @return 黑名单
     */
    List<BlacklistPojo> getBlacklist(@Param("id") String id, @Param("type") String type,
            @Param("start") int start, @Param("limit") int limit);

    /**
     * 查询黑名单总数
     *
     * @param type 类型
     * @return 黑名单总数
     */
    int getCount(@Param("id") String id, @Param("type") String type);
}
