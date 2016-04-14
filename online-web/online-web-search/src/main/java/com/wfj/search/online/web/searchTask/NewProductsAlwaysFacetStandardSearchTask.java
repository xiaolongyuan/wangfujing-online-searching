package com.wfj.search.online.web.searchTask;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wfj.search.online.common.RuntimeTrackingException;
import com.wfj.search.online.common.TrackingException;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.common.pojo.StandardDisplayPojo;
import com.wfj.search.online.web.iao.IItemIAO;
import com.wfj.search.online.web.iao.SolrSearchException;
import com.wfj.search.online.web.service.IStandardService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.FacetField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * <p>create at 15-11-5</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("newProductsAlwaysFacetStandardSearchTask")
public class NewProductsAlwaysFacetStandardSearchTask extends NewProductsAlwaysFacetSearchTaskBase implements SearchTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IItemIAO itemIAO;
    @Autowired
    private IStandardService standardService;

    @Override
    public void doSearch(SearchResult searchResult, SolrQuery baseQuery) {
        logger.debug("##doSearch##baseQuery before {}: {}", getClass().getSimpleName(), baseQuery);
        String beforeQuery = baseQuery.toString();
        SolrQuery query = baseFacetUseQuery(baseQuery);
        List<StandardDisplayPojo> standards = Collections.synchronizedList(Lists.newArrayList());
        List<FacetField.Count> standardIdFFC;
        try {
            standardIdFFC = this.itemIAO.facetField(query, "standardId");
//                if (standardIdFFC.size() <= 1) {
//                    return;
//                }
            standardIdFFC.stream().map(FacetField.Count::getName)
                    .filter(standardId -> StringUtils.isNotBlank(standardId) && !"0".equals(standardId.trim()))
                    .forEach(standardId -> {
                        try {
                            StandardDisplayPojo standardDisplayPojo = this.standardService.restoreStandard(standardId);
                            if (standardDisplayPojo != null) {
                                standards.add(standardDisplayPojo);
                            }
                        } catch (Exception e) {
                            logger.error("从ES恢复规格[{}]失败, 0x530027", standardId, e);
                            throw new RuntimeTrackingException(new TrackingException(e, "0x530027"));
                        }
                    });
        } catch (SolrSearchException e) {
            logger.error("facet规格失败, 0x530026", e);
            throw new RuntimeTrackingException(new TrackingException(e, "0x530026"));
        }
        Map<String, Long> counts = Maps.newHashMap();
        for (FacetField.Count count : standardIdFFC) {
            counts.put(count.getName(), count.getCount());
        }
        List<StandardDisplayPojo> availableStandards = searchResult.getFilters().getAvailableStandards()
                .getAvailables();
        standards.forEach(standard -> {
            Long count = counts.get(standard.getId());
            standard.setFacetCount(count == null ? 0 : count.intValue());
        });
        availableStandards.addAll(standards);
        availableStandards.sort(new Comparator<StandardDisplayPojo>() {
            @Override
            public int compare(StandardDisplayPojo o1, StandardDisplayPojo o2) {
                return o2.getFacetCount() - o1.getFacetCount();
            }
        });
        logger.debug("##doSearch##baseQuery after {}: {}", getClass().getSimpleName(), baseQuery);
        String afterQuery = baseQuery.toString();
        if (!afterQuery.equals(beforeQuery)) {
            logger.error("{}#doSearch() changed baseQuery!");
        }
    }
}
