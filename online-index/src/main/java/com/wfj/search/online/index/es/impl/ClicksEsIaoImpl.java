package com.wfj.search.online.index.es.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wfj.search.online.index.es.ClicksEsIao;
import com.wfj.search.online.index.pojo.ClickCountPojo;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>create at 16-3-27</p>
 *
 * @author liufl
 * @since 1.0.19
 */
@Component("clicksEsIao")
public class ClicksEsIaoImpl implements ClicksEsIao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TYPE = "clicks";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ClickCountPojo get(String spuId) {
        try {
            GetResponse resp = this.esClient.prepareGet(this.index, TYPE, spuId).get();
            String source = resp.getSourceAsString();
            if (source != null) {
                return this.objectMapper.readValue(source, ClickCountPojo.class);
            } else {
                logger.warn("GET SPU点击数[{}]信息失败", spuId);
            }
        } catch (Exception e) {
            logger.warn("GET SPU点击数[{}]信息失败", spuId, e);
        }
        return null;
    }
}
