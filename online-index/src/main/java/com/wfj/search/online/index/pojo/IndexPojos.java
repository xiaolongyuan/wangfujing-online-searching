package com.wfj.search.online.index.pojo;

import com.google.common.collect.Maps;
import com.wfj.search.online.common.pojo.*;
import com.wfj.search.online.index.iao.IPcmRequester;
import com.wfj.search.online.index.iao.RequestException;
import com.wfj.search.online.index.mapper.IndexBlacklistMapper;
import com.wfj.search.online.index.pojo.failure.DataType;
import com.wfj.search.online.index.pojo.failure.Failure;
import com.wfj.search.online.index.pojo.failure.FailureType;
import com.wfj.search.online.index.service.IIndexBlacklistService;
import com.wfj.search.online.index.util.ActiveActivityHolder;
import com.wfj.search.online.index.util.PcmError;
import com.wfj.search.online.index.util.PojoUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 工具类，用于组织数据
 * <p>create at 15-9-11</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public class IndexPojos {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public final Map<String, BrandIndexPojo> brandIndexPojos = Maps.newConcurrentMap();
    public final Map<String, CategoryIndexPojo> categoryIndexPojos = Maps.newConcurrentMap();
    public final Map<String, ColorIndexPojo> colorIndexPojos = Maps.newConcurrentMap();
    public final Map<String, ItemIndexPojo> itemIndexPojos = Maps.newConcurrentMap();
    public final Map<String, ActivityPojo> activityPojos = Maps.newConcurrentMap();
    public final Map<String, SkuIndexPojo> skuIndexPojos = Maps.newConcurrentMap();
    public final Map<String, SpuIndexPojo> spuIndexPojos = Maps.newConcurrentMap();
    public final Map<String, StandardIndexPojo> standardIndexPojos = Maps.newConcurrentMap();
    public final Map<String, PropertyIndexPojo> propertyIndexPojos = Maps.newConcurrentMap();
    public final Map<String, PropertyValueIndexPojo> propertyValueIndexPojos = Maps.newConcurrentMap();
    public final Map<String, TagIndexPojo> tagIndexPojos = Maps.newConcurrentMap();

    public Optional<Failure> assembleIndexPojos(IPcmRequester pcmRequester, IIndexBlacklistService indexBlacklistService,
            ItemPojo itemPojo, ActiveActivityHolder activeActivityHolder) {
        if (itemPojo.getChannels() == null || itemPojo.getChannels().isEmpty()) {
            return Optional.of(new Failure(DataType.item, FailureType.buildData, itemPojo.toString(),
                    PcmError.NO_CHANNEL.getDesc(), null));
        }
        // 专柜商品黑名单
        String itemId = itemPojo.getItemId();
        if (indexBlacklistService.listOfType(IndexBlacklistMapper.TYPE_ITEM).contains(itemId)) {
            return Optional.of(new Failure(DataType.item, FailureType.invalidData, itemPojo.toString(),
                    PcmError.FORBIDDEN_ITEM.getDesc(), null));
        }
        // SKU黑名单
        if (indexBlacklistService.listOfType(IndexBlacklistMapper.TYPE_SKU).contains(itemPojo.getSkuId())) {
            logger.error("商品[itemId:" + itemId + "]被SKU黑名单过滤");
            return Optional.of(new Failure(DataType.item, FailureType.invalidData, itemPojo.toString(),
                    PcmError.FORBIDDEN_SKU.getDesc(), null));
        }
        SkuPojo skuPojo;
        try {
            skuPojo = pcmRequester.getSkuInfo(itemPojo.getSkuId());
        } catch (RequestException e) {
            logger.error("获取商品[itemId:" + itemId + "]对应SKU失败", e);
            return Optional.of(new Failure(DataType.sku, FailureType.requestError, itemPojo.getSkuId(),
                    PcmError.NO_SKU_DATA.getDesc(), e));
        }
        if (!skuPojo.isOnSell()) {
            logger.error("商品[itemId:" + itemId + "]对应SKU未上架");
            return Optional.of(new Failure(DataType.sku, FailureType.invalidData, itemPojo.getSkuId(),
                    PcmError.NOT_SELLING_SKU.getDesc(), null));
        }
        // SPU黑名单
        String spuId = skuPojo.getSpuId();
        if (indexBlacklistService.listOfType(IndexBlacklistMapper.TYPE_SPU).contains(spuId)) {
            logger.error("商品[itemId:" + itemId + "]被SPU黑名单过滤");
            return Optional.of(new Failure(DataType.spu, FailureType.invalidData, spuId,
                    PcmError.FORBIDDEN_SPU.getDesc(), null));
        }
        SpuPojo spuPojo;
        try {
            spuPojo = pcmRequester.getSpuInfo(spuId);
        } catch (RequestException e) {
            logger.error("获取商品[itemId:" + itemId + "]对应SPU失败", e);
            return Optional.of(new Failure(DataType.spu, FailureType.requestError, spuId,
                    PcmError.NO_SPU_DATA.getDesc(), e));
        }
        if (!skuPojo.isOnSell()) {
            logger.error("商品[itemId:" + itemId + "]对应SPU未上架");
            return Optional.of(new Failure(DataType.spu, FailureType.invalidData, spuId,
                    PcmError.NOT_SELLING_SPU.getDesc(), null));
        }
        // 品牌黑名单
        if (indexBlacklistService.listOfType(IndexBlacklistMapper.TYPE_BRAND).contains(spuPojo.getBrandId())) {
            logger.error("商品[itemId:" + itemId + "]被品牌黑名单过滤");
            return Optional.of(new Failure(DataType.brand, FailureType.invalidData, spuPojo.getBrandId(),
                    PcmError.FORBIDDEN_BRAND.getDesc(), null));
        }
        BrandPojo brandPojo;
        try {
            brandPojo = pcmRequester.getBrandInfo(spuPojo.getBrandId());
        } catch (RequestException e) {
            logger.error("获取商品[itemId:" + itemId + "]对应品牌失败", e);
            return Optional.of(new Failure(DataType.brand, FailureType.requestError, spuPojo.getBrandId(),
                    PcmError.NO_BRAND_DATA.getDesc(), e));
        }
        if (spuPojo.getCategoryIds().isEmpty()) {
            logger.error("获取商品[itemId:" + itemId + "]对应SPU所属分类失败");
            return Optional.of(new Failure(DataType.spu, FailureType.buildData, spuId,
                    PcmError.NO_CATEGORY_DATA.getDesc(), null));
        }
        Map<String, Map<String, CategoryIndexPojo>> spuCategoriesOfChannels = Maps.newHashMap();
        try {
            for (String cid : spuPojo.getCategoryIds()) {
                while (StringUtils.isNotBlank(cid) && !"0".equals(cid.trim())) {
                    CategoryPojo categoryPojo = pcmRequester.getCategoryInfo(cid);
                    CategoryIndexPojo cat = PojoUtils.toIndexPojo(categoryPojo, pcmRequester, null);
                    String channel = cat.getChannel();
                    Map<String, CategoryIndexPojo> spuCategories = spuCategoriesOfChannels.get(channel);
                    if (spuCategories == null) {
                        spuCategories = Maps.newHashMap();
                        spuCategoriesOfChannels.put(channel, spuCategories);
                    }
                    String parentCategoryId = cat.getParentCategoryId();
                    if (StringUtils.isNotBlank(parentCategoryId) && !"0".equals(parentCategoryId)) {
                        spuCategories.put(cid, cat);
                    }
                    cid = categoryPojo.getParentCategoryId();
                }
            }
        } catch (RequestException e) {
            logger.error("获取商品[itemId:" + itemId + "]对应分类失败", e);
            return Optional.of(new Failure(DataType.category, FailureType.requestError, spuId,
                    PcmError.NO_CATEGORY_DATA.getDesc(), e));
        }
        spuCategoriesOfChannels.values().forEach(this.categoryIndexPojos::putAll);
        spuPojo.getPropertyValues()
                .forEach(pv -> pv.setChannel(categoryIndexPojos.get(pv.getCategoryId()).getChannel()));
        String brandId = brandPojo.getBrandId();
        BrandIndexPojo brandIndexPojo;
        brandIndexPojo = brandIndexPojos.get(brandId);
        if (brandIndexPojo == null) {
            brandIndexPojo = PojoUtils.toIndexPojo(brandPojo);
            this.brandIndexPojos.put(brandIndexPojo.getBrandId(), brandIndexPojo);
        }
        StandardIndexPojo standardIndexPojo;
        standardIndexPojo = this.standardIndexPojos.get(skuPojo.getStandardPojo().getStandardId());
        if (standardIndexPojo == null) {
            standardIndexPojo = PojoUtils.toIndexPojo(skuPojo.getStandardPojo());
            this.standardIndexPojos.put(standardIndexPojo.getStandardId(), standardIndexPojo);
        }
        List<ActivityPojo> activeActivities = itemPojo.getActivities().stream()
                .filter(activity -> activeActivityHolder.getActiveActivities().containsKey(activity.getActiveId()))
                .collect(Collectors.toList());
        itemPojo.setActivities(activeActivities);
        ItemIndexPojo itemIndexPojo = PojoUtils.toIndexPojo(itemPojo, skuPojo, spuPojo, brandPojo, spuCategoriesOfChannels);
        if (itemIndexPojo.getColorMasterPicture() == null) {
            logger.error("获取商品[itemId:" + itemId + "]颜色主图失败");
            return Optional.of(new Failure(DataType.item, FailureType.buildData, itemId,
                    PcmError.NO_COLOR_MAIN_PIC.getDesc(), null));
        }
        this.itemIndexPojos.put(itemId, itemIndexPojo);
        SkuIndexPojo skuIndexPojo;
        skuIndexPojo = this.skuIndexPojos.get(skuPojo.getSkuId());
        if (skuIndexPojo == null) {
            skuIndexPojo = PojoUtils.toIndexPojo(skuPojo);
            this.skuIndexPojos.put(skuIndexPojo.getSpuId(), skuIndexPojo);
        }
        ColorIndexPojo colorIndexPojo;
        String colorId = skuIndexPojo.getColorId();
        colorIndexPojo = this.colorIndexPojos.get(colorId);
        if (colorIndexPojo == null) {
            colorIndexPojo = new ColorIndexPojo();
            colorIndexPojo.setColorId(colorId);
            colorIndexPojo.setColorName(skuIndexPojo.getColorName());
            this.colorIndexPojos.put(colorId, colorIndexPojo);
        }
        SpuIndexPojo spuIndexPojo;
        spuIndexPojo = this.spuIndexPojos.get(spuPojo.getSpuId());
        if (spuIndexPojo == null) {
            spuIndexPojo = PojoUtils.toIndexPojo(spuPojo);
            this.spuIndexPojos.put(spuIndexPojo.getSpuId(), spuIndexPojo);
        }
        spuIndexPojo.getPropertyIndexPojos()
                .forEach(prop -> this.propertyIndexPojos.put(prop.getPropertyId(), prop));
        spuIndexPojo.getPropertyValueIndexPojos()
                .forEach(pv -> this.propertyValueIndexPojos.put(pv.getPropertyValueId(), pv));
        spuIndexPojo.getTagIndexPojos().forEach(tag -> this.tagIndexPojos.put(tag.getTagId(), tag));
        itemPojo.getActivities().forEach(activity -> this.activityPojos.put(activity.getActiveId(), activity));
        return Optional.empty();
    }
}
