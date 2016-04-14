package com.wfj.search.online.index.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * <br/>create at 15-12-25
 *
 * @author liuxh
 * @since 1.0.0
 */
@Component("pcmUrlConfig")
public class PcmUrlConfig {
    @Value("${pcm.address}")
    private String pcmAddress;
    @Value("${pcm.uri.listItems}")
    private String listItems;
    @Value("${pcm.uri.listItemsByItemId}")
    private String listItemsByItemId;
    @Value("${pcm.uri.getSkuInfo}")
    private String getSkuInfo;
    @Value("${pcm.uri.getSpuInfo}")
    private String getSpuInfo;
    @Value("${pcm.uri.getBrandInfo}")
    private String getBrandInfo;
    @Value("${pcm.uri.getCategoryInfo}")
    private String getCategoryInfo;
    @Value("${pcm.uri.listSkuIdBySpuId}")
    private String listSkuIdBySpuId;
    @Value("${pcm.uri.listItemIdBySkuId}")
    private String listItemIdBySkuId;
    @Value("${pcm.uri.listBrands}")
    private String listBrands;
    @Value("${pcm.uri.listSubCategories}")
    private String listSubCategories;
    @Value("${pcm.uri.listSpuIdByBrandId}")
    private String listSpuIdByBrandId;
    @Value("${pcm.uri.listSpuIdByCategoryId}")
    private String listSpuIdByCategoryId;
    @Value("${pcm.uri.listItemPriceChanges}")
    private String listItemPriceChanges;
    @Value("${pcm.uri.listClosedActivities}")
    private String listClosedActivities;
    @Value("${pcm.uri.listItemOfActivity}")
    private String listItemOfActivity;
    @Value("${pcm.uri.listActiveActivities}")
    private String listActiveActivities;

    private static String urlListItems;
    private static String urlListItemsByItemId;
    private static String urlGetSkuInfo;
    private static String urlGetSpuInfo;
    private static String urlGetBrandInfo;
    private static String urlGetCategoryInfo;
    private static String urlListSkuIdBySpuId;
    private static String urlListItemIdBySkuId;
    private static String urlListBrands;
    private static String urlListSubCategories;
    private static String urlListSpuIdByBrandId;
    private static String urlListSpuIdByCategoryId;
    private static String urlListItemPriceChanges;
    private static String urlListItemsOfActivity;
    private static String urlListClosedActivities;
    private static String urlListActiveActivities;

    @PostConstruct
    public void initUrls() {
        urlListItems = pcmAddress + "/" + listItems;
        urlListItemsByItemId = pcmAddress + "/" + listItemsByItemId;
        urlGetSkuInfo = pcmAddress + "/" + getSkuInfo;
        urlGetSpuInfo = pcmAddress + "/" + getSpuInfo;
        urlGetBrandInfo = pcmAddress + "/" + getBrandInfo;
        urlGetCategoryInfo = pcmAddress + "/" + getCategoryInfo;
        urlListSkuIdBySpuId = pcmAddress + "/" + listSkuIdBySpuId;
        urlListItemIdBySkuId = pcmAddress + "/" + listItemIdBySkuId;
        urlListBrands = pcmAddress + "/" + listBrands;
        urlListSubCategories = pcmAddress + "/" + listSubCategories;
        urlListSpuIdByBrandId = pcmAddress + "/" + listSpuIdByBrandId;
        urlListSpuIdByCategoryId = pcmAddress + "/" + listSpuIdByCategoryId;
        urlListItemPriceChanges = pcmAddress + "/" + listItemPriceChanges;
        urlListItemsOfActivity = pcmAddress + "/" + listItemOfActivity;
        urlListClosedActivities = pcmAddress + "/" + listClosedActivities;
        urlListActiveActivities = pcmAddress + "/" + listActiveActivities;
    }

    public String getUrlListItems() {
        return urlListItems;
    }

    public String getUrlListItemsByItemId() {
        return urlListItemsByItemId;
    }

    public String getUrlGetSkuInfo() {
        return urlGetSkuInfo;
    }

    public String getUrlGetSpuInfo() {
        return urlGetSpuInfo;
    }

    public String getUrlGetBrandInfo() {
        return urlGetBrandInfo;
    }

    public String getUrlGetCategoryInfo() {
        return urlGetCategoryInfo;
    }

    public String getUrlListSkuIdBySpuId() {
        return urlListSkuIdBySpuId;
    }

    public String getUrlListItemIdBySkuId() {
        return urlListItemIdBySkuId;
    }

    public String getUrlListBrands() {
        return urlListBrands;
    }

    public String getUrlListSubCategories() {
        return urlListSubCategories;
    }

    public String getUrlListSpuIdByBrandId() {
        return urlListSpuIdByBrandId;
    }

    public String getUrlListSpuIdByCategoryId() {
        return urlListSpuIdByCategoryId;
    }

    public String getUrlListItemPriceChanges() {
        return urlListItemPriceChanges;
    }

    public String getUrlListItemsOfActivity() {
        return urlListItemsOfActivity;
    }

    public String getUrlListClosedActivities() {
        return urlListClosedActivities;
    }

    public String getUrlListActiveActivities() {
        return urlListActiveActivities;
    }
}
