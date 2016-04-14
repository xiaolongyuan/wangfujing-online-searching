package com.wfj.search.online.web.searchTask;

import com.google.common.collect.Lists;
import com.wfj.search.online.common.RuntimeTrackingException;
import com.wfj.search.online.common.TrackingException;
import com.wfj.search.online.web.common.pojo.CategoryDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.common.pojo.SortDisplayPojo;
import com.wfj.search.online.web.iao.IItemIAO;
import com.wfj.search.online.web.iao.SolrSearchException;
import com.wfj.search.online.web.query.ItemFieldQueryDecorator;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>create at 15-11-5</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("listMainQuerySearchTask")
public class ListMainQuerySearchTask implements SearchTask {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ItemFieldQueryDecorator itemFieldQueryDecorator;
    @Autowired
    private IItemIAO itemIAO;

    @Override
    public void doSearch(SearchResult searchResult, SolrQuery baseQuery) {
        SolrQuery myQuery = baseQuery.getCopy()
                .setStart((searchResult.getParams().getCurrentPage() - 1) * searchResult.getParams().getRows())
                .setRows(searchResult.getParams().getRows());
        SortDisplayPojo sort = searchResult.getParams().getSort();
        if (sort.isDefault()) {
            List<CategoryDisplayPojo> selectedCategories = searchResult.getParams().getSelectedCategories();
            CategoryDisplayPojo cat = selectedCategories.get(selectedCategories.size() - 1);
            myQuery.setSorts(Lists.newArrayList(SolrQuery.SortClause.desc("popSpotWeightCategory_" + cat.getId()),
                    SolrQuery.SortClause.create(sort.getField(), sort.getOrder().name().toLowerCase())));
        }
        this.itemFieldQueryDecorator.decorator(myQuery, searchResult.getParams());
        try {
            this.itemIAO.query(myQuery, searchResult);
        } catch (SolrSearchException e) {
            logger.error("执行分类主查询失败, 0x530053", e);
            throw new RuntimeTrackingException(new TrackingException(e, "0x530053"));
        }
    }
}
