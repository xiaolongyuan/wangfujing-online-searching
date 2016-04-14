package com.wfj.search.online.management.console.service;

import com.wfj.search.online.common.pojo.HotWordPojo;

import java.util.List;

/**
 * <br/>create at 15-12-15
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface HotWordService {
    /**
     * 分页展示热词列表
     *
     * @param start 开始
     * @param limit 条数
     * @return 热词列表
     */
    List<HotWordPojo> list(String site, String channel, int start, int limit);

    /**
     * 热词总数
     *
     * @return 热词总数
     */
    int count(String site, String channel);

    /**
     * 根据热词id获取热词数据
     *
     * @param hotWordId 热词id
     * @return 热词数据
     */
    HotWordPojo get(String hotWordId);

    /**
     * 添加热词
     *
     * @param pojo     热词实体
     * @param modifier 修改人
     * @return 状态，-1数据错误
     */
    int add(HotWordPojo pojo, String modifier);

    /**
     * 修改热词
     *
     * @param pojo     热词实体
     * @param modifier 修改人
     * @return 状态，-1数据错误，-2不存在数据，-3存在数据为有效数据
     */
    int mod(HotWordPojo pojo, String modifier);

    /**
     * 删除热词数据
     *
     * @param sid      热词编号
     * @param modifier 修改人
     * @return 状态，-1数据错误，-2不存在数据，-3存在数据为有效数据
     */
    int del(String sid, String modifier);

    /**
     * 使热词生效
     *
     * @param sid      热词编号
     * @param modifier 修改人
     * @return 状态，-1数据错误，-2不存在数据
     */
    int enabled(String sid, String modifier);

    /**
     * 使热词失效
     *
     * @param sid      热词编号
     * @param modifier 修改人
     * @return 状态，-1数据错误，-2不存在数据
     */
    int disabled(String sid, String modifier);
}
