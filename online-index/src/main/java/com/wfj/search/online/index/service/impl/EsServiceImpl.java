package com.wfj.search.online.index.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.wfj.platform.util.analysis.Timer;
import com.wfj.platform.util.concurrent.BatchRunnables;
import com.wfj.search.online.common.pojo.*;
import com.wfj.search.online.index.es.*;
import com.wfj.search.online.index.iao.IPcmRequester;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.iao.RequestException;
import com.wfj.search.online.index.pojo.*;
import com.wfj.search.online.index.pojo.failure.DataType;
import com.wfj.search.online.index.pojo.failure.Failure;
import com.wfj.search.online.index.pojo.failure.FailureType;
import com.wfj.search.online.index.pojo.failure.MultiFailure;
import com.wfj.search.online.index.service.*;
import com.wfj.search.online.index.util.ActiveActivityHolder;
import com.wfj.search.online.index.util.ExecutorServiceFactory;
import com.wfj.search.online.index.util.PojoUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.wfj.search.online.common.pojo.OnlineRetryNotePojo.*;

/**
 * <p>create at 15-12-3</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
@Service("esService")
public class EsServiceImpl implements IEsService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IPcmRequester pcmRequester;
    @Autowired
    private IRetryService retryService;
    @Autowired
    private IIndexConfigService indexConfigService;
    @Autowired
    private IIndexBlacklistService indexBlacklistService;
    @Autowired
    private BrandEsIao brandEsIao;
    @Autowired
    private CategoryEsIao categoryEsIao;
    @Autowired
    private ItemEsIao itemEsIao;
    @Autowired
    private ActivityEsIao activityEsIao;
    @Autowired
    private ColorEsIao colorEsIao;
    @Autowired
    private StandardEsIao standardEsIao;
    @Autowired
    private SkuEsIao skuEsIao;
    @Autowired
    private SpuEsIao spuEsIao;
    @Autowired
    private PropertyEsIao propertyEsIao;
    @Autowired
    private PropertyValueEsIao propertyValueEsIao;
    @Autowired
    private TagEsIao tagEsIao;
    @Autowired
    private ICacheEvictService cacheEvictService;
    @Autowired
    private SearchQueryRecordEsIao searchQueryRecordEsIao;
    @Autowired
    private ISaleService saleService;
    @Autowired
    private ItemSalesEsIao itemSalesEsIao;
    @Autowired
    private SkuSalesEsIao skuSalesEsIao;
    @Autowired
    private SpuSalesEsIao spuSalesEsIao;
    @Autowired
    private ActiveActivityHolder activeActivityHolder;
    @Autowired
    private IActivityService activityService;
    @Autowired
    private EsBulkIao esBulkIao;

    @Override
    public Optional<Failure> buildAll2Es(long version) {
        Timer opTimer = new Timer();
        opTimer.start();
        final MultiFailure multiFailure = new MultiFailure();
        int total;
        String step = "从PCM获取专柜商品总数";
        try {
            total = this.pcmRequester.countItems();
            multiFailure.setTotal(total);
            opTimer.stop(step);
        } catch (RequestException e) {
            String msg = step + "失败";
            logger.error("{}耗时{}", msg, opTimer.stop(step), e);
            Failure failure = new Failure(DataType.item, FailureType.requestError, null);
            failure.setMessage(msg);
            failure.setThrowable(e);
            multiFailure.addFailure(failure);
            return Optional.of(multiFailure);
        }
        step = "分页导入专柜商品信息";
        int fetchSize = this.indexConfigService.getFetchSize();
        int pageSize = (total + fetchSize - 1) / fetchSize;
        int threads = this.indexConfigService.getFetchThreads();
        final AtomicReference<Throwable> tracker = new AtomicReference<>();
        ExecutorService threadPool = ExecutorServiceFactory.create("rebuildES", threads,
                Thread.currentThread(), tracker);
        CompletionService<Void> completionService = new ExecutorCompletionService<>(threadPool);
        for (int i = 0; i < pageSize; i++) {
            final int start = i * fetchSize;
            completionService.submit(() -> multiFailure.merge(save2ESPages(start, fetchSize, version)), null);
        }
        try {
            for (int i = 0; i < pageSize; i++) {
                completionService.take();
            }
            opTimer.stop();
            logger.info("全量构建ES数据完成。" + multiFailure.toString() + " 耗时" +
                    Duration.between(opTimer.getStartTime(), opTimer.lastStop().getStopTime()).toString());
        } catch (InterruptedException e) {
            Throwable throwable = tracker.get();
            String msg = step + "失败";
            logger.error("{}耗时{}", msg, opTimer.stop(step), throwable);
            multiFailure.addFailure(new Failure(DataType.item, FailureType.save2ES, null, msg, throwable));
        }
        return Optional.of(multiFailure);
    }

    private MultiFailure save2ESPages(int start, int fetch, long version) {
        MultiFailure multiFailure = new MultiFailure();
        Page<ItemPojo> itemPojoPage;
        try {
            itemPojoPage = pcmRequester.listItems(start, fetch);
        } catch (RequestException e) {
            // 当前页按丢失计
            String msg = "取回分页商品列表失败,{start:" + start + ",fetchSize:" + fetch + "}";
            logger.error(msg, e);
            return multiFailure.addFailure(new Failure(DataType.item, FailureType.requestError,
                    "{start:" + start + ",fetch:" + fetch + "}", msg, e));
        }
        //multiFailure.setTotal(itemPojoPage.getTotalElements());
        IndexPojos indexPojos = new IndexPojos();
        long ok = 0;
        for (ItemPojo itemPojo : itemPojoPage.getContent()) {
            Optional<Failure> failureOptional;
            try {
                failureOptional = indexPojos
                        .assembleIndexPojos(pcmRequester, indexBlacklistService, itemPojo, activeActivityHolder);
                if (failureOptional.isPresent()) {
                    multiFailure.addFailure(failureOptional.get());
                    retryService.addUnresolvedRetryNote(itemPojo.getItemId(), Step.request, Type.item, Action.save);
                } else {
                    ok++;
                }
            } catch (Exception e) {
                logger.warn("组装商品过程发生异常", e);
                multiFailure.addFailure(new Failure(DataType.item, FailureType.buildData, itemPojo.getItemId(),
                        "组装商品过程发生异常", e));
                retryService.addUnresolvedRetryNote(itemPojo.getItemId(), Step.request, Type.item, Action.save);
            }
        }
        try {
            this.esBulkIao.bulkIndex(indexPojos, version).ifPresent(multiFailure::addFailure);
            multiFailure.addSuccess(ok);
        } catch (Exception e) {
            String msg = "保存分页商品数据到es失败, " + indexPojos.itemIndexPojos.values().stream().map(item -> {
                String itemId = item.getItemId();
                retryService.addUnresolvedRetryNote(itemId, Step.es, Type.item, Action.save);
                multiFailure.addFailure(new Failure(DataType.item, FailureType.save2ES, itemId));
                return itemId;
            }).collect(Collectors.toSet());
            logger.error(msg, e);
            multiFailure.addFail(multiFailure.getFailures().size());
        }
        return multiFailure;
    }

    @Override
    public Optional<Failure> updateBrand(BrandIndexPojo brand) {
        try {
            this.brandEsIao.upsert(brand);
        } catch (Exception e) {
            String msg = "保存品牌[brandId:" + brand.getBrandId() + "]信息到ES失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.brand, FailureType.save2ES, brand.getBrandId(), msg, e));
        }
        try {
            this.cacheEvictService.removeBrandCache(brand.getBrandId());
        } catch (Exception e) {
            logger.warn("清除品牌[{}]缓存失败", brand.getBrandId(), e);
        }
        try {
            this.itemEsIao.updateBrandNameBrandAliasedByBrandId(brand.getBrandId(), brand.getBrandName(),
                    brand.getBrandAliases());
        } catch (Exception e) {
            String msg = "更新ES中专柜商品的品牌[brandId:" + brand.getBrandId() + "]信息失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.brand, FailureType.updateES, brand.getBrandId(), msg, e));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Failure> rebuildBrandAndItems(BrandIndexPojo brand, long version) {
        String brandId = brand.getBrandId();
        this.pcmRequester.clearBrandInfoCache(brandId);
        try {
            this.brandEsIao.upsert(brand);
        } catch (Exception e) {
            String msg = "保存品牌[brandId:" + brandId + "]信息到ES失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.brand, FailureType.save2ES, brandId, msg, e));
        }
        this.cacheEvictService.removeBrandCache(brandId);
        return this.updateItemOfBrand(brandId, version);
    }

    @Override
    public Optional<Failure> rebuildBrandAndItems(String brandId, long version) {
        try {
            this.pcmRequester.clearBrandInfoCache(brandId);
            BrandPojo brandPojo = this.pcmRequester.getBrandInfo(brandId);
            return rebuildBrandAndItems(PojoUtils.toIndexPojo(brandPojo), version);
        } catch (RequestException e) {
            String msg = "获取品牌[brandId:" + brandId + "]信息失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.brand, FailureType.requestError, brandId, msg, e));
        }
    }

    @Override
    public Optional<Failure> updateCategoryWithoutItems(CategoryIndexPojo category) {
        String categoryId = category.getCategoryId();
        MultiFailure multiFailure = new MultiFailure();
        CategoryIndexPojo parent = category.getParent();
        if (parent != null) {
            this.updateCategoryWithoutItems(parent).ifPresent(multiFailure::merge);
        }
        category.setParent(null);
        this.pcmRequester.clearAllCategoryInfoCache();
        try {
            this.categoryEsIao.upsert(category);
        } catch (Exception e) {
            String msg = "保存分类[categoryId:" + categoryId + "]信息到ES失败";
            logger.error(msg, e);
            multiFailure.addFailure(new Failure(DataType.category, FailureType.save2ES, categoryId, msg, e));
        }
        category.setParent(parent);
        this.cacheEvictService.removeCategoryCache(categoryId);
        return multiFailure.toOptional();
    }

    @Override
    public Optional<Failure> updateCategoryWithoutItems(CategoryPojo category) {
        String categoryId = category.getCategoryId();
        CategoryIndexPojo categoryIndex;
        try {
            categoryIndex = PojoUtils.toIndexPojo(category, this.pcmRequester);
        } catch (RequestException e) {
            String msg = "获取分类[categoryId:" + categoryId + "]的父级分类信息失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.category, FailureType.requestError, categoryId, msg, e));
        }
        return this.updateCategoryWithoutItems(categoryIndex);
    }

    @Override
    public Optional<Failure> updateCategoryWithoutItems(String categoryId) {
        this.pcmRequester.clearAllCategoryInfoCache();
        CategoryPojo category;
        try {
            category = this.pcmRequester.getCategoryInfo(categoryId);
        } catch (RequestException e) {
            String msg = "获取分类[categoryId:" + categoryId + "]或其父级分类信息失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.category, FailureType.requestError, categoryId, msg, e));
        }
        if (!category.isLeafLevel()) {
            return Optional.of(new Failure(DataType.unknown, FailureType.initial, categoryId, "只接受叶子级分类", null));
        }
        return this.updateCategoryWithoutItems(category);
    }

    @Override
    public Optional<Failure> updateItemOfCategory(String categoryId, String channel, long version) {
        pcmRequester.clearAllCategoryInfoCache();
        pcmRequester.clearAllSpuInfoCache();
        MultiFailure multiFailure = new MultiFailure();
        ScrollPage<ItemIndexPojo> scrollPage;
        try {
            scrollPage = this.itemEsIao.startScrollOfCategoryBeforeVersion(categoryId, channel, version);
        } catch (Exception e) {
            Failure failure = new Failure(DataType.item, FailureType.retrieveFromES, "", "scroll商品ES数据失败", e);
            return Optional.of(failure);
        }
        final AtomicReference<Throwable> tracker = new AtomicReference<>();
        int fetchThreads = this.indexConfigService.getFetchThreads();
        ExecutorService pool = ExecutorServiceFactory.create("rebuildCategoryItemsEs", fetchThreads,
                Thread.currentThread(), tracker);
        BatchRunnables batchRunnables = new BatchRunnables(pool);
        while (true) {
            List<ItemIndexPojo> itemIndexPojos = scrollPage.getList();
            if (itemIndexPojos == null) {
                break;
            }
            if (itemIndexPojos.isEmpty()) {
                continue;
            }
            batchRunnables.addRunnable(() -> {
                List<ItemPojo> itemPojos = itemIndexPojos.stream().map(index -> {
                    ItemPojo item = new ItemPojo();
                    item.setChannels(index.getChannels());
                    item.setItemId(index.getItemId());
                    item.setCurrentPrice(new BigDecimal(index.getCurrentPrice()));
                    item.setDiscountRate(index.getDiscountRate());
                    item.setInventory(index.getInventory());
                    item.setOriginalPrice(new BigDecimal(index.getOriginalPrice()));
                    item.setSkuId(index.getSkuId());
                    item.setStockMode(index.getStockMode());
                    item.setSupplierId(index.getSupplierId());
                    for (String activeId : index.getActiveId()) {
                        item.getActivities().add(this.activityEsIao.get(activeId));
                    }
                    return item;
                }).collect(Collectors.toList());
                IndexPojos indexPojos = new IndexPojos();
                for (ItemPojo item : itemPojos) {
                    // 组装的SupplySup信息需要在item写入索引后重新调整
                    indexPojos.assembleIndexPojos(pcmRequester, indexBlacklistService, item, activeActivityHolder)
                            .ifPresent(failure -> {
                                multiFailure.addFailure(failure);
                                retryService
                                        .addUnresolvedRetryNote(item.getItemId(), Step.request, Type.item, Action.save);
                            });
                }
                try {
                    this.esBulkIao.bulkIndex(indexPojos, version).ifPresent(multiFailure::addFailure);
                } catch (Exception e) {
                    String msg = "保存分页商品数据到ES失败";
                    logger.error(msg, e);
                    indexPojos.itemIndexPojos.values().forEach(item -> {
                        multiFailure.addFailure(
                                new Failure(DataType.item, FailureType.buildData, item.getItemId(), msg, e));
                        retryService.addUnresolvedRetryNote(item.getItemId(), Step.es, Type.item, Action.save);
                    });
                }
            });
            try {
                scrollPage = this.itemEsIao.scroll(scrollPage.getScrollId(), scrollPage.getScrollIdTTL());
            } catch (IndexException e) {
                Failure failure = new Failure(DataType.item, FailureType.retrieveFromES, "", "scroll商品ES数据失败", e);
                multiFailure.addFailure(failure);
                return Optional.of(multiFailure);
            }
        }
        try {
            batchRunnables.execute();
        } catch (InterruptedException e) {
            String msg = "重建分类[categoryId:" + categoryId + "]下商品ES数据意外中断";
            Throwable throwable = tracker.get();
            logger.error(msg, throwable);
            multiFailure
                    .addFailure(new Failure(DataType.item, FailureType.save2ES, categoryId, msg, throwable));
        }
        return multiFailure.toOptional();
    }

    @Override
    public Optional<Failure> rebuildItemOfCategory(String categoryId, String channel, long version) {
        this.pcmRequester.clearAllCategoryInfoCache();
        int spuCount;
        try {
            spuCount = this.pcmRequester.totalSpuOfCategoryId(categoryId);
        } catch (RequestException e) {
            String msg = "获取分类[categoryId:" + categoryId + "]下SPU列表失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.spu, FailureType.requestError, categoryId, msg, e));
        }
        int fetch = this.indexConfigService.getFetchSize();
        int spuPages = (spuCount + fetch - 1) / fetch;
        final AtomicReference<Throwable> tracker = new AtomicReference<>();
        int fetchThreads = this.indexConfigService.getFetchThreads();
        ExecutorService pool = ExecutorServiceFactory.create("rebuildCategoryItems", fetchThreads,
                Thread.currentThread(), tracker);
        BatchRunnables batchRunnables = new BatchRunnables(pool);
        MultiFailure multiFailure = new MultiFailure();
        for (int i = 0; i < spuPages; i++) {
            final int finalI = i;
            batchRunnables
                    .addRunnable(() -> this.updateItemsOfCategory(categoryId, finalI * fetch, fetch, version)
                            .ifPresent(multiFailure::merge));
        }
        try {
            batchRunnables.execute();
        } catch (InterruptedException e) {
            String msg = "重建分类[categoryId:" + categoryId + "]下商品ES数据意外中断";
            logger.error(msg, tracker.get());
            multiFailure.addFailure(
                    new Failure(DataType.category, FailureType.save2ES, categoryId, msg, tracker.get()));
        }
        try {
            this.itemEsIao.removeExpiredOfCategory(version, categoryId, channel);
        } catch (Exception e) {
            String msg = "删除分类[categoryId:" + categoryId + "]下过期商品ES数据出错";
            logger.error(msg, e);
            multiFailure.addFailure(new Failure(DataType.item, FailureType.deleteFromES, version + "", msg, e));
        }
        return multiFailure.toOptional();
    }

    @Override
    public Optional<Failure> buildItem(ItemPojo itemPojo, long version) {
        String itemId = itemPojo.getItemId();
        IndexPojos indexPojos = new IndexPojos();
        Optional<Failure> fo = indexPojos
                .assembleIndexPojos(pcmRequester, indexBlacklistService, itemPojo, activeActivityHolder);
        if (fo.isPresent()) {
            return Optional.of(new Failure(DataType.item, FailureType.buildData, itemId, fo.get().toString(), null));
        } else {
            try {
                return this.esBulkIao.bulkIndex(indexPojos, version);
            } catch (Exception e) {
                String msg = "商品数据[itemId:" + itemId + "]写入ES失败";
                logger.error(msg, e);
                retryService.addUnresolvedRetryNote(itemId, Step.es, Type.item, Action.save);
                return Optional.of(new Failure(DataType.item, FailureType.save2ES, itemId, msg, e));
            }
        }
    }

    @Override
    public Optional<Failure> buildItem(String itemId, long version) {
        try {
            List<ItemPojo> itemPojos = this.pcmRequester.listItems(Lists.newArrayList(itemId));
            if (itemPojos.isEmpty()) {
                return Optional.of(new Failure(DataType.item, FailureType.initial, itemId));
            }
            return buildItem(itemPojos.get(0), version);
        } catch (RequestException e) {
            String msg = "根据专柜商品id[itemId:" + itemId + "]查询专柜商品信息失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.item, FailureType.requestError, itemId, msg, e));
        }
    }

    @Override
    public Optional<Failure> removeItem(String itemId) {
        try {
            this.itemEsIao.delete(itemId);
        } catch (Exception e) {
            String msg = "从ES删除专柜商品[iteId:" + itemId + "]失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.item, FailureType.deleteFromES, itemId, msg, e));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Failure> updateSku(SkuPojo sku) {
        String skuId = sku.getSkuId();
        SkuIndexPojo skuIndexPojo = PojoUtils.toIndexPojo(sku);
        ColorIndexPojo colorIndexPojo;
        StandardIndexPojo standardIndexPojo;
        try {
            this.skuEsIao.upsert(skuIndexPojo);
            colorIndexPojo = new ColorIndexPojo();
            colorIndexPojo.setColorId(skuIndexPojo.getColorId());
            colorIndexPojo.setColorName(skuIndexPojo.getColorName());
            this.colorEsIao.upsert(colorIndexPojo);
            standardIndexPojo = PojoUtils.toIndexPojo(sku.getStandardPojo());
            this.standardEsIao.upsert(standardIndexPojo);
        } catch (Exception e) {
            String msg = "SKU[skuId:" + skuId + "]写入ES失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.sku, FailureType.save2ES, skuId, msg, e));
        }
        String masterPicMatchSize = null;
        String masterPicMatchFormat = null;
        Map<String, String> colorMasterPictureOfPix = Maps.newHashMap();
        for (PicturePojo pic : sku.getPictures()) {
            if (pic.isColorMaster()) {
                if ("1000x1000".equals(pic.getSize())) {
                    masterPicMatchSize = pic.getPicture();
                }
                if (StringUtils.isBlank(masterPicMatchSize) && !pic.getPicture()
                        .matches(".+\\.\\d+[xX]\\d+\\..+")) {
                    masterPicMatchFormat = pic.getPicture();
                }
                colorMasterPictureOfPix
                        .put("colorMasterPictureOfPix_" + pic.getSize(), pic.getPicture());
            }
        }
        try {
            this.itemEsIao.updateSkuInfos(skuIndexPojo, colorIndexPojo, standardIndexPojo, StringUtils.isNoneBlank(
                    masterPicMatchSize) ? masterPicMatchSize : masterPicMatchFormat, colorMasterPictureOfPix);
        } catch (Exception e) {
            String msg = "更新SKU[skuId:" + skuId + "]下商品ES数据失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.sku, FailureType.updateES, skuId, msg, e));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Failure> rebuildSkuAndItems(SkuPojo sku, long version) {
        String skuId = sku.getSkuId();
        SkuIndexPojo skuIndexPojo = PojoUtils.toIndexPojo(sku);
        try {
            this.skuEsIao.upsert(skuIndexPojo);
            ColorIndexPojo colorIndexPojo = new ColorIndexPojo();
            colorIndexPojo.setColorId(skuIndexPojo.getColorId());
            colorIndexPojo.setColorName(skuIndexPojo.getColorName());
            this.colorEsIao.upsert(colorIndexPojo);
            this.standardEsIao.upsert(PojoUtils.toIndexPojo(sku.getStandardPojo()));
        } catch (Exception e) {
            String msg = "SKU[skuId:" + skuId + "]写入ES失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.sku, FailureType.save2ES, skuId, msg, e));
        }
        MultiFailure multiFailure = new MultiFailure();
        try {
            List<String> itemIds = this.pcmRequester.listItemIdBySkuId(sku.getSkuId());
            List<ItemPojo> itemPojos = this.pcmRequester.listItems(itemIds);
            itemPojos.forEach(item -> this.buildItem(item, version).ifPresent(multiFailure::merge));
        } catch (Exception e) {
            String msg = "重建SKU[skuId:" + skuId + "]下商品ES数据失败";
            logger.error(msg, e);
            multiFailure.addFailure(new Failure(DataType.sku, FailureType.save2ES, skuId, msg, e));
        }
        try {
            this.itemEsIao.removeExpiredOfSku(version, sku.getSkuId());
        } catch (Exception e) {
            String msg = "删除过期SKU[skuId:" + skuId + "]下商品ES数据失败";
            logger.error(msg, e);
            multiFailure.addFailure(new Failure(DataType.item, FailureType.deleteFromES, version + "", msg, e));
        }
        return multiFailure.toOptional();
    }

    @Override
    public Optional<Failure> rebuildSkuAndItems(String skuId, long version) {
        try {
            this.pcmRequester.clearSkuInfoCache(skuId);
            SkuPojo skuPojo = this.pcmRequester.getSkuInfo(skuId);
            return this.rebuildSkuAndItems(skuPojo, version);
        } catch (RequestException e) {
            String msg = "获取SKU[skuId:" + skuId + "]信息失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.sku, FailureType.requestError, skuId, msg, e));
        }
    }

    @Override
    public Optional<Failure> updateSpu(SpuPojo spu) {
        SpuIndexPojo spuIndexPojo = PojoUtils.toIndexPojo(spu);
        List<PropertyValueIndexPojo> propertyValueIndexPojos;
        List<TagIndexPojo> tagIndexPojos;
        BrandIndexPojo brand;
        Map<String, CategoryIndexPojo> categoryIndexPojoMap = Maps.newHashMap();
        String brandId = spuIndexPojo.getBrandId();
        String spuId = spu.getSpuId();
        this.pcmRequester.clearSpuInfoCache(spuId);
        try {
            this.spuEsIao.upsert(spuIndexPojo);
            for (PropertyIndexPojo propertyIndexPojo : spuIndexPojo.getPropertyIndexPojos()) {
                try {
                    this.propertyEsIao.upsert(propertyIndexPojo);
                } catch (Exception e) {
                    String msg = "SPU[spuId:" + spuId + "]属性[" + propertyIndexPojo.getPropertyId() + "]写入ES失败";
                    logger.error(msg, e);
                    return Optional.of(new Failure(DataType.spu, FailureType.save2ES, spuId, msg, e));
                }
            }
            List<PropertyValueIndexPojo> propertyValueIndexPojos_ = spuIndexPojo.getPropertyValueIndexPojos();
            propertyValueIndexPojos = Lists.newArrayListWithExpectedSize(propertyValueIndexPojos_.size());
            for (PropertyValueIndexPojo propertyValueIndexPojo : propertyValueIndexPojos_) {
                try {
                    this.propertyValueEsIao.upsert(propertyValueIndexPojo);
                    propertyValueIndexPojos.add(propertyValueIndexPojo);
                } catch (Exception e) {
                    String msg = "SPU[spuId:" + spuId + "]属性值[" + propertyValueIndexPojo
                            .getPropertyValueId() + "]写入ES失败";
                    logger.error(msg, e);
                    return Optional.of(new Failure(DataType.spu, FailureType.save2ES, spuId, msg, e));
                }
            }
            tagIndexPojos = spuIndexPojo.getTagIndexPojos();
            tagIndexPojos.forEach(this.tagEsIao::upsert);
            brand = this.brandEsIao.get(brandId);
            if (brand == null) {
                BrandPojo brandPojo;
                try {
                    brandPojo = this.pcmRequester.getBrandInfo(brandId);
                } catch (RequestException e) {
                    String msg = "SPU[spuId:" + spuId + "]获取品牌[brandId:" + brandId + "]信息失败";
                    logger.error(msg, e);
                    return Optional.of(new Failure(DataType.brand, FailureType.requestError, brandId, msg, e));
                }
                brand = PojoUtils.toIndexPojo(brandPojo);
                try {
                    this.brandEsIao.upsert(brand);
                } catch (Exception e) {
                    String msg = "SPU[spuId:" + spuId + "]的品牌[brandId:" + brandId + "]信息写入ES失败";
                    logger.error(msg, e);
                    return Optional.of(new Failure(DataType.brand, FailureType.save2ES, brandId, msg, e));
                }
            }
            Set<String> categoryIds = Sets.newHashSet(spuIndexPojo.getCategoryIds());
            Iterable<CategoryIndexPojo> savedCategories;
            savedCategories = this.categoryEsIao.multiGet(categoryIds);
            for (CategoryIndexPojo savedCategory : savedCategories) {
                categoryIndexPojoMap.put(savedCategory.getCategoryId(), savedCategory);
                String parentCid = savedCategory.getParentCategoryId();
                while (parentCid != null && !"".equals(parentCid.trim()) && !"0".equals(parentCid.trim())) {
                    CategoryIndexPojo parent;
                    parent = this.categoryEsIao.get(parentCid);
                    if (parent == null) {
                        categoryIds.add(parentCid);
                        parentCid = null;
                    } else {
                        categoryIndexPojoMap.put(parentCid, parent);
                        parentCid = parent.getParentCategoryId();
                    }
                }
            }
            categoryIds.removeAll(categoryIndexPojoMap.keySet());
            if (!categoryIds.isEmpty()) {
                Map<String, CategoryIndexPojo> newCategoryIndexPojoMap = Maps.newHashMap();
                for (String categoryId : categoryIds) {
                    CategoryIndexPojo categoryIndexPojo;
                    try {
                        CategoryPojo categoryPojo = this.pcmRequester.getCategoryInfo(categoryId);
                        categoryIndexPojo = PojoUtils.toIndexPojo(categoryPojo, this.pcmRequester);
                        String parentCategoryId = categoryIndexPojo.getParentCategoryId();
                        if (StringUtils.isBlank(parentCategoryId) || "0".equals(parentCategoryId.trim())) {
                            categoryIndexPojo = null;
                        }
                    } catch (RequestException e) {
                        String msg = "SPU[spuId:" + spuId + "]获取分类[categoryId:" + categoryId
                                + "]或其父级分类信息失败";
                        logger.error(msg, e);
                        return Optional
                                .of(new Failure(DataType.category, FailureType.requestError, categoryId, msg, e));
                    }
                    while (categoryIndexPojo != null) {
                        String parentCategoryId = categoryIndexPojo.getParentCategoryId();
                        if (StringUtils.isNotBlank(parentCategoryId) && !"0".equals(parentCategoryId.trim())) {
                            newCategoryIndexPojoMap.put(categoryIndexPojo.getCategoryId(), categoryIndexPojo);
                            categoryIndexPojoMap.put(categoryIndexPojo.getCategoryId(), categoryIndexPojo);
                        }
                        categoryIndexPojo = categoryIndexPojo.getParent();
                    }
                }
                for (CategoryIndexPojo categoryIndexPojo : newCategoryIndexPojoMap.values()) {
                    CategoryIndexPojo parent = categoryIndexPojo.getParent();
                    categoryIndexPojo.setParent(null);
                    this.categoryEsIao.upsert(categoryIndexPojo);
                    categoryIndexPojo.setParent(parent);
                }
            }
        } catch (Exception e) {
            String msg = "SPU[spuId:" + spuId + "]写入ES失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.spu, FailureType.save2ES, spuId, msg, e));
        }
        Map<String, List<String>> allLevelCategoryIds = Maps.newHashMap();
        Map<String, List<String>> allLevelCategoryNames = Maps.newHashMap();
        Map<String, List<String>> categoryIdUnderCategory = Maps.newHashMap();
        for (CategoryIndexPojo categoryIndexPojo : categoryIndexPojoMap.values()) {
            String channel = categoryIndexPojo.getChannel();
            List<String> channelAllLevelCategoryIds = allLevelCategoryIds.get("allLevelCategoryIds_" + channel);
            if (channelAllLevelCategoryIds == null) {
                channelAllLevelCategoryIds = Lists.newArrayList();
                allLevelCategoryIds.put("allLevelCategoryIds_" + channel, channelAllLevelCategoryIds);
            }
            channelAllLevelCategoryIds.add(categoryIndexPojo.getCategoryId());
            List<String> channelAllLevelCategoryNames = allLevelCategoryNames.get("allLevelCategoryNames_" + channel);
            if (channelAllLevelCategoryNames == null) {
                channelAllLevelCategoryNames = Lists.newArrayList();
                allLevelCategoryNames.put("allLevelCategoryNames_" + channel, channelAllLevelCategoryNames);
            }
            channelAllLevelCategoryNames.add(categoryIndexPojo.getCategoryName());
            String parentCid = categoryIndexPojo.getParentCategoryId();
            if (StringUtils.isNotBlank(parentCid) && !"0".equals(parentCid)) {
                List<String> subCategoryIds = categoryIdUnderCategory.get("categoryIdUnderCategory_" + parentCid);
                if (subCategoryIds == null) {
                    subCategoryIds = Lists.newArrayList();
                    categoryIdUnderCategory.put("categoryIdUnderCategory_" + parentCid, subCategoryIds);
                }
                subCategoryIds.add(categoryIndexPojo.getCategoryId());
            }
        }
        Map<String, List<String>> propertyValues = Maps.newHashMap();
        Map<String, List<String>> propertyIds = Maps.newHashMap();
        Map<String, String> propertyValueIdOfPropertyId = Maps.newHashMap();
        for (PropertyValueIndexPojo propertyValueIndexPojo : propertyValueIndexPojos) {
            String propertyId = propertyValueIndexPojo.getPropertyId();
            propertyValueIdOfPropertyId
                    .put("propertyValueIdOfPropertyId_" + propertyId, propertyValueIndexPojo.getPropertyValueId());
            String channel = propertyValueIndexPojo.getChannel();
            List<String> channelPropertyValues = propertyValues.get("propertyValues_" + channel);
            if (channelPropertyValues == null) {
                channelPropertyValues = Lists.newArrayList();
                propertyValues.put("propertyValues_" + channel, channelPropertyValues);
            }
            channelPropertyValues.add(propertyValueIndexPojo.getPropertyValue());
            List<String> channelPropertyIds = propertyIds.get("propertyIds_" + channel);
            if (channelPropertyIds == null) {
                channelPropertyIds = Lists.newArrayList();
                propertyIds.put("propertyIds_" + channel, channelPropertyIds);
            }
            channelPropertyIds.add(propertyId);
        }
        try {
            this.itemEsIao.updateSpuInfos(spuIndexPojo, brand, allLevelCategoryIds, allLevelCategoryNames,
                    categoryIdUnderCategory, tagIndexPojos, propertyValues, propertyIds, propertyValueIdOfPropertyId);
        } catch (Exception e) {
            String msg = "更新SPU[spuId:" + spuId + "]下商品ES数据失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.spu, FailureType.updateES, spuId, msg, e));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Failure> rebuildSpuAndItems(String spuId, long version) {
        return this.updateItemOfSpu(spuId, version);
    }

    @Override
    public Optional<Failure> updateSalesFromMySql(SoldPojo sold) {
        MultiFailure multiFailure = new MultiFailure();
        updateItemSalesFromMySql(sold.getItemId()).ifPresent(multiFailure::merge);
        updateSkuSalesFromMySql(sold.getSkuId()).ifPresent(multiFailure::merge);
        updateSpuSalesFromMySql(sold.getSpuId()).ifPresent(multiFailure::merge);
        return multiFailure.toOptional();
    }

    @Override
    public Optional<Failure> fetchItemPriceChanges(Date after, Date before, long version) {
        Integer changesCount;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String intervalString = dateFormat.format(after) + " - " + dateFormat.format(before);
        try {
            changesCount = this.pcmRequester.countItemPriceChanges(after, before);
        } catch (RequestException e) {
            String msg = "获取" + intervalString + "商品变价总数失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.spu, FailureType.requestError, null, msg, e));
        }
        int fetch = this.indexConfigService.getFetchSize();
        int pages = (changesCount + fetch - 1) / fetch;
        final AtomicReference<Throwable> tracker = new AtomicReference<>();
        int fetchThreads = this.indexConfigService.getFetchThreads();
        ExecutorService pool = ExecutorServiceFactory.create("fetchItemPriceChanges", fetchThreads,
                Thread.currentThread(), tracker);
        BatchRunnables batchRunnables = new BatchRunnables(pool);
        MultiFailure multiFailure = new MultiFailure();
        for (int i = 0; i < pages; i++) {
            final int finalI = i;
            batchRunnables
                    .addRunnable(() -> this.updateItemPrices(after, before, finalI * fetch, fetch, version)
                            .ifPresent(multiFailure::merge));
        }
        try {
            batchRunnables.execute();
        } catch (InterruptedException e) {
            String msg = "抓取" + intervalString + "商品变价数据意外中断";
            logger.error(msg, tracker.get());
            multiFailure.addFailure(
                    new Failure(DataType.price, FailureType.save2ES, null, msg, tracker.get()));
        }
        return multiFailure.toOptional();
    }

    @Override
    public Optional<Failure> updateActivities(Date after, Date before, long version) {
        Map<String, ActivityPojo> activeActivities;
        try {
            activeActivities = this.activityService.freshActiveActivities(before);
        } catch (RequestException e) {
            String msg = "获取活动状态活动信息失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.item, FailureType.requestError, null, msg, e));
        }
        List<String> closedActivityIds;
        try {
            closedActivityIds = this.activityService.listClosedActivityIds(after, before);
        } catch (RequestException e) {
            String msg = "增量获取结束的活动信息失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.item, FailureType.requestError, null, msg, e));
        }
        List<String> freshActivityIds = Lists.newArrayList();
        freshActivityIds.addAll(activeActivities.keySet());
        freshActivityIds.addAll(closedActivityIds);
        MultiFailure multiFailure = new MultiFailure();
        for (String freshActivityId : freshActivityIds) {
            int count;
            try {
                count = this.pcmRequester.countActivityItems(freshActivityId);
            } catch (RequestException e) {
                String msg = "抓取活动[activityId:" + freshActivityId + "]下专柜商品数据失败";
                logger.error(msg, e);
                multiFailure.addFailure(
                        new Failure(DataType.item, FailureType.requestError, null, msg, e));
                continue;
            }
            int fetch = this.indexConfigService.getFetchSize();
            int pages = (count + fetch - 1) / fetch;
            int fetchThreads = this.indexConfigService.getFetchThreads();
            final AtomicReference<Throwable> tracker = new AtomicReference<>();
            ExecutorService pool = ExecutorServiceFactory.create("updateActivityItems", fetchThreads,
                    Thread.currentThread(), tracker);
            BatchRunnables batchRunnables = new BatchRunnables(pool);
            for (int i = 0; i < pages; i++) {
                final int finalI = i;
                batchRunnables
                        .addRunnable(() -> this.updateActivityItems(freshActivityId, finalI * fetch, fetch, version)
                                .ifPresent(multiFailure::merge));
            }
            try {
                batchRunnables.execute();
            } catch (InterruptedException e) {
                String msg = "更新活动[activityId:" + freshActivityId + "]商品数据意外中断";
                logger.error(msg, tracker.get());
                multiFailure.addFailure(
                        new Failure(DataType.item, FailureType.save2ES, null, msg, tracker.get()));
            }
        }
        return multiFailure.toOptional();
    }

    @Override
    public List<SuggestionIndexPojo> aggregateSearchQueries(String channel) {
        return this.searchQueryRecordEsIao.aggregateSearchQueries(channel);
    }

    private Optional<Failure> updateActivityItems(String activityId, int start, int fetch, long version) {
        MultiFailure multiFailure = new MultiFailure();
        List<ItemPojo> itemPojos;
        try {
            itemPojos = this.pcmRequester.listActivityItems(activityId, start, fetch);
        } catch (RequestException e) {
            String msg = "抓取活动[activityId:" + activityId + "]下专柜商品数据失败";
            logger.error(msg, e);
            return Optional.of(
                    new Failure(DataType.item, FailureType.requestError, null, msg, e));
        }
        itemPojos.forEach(itemPojo -> this.buildItem(itemPojo, version).ifPresent(multiFailure::merge));
        return multiFailure.toOptional();
    }

    private Optional<Failure> updateItemPrices(Date after, Date before, int start, int fetch, long version) {
        List<ItemPojo> itemPrices;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String intervalString = dateFormat.format(after) + " - " + dateFormat.format(before);
        try {
            itemPrices = this.pcmRequester.listItemPriceChanges(after, before, start, fetch);
        } catch (RequestException e) {
            String msg = "获取分页" + intervalString + "[start:" + start + ",fetch:" + fetch + "]商品变价信息失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.price, FailureType.requestError, null, msg, e));
        }
        MultiFailure multiFailure = new MultiFailure();
        itemPrices.forEach(price -> this.updateItemPrice(price, version).ifPresent(multiFailure::merge));
        return multiFailure.toOptional();
    }

    private Optional<Failure> updateItemPrice(ItemPojo price, long version) {
        if (price == null) {
            return Optional.empty();
        }
        try {
            this.itemEsIao.updateItemPrice(price.getItemId(), price.getCurrentPrice().doubleValue(), version);
        } catch (Exception e) {
            String msg = "更新ES中商品[itemId:" + price.getItemId() + "]价格[price:" + price.getCurrentPrice() + "]失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.price, FailureType.save2ES, price.getItemId(), msg, e));
        }
        return Optional.empty();
    }

    private Optional<Failure> updateSpuSalesFromMySql(String spuId) {
        Integer spuSales;
        try {
            spuSales = this.saleService.countSpuSales(spuId);
        } catch (Exception e) {
            String msg = "从MySql中获取SPU[spuId:" + spuId + "]销量失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.spu, FailureType.retrieveFromDB, spuId, msg, e));
        }
        try {
            if (spuSales == null) {
                spuSales = 0;
            }
            SpuSalesPojo spuSalesPojo = new SpuSalesPojo();
            spuSalesPojo.setSpuId(spuId);
            spuSalesPojo.setSales(spuSales);
            this.spuSalesEsIao.upsert(spuSalesPojo);
        } catch (Exception e) {
            String msg = "向ES中写入SPU[spuId:" + spuId + "]销量失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.spu, FailureType.save2ES, spuId, msg, e));
        }
        try {
            this.itemEsIao.updateSpuSales(spuId, spuSales);
        } catch (Exception e) {
            String msg = "修改ES中商品SPU[spuId:" + spuId + "]销量失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.spu, FailureType.save2ES, spuId, msg, e));
        }
        return Optional.empty();
    }

    private Optional<Failure> updateSkuSalesFromMySql(String skuId) {
        Integer skuSales;
        try {
            skuSales = this.saleService.countSkuSales(skuId);
        } catch (Exception e) {
            String msg = "从MySql中获取SKU[skuId:" + skuId + "]销量失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.sku, FailureType.retrieveFromDB, skuId, msg, e));
        }
        try {
            if (skuSales == null) {
                skuSales = 0;
            }
            SkuSalesPojo skuSalesPojo = new SkuSalesPojo();
            skuSalesPojo.setSkuId(skuId);
            skuSalesPojo.setSales(skuSales);
            this.skuSalesEsIao.upsert(skuSalesPojo);
        } catch (Exception e) {
            String msg = "向ES中写入SKU[skuId:" + skuId + "]销量失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.sku, FailureType.save2ES, skuId, msg, e));
        }
        try {
            this.itemEsIao.updateSkuSales(skuId, skuSales);
        } catch (Exception e) {
            String msg = "修改ES中商品SKU[skuId:" + skuId + "]销量失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.sku, FailureType.save2ES, skuId, msg, e));
        }
        return Optional.empty();
    }

    private Optional<Failure> updateItemSalesFromMySql(String itemId) {
        Integer itemSales;
        try {
            itemSales = this.saleService.countItemSales(itemId);
            if (itemSales == null) {
                itemSales = 0;
            }
        } catch (Exception e) {
            String msg = "从MySql中获取商品[itemId:" + itemId + "]销量失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.sale, FailureType.retrieveFromDB, itemId, msg, e));
        }
        try {
            ItemSalesPojo itemSalesPojo = new ItemSalesPojo();
            itemSalesPojo.setItemId(itemId);
            itemSalesPojo.setSales(itemSales);
            this.itemSalesEsIao.upsert(itemSalesPojo);
        } catch (Exception e) {
            String msg = "向ES中写入商品[itemId:" + itemId + "]销量失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.sale, FailureType.save2ES, itemId, msg, e));
        }
        try {
            this.itemEsIao.updateItemSales(itemId, itemSales);
        } catch (Exception e) {
            String msg = "修改ES中商品[itemId:" + itemId + "]销量失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.item, FailureType.updateES, itemId, msg, e));
        }
        return Optional.empty();
    }

    private Optional<Failure> updateItemsOfCategory(String categoryId, int start, int fetch, long version) {
        List<String> spuIds;
        try {
            spuIds = this.pcmRequester.listSpuIdByCategoryId(categoryId, start, fetch);
        } catch (RequestException e) {
            String msg = "从PCM查询品类[categoryId:" + categoryId + "]下的SPU id失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.spu, FailureType.requestError, categoryId, msg, e));
        }
        MultiFailure multiFailure = new MultiFailure();
        spuIds.forEach(spuId -> this.updateItemOfSpu(spuId, version).ifPresent(multiFailure::merge));
        return multiFailure.toOptional();
    }

    private Optional<Failure> updateItemOfBrand(String brandId, long version) {
        int spuCount;
        try {
            spuCount = this.pcmRequester.totalSpuOfBrandId(brandId);
        } catch (RequestException e) {
            String msg = "获取品牌[brandId:" + brandId + "]下SPU列表失败";
            return Optional.of(new Failure(DataType.brand, FailureType.requestError, brandId, msg, e));
        }
        int fetch = this.indexConfigService.getFetchSize();
        int pageSize = (spuCount + fetch - 1) / fetch;
        final AtomicReference<Throwable> tracker = new AtomicReference<>();
        int fetchThreads = this.indexConfigService.getFetchThreads();
        ExecutorService pool = ExecutorServiceFactory.create("rebuildBrandItems", fetchThreads, Thread.currentThread(),
                tracker);
        BatchRunnables batchRunnables = new BatchRunnables(pool);
        final MultiFailure multiFailure = new MultiFailure();
        for (int i = 0; i < pageSize; i++) {
            final int start = i * fetch;
            batchRunnables.addRunnable(() -> {
                List<String> spuIds;
                try {
                    spuIds = this.pcmRequester.listSpuIdByBrandId(brandId, start, fetch);
                } catch (RequestException e) {
                    String msg = "从PCM查询品牌[brandId:" + brandId + "]下的SPU id失败";
                    logger.error(msg, e);
                    multiFailure.addFailure(new Failure(DataType.brand, FailureType.requestError, brandId, msg, e));
                    return;
                }
                spuIds.forEach(spuId -> this.updateItemOfSpu(spuId, version).ifPresent(multiFailure::merge));
            });
        }
        try {
            batchRunnables.execute();
        } catch (InterruptedException e) {
            String msg = "重建品牌[brandId:" + brandId + "]下商品ES数据意外中断";
            logger.error(msg, tracker.get());
            multiFailure.addFailure(new Failure(DataType.brand, FailureType.save2ES, brandId, msg, tracker.get()));
        }
        try {
            this.itemEsIao.removeExpiredOfBrand(version, brandId);
        } catch (Exception e) {
            String msg = "删除品牌[brandId:" + brandId + "]下过期商品ES数据出错";
            logger.error(msg, e);
            multiFailure.addFailure(new Failure(DataType.item, FailureType.deleteFromES, version + "", msg, e));
        }
        return multiFailure.toOptional();
    }

    private Optional<Failure> updateItemOfSpu(String spuId, long version) {
        this.pcmRequester.clearSpuInfoCache(spuId);
        List<String> skuIds;
        try {
            skuIds = this.pcmRequester.listSkuIdBySpuId(spuId);
        } catch (RequestException e) {
            String msg = "从PCM查询SPU[spuId:" + spuId + "]下的SKU id失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.spu, FailureType.requestError, spuId, msg, e));
        }
        MultiFailure multiFailure = new MultiFailure();
        skuIds.forEach(skuId -> this.updateItemOfSku(skuId, version).ifPresent(multiFailure::merge));
        return multiFailure.toOptional();
    }

    private Optional<Failure> updateItemOfSku(String skuId, long version) {
        List<String> itemIds;
        try {
            itemIds = this.pcmRequester.listItemIdBySkuId(skuId);
        } catch (RequestException e) {
            String msg = "从PCM查询SKU[skuId:" + skuId + "]下的专柜商品id失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.sku, FailureType.requestError, skuId, msg, e));
        }
        List<ItemPojo> itemPojos;
        try {
            itemPojos = this.pcmRequester.listItems(itemIds);
        } catch (RequestException e) {
            String msg = "根据专柜商品id[itemIds:" + itemIds + "]查询专柜商品信息失败";
            logger.error(msg, e);
            return Optional.of(new Failure(DataType.item, FailureType.requestError, itemIds.toString(), msg, e));
        }
        MultiFailure multiFailure = new MultiFailure();
        itemPojos.forEach(item -> this.buildItem(item, version).ifPresent(multiFailure::merge));
        return multiFailure.toOptional();
    }
}
