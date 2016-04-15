package com.wfj.search.online.index.es.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wfj.search.online.index.es.PropertyValueEsIao;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.PropertyValueIndexPojo;
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
@Component("propertyValueEsIao")
public class PropertyValueEsIaoImpl implements PropertyValueEsIao {
    private static final String TYPE = "property-value";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;

    @Override
    public void upsert(PropertyValueIndexPojo propertyValueIndexPojo) throws JsonProcessingException, IndexException {
        try {
            EsUtil.upsert(this.esClient, propertyValueIndexPojo, propertyValueIndexPojo.getPropertyValueId(), index,
                    TYPE);
        } catch (IllegalStateException e) {
            throw new IndexException(e);
        }
    }
}
