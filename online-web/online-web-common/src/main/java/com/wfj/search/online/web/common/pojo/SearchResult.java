package com.wfj.search.online.web.common.pojo;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.List;

/**
 * 搜索结果
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public class SearchResult {
    private SearchParams params;

    /*成功结果 {*/
    private AvailableFilters filters = new AvailableFilters();
    private List<String> likelyQueries = Collections.synchronizedList(Lists.newArrayList());
    private List<SortDisplayPojo> sorts = Collections.synchronizedList(Lists.newArrayList());
    private List<SpuDisplayPojo> successList = Collections.synchronizedList(Lists.newArrayList());
    private Pagination pagination = new Pagination();
    /*}*/

    /*无结果{*/
    private List<SuggestionDisplayPojo> noResultSuggestion = Collections.synchronizedList(Lists.newArrayList());
    /*}*/

    public SearchResult(SearchParams params) {
        this.params = Validate.notNull(params, "搜索参数对象不能为 null");
        this.pagination.setCurrentPage(params.getCurrentPage());
        this.pagination.setPageSize(params.getRows());
    }

    public SearchParams getParams() {
        return params;
    }

    public AvailableFilters getFilters() {
        return filters;
    }

    public List<String> getLikelyQueries() {
        return likelyQueries;
    }

    public List<SortDisplayPojo> getSorts() {
        return sorts;
    }

    public List<SpuDisplayPojo> getSuccessList() {
        return successList;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public List<SuggestionDisplayPojo> getNoResultSuggestion() {
        return noResultSuggestion;
    }

    /**
     * 仅用于缓存反序列化，不要手工调用！
     */
    @SuppressWarnings("unused")
    public SearchResult() {
    }

    /**
     * 仅用于缓存反序列化，不要手工调用！
     */
    @SuppressWarnings("unused")
    public void setParams(SearchParams params) {
        this.params = params;
    }

    /**
     * 仅用于缓存反序列化，不要手工调用！
     */
    @SuppressWarnings("unused")
    public void setFilters(AvailableFilters filters) {
        this.filters = filters;
    }

    /**
     * 仅用于缓存反序列化，不要手工调用！
     */
    @SuppressWarnings("unused")
    public void setLikelyQueries(List<String> likelyQueries) {
        this.likelyQueries = likelyQueries;
    }

    /**
     * 仅用于缓存反序列化，不要手工调用！
     */
    @SuppressWarnings("unused")
    public void setSorts(List<SortDisplayPojo> sorts) {
        this.sorts = sorts;
    }

    /**
     * 仅用于缓存反序列化，不要手工调用！
     */
    @SuppressWarnings("unused")
    public void setSuccessList(List<SpuDisplayPojo> successList) {
        this.successList = successList;
    }

    /**
     * 仅用于缓存反序列化，不要手工调用！
     */
    @SuppressWarnings("unused")
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    /**
     * 仅用于缓存反序列化，不要手工调用！
     */
    @SuppressWarnings("unused")
    public void setNoResultSuggestion(List<SuggestionDisplayPojo> noResultSuggestion) {
        this.noResultSuggestion = noResultSuggestion;
    }
}
