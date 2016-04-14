package com.wfj.search.online.index.service;

import org.springframework.cache.annotation.CacheEvict;

import static com.wfj.search.online.common.constant.CacheableAware.*;

/**
 * <p>create at 15-12-4</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public interface ICacheEvictService {
    @CacheEvict(value = VALUE_KEY_BRAND)
    @SuppressWarnings("UnusedParameters")
    default void removeBrandCache(String brandId) {
    }

    @CacheEvict(value = VALUE_KEY_CATEGORY)
    @SuppressWarnings("UnusedParameters")
    default void removeCategoryCache( String categoryId) {
    }

    @CacheEvict(value = VALUE_KEY_COLOR)
    @SuppressWarnings("UnusedParameters")
    default void removeColorCache( String colorId) {
    }

    @CacheEvict(value = VALUE_KEY_SKU)
    @SuppressWarnings("UnusedParameters")
    default void removeSkuCache( String skuId) {
    }

    @CacheEvict(value = VALUE_KEY_SPU)
    @SuppressWarnings("UnusedParameters")
    default void removeSpuCache( String spuId) {
    }

    @CacheEvict(value = VALUE_KEY_PROPERTY)
    @SuppressWarnings("UnusedParameters")
    default void removePropertyCache( String propertyId) {
    }

    @CacheEvict(value = VALUE_KEY_PROPERTY_VALUE)
    @SuppressWarnings("UnusedParameters")
    default void removePropertyValueCache( String propertyValueId) {
    }

    @CacheEvict(value = VALUE_KEY_STANDARD)
    @SuppressWarnings("UnusedParameters")
    default void removeStandardCache( String standardId) {
    }

    @CacheEvict(value = VALUE_KEY_TAG)
    @SuppressWarnings("UnusedParameters")
    default void removeTagCache(String tagId) {
    }

    @CacheEvict(value = VALUE_KEY_COMMENT)
    @SuppressWarnings("UnusedParameters")
    default void removeCommentCache(String commentId) {
    }
}