package com.wfj.search.online.index.es.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wfj.search.online.index.es.SkuEsIao;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.SkuIndexPojo;
import com.wfj.search.utils.es.EsUtil;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.engine.DocumentAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>create at 16-3-28</p>
 *
 * @author liufl
 * @since 1.0.19
 */
@Component("skuEsIao")
public class SkuEsIaoImpl implements SkuEsIao {
    private static final String TYPE = "sku";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;

    @Override
    public void upsert(SkuIndexPojo skuIndexPojo) throws JsonProcessingException, IndexException {
        try {
            EsUtil.upsert(this.esClient, skuIndexPojo, skuIndexPojo.getSkuId(), index, TYPE);
        } catch (IllegalStateException e) {
            Throwable cause = e;
            while (cause.getCause() != null) {
                cause = e.getCause();
            }
            if (cause instanceof DocumentAlreadyExistsException) {
                upsert(skuIndexPojo);
            } else {
                throw new IndexException(e);
            }
        }
    }
}
