package com.wfj.search.online.index.mapper;

import com.wfj.search.online.index.pojo.SoldPojo;

/**
 * <p>create at 15-11-10</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface SaleVolumeMapper {
    void save(SoldPojo sold);

    Integer countItemSales(String itemId);

    Integer countSkuSales(String skuId);

    Integer countSpuSales(String spuId);
}
