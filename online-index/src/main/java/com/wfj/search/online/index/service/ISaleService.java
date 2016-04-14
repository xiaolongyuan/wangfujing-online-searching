package com.wfj.search.online.index.service;

import com.wfj.search.online.index.pojo.SoldPojo;
import com.wfj.search.online.index.pojo.failure.Failure;

import java.util.Optional;

/**
 * <p>create at 15-12-9</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface ISaleService {
    Optional<Failure> save(SoldPojo sold);

    Integer countItemSales(String itemId);

    Integer countSkuSales(String skuId);

    Integer countSpuSales(String spuId);
}
