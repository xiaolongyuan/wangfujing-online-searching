package com.wfj.search.online.index.es.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wfj.search.online.index.es.EsBulkIao;
import com.wfj.search.online.index.pojo.IndexPojos;
import com.wfj.search.online.index.pojo.ItemIndexPojo;
import com.wfj.search.online.index.pojo.failure.DataType;
import com.wfj.search.online.index.pojo.failure.Failure;
import com.wfj.search.online.index.pojo.failure.FailureType;
import com.wfj.search.online.index.pojo.failure.MultiFailure;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>create at 16-4-26</p>
 *
 * @author liufl
 * @since 1.0.35
 */
@Component("esBulkIao")
public class EsBulkIaoImpl implements EsBulkIao {
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Optional<Failure> bulkIndex(IndexPojos indexPojos, long version) {
        BulkRequestBuilder bulkRequestBuilder = this.esClient.prepareBulk();
        MultiFailure failure = new MultiFailure();
        indexPojos.brandIndexPojos.values().forEach(
                indexPojo -> addBulkIndex(indexPojo, indexPojo.getBrandId(), "brand", DataType.brand,
                        bulkRequestBuilder, failure));
        indexPojos.categoryIndexPojos.values().forEach(
                indexPojo -> addBulkIndex(indexPojo, indexPojo.getCategoryId(), "category", DataType.category,
                        bulkRequestBuilder, failure));
        indexPojos.colorIndexPojos.values().forEach(
                indexPojo -> addBulkIndex(indexPojo, indexPojo.getColorId(), "color", DataType.color,
                        bulkRequestBuilder, failure));
        saleCount(indexPojos.itemIndexPojos);
        clickCount(indexPojos.itemIndexPojos);
        indexPojos.itemIndexPojos.values().forEach(item -> item.setOperationSid(version));
        indexPojos.itemIndexPojos.values().forEach(
                indexPojo -> addBulkIndex(indexPojo, indexPojo.getItemId(), "item", DataType.item, bulkRequestBuilder,
                        failure));
        indexPojos.activityPojos.values().forEach(
                indexPojo -> addBulkIndex(indexPojo, indexPojo.getActiveId(), "activity", DataType.activity,
                        bulkRequestBuilder, failure));
        indexPojos.skuIndexPojos.values().forEach(
                indexPojo -> addBulkIndex(indexPojo, indexPojo.getSkuId(), "sku", DataType.sku, bulkRequestBuilder,
                        failure));
        indexPojos.spuIndexPojos.values().forEach(
                indexPojo -> addBulkIndex(indexPojo, indexPojo.getSpuId(), "spu", DataType.spu, bulkRequestBuilder,
                        failure));
        indexPojos.propertyIndexPojos.values().forEach(
                indexPojo -> addBulkIndex(indexPojo, indexPojo.getPropertyId(), "property", DataType.property,
                        bulkRequestBuilder, failure));
        indexPojos.propertyValueIndexPojos.values().forEach(
                indexPojo -> addBulkIndex(indexPojo, indexPojo.getPropertyValueId(), "property-value",
                        DataType.property_value, bulkRequestBuilder, failure));
        indexPojos.standardIndexPojos.values().forEach(
                indexPojo -> addBulkIndex(indexPojo, indexPojo.getStandardId(), "standard", DataType.standard,
                        bulkRequestBuilder, failure));
        indexPojos.tagIndexPojos.values().forEach(
                indexPojo -> addBulkIndex(indexPojo, indexPojo.getTagId(), "tag", DataType.tag, bulkRequestBuilder,
                        failure));
        BulkResponse bulkItemResponses = bulkRequestBuilder.get();
        if (bulkItemResponses.hasFailures()) {
            for (BulkItemResponse bulkItemResponse : bulkItemResponses) {
                if (bulkItemResponse.isFailed()) {
                    BulkItemResponse.Failure failure1 = bulkItemResponse.getFailure();
                    failure.addFailure(
                            new Failure(failure1.getType() + '[' + failure1.getId() + ']' + failure1.getMessage()));
                }
            }
        }
        if (failure.getFailures().isEmpty()) {
            return Optional.empty();
        }
        return failure.toOptional();
    }

    private <T> void addBulkIndex(T indexPojo, String typeId, String typeName, DataType dataType,
            BulkRequestBuilder bulkRequestBuilder, MultiFailure failure) {
        try {
            bulkRequestBuilder.add(esClient.prepareIndex(index, typeName, typeId)
                    .setSource(objectMapper.writeValueAsString(indexPojo)));
        } catch (JsonProcessingException e) {
            failure.addFailure(new Failure(dataType, FailureType.buildData, typeId, "转换为JSON失败", e));
        }
    }

    private void saleCount(Map<String, ItemIndexPojo> itemIndexPojos) {
        MultiGetRequestBuilder multiGetRequestBuilder = this.esClient.prepareMultiGet();
        itemIndexPojos.values().forEach(item -> multiGetRequestBuilder.add(index, "item-sales", item.getItemId())
                .add(index, "sku-sales", item.getItemId()).add(index, "spu-sales", item.getItemId()));
        MultiGetResponse multiGetItemResponses = multiGetRequestBuilder.get();
        for (MultiGetItemResponse multiGetItemResponse : multiGetItemResponses) {
            String sourceAsString = multiGetItemResponse.getResponse().getSourceAsString();
            if (StringUtils.isNotBlank(sourceAsString)) {
                JSONObject json = JSONObject.parseObject(sourceAsString);
                Integer sales = json.getInteger("sales");
                String itemId = multiGetItemResponse.getId();
                ItemIndexPojo itemIndexPojo = itemIndexPojos.get(itemId);
                switch (multiGetItemResponse.getType()) {
                    case "item-sales":
                        itemIndexPojo.setItemSale(sales);
                        break;
                    case "sku-sales":
                        itemIndexPojo.setSkuSale(sales);
                        break;
                    case "spu-sales":
                        itemIndexPojo.setSpuSale(sales);
                        break;
                    default:
                }
            }
        }
    }

    private void clickCount(Map<String, ItemIndexPojo> itemIndexPojos) {
        MultiGetRequestBuilder multiGetRequestBuilder = this.esClient.prepareMultiGet();
        Set<String> spuIds = itemIndexPojos.values().stream().map(ItemIndexPojo::getSpuId).collect(Collectors.toSet());
        Map<String, List<ItemIndexPojo>> spuItems = Maps.newConcurrentMap();
        spuIds.forEach(spuId -> {
            multiGetRequestBuilder.add(index, "clicks", spuId);
            spuItems.put(spuId, Collections.synchronizedList(Lists.newArrayList()));
        });
        itemIndexPojos.values().forEach(item -> spuItems.get(item.getSpuId()).add(item));
        MultiGetResponse multiGetItemResponses = multiGetRequestBuilder.get();
        for (MultiGetItemResponse multiGetItemResponse : multiGetItemResponses) {
            String sourceAsString = multiGetItemResponse.getResponse().getSourceAsString();
            if (StringUtils.isNotBlank(sourceAsString)) {
                JSONObject json = JSONObject.parseObject(sourceAsString);
                Integer spuClick = json.getInteger("spuClick");
                String spuId = json.getString("spuId");
                spuItems.get(spuId).forEach(item -> item.setSpuClick(spuClick));
            }
        }
    }
}
