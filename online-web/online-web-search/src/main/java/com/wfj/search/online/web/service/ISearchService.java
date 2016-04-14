package com.wfj.search.online.web.service;

import com.wfj.search.online.common.TrackingException;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.common.pojo.SpuDisplayPojo;
import com.wfj.search.online.web.pojo.PagedResult;

/**
 * <p>create at 15-11-20</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface ISearchService {
    /**
     * 执行search请求
     *
     * @param searchParams 搜索参数
     * @param urlPrefix    路经前缀
     * @return 搜索结果
     */
    SearchResult doSearch(SearchParams searchParams, String urlPrefix) throws TrackingException;

    /**
     * 执行list请求
     *
     * @param searchParams 搜索参数
     * @param urlPrefix    路经前缀
     * @return 搜索结果
     */
    SearchResult doList(SearchParams searchParams, String urlPrefix) throws TrackingException;

    /**
     * 执行brandList请求
     *
     * @param searchParams 搜索参数
     * @param urlPrefix    路经前缀
     * @return 搜索结果
     */
    SearchResult doBrandList(SearchParams searchParams, String urlPrefix) throws TrackingException;

    /**
     * 新品速递查询
     *
     * @param searchParams 搜索参数
     * @return 搜索结果
     */
    SearchResult doNewProductsList(SearchParams searchParams) throws TrackingException;

    PagedResult<SpuDisplayPojo> simpleSearch(String value, int fetch, String channel) throws TrackingException;

    SearchResult doListAllCategories(SearchParams params) throws TrackingException;

    SearchResult doListAvailableBrands(SearchParams params) throws TrackingException;
}
