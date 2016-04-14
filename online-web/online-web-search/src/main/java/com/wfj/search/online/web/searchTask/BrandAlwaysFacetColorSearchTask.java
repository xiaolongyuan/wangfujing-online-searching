package com.wfj.search.online.web.searchTask;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wfj.search.online.common.RuntimeTrackingException;
import com.wfj.search.online.common.TrackingException;
import com.wfj.search.online.web.common.pojo.ColorDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.iao.IItemIAO;
import com.wfj.search.online.web.iao.SolrSearchException;
import com.wfj.search.online.web.service.IColorService;
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
@Component("brandAlwaysFacetColorSearchTask")
public class BrandAlwaysFacetColorSearchTask extends BrandAlwaysFacetSearchTaskBase implements SearchTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IItemIAO itemIAO;
    @Autowired
    private IColorService colorService;

    @Override
    public void doSearch(SearchResult searchResult, SolrQuery baseQuery) {
        logger.debug("##doSearch##baseQuery before {}: {}", getClass().getSimpleName(), baseQuery);
        String beforeQuery = baseQuery.toString();
        SolrQuery query = baseFacetUseQuery(baseQuery);
        List<FacetField.Count> colorAliasFFC;
        List<ColorDisplayPojo> colors = Collections.synchronizedList(Lists.newArrayList());
        try {
            colorAliasFFC = this.itemIAO.facetField(query, "colorId");
//                if (colorAliasFFC.size() == 1) {
//                    return;
//                }
            colorAliasFFC.stream().map(FacetField.Count::getName)
                    .filter(colorId -> StringUtils.isNotBlank(colorId) && !"0".equals(colorId.trim()))
                    .forEach(colorId -> {
                        try {
                            ColorDisplayPojo colorDisplayPojo = this.colorService.restoreColor(colorId);
                            if (colorDisplayPojo != null) {
                                colors.add(colorDisplayPojo);
                            }
                        } catch (Exception e) {
                            logger.error("从ES恢复色系[{}]失败, 0x530021", colorId, e);
                            throw new RuntimeTrackingException(new TrackingException(e, "0x530021"));
                        }
                    });
        } catch (SolrSearchException e) {
            logger.error("facet颜色失败, 0x530020", e);
            throw new RuntimeTrackingException(new TrackingException(e, "0x530020"));
        }
        Map<String, Long> counts = Maps.newHashMap();
        for (FacetField.Count count : colorAliasFFC) {
            counts.put(count.getName(), count.getCount());
        }
        List<ColorDisplayPojo> availableColor = searchResult.getFilters().getAvailableColors().getAvailables();
        colors.forEach(color -> {
            Long count = counts.get(color.getId());
            color.setFacetCount(count == null ? 0 : count.intValue());
        });
        availableColor.addAll(colors);
        availableColor.sort(new Comparator<ColorDisplayPojo>() {
            @Override
            public int compare(ColorDisplayPojo o1, ColorDisplayPojo o2) {
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
