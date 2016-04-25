package com.wfj.search.online.index.es.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wfj.search.online.index.es.ItemSalesEsIao;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.ItemSalesPojo;
import com.wfj.search.utils.es.EsUtil;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.engine.DocumentAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>create at 16-3-28</p>
 *
 * @author liufl
 * @since 1.0.19
 */
@Component("itemSalesEsIao")
public class ItemSalesEsIaoImpl implements ItemSalesEsIao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TYPE = "item-sales";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;

    @Override
    public void upsert(ItemSalesPojo itemSalesPojo) throws JsonProcessingException, IndexException {
        try {
            EsUtil.upsert(this.esClient, itemSalesPojo, itemSalesPojo.getItemId(), index, TYPE);
        } catch (IllegalStateException e) {
            Throwable cause = e;
            while (cause.getCause() != null) {
                cause = e.getCause();
            }
            if (cause instanceof DocumentAlreadyExistsException) {
                upsert(itemSalesPojo);
            } else {
                throw new IndexException(e);
            }
        }
    }

    @Override
    public ItemSalesPojo get(String itemId) {
        try {
            return EsUtil.get(esClient, itemId, index, TYPE, ItemSalesPojo.class);
        } catch (Exception e) {
            if (e.getMessage().contains("BLANK")) {
                return null;
            }
            logger.warn("查找商品[{}]销量失败", itemId, e);
        }
        return null;
    }
}
