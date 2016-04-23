package com.wfj.search.online.web.es.impl;

import com.wfj.search.online.index.pojo.CategoryIndexPojo;
import com.wfj.search.online.web.es.CategoryEsIao;
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
@Component("categoryEsIao")
public class CategoryEsIaoImpl implements CategoryEsIao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TYPE = "category";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;

    @Override
    public CategoryIndexPojo get(String catId) {
        try {
            return EsUtil.get(esClient, catId, index, TYPE, CategoryIndexPojo.class);
        } catch (Exception e) {
            if (e.getMessage().contains("BLANK")) {
                return null;
            }
            logger.warn("GET分类[{}]信息失败", catId, e);
        }
        return null;
    }
}
