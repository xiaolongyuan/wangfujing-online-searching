package com.wfj.search.online.index.service.impl;

import com.wfj.search.online.index.service.IIndexConfigService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 索引配置参数服务
 * <br/>create at 15-7-22
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
@Service("indexConfigService")
public class IndexConfigServiceImpl implements IIndexConfigService {
    @Value("${index.config.default.itemsFetchSize}")
    private String fetchSize;
    @Value("${index.config.default.fetchThreads}")
    private String fetchThreads;
    @Value("${index.config.default.warmUpItemsFetchSize}")
    private String warmUpFetchSize;
    @Value("${index.config.default.warmUpFetchThreads}")
    private String warmUpFetchThreads;

    @Override
    public int getFetchSize() {
        return Integer.parseInt(fetchSize);
    }

    @Override
    public int getFetchThreads() {
        return Integer.parseInt(fetchThreads);
    }

    @Override
    public int getWarmUpFetchSize() {
        return Integer.parseInt(warmUpFetchSize);
    }

    @Override
    public int getWarmUpFetchThreads() {
        return Integer.parseInt(warmUpFetchThreads);
    }
}
