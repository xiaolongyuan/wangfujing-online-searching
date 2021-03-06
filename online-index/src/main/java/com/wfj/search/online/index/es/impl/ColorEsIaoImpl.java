package com.wfj.search.online.index.es.impl;

import com.wfj.search.online.index.es.ColorEsIao;
import com.wfj.search.online.index.pojo.ColorIndexPojo;
import com.wfj.search.utils.es.EsUtil;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.engine.DocumentAlreadyExistsException;
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

    @Override
    public void upsert(ColorIndexPojo colorIndexPojo) {
        try {
            EsUtil.upsert(this.esClient, colorIndexPojo, colorIndexPojo.getColorId(), index, TYPE);
        } catch (Exception e) {
            Throwable cause = e;
            while (cause.getCause() != null) {
                cause = e.getCause();
            }
            if (cause instanceof DocumentAlreadyExistsException) {
                upsert(colorIndexPojo);
            } else {
                logger.warn("保存颜色[{}]到ES失败", colorIndexPojo.getColorId(), e);
            }
        }
    }
}
