package com.wfj.search.online.web.handler;

import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;


/**
 * <br/>create at 15-12-22
 *
 * @author liuxh
 * @since 1.0.0
 */
public abstract class AbstractUrlHandler {
    public abstract void handle(String preUrl, SearchResult result, SearchParams searchParams);
}
