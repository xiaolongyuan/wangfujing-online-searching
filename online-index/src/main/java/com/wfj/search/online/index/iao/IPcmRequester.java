package com.wfj.search.online.index.iao;

import com.wfj.search.online.common.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 数据导入器接口
 * <br/>create at 15-9-8
 *
 * @author liuxh, liufl
 * @since 1.0.0
 */
public interface IPcmRequester {
    Logger LOGGER = LoggerFactory.getLogger(IPcmRequester.class);
    String PCM_SKU = "WFJ_SEARCH_ONLINE_PCM_SKU";
    String PCM_SPU = "WFJ_SEARCH_ONLINE_PCM_SPU";
    String PCM_BRAND = "WFJ_SEARCH_ONLINE_PCM_BRAND";
    String PCM_CATEGORY = "WFJ_SEARCH_ONLINE_PCM_CATEGORY";

    /**
     * 根据itemId列表获取专柜商品信息列表
     *
     * @param itemIds 专柜商品编码列表
     * @return 专柜商品信息列表
     * @throws RequestException
     */
    List<ItemPojo> listItems(List<String> itemIds) throws RequestException;

    /**
     * 根据itemId获取专柜商品信息
     *
     * @param itemId 专柜商品编码
     * @return 专柜商品信息
     * @throws RequestException
     */
    Optional<ItemPojo> getItemsByItemId(String itemId) throws RequestException;

    /**
     * 返回专柜商品总数
     *
     * @return 专柜商品总数
     * @throws RequestException
     */
    int countItems() throws RequestException;

    /**
     * 分页列出专柜商品信息列表
     *
     * @param start 记录开始位置
     * @param fetch 请求取回数量
     * @return 专柜商品信息分页列表
     * @throws RequestException
     */
    Page<ItemPojo> listItems(int start, int fetch) throws RequestException;

    /**
     * 获取SKU信息
     *
     * @param skuId SKU编码
     * @return SKU信息
     * @throws RequestException
     */
    SkuPojo getSkuInfo(String skuId) throws RequestException;

