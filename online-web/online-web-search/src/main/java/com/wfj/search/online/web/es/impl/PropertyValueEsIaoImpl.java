package com.wfj.search.online.web.es.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wfj.search.online.index.pojo.PropertyValueIndexPojo;
import com.wfj.search.online.web.es.PropertyValueEsIao;
import org.elasticsearch.action.get.GetResponse;
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
@Component("propertyValueEsIao")
public class PropertyValueEsIaoImpl implements PropertyValueEsIao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TYPE = "property-value";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public PropertyValueIndexPojo get(String propertyValueId) {
        try {
            GetResponse resp = this.esClient.prepareGet(this.index, TYPE, propertyValueId).get();
            String source = resp.getSourceAsString();
            if (source != null) {
                return this.objectMapper.readValue(source, PropertyValueIndexPojo.class);
            } else {
                logger.warn("GET属性值[{}]信息失败", propertyValueId);
            }
        } catch (Exception e) {
            logger.warn("GET属性值[{}]信息失败", propertyValueId, e);
        }
        return null;
    }
}
