package com.wfj.search.online.web.searchTask;

import com.wfj.search.online.common.RuntimeTrackingException;
import com.wfj.search.online.common.TrackingException;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.iao.IItemIAO;
import com.wfj.search.online.web.query.ItemFieldQueryDecorator;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>create at 15-11-5</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("mainQuerySearchTask")
public class MainQuerySearchTask implements SearchTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ItemFieldQueryDecorator itemFieldQueryDecorator;
    @Autowired
    private IItemIAO itemIAO;

    @Override
    public void doSearch(SearchResult searchResult, SolrQuery baseQuery) {
        logger.debug("##doSearch##baseQuery before {}: {}", getClass().getSimpleName(), baseQuery);
        String beforeQuery = baseQuery.toString();
        SolrQuery myQuery = baseQuery.getCopy()
                .setStart((searchResult.getParams().getCurrentPage() - 1) * searchResult.getParams().getRows())
                .setRows(searchResult.getParams().getRows());
        this.itemFieldQueryDecorator.decorator(myQuery, searchResult.getParams());
        try {
            this.itemIAO.query(myQuery, searchResult);
        } catch (Exception e) {
            logger.error("执行主查询失败, 0x530054", e);
            throw new RuntimeTrackingException(new TrackingException(e, "0x530054"));
        }
        logger.debug("##doSearch##baseQuery after {}: {}", getClass().getSimpleName(), baseQuery);
        String afterQuery = baseQuery.toString();
        if (!afterQuery.equals(beforeQuery)) {
            logger.error("{}#doSearch() changed baseQuery!");
        }
    }
}
