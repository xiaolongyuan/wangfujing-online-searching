package com.wfj.search.online.web.iao.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.wfj.search.online.common.constant.CollectionNames;
import com.wfj.search.online.common.pojo.interval.IntervalContentPojo;
import com.wfj.search.online.common.pojo.interval.IntervalDetailPojo;
import com.wfj.search.online.web.common.pojo.*;
import com.wfj.search.online.web.iao.IItemIAO;
import com.wfj.search.online.web.iao.SolrSearchException;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.*;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <p>create at 15-11-23</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
@Component("itemIAO")
public class ItemIAOImpl implements IItemIAO {
    private final static String COLLECTION_NAME = CollectionNames.ITEM_COLLECTION_NAME;
    private static final DecimalFormat NO_FORMAT = new DecimalFormat("000000000000");

    private final AtomicLong queryNo = new AtomicLong();
    @Autowired
    private SolrClient solrClient;

    @Override
    public List<FacetField.Count> facetField(SolrQuery query, String field) throws SolrSearchException {
        SolrQuery myQuery = query.getCopy().setRows(0).setFacet(true).addFacetField(field).setFacetMinCount(1)
                .setFacetLimit(Integer.MAX_VALUE);
        QueryResponse response;
        try {
            response = solrClient.query(COLLECTION_NAME, myQuery);
        } catch (SolrServerException | IOException e) {
            throw new SolrSearchException(e);
        }
        FacetField ff = response.getFacetField(field);
        return ff.getValues();
    }

    @Override
    public List<RangeDisplayPojo> facetPriceRange(SolrQuery query, IntervalContentPojo rule,
            List<IntervalDetailPojo> ruleDetails) throws SolrSearchException {
        String rangeField = rule.getField();
        SolrQuery myQuery = query.getCopy().setRows(0).setFacet(true).setFacetLimit(Integer.MAX_VALUE);
        Map<String, IntervalDetailPojo> intervalDetailPojoMap = Maps.newConcurrentMap();
        for (IntervalDetailPojo ruleDetail : ruleDetails) {
            String facetQuery = rangeField + ":[" + ruleDetail.getLowerLimit() + " TO " + ruleDetail
                    .getUpperLimit() + "]";
            myQuery.addFacetQuery(facetQuery);
            intervalDetailPojoMap.put(facetQuery, ruleDetail);
        }
        QueryResponse response;
        try {
            response = solrClient.query(COLLECTION_NAME, myQuery);
        } catch (SolrServerException | IOException e) {
            throw new SolrSearchException(e);
        }
        Map<String, Integer> facetQueryResult = response.getFacetQuery();
        if (facetQueryResult.size() <= 1) {// 当结果中只匹配一种价格区间时，可选列表中不添加价格区间
            return null;
        }
        List<RangeDisplayPojo> availableRanges = Collections.synchronizedList(Lists.newArrayList());
        for (Map.Entry<String, Integer> entry : facetQueryResult.entrySet()) {
            Integer count = entry.getValue();
            if (count != null && count > 0) {
                String facetQuery = entry.getKey();
                RangeDisplayPojo range = new RangeDisplayPojo();
                IntervalDetailPojo ruleDetail = intervalDetailPojoMap.get(facetQuery);
                if (!"*".equals(ruleDetail.getLowerLimit()) || !"*".equals(ruleDetail.getUpperLimit())) {
                    if ("*".equals(ruleDetail.getLowerLimit())) {
                        range.setDisplay(ruleDetail.getUpperLimit() + "以下");
                    } else if ("*".equals(ruleDetail.getUpperLimit())) {
                        range.setDisplay(ruleDetail.getLowerLimit() + "以上");
                    } else {
                        range.setDisplay(ruleDetail.getLowerLimit() + "-" + ruleDetail.getUpperLimit());
                    }
                    range.setMin(ruleDetail.getLowerLimit());
                    range.setMax(ruleDetail.getUpperLimit());
                    range.setFacetCount(count);
                    range.setOrder(ruleDetail.getOrderBy());
                    availableRanges.add(range);
                }
            }
        }
        /*
        for (IntervalDetailPojo ruleDetail : ruleDetails) {
            String facetQuery = rangeField + ":[" + ruleDetail.getLowerLimit() + " TO " + ruleDetail
                    .getUpperLimit() + "]";
            Integer count = facetQueryResult.get(facetQuery);
            if(count != null && count > 0) {
                RangeDisplayPojo range = new RangeDisplayPojo();
                if (!"*".equals(ruleDetail.getLowerLimit()) || !"*".equals(ruleDetail.getUpperLimit())) {
                    if ("*".equals(ruleDetail.getLowerLimit())) {
                        range.setDisplay(ruleDetail.getUpperLimit() + "以下");
                    } else if ("*".equals(ruleDetail.getUpperLimit())) {
                        range.setDisplay(ruleDetail.getLowerLimit() + "以上");
                    } else {
                        range.setDisplay(ruleDetail.getLowerLimit() + "-" + ruleDetail.getUpperLimit());
                    }
                    range.setMin(ruleDetail.getLowerLimit());
                    range.setMax(ruleDetail.getUpperLimit());
                    range.setFacetCount(count);
                    range.setOrder(ruleDetail.getOrderBy());
                    availableRanges.add(range);
                }
            }
        }
        */
        Collections.sort(availableRanges, new Comparator<RangeDisplayPojo>() {
            @Override
            public int compare(RangeDisplayPojo o1, RangeDisplayPojo o2) {
                return o1.getOrder() - o2.getOrder();
            }
        });
        return availableRanges;
    }

