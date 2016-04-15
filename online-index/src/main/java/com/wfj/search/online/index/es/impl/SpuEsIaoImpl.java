package com.wfj.search.online.index.es.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wfj.search.online.index.es.SpuEsIao;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.SpuIndexPojo;
import com.wfj.search.utils.es.EsUtil;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>create at 16-3-28</p>
 *
 * @author liufl
 * @since 1.0.19
 */
@Component("spuEsIao")
public class SpuEsIaoImpl implements SpuEsIao {
    private static final String TYPE = "spu";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;

    @Override
    public void upsert(SpuIndexPojo spuIndexPojo) throws JsonProcessingException, IndexException {
        try {
            EsUtil.upsert(this.esClient, spuIndexPojo, spuIndexPojo.getSpuId(), index, TYPE);
        } catch (IllegalStateException e) {
            throw new IndexException(e);
        }
    }
}
