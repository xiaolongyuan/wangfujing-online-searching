package com.wfj.search.online.index.es.impl;

import com.wfj.search.online.common.pojo.ActivityPojo;
import com.wfj.search.online.index.es.ActivityEsIao;
import com.wfj.search.utils.es.EsUtil;
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

    @Override
    public ActivityPojo get(String activeId) {
        try {
            return EsUtil.get(this.esClient, activeId, index, TYPE, ActivityPojo.class);
        } catch (Exception e) {
            if (e.getMessage().contains("BLANK")) {
                return null;
            }
            logger.warn("GET活动[{}]信息失败", activeId, e);
        }
        return null;
    }

    @Override
    public void upsert(ActivityPojo activityPojo) {
        try {
            EsUtil.upsert(this.esClient, activityPojo, activityPojo.getActiveId(), this.index, TYPE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
