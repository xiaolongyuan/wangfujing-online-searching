package com.wfj.search.online.web.searchTask;

import com.google.common.collect.Lists;
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
import java.util.List;

/**
 * <br/>create at 16-1-16
 *
 * @author liuxh
 * @since 1.0.0
 */
@Component("availableBrandsSearchTask")
public class AvailableBrandsSearchTask implements SearchTask {
    private static final Logger logger = LoggerFactory.getLogger(AvailableBrandsSearchTask.class);
    @Autowired
    private IItemIAO itemIAO;
    @Autowired
    private IBrandService brandService;

    @Override
    public void doSearch(SearchResult searchResult, SolrQuery baseQuery) {
        logger.debug("##doSearch##baseQuery before {}: {}", getClass().getSimpleName(), baseQuery);
        String beforeQuery = baseQuery.toString();
        SolrQuery query = baseQuery.getCopy();
        String channel = searchResult.getParams().getChannel();
        boolean facetBrandCategories = searchResult.getParams().isFacetBrandCategories();
        final List<BrandDisplayPojo> brands = Collections.synchronizedList(Lists.newArrayList());
        List<FacetField.Count> brandIdFFC;
        try {
            brandIdFFC = this.itemIAO.facetField(query, "brandId");
            brandIdFFC.stream().map(FacetField.Count::getName)
                    .filter(brandId -> StringUtils.isNotBlank(brandId) && !"0".equals(brandId.trim()))
                    .forEach(brandId -> {
                        BrandDisplayPojo brandDisplayPojo;
                        try {
                            brandDisplayPojo = this.brandService.restoreBrand(brandId);
                        } catch (Exception e) {
                            logger.error("从ES恢复品牌[{}]失败, 0x530031", brandId, e);
                            throw new RuntimeTrackingException(new TrackingException(e, "0x530031"));
                        }
                        if (brandDisplayPojo != null) {
                            brands.add(brandDisplayPojo);
                            if (facetBrandCategories) {
                                SolrQuery brandQuery = query.getCopy().addFilterQuery("brandId:" + brandId);
                                List<FacetField.Count> brandCategoriesFFC = null;
                                try {
                                    brandCategoriesFFC = this.itemIAO
                                            .facetField(brandQuery, "allLevelCategoryIds_" + channel);
                                } catch (SolrSearchException e) {
                                    logger.error("facet品牌下分类失败", e);
                                }
                                if (brandCategoriesFFC != null) {
                                    List<String> availableCategoryIds = brandDisplayPojo.getAvailableCategoryIds();
                                    brandCategoriesFFC.stream().map(FacetField.Count::getName)
                                            .filter(cid -> StringUtils.isNoneBlank(cid) && !"0".endsWith(cid.trim()))
                                            .forEach(availableCategoryIds::add);
                                }
                            }
                        }
                    });
        } catch (SolrSearchException e) {
            logger.error("facet品牌失败, 0x530030", e);
            throw new RuntimeTrackingException(new TrackingException(e, "0x530030"));
        }
        List<BrandDisplayPojo> availableBrands = searchResult.getFilters().getAvailableBrands().getAvailables();
        availableBrands.addAll(brands);
        logger.debug("##doSearch##baseQuery after {}: {}", getClass().getSimpleName(), baseQuery);
        String afterQuery = baseQuery.toString();
        if (!afterQuery.equals(beforeQuery)) {
            logger.error("{}#doSearch() changed baseQuery!");
        }
    }
}
