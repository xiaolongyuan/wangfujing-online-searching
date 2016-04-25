package com.wfj.search.online.index.es.impl;

import com.wfj.search.online.index.es.TagEsIao;
import com.wfj.search.online.index.pojo.TagIndexPojo;
import com.wfj.search.utils.es.EsUtil;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.engine.DocumentAlreadyExistsException;
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
    public void upsert(TagIndexPojo tagIndexPojo) {
        try {
            EsUtil.upsert(this.esClient, tagIndexPojo, tagIndexPojo.getTagId(), index, TYPE);
        } catch (Exception e) {
            Throwable cause = e;
            while (cause.getCause() != null) {
                cause = e.getCause();
            }
            if (cause instanceof DocumentAlreadyExistsException) {
                upsert(tagIndexPojo);
            } else {
                logger.warn("保存标签[{}]到ES失败", tagIndexPojo.getTagId(), e);
            }
        }
    }
}
