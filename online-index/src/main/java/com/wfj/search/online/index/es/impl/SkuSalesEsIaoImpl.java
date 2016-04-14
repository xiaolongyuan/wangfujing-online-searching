package com.wfj.search.online.index.es.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wfj.search.online.index.es.SkuSalesEsIao;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.SkuSalesPojo;
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
 * <p>create at 16-3-28</p>
 *
 * @author liufl
 * @since 1.0.19
 */
@Component("skuSalesEsIao")
public class SkuSalesEsIaoImpl implements SkuSalesEsIao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TYPE = "sku-sales";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void upsert(SkuSalesPojo skuSalesPojo) throws JsonProcessingException, IndexException {
        String source = this.objectMapper.writeValueAsString(skuSalesPojo);
        UpdateResponse resp = this.esClient.prepareUpdate(this.index, TYPE, skuSalesPojo.getSkuId()).setDoc(source)
                .setUpsert(source).get();
        ActionWriteResponse.ShardInfo shardInfo = resp.getShardInfo();
        if (shardInfo.getFailed() == shardInfo.getTotal() && shardInfo.getTotal() > 0) {
            logger.error("写入SKU销量[{}]失败，所有节点都写入失败！", skuSalesPojo.getSkuId());
            throw new IndexException("所有节点都写入失败！");
        }
    }

    @Override
    public SkuSalesPojo get(String skuId) {
        try {
            GetResponse resp = this.esClient.prepareGet(this.index, TYPE, skuId).get();
            String source = resp.getSourceAsString();
            if (source != null) {
                return this.objectMapper.readValue(source, SkuSalesPojo.class);
            } else {
                logger.warn("查找SKU[{}]销量失败", skuId);
            }
        } catch (Exception e) {
            logger.warn("查找SKU[{}]销量失败", skuId, e);
        }
        return null;
    }
}