    /**
     * 清理SKU信息缓存
     *
     * @param skuId SKU编码
     */
    @CacheEvict(value = PCM_SKU)
    default void clearSkuInfoCache(String skuId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("清除SKU缓存[skuId:" + skuId + "]");
        }
    }

    /**
     * 清理SKU信息缓存
     */
    @CacheEvict(value = PCM_SKU, allEntries = true)
    default void clearAllSkuInfoCache() {
    }

    /**
     * 获取SPU信息
     *
     * @param spuId SPU编码
     * @return SPU信息
     * @throws RequestException
     */
    SpuPojo getSpuInfo(String spuId) throws RequestException;

    /**
     * 清理SPU信息缓存
     *
     * @param spuId SPU编码
     */
    @CacheEvict(value = PCM_SPU)
    default void clearSpuInfoCache(String spuId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("清除SPU缓存[spuId:" + spuId + "]");
        }
    }

    /**
     * 获取品牌详细信息
     *
     * @param brandId 网站品牌编码
     * @return 品牌详细信息
     * @throws RequestException
     */
    BrandPojo getBrandInfo(String brandId) throws RequestException;

    /**
     * 获取品牌详细信息 </br>
     * 从PCM直接获取，不使用缓存数据
     *
     * @param brandId 网站品牌编码
     * @return 品牌详细信息
     * @throws RequestException
     */
    BrandPojo directGetBrandInfo(String brandId) throws RequestException;

    /**
     * 清理品牌详细信息缓存
     *
     * @param brandId 网站品牌编码
     */
    @CacheEvict(value = PCM_BRAND)
    default void clearBrandInfoCache(String brandId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("清除品牌缓存[skuId:" + brandId + "]");
        }
    }

    /**
     * 清理品牌详细信息缓存
     */
    @CacheEvict(value = PCM_BRAND, allEntries = true)
    default void clearAllBrandInfoCache() {
    }

    /**
     * 获取分类详细信息
     *
     * @param categoryId 展示分类编码
     * @return 分类详细信息
     * @throws RequestException
     */
    CategoryPojo getCategoryInfo(String categoryId) throws RequestException;

    /**
     * 获取分类详细信息</br>
     * 从PCM直接获取，不使用缓存数据
     *
     * @param categoryId 展示分类编码
     * @return 分类详细信息
     * @throws RequestException
     */
    CategoryPojo directGetCategoryInfo(String categoryId) throws RequestException;

    /**
     * 清理分类详细信息缓存
     */
    @CacheEvict(value = PCM_CATEGORY, allEntries = true)
    default void clearAllCategoryInfoCache() {
    }

    /**
     * 按SPU列出SKU编码
     *
     * @param spuId SPU编码
     * @return SKU编码列表
     * @throws RequestException
     */
    List<String> listSkuIdBySpuId(String spuId) throws RequestException;

    /**
     * 按SKU列出专柜商品编码
     *
     * @param skuId SKU编码
     * @return 专柜商品编码列表
     * @throws RequestException
     */
    List<String> listItemIdBySkuId(String skuId) throws RequestException;

    /**
     * 列出所有网站品牌
     *
     * @return 所有网站品牌列表
     * @throws RequestException
     */
    List<BrandPojo> listBrands() throws RequestException;

    /**
     * 按分类编码列出子级分类编码列表
     *
     * @param categoryId 分类编码
     * @return 子级分类编码列表
     * @throws RequestException
     */
    List<String> listSubCategories(String categoryId) throws RequestException;

    /**
     * 品牌下SPU总数
     *
     * @param brandId 品牌编码
     * @return 品牌下SPU总数
     * @throws RequestException
     */
    int totalSpuOfBrandId(String brandId) throws RequestException;

    /**
     * 按Brand列出SPU编码
     *
     * @param brandId 品牌编码
     * @param start   记录开始位置
     * @param fetch   请求取回数量
     * @return SPU编码
     * @throws RequestException
     */
    List<String> listSpuIdByBrandId(String brandId, int start, int fetch) throws RequestException;

    /**
     * 叶子级分类下SPU总数
     *
     * @param categoryId 叶子级分类编码。非叶子级分类编码将返回错误
     * @return 分类下SPU总数
     * @throws RequestException
     */
    @SuppressWarnings("unused")
    int totalSpuOfCategoryId(String categoryId) throws RequestException;

    /**
     * 按叶子级分类列出SPU编码
     *
     * @param categoryId 叶子级分类编码。非叶子级分类编码将返回错误
     * @param start      记录开始位置
     * @param fetch      请求取回数量
     * @return SPU编码
     * @throws RequestException
     */
    @SuppressWarnings("unused")
    List<String> listSpuIdByCategoryId(String categoryId, int start, int fetch) throws RequestException;

    @CacheEvict(value = PCM_SPU, allEntries = true)
    default void clearAllSpuInfoCache() {
    }

    /**
     * 指定时间区间内变价商品总数
     *
     * @param after  时间区间开始点
     * @param before 时间区间结束点
     * @return 变价商品数
     * @throws RequestException
     */
    int countItemPriceChanges(Date after, Date before) throws RequestException;

    /**
     * 分页获取指定时间区间内变价商品
     *
     * @param after  时间区间开始点
     * @param before 时间区间结束点
     * @param start  记录开始位置
     * @param fetch  请求取回数量
     * @return 变价信息，注意只有itemId和currentPrice字段可用！
     * @throws RequestException
     */
    List<ItemPojo> listItemPriceChanges(Date after, Date before, int start, int fetch) throws RequestException;

    /**
     * 指定活动下商品总数
     *
     * @param activityId 活动ID
     * @return 商品数
     */
    int countActivityItems(String activityId) throws RequestException;

    /**
     * 分页列出指定活动下的商品
     *
     * @param activityId 活动ID
     * @param start      记录开始位置
     * @param fetch      请求取回数量
     * @return 商品列表
     * @throws RequestException
     */
    List<ItemPojo> listActivityItems(String activityId, int start, int fetch) throws RequestException;

    /**
     * 指定时间区间内结束的活动总数
     *
     * @param after  时间区间开始点
     * @param before 时间区间结束点
     * @return 结束的活动总数
     * @throws RequestException
     */
    int countClosedActivities(Date after, Date before) throws RequestException;

    /**
     * 分页获取指定时间区间结束的活动
     *
     * @param after  时间区间开始点
     * @param before 时间区间结束点
     * @param start  记录开始位置
     * @param fetch  请求取回数量
     * @return 结束的活动编码
     * @throws RequestException
     */
    List<String> listClosedActivities(Date after, Date before, int start, int fetch) throws RequestException;

    /**
     * 指定时间点活动的活动总数
     *
     * @param when 指定时间点
     * @return 活动的活动总数
     * @throws RequestException
     */
    int countActiveActivities(Date when) throws RequestException;

    /**
     * 分页获取指定时间点活动的活动
     * @param when 指定时间点
     * @param start  记录开始位置
     * @param fetch  请求取回数量
     * @return 活动的活动信息列表
     * @throws RequestException
     */
    List<ActivityPojo> listActiveActivities(Date when, int start, int fetch) throws RequestException;
}
