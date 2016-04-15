package com.wfj.search.online.index.es.impl;

import com.wfj.search.online.index.es.ClicksEsIao;
import com.wfj.search.online.index.pojo.ClickCountPojo;
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
@Component("clicksEsIao")
public class ClicksEsIaoImpl implements ClicksEsIao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TYPE = "clicks";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;

    @Override
    public ClickCountPojo get(String spuId) {
        try {
            return EsUtil.get(this.esClient, spuId, index, TYPE, ClickCountPojo.class);
        } catch (Exception e) {
            logger.warn("GET SPU点击数[{}]信息失败", spuId, e);
        }
        return null;
    }
}
