package com.wfj.search.online.index.service.impl;

import com.wfj.search.online.index.mapper.IndexConfigMapper;
import com.wfj.search.online.index.service.IIndexConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.wfj.search.online.common.constant.ConfigurationParameters.CONFIG_FETCH_THREADS;
import static com.wfj.search.online.common.constant.ConfigurationParameters.CONFIG_ITEMS_FETCH_SIZE;
import static com.wfj.search.online.common.constant.IndexCacheableAware.VALUE_KEY_INDEX_CONFIG_FETCH_SIZE;
import static com.wfj.search.online.common.constant.IndexCacheableAware.VALUE_KEY_INDEX_CONFIG_PRO_FETCH_THREADS;

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
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IndexConfigMapper indexConfigMapper;
    @Value("${index.config.default.itemsFetchSize}")
    private String defaultItemsFetchSize;
    @Value("${index.config.default.fetchThreads}")
    private String defaultFetchThreads;


    @Override
    @Cacheable(value = VALUE_KEY_INDEX_CONFIG_FETCH_SIZE)
    public int getFetchSize() {
        String itemsFetchSize;
        int intValue;
        try {
            itemsFetchSize = indexConfigMapper.get(CONFIG_ITEMS_FETCH_SIZE);
            assertLong(itemsFetchSize);
            intValue = Integer.parseInt(itemsFetchSize);
        } catch (Exception e) {
            logger.warn("查询索引配置失败, 使用编码默认值" + defaultItemsFetchSize, e);
            try {
                this.indexConfigMapper.save(CONFIG_ITEMS_FETCH_SIZE, defaultItemsFetchSize);
            } catch (Exception ignored) {
            }
            return Integer.parseInt(defaultItemsFetchSize);
        }
        return intValue;
    }

    private void assertLong(String longString) {
        Long l = Long.valueOf(longString);
        assert l != null;
    }

    @Override
    @Cacheable(value = VALUE_KEY_INDEX_CONFIG_PRO_FETCH_THREADS)
    public int getFetchThreads() {
        String proFetchThreads;
        int intValue;
        try {
            proFetchThreads = indexConfigMapper.get(CONFIG_FETCH_THREADS);
            assertLong(proFetchThreads);
            intValue = Integer.parseInt(proFetchThreads);
        } catch (Exception e) {
            logger.warn("查询索引配置失败, 使用编码默认值" + defaultFetchThreads, e);
            try {
                this.indexConfigMapper.save(CONFIG_FETCH_THREADS, defaultFetchThreads);
            } catch (Exception ignored) {
            }
            return Integer.parseInt(defaultFetchThreads);
        }
        return intValue;
    }
}