    @Override
    public Map<String, List<FacetField.Count>> facetPropertyValueIds(SolrQuery query,
            List<PropertyDisplayPojo> properties, String channel) throws SolrSearchException {
        SolrQuery myQuery = query.setRows(0).setFacet(true).setFacetLimit(Integer.MAX_VALUE);
        for (PropertyDisplayPojo property : properties) {
            myQuery.addFacetField("propertyValueIdOfPropertyId_" + property.getId() + "_" + channel)
                    .setFacetMinCount(1);
        }

        QueryResponse response;
        try {
            response = solrClient.query(COLLECTION_NAME, myQuery);
        } catch (SolrServerException | IOException e) {
            throw new SolrSearchException(e);
        }
        Map<String, List<FacetField.Count>> idFFCs = Maps.newConcurrentMap();
        for (PropertyDisplayPojo property : properties) {
            FacetField idsFF = response
                    .getFacetField("propertyValueIdOfPropertyId_" + property.getId() + "_" + channel);
            idFFCs.put(property.getId(), idsFF.getValues());
        }
        return idFFCs;
    }

    @Override
    public void query(SolrQuery query, SearchResult searchResult) throws SolrSearchException {
        QueryResponse response;
        try {
            response = solrClient.query(COLLECTION_NAME, query);
        } catch (SolrServerException | IOException e) {
            throw new SolrSearchException(e);
        }
        GroupResponse groupResponse = response.getGroupResponse();
        List<GroupCommand> groups = groupResponse.getValues();
        assert groups.size() == 1;// 只定义对spuId进行group计算
        GroupCommand groupBySpuId = groups.get(0);
        assert "spuId".equals(groupBySpuId.getName());// 只定义对spuId进行group计算
        searchResult.getSuccessList().clear();
        List<Group> spuItems = groupBySpuId.getValues();
        String channel = searchResult.getParams().getChannel();
        spuItems.forEach(group -> {
            SolrDocumentList itemDocs = group.getResult();
            SpuDisplayPojo spu = null;
            if (itemDocs.size() > 0) {
                // hhh 此处是否应该在group分组中将group.limit设置为最大，一次返回所有item，并进行填充？
                SolrDocument itemDoc = itemDocs.get(0);// 如果有数据，只会返回1条。(因为group.limit默认值1，所以没有特殊设置，此处最多只会返回一条。)
                ItemDisplayPojo item = new ItemDisplayPojo();
                item.setItemId((String) itemDoc.getFieldValue("itemId"));
                item.setSupplierId((String) itemDoc.getFieldValue("supplierId"));
                item.setStockMode((Integer) itemDoc.getFieldValue("stockMode"));
                item.setInventory((Integer) itemDoc.getFieldValue("inventory"));
                item.setOriginalPrice((Double) itemDoc.getFieldValue(("originalPrice")));
                item.setCurrentPrice((Double) itemDoc.getFieldValue("currentPrice"));
                item.setDiscountRate((Double) itemDoc.getFieldValue("discountRate"));
                item.setSkuId((String) itemDoc.getFieldValue("skuId"));
                item.setColorId((String) itemDoc.getFieldValue("colorId"));
                item.setColorName((String) itemDoc.getFieldValue("colorName"));
                item.setColorMasterPicture((String) itemDoc.getFieldValue("colorMasterPicture"));
                item.getColorMasterPictureOfPix().put("220x220",
                        (String) itemDoc.getFieldValue("colorMasterPictureOfPix_220x220"));
                item.getColorMasterPictureOfPix().put("60x60",
                        (String) itemDoc.getFieldValue("colorMasterPictureOfPix_60x60"));
                item.setType((Integer) itemDoc.getFieldValue("type"));
                item.setSpuId((String) itemDoc.getFieldValue("spuId"));
                item.setSpuName((String) itemDoc.getFieldValue("spuName"));
                item.setModel((String) itemDoc.getFieldValue("model"));
                item.setActiveBit((Integer) itemDoc.getFieldValue("activeBit"));
                item.setOnSellSince((Date) itemDoc.getFieldValue("onSellSince"));
                item.setBrandId((String) itemDoc.getFieldValue("brandId"));
                item.setBrandName((String) itemDoc.getFieldValue("brandName"));
                List<String> activeName = Lists.newArrayList();
                try {
                    activeName = itemDoc.getFieldValues("activeName").stream().map(Object::toString)
                            .collect(Collectors.toList());
                } catch (Exception ignored) {
                }
                item.setActiveName(activeName);
                item.setTitle((String) itemDoc.getFieldValue("title"));
                Object subTitle = itemDoc.getFieldValue("subTitle");
                Collection<Object> channelCategoryIds = itemDoc.getFieldValues("allLevelCategoryIds_" + channel);
                if (channelCategoryIds != null) {
                    item.setCategoryIds(channelCategoryIds.stream().map(Object::toString).collect(Collectors.toList()));
                }
                item.setSubTitle(subTitle == null ? "" : subTitle.toString());
                spu = SpuDisplayPojo.copyOf(item);
                spu.getColorItems().add(item);
                searchResult.getSuccessList().add(spu);
            }
        });
        final Thread masterThread = Thread.currentThread();
        final AtomicReference<Throwable> atomicException = new AtomicReference<>();
        ExecutorService pool = Executors.newCachedThreadPool(r -> {
            Thread thread = new Thread(r, "SearchThreads-" + NO_FORMAT.format(queryNo.addAndGet(1L)));
            thread.setDaemon(true);
            thread.setUncaughtExceptionHandler((t, e) -> {
                atomicException.set(e);
                masterThread.interrupt();
            });
            return thread;
        });
        CompletionService<Void> completionService = new ExecutorCompletionService<>(pool);
        try {
            searchResult.getSuccessList()
                    .forEach(spu -> completionService.submit(() -> {
                        SolrQuery solrQuery = new SolrQuery();
                        solrQuery.setQuery("spuId:" + spu.getSpuId());
                        // 因为以spuId分组查询时已经查询出一条item，接下来补全spu数据时跳过已查出的item
                        solrQuery.addFilterQuery("-itemId:" + spu.getColorItems().get(0).getItemId());
                        solrQuery.addFilterQuery("channels:" + channel);
                        solrQuery.addFilterQuery("allLevelCategoryIds_" + channel + ":*");
                        QueryResponse resp;
                        try {
                            resp = solrClient.query(COLLECTION_NAME, solrQuery);
                        } catch (IOException | SolrServerException e) {
                            try {
                                throw new SolrSearchException(e);
                            } catch (SolrSearchException e1) {
                                throw new RuntimeException(e1);
                            }
                        }
                        SolrDocumentList results = resp.getResults();
                        // 每个item有一个colorId，在返回的spu中，每个item对应不同的色系color。
                        Set<String> colors = Sets.newConcurrentHashSet();
                        colors.add(spu.getColorItems().get(0).getColorId());
                        int inventory = spu.getColorItems().get(0).getInventory();
                        Set<String> activeNames = Sets.newHashSet();
                        for (SolrDocument itemDoc : results) {
                            int _inventory = (Integer) itemDoc.getFieldValue("inventory");
                            inventory += _inventory;
                            String colorId = (String) itemDoc.getFieldValue("colorId");
                            if (!colors.contains(colorId)) {
                                ItemDisplayPojo item = new ItemDisplayPojo();
                                item.setItemId((String) itemDoc.getFieldValue("itemId"));
                                item.setSupplierId((String) itemDoc.getFieldValue("supplierId"));
                                item.setStockMode((Integer) itemDoc.getFieldValue("stockMode"));
                                item.setInventory(inventory);
                                item.setOriginalPrice((Double) itemDoc.getFieldValue(("originalPrice")));
                                item.setCurrentPrice((Double) itemDoc.getFieldValue("currentPrice"));
                                item.setDiscountRate((Double) itemDoc.getFieldValue("discountRate"));
                                item.setSkuId((String) itemDoc.getFieldValue("skuId"));
                                item.setColorId(colorId);
                                item.setColorName((String) itemDoc.getFieldValue("colorName"));
                                item.setColorMasterPicture((String) itemDoc.getFieldValue("colorMasterPicture"));
                                item.getColorMasterPictureOfPix().put("220x220",
                                        (String) itemDoc.getFieldValue("colorMasterPictureOfPix_220x220"));
                                item.getColorMasterPictureOfPix().put("60x60",
                                        (String) itemDoc.getFieldValue("colorMasterPictureOfPix_60x60"));
                                item.setType((Integer) itemDoc.getFieldValue("type"));
                                item.setSpuId((String) itemDoc.getFieldValue("spuId"));
                                item.setSpuName((String) itemDoc.getFieldValue("spuName"));
                                item.setModel((String) itemDoc.getFieldValue("model"));
                                item.setActiveBit((Integer) itemDoc.getFieldValue("activeBit"));
                                item.setOnSellSince((Date) itemDoc.getFieldValue("onSellSince"));
                                item.setBrandId((String) itemDoc.getFieldValue("brandId"));
                                item.setBrandName((String) itemDoc.getFieldValue("brandName"));
                                try {
                                    item.setActiveName(
                                            itemDoc.getFieldValues("activeName").stream().map(Object::toString)
                                                    .collect(Collectors.toList()));
                                } catch (Exception ignored) {
                                }
                                item.setTitle((String) itemDoc.getFieldValue("title"));
                                item.setSubTitle((String) itemDoc.getFieldValue("subTitle"));
                                activeNames.addAll(item.getActiveName());
                                spu.getColorItems().add(item);
                                colors.add(colorId);
                            }
                            spu.getActiveName().addAll(activeNames);
                            spu.setInventory(inventory);
                        }
                    }, null));
            for (int i = 0; i < searchResult.getSuccessList().size(); i++) {
                completionService.take();
            }
        } catch (InterruptedException e) {
            Throwable throwable = atomicException.get();
            if (throwable instanceof RuntimeException) {
                if (throwable.getCause() instanceof SolrSearchException) {
                    throw (SolrSearchException) throwable.getCause();
                }
                throw (RuntimeException) throwable;
            } else {
                throw new RuntimeException(throwable.getMessage(), throwable);
            }
        } finally {
            pool.shutdown();
        }
        Pagination pages = searchResult.getPagination();
        pages.setTotalResults(groupBySpuId.getNGroups());
        pages.setTotalPage((pages.getTotalResults() + pages.getPageSize() - 1) / pages.getPageSize());
    }

    @Override
    public QueryResponse query(SolrQuery query) throws SolrSearchException {
        QueryResponse response;
        try {
            response = solrClient.query(COLLECTION_NAME, query);
        } catch (SolrServerException | IOException e) {
            throw new SolrSearchException(e);
        }
        return response;
    }

    @Override
    public void spellCheckSuggest(SolrQuery query, SearchResult searchResult) throws SolrSearchException {
        QueryResponse response;
        try {
            response = solrClient.query(COLLECTION_NAME, query);
        } catch (SolrServerException | IOException e) {
            throw new SolrSearchException(e);
        }
        SpellCheckResponse spellCheckResponse = response.getSpellCheckResponse();
        List<SpellCheckResponse.Suggestion> suggestions = spellCheckResponse.getSuggestions();
        for (SpellCheckResponse.Suggestion suggestion : suggestions) {
            searchResult.getLikelyQueries().add(suggestion.getToken());
        }
    }
}
