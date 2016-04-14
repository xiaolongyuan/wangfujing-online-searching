package com.wfj.search.online.index.service;

import com.wfj.search.online.common.pojo.CategoryPojo;
import com.wfj.search.online.common.pojo.ItemPojo;
import com.wfj.search.online.common.pojo.SkuPojo;
import com.wfj.search.online.common.pojo.SpuPojo;
import com.wfj.search.online.index.pojo.BrandIndexPojo;
import com.wfj.search.online.index.pojo.CategoryIndexPojo;
import com.wfj.search.online.index.pojo.SoldPojo;
import com.wfj.search.online.index.pojo.SuggestionIndexPojo;
import com.wfj.search.online.index.pojo.failure.Failure;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * <p>create at 15-12-3</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public interface IEsService {
    Optional<Failure> buildAll2Es(long version);

    Optional<Failure> updateBrand(BrandIndexPojo brand);

    Optional<Failure> rebuildBrandAndItems(BrandIndexPojo brand, long version);

    Optional<Failure> rebuildBrandAndItems(String brandId, long version);

    /**
     * 根据输入的分类索引对象，将包括其父节点的所有分类保存到ES中。
     *
     * @param category 分类索引对象
     * @return 错误对象
     */
    Optional<Failure> updateCategoryWithoutItems(CategoryIndexPojo category);

    /**
     * 根据分类对象，将包括其父节点的所有分类保存到ES中。
     *
     * @param category 分类对象，有pcm下发或从pcm获取数据
     * @return 错误对象
     */
    Optional<Failure> updateCategoryWithoutItems(CategoryPojo category);

    /**
     * 根据分类id，将包括其父节点的所有分类保存到ES中。
     *
     * @param categoryId 分类id
     * @return 错误对象
     */
    Optional<Failure> updateCategoryWithoutItems(String categoryId);

    /**
     * 根据分类id、渠道重建所有专柜商品数据。仅对本地ES中的专柜商品进行重建。
     *
     * @param categoryId 分类id
     * @param channel    渠道
     * @param version    版本号
     * @return 错误对象
     */
    Optional<Failure> updateItemOfCategory(String categoryId, String channel, long version);

    /**
     * 根据分类id、渠道重建所有专柜商品。从pcm重新获取数据，重建专柜商品数据。
     *
     * @param categoryId 分类id
     * @param channel    渠道
     * @param version    版本号
     * @return 错误对象
     */
    @SuppressWarnings("unused")
    Optional<Failure> rebuildItemOfCategory(String categoryId, String channel, long version);

    Optional<Failure> buildItem(ItemPojo itemPojo, long version);

    Optional<Failure> buildItem(String itemId, long version);

    Optional<Failure> removeItem(String itemId);

    Optional<Failure> updateSku(SkuPojo sku);

    Optional<Failure> rebuildSkuAndItems(SkuPojo sku, long version);

    Optional<Failure> rebuildSkuAndItems(String skuId, long version);

    Optional<Failure> updateSpu(SpuPojo spu);

    Optional<Failure> rebuildSpuAndItems(String spuId, long version);

    Optional<Failure> updateSalesFromMySql(SoldPojo sold);

    Optional<Failure> fetchItemPriceChanges(Date after, Date before, long version);

    Optional<Failure> updateActivities(Date after, Date before, long version);

    List<SuggestionIndexPojo> aggregateSearchQueries(String channel);
}
