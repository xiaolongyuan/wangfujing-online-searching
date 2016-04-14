package com.wfj.search.online.web.es.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wfj.search.online.index.pojo.StandardIndexPojo;
import com.wfj.search.online.web.es.StandardEsIao;
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
@Component("standardEsIao")
public class StandardEsIaoImpl implements StandardEsIao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TYPE = "standard";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public StandardIndexPojo get(String standardId) {
        try {
            GetResponse resp = this.esClient.prepareGet(this.index, TYPE, standardId).get();
            String source = resp.getSourceAsString();
            if (source != null) {
                return this.objectMapper.readValue(source, StandardIndexPojo.class);
            } else {
                logger.warn("GET规格[{}]信息失败", standardId);
            }
        } catch (Exception e) {
            logger.warn("GET规格[{}]信息失败", standardId, e);
        }
        return null;
    }
}
