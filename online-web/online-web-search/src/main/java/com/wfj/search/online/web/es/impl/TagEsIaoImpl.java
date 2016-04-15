package com.wfj.search.online.web.es.impl;

import com.wfj.search.online.index.pojo.TagIndexPojo;
import com.wfj.search.online.web.es.TagEsIao;
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
@Component("tagEsIao")
public class TagEsIaoImpl implements TagEsIao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TYPE = "tag";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;

    @Override
    public TagIndexPojo get(String tagId) {
        try {
            EsUtil.get(esClient, tagId, index, TYPE, TagIndexPojo.class);
        } catch (Exception e) {
            logger.warn("GET标签[{}]信息失败", tagId, e);
        }
        return null;
    }
}
