package com.wfj.search.online.management.console.service.boost.impl;

import com.wfj.search.online.common.pojo.boost.ManualBoostPojo;
import com.wfj.search.online.management.console.mapper.boost.ManualBoostMapper;
import com.wfj.search.online.management.console.service.ICacheService;
import com.wfj.search.online.management.console.service.boost.IManualBoostService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <br/>create at 15-11-10
 *
 * @author liuxh
 * @since 1.0.0
 */
@Service("manualBoostService")
public class ManualBoostServiceImpl implements IManualBoostService {
    private static final Logger logger = LoggerFactory.getLogger(ManualBoostServiceImpl.class);
    @Autowired
    private ManualBoostMapper manualBoostMapper;
    @Autowired
    private ICacheService cacheService;

    @Override
    public List<ManualBoostPojo> list(String skuId, int start, int limit) {
        logger.debug("查询数据列表，参数：skuId={}, start={}, limit={}", skuId, start, limit);
        return manualBoostMapper.list(skuId, start, limit);
    }

    @Override
    public int count(String skuId) {
        logger.debug("查询数据总数，参数：skuId={}", skuId);
        return manualBoostMapper.count(skuId);
    }

    @Override
    public int save(String skuId, float boost) {
        logger.debug("添加或修改数据，参数：skuId={}, boost={}", skuId, boost);
        if (StringUtils.isBlank(skuId) || boost <= 0) {
            return -1;
        }
        int row = manualBoostMapper.save(skuId, boost);
        this.cacheService.invalidManualBoostsCache();
        return row;
    }

    @Override
    public int delete(String skuId) {
        logger.debug("删除数据，参数：skuId={}", skuId);
        if (StringUtils.isBlank(skuId)) {
            return -1;
        }
        int row = manualBoostMapper.delete(skuId);
        this.cacheService.invalidManualBoostsCache();
        return row;
    }
}
