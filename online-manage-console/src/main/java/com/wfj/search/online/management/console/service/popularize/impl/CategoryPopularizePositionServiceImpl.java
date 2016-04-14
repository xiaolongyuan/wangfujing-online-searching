package com.wfj.search.online.management.console.service.popularize.impl;

import com.wfj.search.online.common.pojo.popularize.CategoryPopularizePositionLogPojo;
import com.wfj.search.online.common.pojo.popularize.CategoryPopularizePositionPojo;
import com.wfj.search.online.management.console.mapper.popularize.CategoryTopSpotLogMapper;
import com.wfj.search.online.management.console.mapper.popularize.CategoryTopSpotMapper;
import com.wfj.search.online.management.console.service.ICacheService;
import com.wfj.search.online.management.console.service.popularize.ICategoryPopularizePositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <br/>create at 15-8-25
 *
 * @author liuxh
 * @since 1.0.0
 */
@Service("categoryPopularizePositionService")
public class CategoryPopularizePositionServiceImpl implements ICategoryPopularizePositionService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryPopularizePositionServiceImpl.class);
    @Autowired
    private CategoryTopSpotMapper categoryTopSpotMapper;
    @Autowired
    private CategoryTopSpotLogMapper categoryTopSpotLogMapper;
    @Autowired
    private ICacheService cacheService;

    @Override
    public List<CategoryPopularizePositionPojo> listWithPage(CategoryPopularizePositionPojo position, int start,
            int limit) {
        if (start < 0 || limit <= 0) {
            return Collections.emptyList();
        }
        return categoryTopSpotMapper.listWithPage(position, start, limit);
    }

    @Override
    public long categoryPositionTotal(CategoryPopularizePositionPojo position) {
        return categoryTopSpotMapper.categoryPositionTotal(position);
    }

    @Override
    public int addCategoryPosition(CategoryPopularizePositionPojo position, String modifier) {
        if (position == null || position.getCategoryId() == null || position.getSpuId() == null
                || position.getCreateOperator() == null) {
            return -1;
        }
        long count = categoryTopSpotMapper.categoryPositionTotal(position);
        if (count > 0) {
            return -2;
        }
        int row = categoryTopSpotMapper.addCategoryPosition(position);
        if (row == 1) {
            this.cacheService.clearCategoryTopSpotsCache();
        }
        addRecord(position.getCategoryId(), position.getSpuId(), position.getOrders(), modifier,
                CategoryPopularizePositionLogPojo.ModifyType.ADD);
        return row;
    }

    @Override
    public int deleteCategoryPosition(CategoryPopularizePositionPojo position, String modifier) {
        if (position == null || position.getCategoryId() == null || position.getSpuId() == null) {
            return -1;
        }
        long count = categoryTopSpotMapper.categoryPositionTotal(position);
        if (count == 0) {
            return -2;
        }
        int row = categoryTopSpotMapper.deleteCategoryPosition(position);
        if (row == 1) {
            this.cacheService.clearCategoryTopSpotsCache();
        }
        addRecord(position.getCategoryId(), position.getSpuId(), position.getOrders(), modifier,
                CategoryPopularizePositionLogPojo.ModifyType.DEL);
        return row;
    }

    private void addRecord(String categoryId, String spuId, int orders, String modifier,
            CategoryPopularizePositionLogPojo.ModifyType modifyType) {
        try {
            CategoryPopularizePositionLogPojo record = new CategoryPopularizePositionLogPojo(categoryId, spuId, orders,
                    modifier,
                    modifyType);
            categoryTopSpotLogMapper.addLog(record);
        } catch (Exception e) {
            logger.error("添加分类坑位操作记录失败，失败数据：brandId={},spuId={},orders={},modifier={},modifyTime={},modifyType={}",
                    categoryId, spuId, orders, modifier, modifyType.getType(), new Date(), e);
        }
    }
}
