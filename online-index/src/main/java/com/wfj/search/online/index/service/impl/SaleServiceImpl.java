package com.wfj.search.online.index.service.impl;

import com.wfj.search.online.index.mapper.SaleVolumeMapper;
import com.wfj.search.online.index.pojo.SoldPojo;
import com.wfj.search.online.index.pojo.failure.DataType;
import com.wfj.search.online.index.pojo.failure.Failure;
import com.wfj.search.online.index.pojo.failure.FailureType;
import com.wfj.search.online.index.service.ISaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.wfj.search.online.common.constant.IndexCacheableAware.*;

/**
 * <p>create at 15-12-9</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service("saleService")
public class SaleServiceImpl implements ISaleService {
    private static final Logger logger = LoggerFactory.getLogger(SaleServiceImpl.class);
    @Autowired
    private SaleVolumeMapper saleVolumeMapper;

    @Caching(
            evict = {
                    @CacheEvict(value = VALUE_KEY_SALE_COUNT_ITEM, key = "#sold.itemId"),
                    @CacheEvict(value = VALUE_KEY_SALE_COUNT_SKU, key = "#sold.skuId"),
                    @CacheEvict(value = VALUE_KEY_SALE_COUNT_SPU, key = "#sold.spuId")
            }
    )
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public Optional<Failure> save(SoldPojo sold) {
        try {
            this.saleVolumeMapper.save(sold);
        } catch (Exception e) {
            String msg = "销量信息[itemId:" + sold.getItemId() + ",sales:" + sold.getSales() + "]写入MySql失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.sale, FailureType.save2DB, sold.getItemId(), msg, e));
        }
        return Optional.empty();
    }

    @Override
    @Cacheable(value = VALUE_KEY_SALE_COUNT_ITEM)
    public Integer countItemSales(String itemId) {
        return this.saleVolumeMapper.countItemSales(itemId);
    }

    @Override
    @Cacheable(value = VALUE_KEY_SALE_COUNT_SKU)
    public Integer countSkuSales(String skuId) {
        return this.saleVolumeMapper.countSkuSales(skuId);
    }

    @Override
    @Cacheable(value = VALUE_KEY_SALE_COUNT_SPU)
    public Integer countSpuSales(String spuId) {
        return this.saleVolumeMapper.countSpuSales(spuId);
    }
}
