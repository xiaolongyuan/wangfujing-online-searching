package com.wfj.search.online.web.es.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wfj.search.online.index.pojo.ColorIndexPojo;
import com.wfj.search.online.web.es.ColorEsIao;
import org.elasticsearch.action.get.GetResponse;
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
@Component("colorEsIao")
public class ColorEsIaoImpl implements ColorEsIao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TYPE = "color";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ColorIndexPojo get(String colorId) {
        try {
            GetResponse resp = this.esClient.prepareGet(this.index, TYPE, colorId).get();
            String source = resp.getSourceAsString();
            if (source != null) {
                return this.objectMapper.readValue(source, ColorIndexPojo.class);
            } else {
                logger.warn("GET颜色[{}]信息失败", colorId);
            }
        } catch (Exception e) {
            logger.warn("GET颜色[{}]信息失败", colorId, e);
        }
        return null;
    }
}
