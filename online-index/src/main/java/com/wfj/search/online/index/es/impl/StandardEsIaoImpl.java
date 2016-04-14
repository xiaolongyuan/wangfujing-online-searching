package com.wfj.search.online.index.es.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wfj.search.online.index.es.StandardEsIao;
import com.wfj.search.online.index.pojo.StandardIndexPojo;
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
    public void upsert(StandardIndexPojo standardIndexPojo) {
        try {
            String source = this.objectMapper.writeValueAsString(standardIndexPojo);
            this.esClient.prepareUpdate(this.index, TYPE, standardIndexPojo.getStandardId()).setDoc(source)
                    .setUpsert(source).get();
        } catch (Exception e) {
            logger.warn("保存规格[{}]到ES失败", standardIndexPojo.getStandardId(), e);
        }
    }
}
