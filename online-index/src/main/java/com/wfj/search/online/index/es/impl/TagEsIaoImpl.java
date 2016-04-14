package com.wfj.search.online.index.es.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wfj.search.online.index.es.TagEsIao;
import com.wfj.search.online.index.pojo.TagIndexPojo;
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
@Component("tagEsIao")
public class TagEsIaoImpl implements TagEsIao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TYPE = "tag";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void upsert(TagIndexPojo tagIndexPojo) {
        try {
            String source = this.objectMapper.writeValueAsString(tagIndexPojo);
            this.esClient.prepareUpdate(this.index, TYPE, tagIndexPojo.getTagId()).setDoc(source)
                    .setUpsert(source).get();
        } catch (Exception e) {
            logger.warn("保存标签[{}]到ES失败", tagIndexPojo.getTagId(), e);
        }
    }
}
