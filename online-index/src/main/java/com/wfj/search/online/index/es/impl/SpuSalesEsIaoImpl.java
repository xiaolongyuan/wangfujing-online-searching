package com.wfj.search.online.index.es.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wfj.search.online.index.es.SpuSalesEsIao;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.SpuSalesPojo;
import com.wfj.search.utils.es.EsUtil;
import org.elasticsearch.client.Client;
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
@Component("spuSalesEsIao")
public class SpuSalesEsIaoImpl implements SpuSalesEsIao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TYPE = "spu-sales";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;

    @Override
    public void upsert(SpuSalesPojo spuSalesPojo) throws JsonProcessingException, IndexException {
        try {
            EsUtil.upsert(this.esClient, spuSalesPojo, spuSalesPojo.getSpuId(), index, TYPE);
        } catch (IllegalStateException e) {
            throw new IndexException(e);
        }
    }

    @Override
    public SpuSalesPojo get(String spuId) {
        try {
            return EsUtil.get(esClient, spuId, index, TYPE, SpuSalesPojo.class);
        } catch (Exception e) {
            if (e.getMessage().contains("BLANK")) {
                return null;
            }
            logger.warn("查找SPU[{}]销量失败", spuId, e);
        }
        return null;
    }
}
