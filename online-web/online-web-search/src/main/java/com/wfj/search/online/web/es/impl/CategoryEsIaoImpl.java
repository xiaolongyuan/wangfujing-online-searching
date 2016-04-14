package com.wfj.search.online.web.es.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wfj.search.online.index.pojo.CategoryIndexPojo;
import com.wfj.search.online.web.es.CategoryEsIao;
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
@Component("categoryEsIao")
public class CategoryEsIaoImpl implements CategoryEsIao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TYPE = "category";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public CategoryIndexPojo get(String catId) {
        try {
            GetResponse resp = this.esClient.prepareGet(this.index, TYPE, catId).get();
            String source = resp.getSourceAsString();
            if (source != null) {
                return this.objectMapper.readValue(source, CategoryIndexPojo.class);
            } else {
                logger.warn("GET分类[{}]信息失败", catId);
            }
        } catch (Exception e) {
            logger.warn("GET分类[{}]信息失败", catId, e);
        }
        return null;
    }
}
