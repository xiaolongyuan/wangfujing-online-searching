package com.wfj.search.online.index.es.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wfj.search.online.common.pojo.ActivityPojo;
import com.wfj.search.online.index.es.ActivityEsIao;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>create at 16-3-26</p>
 *
 * @author liufl
 * @since 1.0.19
 */
@Component("activityEsIao")
public class ActivityEsIaoImpl implements ActivityEsIao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TYPE = "activity";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ActivityPojo get(String activeId) {
        try {
            GetResponse resp = this.esClient.prepareGet(this.index, TYPE, activeId).get();
            String source = resp.getSourceAsString();
            if (source != null) {
                return this.objectMapper.readValue(source, ActivityPojo.class);
            } else {
                logger.warn("GET活动[{}]信息失败", activeId);
            }
        } catch (Exception e) {
            logger.warn("GET活动[{}]信息失败", activeId, e);
        }
        return null;
    }

    @Override
    public void upsert(ActivityPojo activityPojo) {
        try {
            String source = objectMapper.writeValueAsString(activityPojo);
            this.esClient.prepareUpdate(this.index, TYPE, activityPojo.getActiveId()).setDoc(source).setUpsert(source)
                    .get();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
