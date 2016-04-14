package com.wfj.search.online.management.console.service.cms;

import com.alibaba.fastjson.JSONArray;

/**
 * CMS接口调用
 * <br/>create at 15-12-22
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
public interface ICmsRequester {
    /**
     * 获取站点列表
     *
     * @return 站点列表
     */
    JSONArray getSiteList();

    /**
     * 根据站点id获取频道列表
     *
     * @param siteId 站点id
     * @return 频道列表
     */
    JSONArray getChannelListBySid(String siteId);
}
