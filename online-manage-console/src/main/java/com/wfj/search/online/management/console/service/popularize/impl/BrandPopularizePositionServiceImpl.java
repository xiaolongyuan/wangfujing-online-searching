package com.wfj.search.online.management.console.service.popularize.impl;

import com.wfj.search.online.common.pojo.popularize.BrandPopularizePositionLogPojo;
import com.wfj.search.online.common.pojo.popularize.BrandPopularizePositionPojo;
import com.wfj.search.online.management.console.mapper.popularize.BrandTopSpotLogMapper;
import com.wfj.search.online.management.console.mapper.popularize.BrandTopSpotMapper;
import com.wfj.search.online.management.console.service.ICacheService;
import com.wfj.search.online.management.console.service.popularize.IBrandPopularizePositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <br/>create at 15-8-21
 *
 * @author liuxh
 * @since 1.0.0
 */
@Service("brandPopularizePositionService")
public class BrandPopularizePositionServiceImpl implements IBrandPopularizePositionService {
    private static final Logger logger = LoggerFactory.getLogger(BrandPopularizePositionServiceImpl.class);
    @Autowired
    private BrandTopSpotMapper brandTopSpotMapper;
    @Autowired
    private BrandTopSpotLogMapper brandTopSpotLogMapper;
    @Autowired
    private ICacheService cacheService;

    @Override
    public List<BrandPopularizePositionPojo> listWithPage(BrandPopularizePositionPojo position, int start, int limit) {
        if (start < 0 || limit <= 0) {
            return Collections.emptyList();
        }
        return brandTopSpotMapper.listWithPage(position, start, limit);
    }

    @Override
    public long brandPositionTotal(BrandPopularizePositionPojo position) {
        return brandTopSpotMapper.brandPositionTotal(position);
    }

    @Override
    public int addBrandPosition(BrandPopularizePositionPojo position, String modifier) {
        if (position == null || position.getBrandId() == null || position.getSpuId() == null
                || position.getCreateOperator() == null) {
            return -1;
        }
        long count = brandTopSpotMapper.brandPositionTotal(position);
        if (count > 0) {
            return -2;
        }
        int row = brandTopSpotMapper.addBrandPosition(position);
        if (row == 1) {
            this.cacheService.clearBrandTopSpotsCache();
        }
        addRecord(position.getBrandId(), position.getSpuId(), position.getOrders(), modifier,
                BrandPopularizePositionLogPojo.ModifyType.ADD);
        return row;
    }

    @Override
    public int deleteBrandPosition(BrandPopularizePositionPojo position, String modifier) {
        if (position == null || position.getBrandId() == null || position.getSpuId() == null) {
            return -1;
        }
        long count = brandTopSpotMapper.brandPositionTotal(position);
        if (count == 0) {
            return -2;
        }
        int row = brandTopSpotMapper.deleteBrandPosition(position);
        if (row == 1) {
            this.cacheService.clearBrandTopSpotsCache();
        }
        addRecord(position.getBrandId(), position.getSpuId(), position.getOrders(), modifier,
                BrandPopularizePositionLogPojo.ModifyType.DEL);
        return row;
    }

    private void addRecord(String brandId, String spuId, int orders, String modifier,
            BrandPopularizePositionLogPojo.ModifyType modifyType) {
        try {
            BrandPopularizePositionLogPojo record = new BrandPopularizePositionLogPojo(brandId, spuId, orders, modifier,
                    modifyType);
            brandTopSpotLogMapper.addLog(record);
        } catch (Exception e) {
            logger.error("添加品牌坑位操作记录失败，失败数据：brandId={},spuId={},orders={},modifier={},modifyTime={},modifyType={}",
                    brandId, spuId, orders, modifier, modifyType.getType(), new Date(), e);
        }
    }
}
