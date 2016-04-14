package com.wfj.search.online.management.console.service.index;

import com.alibaba.fastjson.JSONObject;

/**
 * <br/>create at 15-11-2
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
public interface ISkuIndexService {
    /**
     * 根据sku刷新专柜商品
     *
     * @param message 参数
     * @return 结果
     */
    JSONObject refreshItems(String message);

    /**
     * 根据sku下架专柜商品
     *
     * @param message 参数
     * @return 结果
     */
    JSONObject removeItems(String message);
}
