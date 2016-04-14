package com.wfj.search.online.index.service;

import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.iao.QueryException;
import com.wfj.search.online.index.pojo.SuggestionIndexPojo;
import com.wfj.search.online.index.pojo.failure.Failure;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 索引服务
 * <p>create at 15-9-9</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public interface IIndexService {
    void commit() throws IndexException;

    Optional<Failure> indexAllFromEs(long version);

    Optional<Failure> indexItemsOfBrandFromEs(String brandId, Long version);

    void removeItemsOfBrand(String brandId) throws IndexException;

    Optional<Failure> indexItemsOfCategoryFromEs(String categoryId, String channel, Long version);

    Optional<Failure> indexItemsFromEs(Collection<String> itemIds, Long version);

    void removeItem(String itemId) throws IndexException;

    Optional<Failure> indexItemsOfSkuFromEs(String skuId, Long version);

    void removeItemsOfSku(String skuId) throws IndexException;

    Optional<Failure> indexItemsOfSpuFromEs(String spuId, Long version);

    void removeItemsOfSpu(String spuId) throws IndexException;

    Optional<Failure> indexNewerFromEs(Long version);

    List<SuggestionIndexPojo> facetAllItemKeywords(String channel) throws QueryException;

    List<String> facetChannels() throws QueryException;

    void fillKeywordMatches(SuggestionIndexPojo historyRecord) throws QueryException;
}
