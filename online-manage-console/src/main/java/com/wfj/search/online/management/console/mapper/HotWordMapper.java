package com.wfj.search.online.management.console.mapper;

import com.wfj.search.online.common.pojo.HotWordPojo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br/>create at 15-12-15
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface HotWordMapper {
    /**
     * 分页展示热词列表
     *
     * @param site    站点
     * @param channel 频道
     * @param start   开始
     * @param limit   条数
     * @return 热词列表
     */
    List<HotWordPojo> list(@Param("site") String site, @Param("channel") String channel,
            @Param("start") int start, @Param("limit") int limit);

    /**
     * 热词总数
     *
     * @param site    站点
     * @param channel 频道
     * @return 热词总数
     */
    int count(@Param("site") String site, @Param("channel") String channel);

    /**
     * 根据编号查询热词
     *
     * @param sid 编号
     * @return 热词对象
     */
    HotWordPojo getBySid(String sid);

    /**
     * 添加热词
     *
     * @param pojo 热词实体
     * @return 添加条数
     */
    int add(HotWordPojo pojo);

    /**
     * 修改热词
     *
     * @param pojo 热词实体
     * @return 修改条数
     */
    int mod(HotWordPojo pojo);

    /**
     * 删除热词数据
     *
     * @param pojo 热词实体
     * @return 修改条数
     */
    int del(HotWordPojo pojo);

    /**
     * 使热词生效或失效
     *
     * @param ids 热词编号列表
     * @return 修改条数
     */
    int enabled(@Param("ids") List<String> ids, @Param("enabled") boolean enabled);
}
