package com.wfj.search.online.web.es.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wfj.search.online.common.pojo.Gp;
import com.wfj.search.online.web.es.GpEsIao;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>create at 16-5-12</p>
 *
 * @author liufl
 * @since 1.0.36
 */
@Component("gpEsIao")
public class GpEsIaoImpl implements GpEsIao {
    private static final String TYPE = "gp";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private Client esClient;
    @Value("${es.config.index}")
    private String index;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Gp get(String gp) {
        GetResponse response = this.esClient.prepareGet(this.index, TYPE, gp).get();
        if (response.isExists()) {
            String sourceAsString = response.getSourceAsString();
            if (StringUtils.isNotBlank(sourceAsString)) {
                try {
                    return objectMapper.readValue(sourceAsString, Gp.class);
                } catch (IOException e) {
                    logger.error("反序列GP失败", e);
                }
            }
        }
        return null;
    }
}
