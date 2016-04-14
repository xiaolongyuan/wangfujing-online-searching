package com.wfj.search.online.index.service;

import java.util.Set;

/**
 * 索引黑名单服务
 * <p>create at 15-9-21</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface IIndexBlacklistService {
    /**
     * 列出指定类型的黑名单编码列表
     *
     * @param type 黑名单类型，可用：ITEM/SKU/SPU/BRAND
     * @return 黑名单编码列表
     */
    Set<String> listOfType(String type);
}
