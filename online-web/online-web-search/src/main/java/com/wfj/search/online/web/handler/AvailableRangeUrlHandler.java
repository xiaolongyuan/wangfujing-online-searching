package com.wfj.search.online.web.handler;

import com.wfj.search.online.web.common.pojo.RangeDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <br/>create at 15-12-22
 *
 * @author liuxh
 * @since 1.0.0
 */
public class AvailableRangeUrlHandler extends AbstractUrlHandler {
    private static final Logger logger = LoggerFactory.getLogger(AvailableRangeUrlHandler.class);

    @Override
    public void handle(String preUrl, SearchResult result, SearchParams searchParams) {
        List<RangeDisplayPojo> availableRanges = result.getFilters().getAvailableRanges().getAvailables();
        availableRanges.forEach(range ->
                range.setUrl(preUrl +
                        searchParams.copy()
                                .setCurrentPage(1)
                                .setSelectedRange(range).toUrl()));
    }
}
