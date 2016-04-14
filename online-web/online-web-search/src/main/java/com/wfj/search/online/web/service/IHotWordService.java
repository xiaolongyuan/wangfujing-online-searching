package com.wfj.search.online.web.service;

import com.wfj.search.online.web.pojo.HotWord;
import com.wfj.search.online.web.pojo.HotWordsOfChannelPojo;

import java.util.List;

/**
 * <br/>create at 15-12-22
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface IHotWordService {
    /**
     * 查询热词列表
     *
     * @param siteId    站点编号
     * @param channelId 频道编号
     * @return 热词列表
     */
    List<HotWordsOfChannelPojo> listHotWords(String siteId, String channelId);

    /**
     * 查询热词列表
     *
     * @param siteId    站点id
     * @param channelId 频道id
     * @param value     过滤词
     * @param start     开始
     * @param fetch     分页
     * @return 热词列表
     */
    List<HotWord> listHotWords(String siteId, String channelId, String value, int start, int fetch);

    int count(String siteId, String channelId, String value);
}
