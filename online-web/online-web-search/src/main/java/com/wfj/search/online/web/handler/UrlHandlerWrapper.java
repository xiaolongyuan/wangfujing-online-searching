package com.wfj.search.online.web.handler;

import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;

import java.util.List;

/**
 * <br/>create at 15-12-22
 *
 * @author liuxh
 * @since 1.0.0
 */
public class UrlHandlerWrapper extends AbstractUrlHandler {
    private List<AbstractUrlHandler> handlers;

    @Override
    public void handle(String preUrl, SearchResult result, SearchParams searchParams) {
        if (this.getHandlers() != null) {
            this.getHandlers().forEach(handler -> handler.handle(preUrl, result, searchParams));
        }
    }

    public List<AbstractUrlHandler> getHandlers() {
        return handlers;
    }

    public void setHandlers(List<AbstractUrlHandler> handlers) {
        this.handlers = handlers;
    }
}
