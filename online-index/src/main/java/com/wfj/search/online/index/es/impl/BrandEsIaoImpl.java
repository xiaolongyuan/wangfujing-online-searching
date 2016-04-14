package com.wfj.search.online.index.es.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wfj.search.online.index.es.BrandEsIao;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.BrandIndexPojo;
import org.elasticsearch.action.ActionWriteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateResponse;
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
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void upsert(BrandIndexPojo brand) throws JsonProcessingException, IndexException {
        String source = this.objectMapper.writeValueAsString(brand);
        UpdateResponse resp = this.esClient.prepareUpdate(this.index, TYPE, brand.getBrandId()).setDoc(source)
                .setUpsert(source).get();
        ActionWriteResponse.ShardInfo shardInfo = resp.getShardInfo();
        if (shardInfo.getFailed() == shardInfo.getTotal() && shardInfo.getTotal() > 0)
            throw new IndexException("所有节点都写入失败！");
    }

    @Override
    public BrandIndexPojo get(String brandId) {
        try {
            GetResponse resp = this.esClient.prepareGet(this.index, TYPE, brandId).get();
            String source = resp.getSourceAsString();
            if (source != null) {
                return this.objectMapper.readValue(source, BrandIndexPojo.class);
            } else {
                logger.warn("GET品牌[{}]信息失败", brandId);
            }
        } catch (Exception e) {
            logger.warn("GET品牌[{}]信息失败", brandId, e);
        }
        return null;
    }
}
