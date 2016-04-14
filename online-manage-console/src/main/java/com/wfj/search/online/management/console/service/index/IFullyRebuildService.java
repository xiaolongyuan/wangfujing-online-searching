package com.wfj.search.online.management.console.service.index;

import com.alibaba.fastjson.JSONObject;

/**
 * <br/>create at 15-11-4
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
public interface IFullyRebuildService {
    /**
     * 全量刷新ES数据
     *
     * @param message 参数
     * @return 结果
     */
    JSONObject refreshEsData(String message);

    /**
     * 全量刷新索引
     *
     * @param message 参数
     * @return 结果
     */
    JSONObject refreshItems(String message);
}
