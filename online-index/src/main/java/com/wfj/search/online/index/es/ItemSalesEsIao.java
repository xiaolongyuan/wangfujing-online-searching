package com.wfj.search.online.index.es;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.ItemSalesPojo;

/**
 * <p>create at 16-3-28</p>
 *
 * @author liufl
 * @since 1.0.19
 */
public interface ItemSalesEsIao {
    void upsert(ItemSalesPojo itemSalesPojo) throws JsonProcessingException, IndexException;

    ItemSalesPojo get(String itemId);
}
