package com.wfj.search.online.web.service;

import com.wfj.search.online.web.pojo.SearchResult;

import java.util.Map;

/**
 * <br/>create at 16-1-12
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface ICommentService {
    SearchResult<Map<String, Object>> query(int start, int fetch, String skuId);
}
