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

    @CacheEvict(value = VALUE_KEY_COMMENT)
    @SuppressWarnings("UnusedParameters")
    default void removeCommentCache(String commentId) {
    }
}