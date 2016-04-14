package com.wfj.search.online.statistics.es.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wfj.search.online.statistics.es.ClicksEsIao;
import com.wfj.search.online.statistics.pojo.ClickCountPojo;
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
@Component("clickEsIao")
public class ClicksEsIaoImpl implements ClicksEsIao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TYPE = "clicks";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void upsert(ClickCountPojo clickCountPojo) {
        try {
            String source = this.objectMapper.writeValueAsString(clickCountPojo);
            this.esClient.prepareUpdate(this.index, TYPE, clickCountPojo.getSpuId()).setDoc(source)
                    .setUpsert(source).get();
        } catch (Exception e) {
            logger.warn("写点击量数据[SpuId:{}]到ES失败", clickCountPojo.getSpuId(), e);
        }
    }
}
