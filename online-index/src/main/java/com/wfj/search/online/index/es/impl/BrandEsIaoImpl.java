package com.wfj.search.online.index.es.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wfj.search.online.index.es.BrandEsIao;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.BrandIndexPojo;
import com.wfj.search.utils.es.EsUtil;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.engine.DocumentAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>create at 16-3-26</p>
 *
 * @author liufl
 * @since 1.0.19
 */
@Component("brandEsIao")
public class BrandEsIaoImpl implements BrandEsIao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TYPE = "brand";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;

    @Override
    public void upsert(BrandIndexPojo brand) throws JsonProcessingException, IndexException {
        try {
            EsUtil.upsert(this.esClient, brand, brand.getBrandId(), index, TYPE);
        } catch (IllegalStateException e) {
            Throwable cause = e;
            while (cause.getCause() != null) {
                cause = e.getCause();
            }
            if (cause instanceof DocumentAlreadyExistsException) {
                upsert(brand);
            } else {
                throw new IndexException(e);
            }
        }
    }

    @Override
    public BrandIndexPojo get(String brandId) {
        try {
            return EsUtil.get(this.esClient, brandId, this.index, TYPE, BrandIndexPojo.class);
        } catch (Exception e) {
            if (e.getMessage().contains("BLANK")) {
                return null;
            }
            logger.warn("GET品牌[{}]信息失败", brandId, e);
        }
        return null;
    }
}
