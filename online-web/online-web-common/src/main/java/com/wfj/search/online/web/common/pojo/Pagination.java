package com.wfj.search.online.web.common.pojo;

import java.util.List;

/**
 * 分页信息
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public class Pagination {
    private int pageSize;
    private int currentPage;
    private int totalResults;
    private int totalPage;

    private PageDisplayPojo firstPage;
    private PageDisplayPojo lastPage;
    private PageDisplayPojo prePage;
    private PageDisplayPojo postPage;
    private List<PageDisplayPojo> pages;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public PageDisplayPojo getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(PageDisplayPojo firstPage) {
        this.firstPage = firstPage;
    }

    public PageDisplayPojo getLastPage() {
        return lastPage;
    }

    public void setLastPage(PageDisplayPojo lastPage) {
        this.lastPage = lastPage;
    }

    public PageDisplayPojo getPrePage() {
        return prePage;
    }

    public void setPrePage(PageDisplayPojo prePage) {
        this.prePage = prePage;
    }

    public PageDisplayPojo getPostPage() {
        return postPage;
    }

    public void setPostPage(PageDisplayPojo postPage) {
        this.postPage = postPage;
    }

    public List<PageDisplayPojo> getPages() {
        return pages;
    }

    public void setPages(List<PageDisplayPojo> pages) {
        this.pages = pages;
    }
}
