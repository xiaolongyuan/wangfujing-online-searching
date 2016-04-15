package com.wfj.search.online.index.es.impl;

import com.wfj.search.online.index.es.StandardEsIao;
import com.wfj.search.online.index.pojo.StandardIndexPojo;
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
    public void upsert(StandardIndexPojo standardIndexPojo) {
        try {
            EsUtil.upsert(this.esClient, standardIndexPojo, standardIndexPojo.getStandardId(), index, TYPE);
        } catch (Exception e) {
            logger.warn("保存规格[{}]到ES失败", standardIndexPojo.getStandardId(), e);
        }
    }
}
