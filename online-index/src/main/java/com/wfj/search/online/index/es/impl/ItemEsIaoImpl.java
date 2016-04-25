package com.wfj.search.online.index.es.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.wfj.search.online.index.es.ItemEsIao;
import com.wfj.search.online.index.es.ScrollPage;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.*;
import com.wfj.search.utils.es.EsUtil;
import org.elasticsearch.action.ActionWriteResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.engine.DocumentAlreadyExistsException;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>create at 16-3-27</p>
 *
 * @author liufl
 * @since 1.0.19
 */
@Component("itemEsIao")
public class ItemEsIaoImpl implements ItemEsIao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TYPE = "item";
    @Autowired
    private Client esClient;
    @Value("${es.index}")
    private String index;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void delete(String itemId) {
        this.esClient.prepareDelete(this.index, TYPE, itemId).get();
    }

    @Override
    public void upsert(ItemIndexPojo itemIndexPojo) throws IndexException, JsonProcessingException {
        try {
            EsUtil.upsert(this.esClient, itemIndexPojo, itemIndexPojo.getItemId(), index, TYPE);
        } catch (IllegalStateException e) {
            Throwable cause = e;
            while (cause.getCause() != null) {
                cause = e.getCause();
            }
            if (cause instanceof DocumentAlreadyExistsException) {
                upsert(itemIndexPojo);
            } else {
                throw new IndexException(e);
            }
        }
    }

    @Override
    public ScrollPage<ItemIndexPojo> startScrollForAll() throws IndexException {
        SearchResponse scrollResp;
        try {
            scrollResp = this.esClient.prepareSearch(this.index).setTypes(TYPE)
                    .setSearchType(SearchType.DEFAULT)
                    .setQuery(QueryBuilders.matchAllQuery()).setSize(100)
                    .setScroll(new TimeValue(60000)).get();
        } catch (IllegalStateException e) {
            throw new IndexException("创建scroll失败，操作被中断", e);
        } catch (RuntimeException e) {
            throw new IndexException("创建scroll失败，" + e.getMessage(), e);
        }
        ScrollPage<ItemIndexPojo> scrollPage = new ScrollPage<>();
        scrollPage.setTotal(scrollResp.getHits().totalHits());
        scrollPage.setScrollId(scrollResp.getScrollId());
        scrollPage.setScrollIdTTL(60000L);
        SearchHit[] hits = scrollResp.getHits().getHits();
        if (hits.length > 0) {
            scrollPage.setList(Lists.newArrayListWithExpectedSize(hits.length));
            for (SearchHit hit : hits) {
                try {
                    String source = hit.getSourceAsString();
                    if (source != null) {
                        scrollPage.getList().add(this.objectMapper.readValue(source, ItemIndexPojo.class));
                    }
                } catch (IOException e) {
                    logger.error("JSON反序列化失败", e);
                    throw new IndexException("从JSON反序列化商品信息失败", e);
                }
            }
        }
        return scrollPage;
    }

    @Override
    public ScrollPage<ItemIndexPojo> scroll(String scrollId, Long scrollIdTTL) throws IndexException {
        SearchResponse scrollResp;
        try {
            scrollResp = this.esClient.prepareSearchScroll(scrollId)
                    .setScroll(new TimeValue(scrollIdTTL)).get();
        } catch (IllegalStateException e) {
            throw new IndexException("执行scroll失败，操作被中断", e);
        } catch (RuntimeException e) {
            throw new IndexException("执行scroll失败，" + e.getMessage(), e);
        }
        ScrollPage<ItemIndexPojo> scrollPage = new ScrollPage<>();
        scrollPage.setTotal(scrollResp.getHits().getTotalHits());
        scrollPage.setScrollId(scrollResp.getScrollId());
        scrollPage.setScrollIdTTL(scrollIdTTL);
        SearchHit[] hits = scrollResp.getHits().getHits();
        if (hits.length > 0) {
            scrollPage.setList(Lists.newArrayListWithExpectedSize(hits.length));
            for (SearchHit hit : hits) {
                try {
                    String source = hit.getSourceAsString();
                    if (source != null) {
                        scrollPage.getList().add(this.objectMapper.readValue(source, ItemIndexPojo.class));
                    }
                } catch (IOException e) {
                    logger.error("JSON反序列化失败", e);
                    throw new IndexException("从JSON反序列化评论信息失败", e);
                }
            }
        }
        return scrollPage;
    }

    @Override
    public ScrollPage<ItemIndexPojo> startScrollOfBrand(String brandId) throws IndexException {
        SearchResponse scrollResp;
        try {
            scrollResp = this.esClient.prepareSearch(this.index).setTypes(TYPE)
                    .setSearchType(SearchType.DEFAULT)
                    .setQuery(QueryBuilders.matchQuery("brandId", brandId)).setSize(100)
                    .setScroll(new TimeValue(60000)).get();
        } catch (IllegalStateException e) {
            throw new IndexException("创建scroll失败，操作被中断", e);
        } catch (RuntimeException e) {
            throw new IndexException("创建scroll失败，" + e.getMessage(), e);
        }
        ScrollPage<ItemIndexPojo> scrollPage = new ScrollPage<>();
        scrollPage.setTotal(scrollResp.getHits().totalHits());
        scrollPage.setScrollId(scrollResp.getScrollId());
        scrollPage.setScrollIdTTL(60000L);
        SearchHit[] hits = scrollResp.getHits().getHits();
        if (hits.length > 0) {
            scrollPage.setList(Lists.newArrayListWithExpectedSize(hits.length));
            for (SearchHit hit : hits) {
                try {
                    String source = hit.getSourceAsString();
                    if (source != null) {
                        scrollPage.getList().add(this.objectMapper.readValue(source, ItemIndexPojo.class));
                    }
                } catch (IOException e) {
                    logger.error("JSON反序列化失败", e);
                    throw new IndexException("从JSON反序列化商品信息失败", e);
                }
            }
        }
        return scrollPage;
    }

    @Override
    public Iterable<ItemIndexPojo> multiGet(Collection<String> itemIds) throws IOException {
        MultiGetRequestBuilder multiGetRequestBuilder = this.esClient.prepareMultiGet();
        itemIds.forEach(itemId -> multiGetRequestBuilder.add(this.index, TYPE, itemId));
        MultiGetResponse multiGetItemResponses = multiGetRequestBuilder.get();
        List<ItemIndexPojo> pojos = Lists.newArrayListWithExpectedSize(itemIds.size());
        for (MultiGetItemResponse multiGetItemResponse : multiGetItemResponses) {
            String source = multiGetItemResponse.getResponse().getSourceAsString();
            if (source != null) {
                pojos.add(this.objectMapper.readValue(source, ItemIndexPojo.class));
            }
        }
        return pojos;
    }

    @Override
    public List<ItemIndexPojo> findBySkuId(String skuId) {
        try {
            SearchResponse resp = this.esClient.prepareSearch(this.index).setTypes(TYPE)
                    .setSearchType(SearchType.DEFAULT)
                    .setQuery(QueryBuilders.matchQuery("skuId", skuId)).setSize(10000).get();
            SearchHit[] hits = resp.getHits().getHits();
            List<ItemIndexPojo> pojos = Lists.newArrayListWithExpectedSize(hits.length);
            for (SearchHit searchHitFields : hits) {
                String source = searchHitFields.getSourceAsString();
                if (source != null) {
                    pojos.add(this.objectMapper.readValue(source, ItemIndexPojo.class));
                }
            }
            return pojos;
        } catch (Exception e) {
            logger.error("从ES查找SKU[{}]下商品失败", skuId, e);
            return Lists.newArrayList();
        }
    }

    @Override
    public List<ItemIndexPojo> findBySpuId(String spuId) {
        try {
            SearchResponse resp = this.esClient.prepareSearch(this.index).setTypes(TYPE)
                    .setSearchType(SearchType.DEFAULT)
                    .setQuery(QueryBuilders.matchQuery("spuId", spuId)).setSize(10000).get();
            SearchHit[] hits = resp.getHits().getHits();
            List<ItemIndexPojo> pojos = Lists.newArrayListWithExpectedSize(hits.length);
            for (SearchHit searchHitFields : hits) {
                String source = searchHitFields.getSourceAsString();
                if (source != null) {
                    pojos.add(this.objectMapper.readValue(source, ItemIndexPojo.class));
                }
            }
            return pojos;
        } catch (Exception e) {
            logger.error("从ES查找SPU[{}]下商品失败", spuId, e);
            return Lists.newArrayList();
        }
    }

    @Override
    public ScrollPage<ItemIndexPojo> startScrollOfOperationSidGreaterThanEqual(long version) throws IndexException {
        SearchResponse scrollResp;
        try {
            scrollResp = this.esClient.prepareSearch(this.index).setTypes(TYPE)
                    .setSearchType(SearchType.DEFAULT)
                    .setQuery(QueryBuilders.rangeQuery("operationSid").gte(version)).setSize(100)
                    .setScroll(new TimeValue(60000)).get();
        } catch (IllegalStateException e) {
            throw new IndexException("创建scroll失败，操作被中断", e);
        } catch (RuntimeException e) {
            throw new IndexException("创建scroll失败，" + e.getMessage(), e);
        }
        ScrollPage<ItemIndexPojo> scrollPage = new ScrollPage<>();
        scrollPage.setTotal(scrollResp.getHits().totalHits());
        scrollPage.setScrollId(scrollResp.getScrollId());
        scrollPage.setScrollIdTTL(60000L);
        SearchHit[] hits = scrollResp.getHits().getHits();
        if (hits.length > 0) {
            scrollPage.setList(Lists.newArrayListWithExpectedSize(hits.length));
            for (SearchHit hit : hits) {
                try {
                    String source = hit.getSourceAsString();
                    if (source != null) {
                        scrollPage.getList().add(this.objectMapper.readValue(source, ItemIndexPojo.class));
                    }
                } catch (IOException e) {
                    logger.error("JSON反序列化失败", e);
                    throw new IndexException("从JSON反序列化商品信息失败", e);
                }
            }
        }
        return scrollPage;
    }

    @Override
    public ScrollPage<ItemIndexPojo> startScrollOfCategory(String categoryId, String channel) throws IndexException {
        SearchResponse scrollResp;
        try {
            scrollResp = this.esClient.prepareSearch(this.index).setTypes(TYPE)
                    .setSearchType(SearchType.DEFAULT)
                    .setQuery(QueryBuilders.termQuery("allLevelCategoryIds.allLevelCategoryIds_" + channel, categoryId))
                    .setSize(100).setScroll(new TimeValue(60000)).get();
        } catch (IllegalStateException e) {
            throw new IndexException("创建scroll失败，操作被中断", e);
        } catch (RuntimeException e) {
            throw new IndexException("创建scroll失败，" + e.getMessage(), e);
        }
        ScrollPage<ItemIndexPojo> scrollPage = new ScrollPage<>();
        scrollPage.setTotal(scrollResp.getHits().totalHits());
        scrollPage.setScrollId(scrollResp.getScrollId());
        scrollPage.setScrollIdTTL(60000L);
        SearchHit[] hits = scrollResp.getHits().getHits();
        if (hits.length > 0) {
            scrollPage.setList(Lists.newArrayListWithExpectedSize(hits.length));
            for (SearchHit hit : hits) {
                try {
                    String source = hit.getSourceAsString();
                    if (source != null) {
                        scrollPage.getList().add(this.objectMapper.readValue(source, ItemIndexPojo.class));
                    }
                } catch (IOException e) {
                    logger.error("JSON反序列化失败", e);
                    throw new IndexException("从JSON反序列化商品信息失败", e);
                }
            }
        }
        return scrollPage;
    }

    @Override
    public void updateItemSales(String itemId, Integer itemSales) throws IndexException {
        JSONObject doc = new JSONObject();
        doc.put("itemSale", itemSales);
        UpdateResponse resp = this.esClient.prepareUpdate(this.index, TYPE, itemId).setDoc(doc.toJSONString())
                .get();
        ActionWriteResponse.ShardInfo shardInfo = resp.getShardInfo();
        if (shardInfo.getFailed() == shardInfo.getTotal() && shardInfo.getTotal() > 0) {
            logger.error("写入商品[{}]失败，所有节点都写入失败！", itemId);
            throw new IndexException("所有节点都写入失败！");
        }
    }

    @Override
    public void updateBrandNameBrandAliasedByBrandId(String brandId, String brandName, List<String> brandAliases)
            throws IndexException, JsonProcessingException {
        ScrollPage<ItemIndexPojo> scrollPage = this.startScrollOfBrand(brandId);
        while (true) {
            List<ItemIndexPojo> list = scrollPage.getList();
            if (list == null) {
                break;
            }
            list.forEach(item -> {
                item.setBrandName(brandName);
                item.getBrandAliases().clear();
                item.getBrandAliases().addAll(brandAliases);
            });
            for (ItemIndexPojo itemIndexPojo : list) {
                this.upsert(itemIndexPojo);
            }
            scrollPage = this.scroll(scrollPage.getScrollId(), scrollPage.getScrollIdTTL());
        }
    }

    @Override
    public ScrollPage<ItemIndexPojo> startScrollOfCategoryBeforeVersion(String categoryId, String channel,
            long version) throws IndexException {
        SearchResponse scrollResp;
        try {
            QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("allLevelCategoryIds.allLevelCategoryIds_" + channel, categoryId))
                    .must(QueryBuilders.rangeQuery("operationSid").lt(version));
            scrollResp = this.esClient.prepareSearch(this.index).setTypes(TYPE)
                    .setSearchType(SearchType.DEFAULT)
                    .setQuery(queryBuilder)
                    .setSize(100).setScroll(new TimeValue(60000)).get();
        } catch (IllegalStateException e) {
            throw new IndexException("创建scroll失败，操作被中断", e);
        } catch (RuntimeException e) {
            throw new IndexException("创建scroll失败，" + e.getMessage(), e);
        }
        ScrollPage<ItemIndexPojo> scrollPage = new ScrollPage<>();
        scrollPage.setTotal(scrollResp.getHits().totalHits());
        scrollPage.setScrollId(scrollResp.getScrollId());
        scrollPage.setScrollIdTTL(60000L);
        SearchHit[] hits = scrollResp.getHits().getHits();
        if (hits.length > 0) {
            scrollPage.setList(Lists.newArrayListWithExpectedSize(hits.length));
            for (SearchHit hit : hits) {
                try {
                    String source = hit.getSourceAsString();
                    if (source != null) {
                        scrollPage.getList().add(this.objectMapper.readValue(source, ItemIndexPojo.class));
                    }
                } catch (IOException e) {
                    logger.error("JSON反序列化失败", e);
                    throw new IndexException("从JSON反序列化商品信息失败", e);
                }
            }
        }
        return scrollPage;
    }

    @Override
    public void removeExpiredOfCategory(long lastKeepVersion, String categoryId, String channelOfCategory)
            throws IndexException {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders
                        .termQuery("allLevelCategoryIds.allLevelCategoryIds_" + channelOfCategory, categoryId))
                .must(QueryBuilders.rangeQuery("operationSid").lt(lastKeepVersion));
        while (true) {
            SearchResponse searchResponse = this.esClient.prepareSearch(this.index).setTypes(TYPE)
                    .setQuery(queryBuilder)
                    .setSize(10000).get();
            if (searchResponse.getHits().totalHits() == 0) {
                break;
            }
            SearchHit[] hits = searchResponse.getHits().getHits();
            for (SearchHit hit : hits) {
                this.delete(hit.getId());
            }
        }
    }

    @Override
    public void updateSkuInfos(SkuIndexPojo skuIndexPojo, ColorIndexPojo colorIndexPojo,
            StandardIndexPojo standardIndexPojo, String masterPic,
            Map<String, String> colorMasterPictureOfPix) throws IndexException, JsonProcessingException {
        List<ItemIndexPojo> bySkuId = this.findBySkuId(skuIndexPojo.getSkuId());
        for (ItemIndexPojo itemIndexPojo : bySkuId) {
            itemIndexPojo.setType(skuIndexPojo.getType());
            itemIndexPojo.setColorId(skuIndexPojo.getColorId());
            itemIndexPojo.setColorName(colorIndexPojo.getColorName());
            itemIndexPojo.setColorAlias(skuIndexPojo.getColorAlias());
            itemIndexPojo.setStandardId(standardIndexPojo.getStandardId());
            itemIndexPojo.setStandardName(standardIndexPojo.getStandardName());
            itemIndexPojo.getPictures().clear();
            itemIndexPojo.getPictures().addAll(skuIndexPojo.getPictures());
            itemIndexPojo.setColorMasterPicture(masterPic);
            itemIndexPojo.getColorMasterPictureOfPix().clear();
            itemIndexPojo.getColorMasterPictureOfPix().putAll(colorMasterPictureOfPix);
            itemIndexPojo.setTitle(skuIndexPojo.getTitle());
            itemIndexPojo.setSubTitle(skuIndexPojo.getSubTitle());
            itemIndexPojo.getActiveKeywords().clear();
            itemIndexPojo.getActiveKeywords().addAll(skuIndexPojo.getActiveKeywords());
            this.upsert(itemIndexPojo);
        }
    }

    @Override
    public void removeExpiredOfSku(long lastKeepVersion, String skuId) {
        List<ItemIndexPojo> bySkuId = this.findBySkuId(skuId);
        bySkuId.stream().filter(itemIndexPojo -> itemIndexPojo.getOperationSid() < lastKeepVersion)
                .forEach(itemIndexPojo -> this.delete(itemIndexPojo.getItemId()));
    }

    @Override
    public void updateSpuInfos(SpuIndexPojo spuIndexPojo, BrandIndexPojo brandIndexPojo,
            Map<String, List<String>> allLevelCategoryIds, Map<String, List<String>> allLevelCategoryNames,
            Map<String, List<String>> categoryIdUnderCategory, List<TagIndexPojo> tagIndexPojos,
            Map<String, List<String>> propertyValues, Map<String, List<String>> propertyIds,
            Map<String, String> propertyValueIdOfPropertyId) throws IndexException, JsonProcessingException {
        List<ItemIndexPojo> bySpuId = this.findBySpuId(spuIndexPojo.getSpuId());
        for (ItemIndexPojo itemIndexPojo : bySpuId) {
            itemIndexPojo.setSpuName(spuIndexPojo.getSpuName());
            itemIndexPojo.setLongDesc(spuIndexPojo.getLongDesc());
            itemIndexPojo.setShortDesc(spuIndexPojo.getShortDesc());
            itemIndexPojo.setModel(spuIndexPojo.getModel());
            itemIndexPojo.setActiveBit(spuIndexPojo.getActiveBit());
            itemIndexPojo.setPageDescription(spuIndexPojo.getPageDescription());
            itemIndexPojo.getAliases().clear();
            itemIndexPojo.getAliases().addAll(spuIndexPojo.getAliases());
            itemIndexPojo.setOnSellSince(spuIndexPojo.getOnSellSince());
            itemIndexPojo.setBrandId(spuIndexPojo.getBrandId());
            itemIndexPojo.setBrandName(brandIndexPojo.getBrandName());
            itemIndexPojo.getBrandAliases().clear();
            itemIndexPojo.getBrandAliases().addAll(brandIndexPojo.getBrandAliases());
            itemIndexPojo.getAllLevelCategoryIds().clear();
            itemIndexPojo.getAllLevelCategoryIds().putAll(allLevelCategoryIds);
            itemIndexPojo.getAllLevelCategoryNames().clear();
            itemIndexPojo.getAllLevelCategoryNames().putAll(allLevelCategoryNames);
            itemIndexPojo.getCategoryIdUnderCategory().clear();
            itemIndexPojo.getCategoryIdUnderCategory().putAll(categoryIdUnderCategory);
            itemIndexPojo.getTagIds().clear();
            itemIndexPojo.getTagIds().addAll(tagIndexPojos.stream().map(TagIndexPojo::getTagId).collect(
                    Collectors.toList()));
            itemIndexPojo.getTags().clear();
            itemIndexPojo.getTags()
                    .addAll(tagIndexPojos.stream().map(TagIndexPojo::getTagName).collect(Collectors.toList()));
            itemIndexPojo.getPropertyValues().clear();
            itemIndexPojo.getPropertyValues().putAll(propertyValues);
            itemIndexPojo.getPropertyIds().clear();
            itemIndexPojo.getPropertyIds().putAll(propertyIds);
            itemIndexPojo.getPropertyValueIdOfPropertyId().clear();
            itemIndexPojo.getPropertyValueIdOfPropertyId().putAll(propertyValueIdOfPropertyId);
            this.upsert(itemIndexPojo);
        }
    }

    @Override
    public void updateItemPrice(String itemId, double currentPrice, long version) throws IndexException {
        JSONObject doc = new JSONObject();
        doc.put("currentPrice", currentPrice);
        doc.put("operationSid", version);
        UpdateResponse resp = this.esClient.prepareUpdate(this.index, TYPE, itemId).setDoc(doc.toJSONString())
                .get();
        ActionWriteResponse.ShardInfo shardInfo = resp.getShardInfo();
        if (shardInfo.getFailed() == shardInfo.getTotal() && shardInfo.getTotal() > 0) {
            logger.error("写入商品价格[{}]失败，所有节点都写入失败！", itemId);
            throw new IndexException("所有节点都写入失败！");
        }
    }

    @Override
    public void updateSpuSales(String spuId, Integer spuSales) throws IndexException, JsonProcessingException {
        List<ItemIndexPojo> bySpuId = this.findBySpuId(spuId);
        for (ItemIndexPojo itemIndexPojo : bySpuId) {
            itemIndexPojo.setSpuSale(spuSales);
            this.upsert(itemIndexPojo);
        }
    }

    @Override
    public void updateSkuSales(String skuId, Integer skuSales) throws IndexException, JsonProcessingException {
        List<ItemIndexPojo> bySkuId = this.findBySkuId(skuId);
        for (ItemIndexPojo itemIndexPojo : bySkuId) {
            itemIndexPojo.setSkuSale(skuSales);
            this.upsert(itemIndexPojo);
        }
    }

    @Override
    public void removeExpiredOfBrand(long lastKeepVersion, String brandId) {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("brandId", brandId))
                .must(QueryBuilders.rangeQuery("operationSid").lt(lastKeepVersion));
        while (true) {
            SearchResponse searchResponse = this.esClient.prepareSearch(this.index).setTypes(TYPE)
                    .setQuery(queryBuilder)
                    .setSize(10000).get();
            if (searchResponse.getHits().totalHits() == 0) {
                break;
            }
            SearchHit[] hits = searchResponse.getHits().getHits();
            for (SearchHit hit : hits) {
                this.delete(hit.getId());
            }
        }
    }
}
