package com.wfj.search.online.index.es;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.SkuSalesPojo;

/**
 * <p>create at 16-3-28</p>
 *
 * @author liufl
 * @since 1.0.19
 */
public interface SkuSalesEsIao {
    void upsert(SkuSalesPojo skuSalesPojo) throws JsonProcessingException, IndexException;

    SkuSalesPojo get(String skuId);
}
