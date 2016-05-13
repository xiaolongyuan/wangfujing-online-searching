package com.wfj.search.online.web.searchTask;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wfj.search.online.common.RuntimeTrackingException;
import com.wfj.search.online.common.TrackingException;
import com.wfj.search.online.web.common.pojo.CategoryDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.iao.IItemIAO;
import com.wfj.search.online.web.iao.SolrSearchException;
import com.wfj.search.online.web.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.FacetField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>create at 15-11-5</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("facetCategoryTreeSearchTask")
public class FacetCategoryTreeSearchTask implements SearchTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IItemIAO itemIAO;
    @Autowired
    private ICategoryService categoryService;

    @Override
    public void doSearch(SearchResult searchResult, SolrQuery baseQuery) {
        logger.debug("##doSearch##baseQuery before {}: {}", getClass().getSimpleName(), baseQuery);
        String beforeQuery = baseQuery.toString();
        SolrQuery query = baseQuery.getCopy();
        List<CategoryDisplayPojo> categories = Collections.synchronizedList(Lists.newArrayList());
        List<FacetField.Count> categoryIdFFC;
        try {
            categoryIdFFC = this.itemIAO.facetField(query,
                    "allLevelCategoryIds_" + searchResult.getParams().getChannel());
            List<String> catIds = categoryIdFFC.stream().map(FacetField.Count::getName).collect(Collectors.toList());
            for (String catId : catIds) {
                try {
                    CategoryDisplayPojo categoryDisplayPojo = this.categoryService.restoreCategory(catId);
                    if (categoryDisplayPojo != null) {
                        categories.add(categoryDisplayPojo);
                    }
                } catch (Exception e) {
                    logger.error("从ES恢复分类[{}]失败, 0x530042", catId, e);
                    throw new RuntimeTrackingException(new TrackingException(e, "0x530042"));
                }
            }
        } catch (SolrSearchException e) {
            logger.error("facet分类失败, 0x530041", e);
            throw new RuntimeTrackingException(new TrackingException(e, "0x530041"));
        }
        Map<String, CategoryDisplayPojo> categoryMap = Maps.newConcurrentMap();
        categories.forEach(cat -> categoryMap.put(cat.getId(), cat));
        for (FacetField.Count count : categoryIdFFC) {
            CategoryDisplayPojo categoryDisplayPojo = categoryMap.get(count.getName());
            if (categoryDisplayPojo != null) {
                categoryDisplayPojo.setFacetCount((int) count.getCount());
            }
        }
        categories.forEach(cat -> {
            String parentCatId = cat.getParentId();
            if (StringUtils.isBlank(parentCatId) || "0".equals(parentCatId.trim())) {
                return;
            }
            CategoryDisplayPojo parentCat = categoryMap.get(parentCatId);
            if (parentCat == null) {
                cat.setRoot(true);
                searchResult.getFilters().getCategoryTree().add(cat);
            } else {
                parentCat.getChildren().add(cat);
            }
        });
        this.sortCategories(searchResult.getFilters().getCategoryTree());
        logger.debug("##doSearch##baseQuery after {}: {}", getClass().getSimpleName(), baseQuery);
        String afterQuery = baseQuery.toString();
        if (!afterQuery.equals(beforeQuery)) {
            logger.error("{}#doSearch() changed baseQuery!");
        }
    }

    private void sortCategories(List<CategoryDisplayPojo> categories) {
        Collections.sort(categories, (o1, o2) -> o1.getOrder() - o2.getOrder());
        for (CategoryDisplayPojo category : categories) {
            sortCategories(category.getChildren());
        }
    }
}
