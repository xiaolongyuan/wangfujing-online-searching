package com.wfj.search.online.index.iao;

import com.wfj.search.online.index.pojo.ItemIndexPojo;
import org.apache.solr.client.solrj.response.FacetField;

import java.util.Collection;
import java.util.List;

/**
 * <br/>create at 15-7-21
 *
 * @author liufl
 * @since 1.0.0
 */
public interface IItemIAO extends CommitableIAO {
    /**
     * 写入专柜商品记录。如果已存在对应记录将会覆盖
     *
     * @param items 新记录
     * @throws IndexException
     */
    void saveItems(Collection<ItemIndexPojo> items) throws IndexException;

    /**
     * 删除品牌下过期索引
     *
     * @param versionNo 标记起始保留操作版本
     * @throws IndexException
     */
    void removeExpired(long versionNo) throws IndexException;

    /**
     * 根据专柜商品编号删除索引
     *
     * @param itemId 专柜商品编号
     * @throws IndexException
     */
    public void removeItem(String itemId) throws IndexException;

    /**
     * 删除SKU对应的专柜商品记录
     * @param skuId SKU编码
     * @throws IndexException
     */
    void removeItemsOfSku(String skuId) throws IndexException;

    void removeExpiredOfBrand(String brandId, long version) throws IndexException;

    void removeItemsOfBrand(String brandId) throws IndexException;

    void removeExpiredOfCategory(String categoryId, String channel, long version) throws IndexException;

    void removeExpiredOfSku(String skuId, long version) throws IndexException;

    void removeExpiredOfSpu(String spuId, long version) throws IndexException;

    void removeItemsOfSpu(String spuId) throws IndexException;

    void rebuildSpell() throws IndexException;

    List<FacetField.Count> facetChannels() throws QueryException;

    List<FacetField.Count> facetField(String channel, String field) throws QueryException;

    Long queryMatchCount(String keyword, String channel) throws QueryException;
}
