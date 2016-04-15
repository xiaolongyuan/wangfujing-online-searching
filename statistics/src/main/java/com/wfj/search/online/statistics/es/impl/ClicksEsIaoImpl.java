package com.wfj.search.online.statistics.es.impl;

import com.wfj.search.online.statistics.es.ClicksEsIao;
import com.wfj.search.online.statistics.pojo.ClickCountPojo;
import com.wfj.search.utils.es.EsUtil;
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

    @Override
    public void upsert(ClickCountPojo clickCountPojo) {
        try {
            EsUtil.upsert(this.esClient, clickCountPojo, clickCountPojo.getSpuId(), index, TYPE);
        } catch (Exception e) {
            logger.warn("写点击量数据[SpuId:{}]到ES失败", clickCountPojo.getSpuId(), e);
        }
    }
}
