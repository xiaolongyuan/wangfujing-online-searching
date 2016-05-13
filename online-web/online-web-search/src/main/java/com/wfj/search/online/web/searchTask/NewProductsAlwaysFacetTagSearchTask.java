package com.wfj.search.online.web.searchTask;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wfj.search.online.common.RuntimeTrackingException;
import com.wfj.search.online.common.TrackingException;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.common.pojo.TagDisplayPojo;
import com.wfj.search.online.web.iao.IItemIAO;
import com.wfj.search.online.web.iao.SolrSearchException;
import com.wfj.search.online.web.service.ITagService;
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

/**
 * <p>create at 15-11-5</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("newProductsAlwaysFacetTagSearchTask")
public class NewProductsAlwaysFacetTagSearchTask extends NewProductsAlwaysFacetSearchTaskBase implements SearchTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IItemIAO itemIAO;
    @Autowired
    private ITagService tagService;

    @Override
    public void doSearch(SearchResult searchResult, SolrQuery baseQuery) {
        logger.debug("##doSearch##baseQuery before {}: {}", getClass().getSimpleName(), baseQuery);
        String beforeQuery = baseQuery.toString();
        SolrQuery query = baseFacetUseQuery(baseQuery);
        List<TagDisplayPojo> tags = Collections.synchronizedList(Lists.newArrayList());
        List<FacetField.Count> idFFC;
        try {
            idFFC = this.itemIAO.facetField(query, "tagIds");
            if (idFFC.size() == 1) {
                return;
            }
            idFFC.stream().map(FacetField.Count::getName)
                    .filter(tagId -> StringUtils.isNotBlank(tagId) && !"0".equals(tagId.trim()))
                    .forEach(tagId -> {
                        try {
                            TagDisplayPojo tagDisplayPojo = this.tagService.restoreTag(tagId);
                            if (tagDisplayPojo != null) {
                                tags.add(tagDisplayPojo);
                            }
                        } catch (Exception e) {
                            logger.error("从ES恢复标签[{}]失败, 0x530088", tagId, e);
                            throw new RuntimeTrackingException(new TrackingException(e, "0x530088"));
                        }
                    });
        } catch (SolrSearchException e) {
            logger.error("facet标签失败, 0x530087", e);
            throw new RuntimeTrackingException(new TrackingException(e, "0x530087"));
        }
        Map<String, Long> counts = Maps.newHashMap();
        for (FacetField.Count count : idFFC) {
            counts.put(count.getName(), count.getCount());
        }
        List<TagDisplayPojo> availableTags = searchResult.getFilters().getAvailableTags().getAvailables();
        tags.forEach(tag -> {
            Long count = counts.get(tag.getId());
            tag.setFacetCount(count == null ? 0 : count.intValue());
        });
        availableTags.addAll(tags);
        availableTags.sort((o1, o2) -> o2.getFacetCount() - o1.getFacetCount());
        logger.debug("##doSearch##baseQuery after {}: {}", getClass().getSimpleName(), baseQuery);
        String afterQuery = baseQuery.toString();
        if (!afterQuery.equals(beforeQuery)) {
            logger.error("{}#doSearch() changed baseQuery!");
        }
    }
}
