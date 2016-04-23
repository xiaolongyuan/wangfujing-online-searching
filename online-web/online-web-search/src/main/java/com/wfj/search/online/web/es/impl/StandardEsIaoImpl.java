package com.wfj.search.online.web.es.impl;

import com.wfj.search.online.index.pojo.StandardIndexPojo;
import com.wfj.search.online.web.es.StandardEsIao;
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
@Component("standardEsIao")
public class StandardEsIaoImpl implements StandardEsIao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TYPE = "standard";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;

    @Override
    public StandardIndexPojo get(String standardId) {
        try {
            return EsUtil.get(esClient, standardId, index, TYPE, StandardIndexPojo.class);
        } catch (Exception e) {
            if (e.getMessage().contains("BLANK")) {
                return null;
            }
            logger.warn("GET规格[{}]信息失败", standardId, e);
        }
        return null;
    }
}
