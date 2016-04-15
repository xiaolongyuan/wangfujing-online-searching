package com.wfj.search.online.index.es.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wfj.search.online.index.es.PropertyEsIao;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.PropertyIndexPojo;
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
@Component("propertyEsIao")
public class PropertyEsIaoImpl implements PropertyEsIao {
    private static final String TYPE = "property";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;

    @Override
    public void upsert(PropertyIndexPojo propertyIndexPojo) throws JsonProcessingException, IndexException {
        try {
            EsUtil.upsert(this.esClient, propertyIndexPojo, propertyIndexPojo.getPropertyId(), index, TYPE);
        } catch (IllegalStateException e) {
            throw new IndexException(e);
        }
    }
}
