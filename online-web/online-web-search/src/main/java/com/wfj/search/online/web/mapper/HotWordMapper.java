package com.wfj.search.online.web.mapper;

import com.wfj.search.online.common.pojo.HotWordPojo;
import com.wfj.search.online.web.pojo.HotWord;
import com.wfj.search.online.web.pojo.HotWordsOfChannelPojo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br/>create at 15-12-22
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface HotWordMapper {
    /**
     * 根据站点id、频道id查询热词列表
     *
     * @param siteId    站点id
     * @param channelId 频道id
     * @return 热词列表
     */
    List<HotWordsOfChannelPojo> listHotWords(@Param("siteId") String siteId, @Param("channelId") String channelId);

    /**
     * 根据站点id、频道id查询热词列表
     *
     * @param site    站点id
     * @param channel 频道id
     * @return 热词列表
     */
    List<HotWordPojo> listHotWord(@Param("site") String site, @Param("channel") String channel);

    List<HotWord> listHotWordPages(@Param("siteId") String siteId, @Param("channelId") String channelId,
            @Param("value") String value, @Param("start") int start, @Param("limit") int limit);

    int count(@Param("siteId") String siteId, @Param("channelId") String channelId,
            @Param("value") String value);
}
