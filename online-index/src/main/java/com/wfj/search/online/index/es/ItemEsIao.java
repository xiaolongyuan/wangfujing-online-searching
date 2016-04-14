package com.wfj.search.online.index.es;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.pojo.*;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>create at 16-3-27</p>
 *
 * @author liufl
 * @since 1.0.19
 */
public interface ItemEsIao {
    void delete(String itemId);

    void upsert(ItemIndexPojo itemIndexPojo) throws IndexException, JsonProcessingException;

    ScrollPage<ItemIndexPojo> startScrollForAll() throws IndexException;

    ScrollPage<ItemIndexPojo> scroll(String scrollId, Long scrollIdTTL) throws IndexException;

    ScrollPage<ItemIndexPojo> startScrollOfBrand(String brandId) throws IndexException;

    Iterable<ItemIndexPojo> multiGet(Collection<String> itemIds) throws IOException;

    List<ItemIndexPojo> findBySkuId(String skuId);

    List<ItemIndexPojo> findBySpuId(String spuId);

    ScrollPage<ItemIndexPojo> startScrollOfOperationSidGreaterThanEqual(long version) throws IndexException;

    ScrollPage<ItemIndexPojo> startScrollOfCategory(String categoryId, String channel) throws IndexException;

    void updateItemSales(String itemId, Integer itemSales) throws IndexException;

    void updateBrandNameBrandAliasedByBrandId(String brandId, String brandName, List<String> brandAliases)
            throws IndexException, JsonProcessingException;

    ScrollPage<ItemIndexPojo> startScrollOfCategoryBeforeVersion(String categoryId, String channel, long version)
            throws IndexException;

    void removeExpiredOfCategory(long lastKeepVersion, String categoryId, String channelOfCategory)
            throws IndexException;

    void updateSkuInfos(SkuIndexPojo skuIndexPojo, ColorIndexPojo colorIndexPojo, StandardIndexPojo standardIndexPojo,
            String masterPic, Map<String, String> colorMasterPictureOfPix)
            throws IndexException, JsonProcessingException;

    void removeExpiredOfSku(long lastKeepVersion, String skuId);

    void updateSpuInfos(SpuIndexPojo spuIndexPojo, BrandIndexPojo brandIndexPojo, Map<String, List<String>> allLevelCategoryIds,
            Map<String, List<String>> allLevelCategoryNames, Map<String, List<String>> categoryIdUnderCategory,
            List<TagIndexPojo> tagIndexPojos, Map<String, List<String>> propertyValues,
            Map<String, List<String>> propertyIds, Map<String, String> propertyValueIdOfPropertyId)
            throws IndexException, JsonProcessingException;

    void updateItemPrice(String itemId, double currentPrice, long version) throws IndexException;

    void updateSpuSales(String spuId, Integer spuSales) throws IndexException, JsonProcessingException;

    void updateSkuSales(String skuId, Integer skuSales) throws IndexException, JsonProcessingException;

    void removeExpiredOfBrand(long lastKeepVersion, String brandId);
}
