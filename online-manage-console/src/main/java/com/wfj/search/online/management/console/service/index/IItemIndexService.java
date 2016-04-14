package com.wfj.search.online.management.console.service.index;

import com.alibaba.fastjson.JSONObject;

/**
 * <br/>create at 15-11-3
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
public interface IItemIndexService {
    /**
     * 刷新专柜商品
     *
     * @param message 参数
     * @return 结果
     */
    JSONObject refreshItem(String message);

    /**
     * 下架专柜商品
     *
     * @param message 参数
     * @return 结果
     */
    JSONObject removeItem(String message);
}
