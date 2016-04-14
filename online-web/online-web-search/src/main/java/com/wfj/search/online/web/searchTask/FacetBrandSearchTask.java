package com.wfj.search.online.web.searchTask;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wfj.search.online.common.RuntimeTrackingException;
import com.wfj.search.online.common.TrackingException;
import com.wfj.search.online.web.common.pojo.BrandDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.iao.IItemIAO;
import com.wfj.search.online.web.iao.SolrSearchException;
import com.wfj.search.online.web.service.IBrandService;
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
@Component("facetBrandSearchTask")
public class FacetBrandSearchTask implements SearchTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IItemIAO itemIAO;
    @Autowired
    private IBrandService brandService;

    @Override
    public void doSearch(SearchResult searchResult, SolrQuery baseQuery) {
        logger.debug("##doSearch##baseQuery before {}: {}", getClass().getSimpleName(), baseQuery);
        String beforeQuery = baseQuery.toString();
        if (searchResult.getParams().getSelectedBrands().getSelected().isEmpty()) {
            SolrQuery query = baseQuery.getCopy();
            final List<BrandDisplayPojo> brands = Collections.synchronizedList(Lists.newArrayList());
            List<FacetField.Count> brandIdFFC;
            try {
                brandIdFFC = this.itemIAO.facetField(query, "brandId");
//                if (brandIdFFC.size() <= 1) {
//                    return;
//                }
                brandIdFFC.stream().map(FacetField.Count::getName)
                        .filter(brandId -> StringUtils.isNotBlank(brandId) && !"0".equals(brandId.trim()))
                        .forEach(brandId -> {
                            try {
                                BrandDisplayPojo brandDisplayPojo = this.brandService.restoreBrand(brandId);
                                if (brandDisplayPojo != null) {
                                    brands.add(brandDisplayPojo);
                                }
                            } catch (Exception e) {
                                logger.error("从ES恢复品牌[{}]失败, 0x530040", brandId, e);
                                throw new RuntimeTrackingException(new TrackingException(e, "0x530040"));
                            }
                        });
            } catch (SolrSearchException e) {
                logger.error("facet品牌失败, 0x530039", e);
                throw new RuntimeTrackingException(new TrackingException(e, "0x530039"));
            }
            Map<String, Long> counts = Maps.newHashMap();
            for (FacetField.Count count : brandIdFFC) {
                counts.put(count.getName(), count.getCount());
            }
            brands.forEach(brand -> {
                Long count = counts.get(brand.getId());
                brand.setFacetCount(count == null ? 0 : count.intValue());
            });
            List<BrandDisplayPojo> availableBrands = searchResult.getFilters().getAvailableBrands().getAvailables();
            availableBrands.addAll(brands);
            availableBrands.sort(new Comparator<BrandDisplayPojo>() {
                @Override
                public int compare(BrandDisplayPojo o1, BrandDisplayPojo o2) {
                    return o2.getFacetCount() - o1.getFacetCount();
                }
            });
        }
        logger.debug("##doSearch##baseQuery after {}: {}", getClass().getSimpleName(), baseQuery);
        String afterQuery = baseQuery.toString();
        if (!afterQuery.equals(beforeQuery)) {
            logger.error("{}#doSearch() changed baseQuery!");
        }
    }
}
