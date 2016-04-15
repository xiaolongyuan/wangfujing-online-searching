package com.wfj.search.online.web.es.impl;

import com.wfj.search.online.index.pojo.BrandIndexPojo;
import com.wfj.search.online.web.es.BrandEsIao;
import com.wfj.search.utils.es.EsUtil;
import org.elasticsearch.client.Client;
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
    public BrandIndexPojo get(String brandId) {
        try {
            return EsUtil.get(this.esClient, brandId, this.index, TYPE, BrandIndexPojo.class);
        } catch (Exception e) {
            logger.warn("GET品牌[{}]信息失败", brandId, e);
        }
        return null;
    }
}
