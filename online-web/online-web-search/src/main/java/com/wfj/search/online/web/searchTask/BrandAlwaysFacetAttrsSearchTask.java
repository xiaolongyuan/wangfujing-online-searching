package com.wfj.search.online.web.searchTask;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wfj.search.online.common.RuntimeTrackingException;
import com.wfj.search.online.common.TrackingException;
import com.wfj.search.online.web.common.pojo.AvailableDisplayPojo;
import com.wfj.search.online.web.common.pojo.PropertyDisplayPojo;
import com.wfj.search.online.web.common.pojo.PropertyValueDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.iao.IItemIAO;
import com.wfj.search.online.web.iao.SolrSearchException;
import com.wfj.search.online.web.service.IPropertyService;
import com.wfj.search.online.web.service.IPropertyValueService;
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
@Component("brandAlwaysFacetAttrsSearchTask")
public class BrandAlwaysFacetAttrsSearchTask extends BrandAlwaysFacetSearchTaskBase implements SearchTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IItemIAO itemIAO;
    @Autowired
    private IPropertyService propertyService;
    @Autowired
    private IPropertyValueService propertyValueService;

    @Override
    public void doSearch(SearchResult searchResult, SolrQuery baseQuery) {
        logger.debug("##doSearch##baseQuery before {}: {}", getClass().getSimpleName(), baseQuery);
        String beforeQuery = baseQuery.toString();
        SolrQuery query = baseFacetUseQuery(baseQuery);
        List<FacetField.Count> propertyIdFFC;
        Map<String, List<FacetField.Count>> propertyValueIdFFCs;
        List<PropertyDisplayPojo> properties = Collections.synchronizedList(Lists.newArrayList());
        Map<String, List<PropertyValueDisplayPojo>> propertyValues = Maps.newConcurrentMap();
        String channel = searchResult.getParams().getChannel();
        try {
            propertyIdFFC = this.itemIAO.facetField(query, "propertyIds_" + channel);
        } catch (SolrSearchException e) {
            logger.error("facet属性失败, 0x530014", e);
            throw new RuntimeTrackingException(new TrackingException(e, "0x530014"));
        }
        propertyIdFFC.stream().map(FacetField.Count::getName)
                .filter(propertyId -> StringUtils.isNotBlank(propertyId) && !"0".equals(propertyId))
                .forEach(propertyId -> {
                    try {
                        PropertyDisplayPojo propertyDisplayPojo = this.propertyService.restoreProperty(propertyId);
                        if (propertyDisplayPojo != null) {
                            properties.add(propertyDisplayPojo);
                        }
                    } catch (Exception e) {
                        logger.error("从ES恢复属性[{}]失败, 0x530015", propertyId, e);
                        throw new RuntimeTrackingException(new TrackingException(e, "0x530015"));
                    }
                });
        try {
            propertyValueIdFFCs = this.itemIAO.facetPropertyValueIds(query, properties, channel);
        } catch (SolrSearchException e) {
            logger.error("facet属性失败, 0x530016", e);
            throw new RuntimeTrackingException(new TrackingException(e, "0x530016"));
        }
        for (String propertyId : propertyValueIdFFCs.keySet()) {
            List<FacetField.Count> counts = propertyValueIdFFCs.get(propertyId);
//                if (counts.size() == 1) {
//                    continue;
//                }
            List<PropertyValueDisplayPojo> values = Lists.newArrayList();
            counts.stream().map(FacetField.Count::getName).filter(
                    propertyValueId -> StringUtils.isNotBlank(propertyValueId) && !"0"
                            .equals(propertyValueId))
                    .forEach(propertyValueId -> {
                        try {
                            PropertyValueDisplayPojo propertyValueDisplayPojo = this.propertyValueService
                                    .restorePropertyValue(propertyValueId);
                            if (propertyValueDisplayPojo != null) {
                                values.add(propertyValueDisplayPojo);
                            }
                        } catch (Exception e) {
                            logger.error("从ES恢复属性值[{}]失败, 0x530017", propertyValueId, e);
                            throw new RuntimeTrackingException(new TrackingException(e, "0x530017"));
                        }
                    });
            if (values.size() > 0) {
                propertyValues.put(propertyId, values);
            }
        }
        properties.sort(new Comparator<PropertyDisplayPojo>() {
            @Override
            public int compare(PropertyDisplayPojo o1, PropertyDisplayPojo o2) {
                return o1.getPropertyOrder() - o2.getPropertyOrder();
            }
        });
        for (PropertyDisplayPojo property : properties) {
            List<PropertyValueDisplayPojo> pvList = propertyValues.get(property.getId());
            if (pvList == null || pvList.size() < 1) {
                continue;
            }
            AvailableDisplayPojo<PropertyValueDisplayPojo> availableProperty = new AvailableDisplayPojo<>();
            availableProperty.setDisplay(property.getDisplay());
            availableProperty.setId(property.getId());
            List<FacetField.Count> pvFFC = propertyValueIdFFCs.get(property.getId());
            Map<String, Long> counts = Maps.newConcurrentMap();
            pvFFC.forEach(count -> counts.put(count.getName(), count.getCount()));
            for (PropertyValueDisplayPojo pv : pvList) {
                Long count = counts.get(pv.getId());
                pv.setFacetCount(count == null ? 0 : count.intValue());
            }
            availableProperty.getAvailables().addAll(pvList);
            searchResult.getFilters().getAvailableProperties().add(availableProperty);
        }
        logger.debug("##doSearch##baseQuery after {}: {}", getClass().getSimpleName(), baseQuery);
        String afterQuery = baseQuery.toString();
        if (!afterQuery.equals(beforeQuery)) {
            logger.error("{}#doSearch() changed baseQuery!");
        }
    }
}
