package com.wfj.search.online.management.console.service.blacklist;

import com.wfj.search.online.common.pojo.blacklist.BlacklistPojo;

import java.util.List;

/**
 * <br/>create at 15-10-28
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface IBlackListService {
    /**
     * 向黑名单添加数据
     *
     * @param id       编号
     * @param type     类型
     * @param modifier 添加人
     * @return 添加数
     */
    int add(String id, String type, String modifier);

    /**
     * 从黑名单删除数据
     *
     * @param id       编号
     * @param type     类型
     * @param modifier 删除人
     * @return 删除数
     */
    int del(String id, String type, String modifier);

    /**
     * 根据类型获取黑名单
     *
     * @param id    编号
     * @param type  类型，如果为空，返回整个黑名单
     * @param start 分页数据：开始下标
     * @param limit 分页数据：返回条数
     * @return 黑名单
     */
    List<BlacklistPojo> getBlackList(String id, String type, int start, int limit);

    /**
     * 获取黑名单数量
     *
     * @param id   编号
     * @param type 类型，如果为空，返回黑名单总数
     * @return 黑名单数量
     */
    int getCount(String id, String type);
}
