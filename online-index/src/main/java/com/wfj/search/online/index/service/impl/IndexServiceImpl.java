package com.wfj.search.online.index.service.impl;

import com.google.common.collect.Lists;
import com.wfj.search.online.index.es.ItemEsIao;
import com.wfj.search.online.index.es.ScrollPage;
import com.wfj.search.online.index.iao.IItemIAO;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.iao.QueryException;
import com.wfj.search.online.index.pojo.ItemIndexPojo;
import com.wfj.search.online.index.pojo.SuggestionIndexPojo;
import com.wfj.search.online.index.pojo.failure.DataType;
import com.wfj.search.online.index.pojo.failure.Failure;
import com.wfj.search.online.index.pojo.failure.FailureType;
import com.wfj.search.online.index.pojo.failure.MultiFailure;
import com.wfj.search.online.index.service.IIndexService;
import com.wfj.search.online.index.service.IRetryService;
import com.wfj.search.utils.timer.Timer;
import com.wfj.search.utils.timer.TimerStop;
import org.apache.solr.client.solrj.response.FacetField;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.wfj.search.online.common.pojo.OnlineRetryNotePojo.*;

/**
 * <p>create at 15-9-9</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
@Service("indexService")
public class IndexServiceImpl implements IIndexService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ItemEsIao itemEsIao;
    @Autowired
    private IItemIAO itemIAO;
    @Autowired
    private IRetryService retryService;

    @Override
    public void commit() throws IndexException {
        this.itemIAO.commit();
    }

    @Override
    public Optional<Failure> indexAllFromEs(long version) {
        logger.debug("version {}", version);
        Timer timer = new Timer();
        timer.start();
        MultiFailure multiFailure = new MultiFailure();
        ScrollPage<ItemIndexPojo> scrollPage;
        try {
            scrollPage = this.itemEsIao.startScrollForAll();
        } catch (IndexException e) {
            Failure failure = new Failure(DataType.item, FailureType.retrieveFromES, "", "scroll商品ES数据失败", e);
            return Optional.of(failure);
        }
        multiFailure.setTotal(scrollPage.getTotal());
        while (true) {
            List<ItemIndexPojo> list = scrollPage.getList();
            if (list == null) {
                break;
            }
            list.forEach(item -> item.setOperationSid(version));
            list = list.stream().map(item -> {
                if (item.getUpTime() == null) {
                    multiFailure.addFailure(
                            new Failure(DataType.item, FailureType.save2Index, item.getItemId(), "上架时间为空", null));
                    return null;
                }
                return item;
            }).filter(item -> item != null).collect(Collectors.toList());
            int size = list.size();
            try {
                this.itemIAO.saveItems(list);
                multiFailure.addSuccess(size);
                Duration stop = timer.stop();
                logger.debug("es -> solr {}/{}, cost {}", multiFailure.getSuccess(), multiFailure.getTotal(),
                        stop.toString());
            } catch (Exception e) {
                multiFailure.addFail(size);
                String msg = "写入索引失败，itemIds：" + list.stream().map(item -> {
                    String itemId = item.getItemId();
                    retryService.addUnresolvedRetryNote(itemId, Step.index, Type.item, Action.save);
                    multiFailure.addFailure(new Failure(DataType.item, FailureType.save2Index, itemId, null, e));
                    return itemId;
                }).collect(Collectors.toSet());
                logger.error(msg, e);
            }
            try {
                scrollPage = this.itemEsIao.scroll(scrollPage.getScrollId(), scrollPage.getScrollIdTTL());
            } catch (IndexException e) {
                Failure failure = new Failure(DataType.item, FailureType.retrieveFromES, "", "scroll商品ES数据失败", e);
                multiFailure.addFailure(failure);
                return Optional.of(multiFailure);
            }
        }
        timer.stop();
        TimerStop lastStop = timer.lastStop();
        assert lastStop != null;
        logger.debug("es -> solr , coast {}",
                new Duration(timer.getStartTime().toDateTime(), lastStop.getStopTime().toDateTime()).toString());
        try {
            this.itemIAO.removeExpired(version);
        } catch (IndexException e) {
            String msg = "清除过期索引失败, version:" + version;
            logger.error("清除过期索引失败", e);
            multiFailure.addFailure(new Failure(DataType.item, FailureType.deleteFromIndex, version + "", msg, e));
        }
        if (logger.isInfoEnabled()) {
            logger.info("执行完成,{}", multiFailure.toString());
        }
        return multiFailure.toOptional();
    }

    @Override
    public Optional<Failure> indexItemsOfBrandFromEs(String brandId, Long version) {
        MultiFailure multiFailure = new MultiFailure();
        ScrollPage<ItemIndexPojo> scrollPage;
        try {
            scrollPage = this.itemEsIao.startScrollOfBrand(brandId);
        } catch (IndexException e) {
            Failure failure = new Failure(DataType.item, FailureType.retrieveFromES, "", "scroll商品ES数据失败", e);
            return Optional.of(failure);
        }
        multiFailure.setTotal(scrollPage.getTotal());
        while (true) {
            List<ItemIndexPojo> list = scrollPage.getList();
            if (list == null) {
                break;
            }
            list.forEach(item -> item.setOperationSid(version));
            int size = list.size();
            try {
                this.itemIAO.saveItems(list);
                multiFailure.addSuccess(size);
            } catch (IndexException e) {
                multiFailure.addFail(size);
                String msg = "写入索引失败，itemIds：" + list.stream().map(item -> {
                    String itemId = item.getItemId();
                    retryService.addUnresolvedRetryNote(itemId, Step.index, Type.item, Action.save);
                    multiFailure.addFailure(new Failure(DataType.item, FailureType.save2Index, itemId, null, e));
                    return itemId;
                }).collect(Collectors.toSet());
                logger.error(msg, e);
            }
            try {
                scrollPage = this.itemEsIao.scroll(scrollPage.getScrollId(), scrollPage.getScrollIdTTL());
            } catch (IndexException e) {
                Failure failure = new Failure(DataType.item, FailureType.retrieveFromES, "", "scroll商品ES数据失败", e);
                multiFailure.addFailure(failure);
                return Optional.of(multiFailure);
            }
        }
        try {
            this.itemIAO.removeExpiredOfBrand(brandId, version);
        } catch (IndexException e) {
            String msg = "删除品牌[brandId:" + brandId + ", version:" + version + "]过期专柜商品索引失败";
            logger.error("清除过期索引失败", e);
            multiFailure.addFailure(new Failure(DataType.item, FailureType.deleteFromIndex, brandId, msg, e));
        }
        if (logger.isInfoEnabled()) {
            logger.info("执行完成{}", multiFailure.toString());
        }
        return multiFailure.toOptional();
    }

    @Override
    public void removeItemsOfBrand(String brandId) throws IndexException {
        this.itemIAO.removeItemsOfBrand(brandId);
    }

    @Override
    public Optional<Failure> indexItemsOfCategoryFromEs(String categoryId, String channel, Long version) {
        MultiFailure multiFailure = new MultiFailure();
        ScrollPage<ItemIndexPojo> scrollPage;
        try {
            scrollPage = this.itemEsIao.startScrollOfCategory(categoryId, channel);
        } catch (IndexException e) {
            Failure failure = new Failure(DataType.item, FailureType.retrieveFromES, "", "scroll商品ES数据失败", e);
            return Optional.of(failure);
        }
        multiFailure.setTotal(scrollPage.getTotal());
        while (true) {
            List<ItemIndexPojo> list = scrollPage.getList();
            if (list == null) {
                break;
            }
            list.forEach(item -> item.setOperationSid(version));
            int size = list.size();
            try {
                this.itemIAO.saveItems(list);
                multiFailure.addSuccess(size);
            } catch (IndexException e) {
                multiFailure.addFail(size);
                String msg = "写入索引失败，itemIds:" + list.stream().map(item -> {
                    String itemId = item.getItemId();
                    retryService.addUnresolvedRetryNote(itemId, Step.index, Type.item, Action.save);
                    multiFailure.addFailure(new Failure(DataType.item, FailureType.save2Index, itemId, null, e));
                    return itemId;
                }).collect(Collectors.toSet());
                logger.error(msg, e);
            }
            try {
                scrollPage = this.itemEsIao.scroll(scrollPage.getScrollId(), scrollPage.getScrollIdTTL());
            } catch (IndexException e) {
                Failure failure = new Failure(DataType.item, FailureType.retrieveFromES, "", "scroll商品ES数据失败", e);
                multiFailure.addFailure(failure);
                return Optional.of(multiFailure);
            }
        }
        try {
            this.itemIAO.removeExpiredOfCategory(categoryId, channel, version);
        } catch (IndexException e) {
            String msg = "删除分类[categoryId:" + categoryId + "]过期专柜商品索引失败";
            logger.error(msg, e);
            multiFailure.addFailure(new Failure(DataType.item, FailureType.deleteFromIndex, categoryId, msg, e));
        }
        return multiFailure.toOptional();
    }

    @Override
    public Optional<Failure> indexItemsFromEs(Collection<String> itemIds, Long version) {
        MultiFailure multiFailure = new MultiFailure();
        multiFailure.setTotal(itemIds.size());
        Iterable<ItemIndexPojo> iterable;
        try {
            iterable = this.itemEsIao.multiGet(itemIds);
        } catch (Exception e) {
            multiFailure.setFail(itemIds.size());
            multiFailure.setThrowable(e);
            logger.error("从ES读取数据失败，itemIds:{}", itemIds, e);
            itemIds.forEach(itemId -> {
                retryService.addUnresolvedRetryNote(itemId, Step.index, Type.item, Action.save);
                multiFailure
                        .addFailure(new Failure(DataType.item, FailureType.retrieveFromES, itemIds.toString()));
            });
            return multiFailure.toOptional();
        }
        List<ItemIndexPojo> items = Lists.newArrayList(iterable);
        items.forEach(item -> item.setOperationSid(version));
        try {
            this.itemIAO.saveItems(items);
        } catch (IndexException e) {
            multiFailure.setFail(items.size());
            multiFailure.setThrowable(e);
            logger.error("商品数据写入索引失败,itemIds:{}", items.stream().map(item -> {
                String itemId = item.getItemId();
                retryService.addUnresolvedRetryNote(itemId, Step.index, Type.item, Action.save);
                multiFailure
                        .addFailure(new Failure(DataType.item, FailureType.retrieveFromES, itemIds.toString()));
                return itemId;
            }).collect(Collectors.toSet()), e);
        }
        return multiFailure.toOptional();
    }

    @Override
    public void removeItem(String itemId) throws IndexException {
        this.itemIAO.removeItem(itemId);
    }

    @Override
    public Optional<Failure> indexItemsOfSkuFromEs(String skuId, Long version) {
        List<ItemIndexPojo> items;
        items = this.itemEsIao.findBySkuId(skuId);
        items.forEach(item -> item.setOperationSid(version));
        MultiFailure multiFailure = new MultiFailure();
        multiFailure.setTotal(items.size());
        try {
            this.itemIAO.saveItems(items);
            multiFailure.addSuccess(items.size());
        } catch (IndexException e) {
            multiFailure.addFail(items.size());
            multiFailure.setThrowable(e);
            logger.error("Sku[skuId:" + skuId + "]下的专柜商品:{}写入索引失败", items.stream().map(item -> {
                String itemId = item.getItemId();
                retryService.addUnresolvedRetryNote(item.getItemId(), Step.index, Type.item, Action.save);
                multiFailure.addFailure(new Failure(DataType.item, FailureType.save2Index, itemId));
                return itemId;
            }).collect(Collectors.toSet()), e);
        }
        try {
            this.itemIAO.removeExpiredOfSku(skuId, version);
        } catch (IndexException e) {
            String msg = "删除SKU[skuId:" + skuId + "]过期专柜商品索引失败";
            logger.error(msg, e);
            multiFailure.addFailure(new Failure(DataType.item, FailureType.deleteFromIndex, skuId, msg, e));
        }
        return multiFailure.toOptional();
    }

    @Override
    public void removeItemsOfSku(String skuId) throws IndexException {
        this.itemIAO.removeItemsOfSku(skuId);
    }

    @Override
    public Optional<Failure> indexItemsOfSpuFromEs(String spuId, Long version) {
        List<ItemIndexPojo> items;
        items = this.itemEsIao.findBySpuId(spuId);
        items.forEach(item -> item.setOperationSid(version));
        MultiFailure multiFailure = new MultiFailure();
        int size = items.size();
        multiFailure.setTotal(size);
        try {
            this.itemIAO.saveItems(items);
            multiFailure.addSuccess(size);
        } catch (IndexException e) {
            multiFailure.addFail(items.size());
            multiFailure.setThrowable(e);
            logger.error("SPU[spuId:" + spuId + "]下的专柜商品:{}写入索引失败", items.stream().map(item -> {
                String itemId = item.getItemId();
                retryService.addUnresolvedRetryNote(item.getItemId(), Step.index, Type.item, Action.save);
                multiFailure.addFailure(new Failure(DataType.item, FailureType.save2Index, itemId));
                return itemId;
            }).collect(Collectors.toSet()), e);
        }
        try {
            this.itemIAO.removeExpiredOfSpu(spuId, version);
        } catch (IndexException e) {
            String msg = "删除SPU[spuId:" + spuId + "]过期专柜商品索引失败";
            logger.error(msg, e);
            multiFailure.addFailure(new Failure(DataType.item, FailureType.deleteFromIndex, spuId, msg, e));
        }
        return multiFailure.toOptional();
    }

    @Override
    public void removeItemsOfSpu(String spuId) throws IndexException {
        this.itemIAO.removeItemsOfSpu(spuId);
    }

    @Override
    public Optional<Failure> indexNewerFromEs(Long version) {
        MultiFailure multiFailure = new MultiFailure();
        ScrollPage<ItemIndexPojo> scrollPage;
        try {
            scrollPage = this.itemEsIao.startScrollOfOperationSidGreaterThanEqual(version);
        } catch (IndexException e) {
            Failure failure = new Failure(DataType.item, FailureType.retrieveFromES, "", "scroll商品ES数据失败", e);
            return Optional.of(failure);
        }
        multiFailure.setTotal(scrollPage.getTotal());
        while (true) {
            List<ItemIndexPojo> list = scrollPage.getList();
            if (list == null) {
                break;
            }
            list.forEach(item -> item.setOperationSid(version));
            int size = list.size();
            try {
                this.itemIAO.saveItems(list);
                multiFailure.addSuccess(size);
            } catch (Exception e) {
                multiFailure.addFail(size);
                String msg = "写入索引失败，itemIds：" + list.stream().map(item -> {
                    String itemId = item.getItemId();
                    retryService.addUnresolvedRetryNote(itemId, Step.index, Type.item, Action.save);
                    multiFailure.addFailure(new Failure(DataType.item, FailureType.save2Index, itemId, null, e));
                    return itemId;
                }).collect(Collectors.toSet());
                logger.error(msg, e);
            }
            try {
                scrollPage = this.itemEsIao.scroll(scrollPage.getScrollId(), scrollPage.getScrollIdTTL());
            } catch (IndexException e) {
                Failure failure = new Failure(DataType.item, FailureType.retrieveFromES, "", "scroll商品ES数据失败", e);
                multiFailure.addFailure(failure);
                return Optional.of(multiFailure);
            }
        }
        if (logger.isInfoEnabled()) {
            logger.info("执行完成, {}", multiFailure.toString());
        }
        return multiFailure.toOptional();
    }

    @Override
    public List<SuggestionIndexPojo> facetAllItemKeywords(String channel) throws QueryException {
        List<SuggestionIndexPojo> list = Lists.newArrayList();
        List<FacetField.Count> keywordCollectFFCs = this.itemIAO.facetField(channel, "keywordCollect");
        keywordCollectFFCs.forEach(ffc -> {
            String keyword = ffc.getName();
            long count = ffc.getCount();
            SuggestionIndexPojo pojo = new SuggestionIndexPojo();
            pojo.setCk(channel + "-" + keyword);
            pojo.setKeyword(keyword);
            pojo.setMatchCount(count);
            pojo.setChannel(channel);
            list.add(pojo);
        });
        return list;
    }

    @Override
    public List<String> facetChannels() throws QueryException {
        List<FacetField.Count> channelFFCs = this.itemIAO.facetChannels();
        return channelFFCs.stream().map(FacetField.Count::getName).collect(Collectors.toList());
    }

    @Override
    public void fillKeywordMatches(SuggestionIndexPojo historyRecord) throws QueryException {
        historyRecord
                .setMatchCount(this.itemIAO.queryMatchCount(historyRecord.getKeyword(), historyRecord.getChannel()));
    }
}
