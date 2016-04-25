package com.wfj.search.online.index.service;

/**
 * 索引配置参数服务
 * <p>create at 15-9-21</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public interface IIndexConfigService {
    /**
     * 列表每页获取长度上限
     *
     * @return 列表每页获取长度上限
     */
    int getFetchSize();

    /**
     * 商品数据抓取线程数
     *
     * @return 商品数据抓取线程数
     */
    int getFetchThreads();
    int getWarmUpFetchSize();
    int getWarmUpFetchThreads();
}
