package com.wfj.search.online.web.es.impl;

import com.wfj.search.online.index.pojo.PropertyValueIndexPojo;
import com.wfj.search.online.web.es.PropertyValueEsIao;
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
@Component("propertyValueEsIao")
public class PropertyValueEsIaoImpl implements PropertyValueEsIao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TYPE = "property-value";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;

    @Override
    public PropertyValueIndexPojo get(String propertyValueId) {
        try {
            return EsUtil.get(esClient, propertyValueId, index, TYPE, PropertyValueIndexPojo.class);
        } catch (Exception e) {
            if (e.getMessage().contains("BLANK")) {
                return null;
            }
            logger.warn("GET属性值[{}]信息失败", propertyValueId, e);
        }
        return null;
    }
}
