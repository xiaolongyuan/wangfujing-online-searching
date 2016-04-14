package com.wfj.search.online.web.handler;

import com.wfj.search.online.web.common.pojo.PageDisplayPojo;
import com.wfj.search.online.web.common.pojo.Pagination;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.service.ISearchConfigService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * <br/>create at 15-12-23
 *
 * @author liuxh
 * @since 1.0.0
 */
public class AvailablePaginationUrlHandler extends AbstractUrlHandler {
    @Autowired
    private ISearchConfigService searchConfigService;

    @Override
    public void handle(String preUrl, SearchResult result, SearchParams searchParams) {
        Pagination pagination = result.getPagination();
        int totalPage = pagination.getTotalPage();
        if (totalPage <= 0) {
            totalPage = 1;
        }
        int currentPage = pagination.getCurrentPage();

        PageDisplayPojo firstPage = new PageDisplayPojo();
        firstPage.setPageNum(1);
        firstPage.setDisplay("" + 1);
        firstPage.setUrl(preUrl + searchParams.copy().setCurrentPage(firstPage.getPageNum()).toUrl());
        pagination.setFirstPage(firstPage);
        PageDisplayPojo lastPage = new PageDisplayPojo();
        lastPage.setPageNum(totalPage);
        lastPage.setDisplay("" + totalPage);
        lastPage.setUrl(preUrl + searchParams.copy().setCurrentPage(lastPage.getPageNum()).toUrl());
        pagination.setLastPage(lastPage);

        List<PageDisplayPojo> pages = new ArrayList<>(totalPage);
        int numDisplayEntries = searchConfigService.getNumDisplayEntries();
        int pageHalf = (numDisplayEntries ) / 2;
        int start = currentPage - pageHalf;
        int end = currentPage + pageHalf;
        for (int i = start; i <= end; i++) {
            if (i < 1) {
                continue;
            }
            if (i > totalPage) {
                continue;
            }
            PageDisplayPojo page = new PageDisplayPojo();
            page.setPageNum(i);
            page.setDisplay("" + i);
            page.setUrl(preUrl + searchParams.copy().setCurrentPage(page.getPageNum()).toUrl());
            pages.add(page);
            if (i + 1 == currentPage) {
                PageDisplayPojo prePage = new PageDisplayPojo();
                prePage.setPageNum(i);
                prePage.setDisplay("上一页");
                prePage.setUrl(preUrl + searchParams.copy().setCurrentPage(i).toUrl());
                pagination.setPrePage(prePage);
            }
            if (i - 1 == currentPage) {
                PageDisplayPojo postPage = new PageDisplayPojo();
                postPage.setPageNum(i);
                postPage.setDisplay("下一页");
                postPage.setUrl(preUrl + searchParams.copy().setCurrentPage(i).toUrl());
                pagination.setPostPage(postPage);
            }
        }
        pagination.setPages(pages);
    }
}
