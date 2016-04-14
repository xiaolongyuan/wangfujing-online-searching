package com.wfj.search.online.index.service.impl;

import com.google.common.collect.Maps;
import com.wfj.search.online.index.mapper.ManualBoostMapper;
import com.wfj.search.online.index.service.IManualBoostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.wfj.search.online.common.constant.IndexCacheableAware.VALUE_KEY_INDEX_VARIABLE_MANUAL_BOOSTS;

/**
 * <p>create at 15-11-10</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service("manualBoostService")
public class ManualBoostServiceImpl implements IManualBoostService {
    @Autowired
    private ManualBoostMapper manualBoostMapper;

    @Override
    @Cacheable(VALUE_KEY_INDEX_VARIABLE_MANUAL_BOOSTS)
    public Map<String, Float> listBoosts(String channel) {
        // 所有渠道使用同一加权值……反正手机端没什么人(-_-!)
        Map<String, Float> map = Maps.newConcurrentMap();
        this.manualBoostMapper.listAll().forEach(boost -> map.put(boost.getSkuId(), boost.getBoost()));
        return Maps.newHashMap(map);
    }
}
