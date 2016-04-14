package com.wfj.search.online.index.es.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wfj.search.online.index.es.ColorEsIao;
import com.wfj.search.online.index.pojo.ColorIndexPojo;
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
    public void upsert(ColorIndexPojo colorIndexPojo) {
        try {
            String source = this.objectMapper.writeValueAsString(colorIndexPojo);
            this.esClient.prepareUpdate(this.index, TYPE, colorIndexPojo.getColorId()).setDoc(source)
                    .setUpsert(source).get();
        } catch (Exception e) {
            logger.warn("保存颜色[{}]到ES失败", colorIndexPojo.getColorId(), e);
        }
    }
}
