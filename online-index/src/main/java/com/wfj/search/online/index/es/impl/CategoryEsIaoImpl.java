package com.wfj.search.online.index.es.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.wfj.search.online.index.es.CategoryEsIao;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.CategoryIndexPojo;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.ActionWriteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>create at 16-3-26</p>
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
    public void upsert(CategoryIndexPojo category) throws JsonProcessingException, IndexException {
        String source = this.objectMapper.writeValueAsString(category);
        UpdateResponse resp = this.esClient.prepareUpdate(this.index, TYPE, category.getCategoryId()).setDoc(source)
                .setUpsert(source).get();
        ActionWriteResponse.ShardInfo shardInfo = resp.getShardInfo();
        if (shardInfo.getFailed() == shardInfo.getTotal() && shardInfo.getTotal() > 0) {
            logger.error("写入分类[{}]失败，所有节点都写入失败！", category.getCategoryId());
            throw new IndexException("所有节点都写入失败！");
        }
    }

    @Override
    public Collection<CategoryIndexPojo> multiGet(Set<String> categoryIds) throws IOException {
        MultiGetRequestBuilder multiGetRequestBuilder = this.esClient.prepareMultiGet();
        for (String categoryId : categoryIds) {
            multiGetRequestBuilder.add(this.index, TYPE, categoryId);
        }
        MultiGetResponse multiGetItemResponses = multiGetRequestBuilder.get();
        List<CategoryIndexPojo> pojos = Lists.newArrayListWithExpectedSize(categoryIds.size());
        for (MultiGetItemResponse multiGetItemResponse : multiGetItemResponses) {
            String source = multiGetItemResponse.getResponse().getSourceAsString();
            if (StringUtils.isNoneBlank(source)) {
                pojos.add(this.objectMapper.readValue(source, CategoryIndexPojo.class));
            }
        }
        return pojos;
    }

    @Override
    public CategoryIndexPojo get(String cid) {
        try {
            GetResponse resp = this.esClient.prepareGet(this.index, TYPE, cid).get();
            String source = resp.getSourceAsString();
            if (source != null) {
                return this.objectMapper.readValue(source, CategoryIndexPojo.class);
            } else {
                logger.warn("GET分类[{}]信息失败", cid);
            }
        } catch (Exception e) {
            logger.warn("GET分类[{}]信息失败", cid, e);
        }
        return null;
    }
}
