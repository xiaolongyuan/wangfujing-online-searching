package com.wfj.search.online.web.handler;

import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.common.pojo.SelectedDisplayPojo;
import com.wfj.search.online.web.common.pojo.TagDisplayPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <br/>create at 15-12-22
 *
 * @author liuxh
 * @since 1.0.0
 */
public class AvailableTagUrlHandler extends AbstractUrlHandler {
    private static final Logger logger = LoggerFactory.getLogger(AvailableTagUrlHandler.class);

    @Override
    public void handle(String preUrl, SearchResult result, SearchParams searchParams) {
        List<TagDisplayPojo> availableTags = result.getFilters().getAvailableTags().getAvailables();
        if(availableTags.size() > 0) {
            availableTags.forEach(tag -> {
                SelectedDisplayPojo<TagDisplayPojo> selectedTags = new SelectedDisplayPojo<>();
                selectedTags.getSelected().add(tag);
                tag.setUrl(preUrl + searchParams.copy().setCurrentPage(1).setSelectedTags(selectedTags).toUrl());
            });
        } else {
            logger.debug("non-tag is available.");
        }
    }
}
