package com.wfj.search.online.web.searchTask;

import com.wfj.search.online.common.RuntimeTrackingException;
import com.wfj.search.online.common.TrackingException;
import com.wfj.search.online.common.pojo.interval.IntervalContentPojo;
import com.wfj.search.online.common.pojo.interval.IntervalDetailPojo;
import com.wfj.search.online.web.common.pojo.RangeDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.iao.IItemIAO;
import com.wfj.search.online.web.iao.SolrSearchException;
import com.wfj.search.online.web.service.IRangeRuleService;
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
 * @author liuxh
 * @since 1.0.0
 */
@Component("alwaysFacetPriceRangeSearchTask")
public class AlwaysFacetPriceRangeSearchTask extends NewProductsAlwaysFacetSearchTaskBase implements SearchTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IRangeRuleService rangeRuleService;
    @Autowired
    private IItemIAO itemIAO;

    @Override
    public void doSearch(SearchResult searchResult, SolrQuery baseQuery) {
        logger.debug("##doSearch##baseQuery before {}: {}", getClass().getSimpleName(), baseQuery);
        String beforeQuery = baseQuery.toString();
        SolrQuery query = baseFacetUseQuery(baseQuery);
        // 线上区间facet
        assert searchResult != null;
        assert searchResult.getParams() != null;
        String channel = searchResult.getParams().getChannel();
        List<IntervalContentPojo> rules;
        try {
            rules = this.rangeRuleService.listRangeRules(channel);
        } catch (Exception e) {
            logger.error("加载排序规则失败, 0x530079", e);
            throw new RuntimeTrackingException(new TrackingException(e, "0x530079"));
        }
        IntervalContentPojo rule = null;
        for (IntervalContentPojo _rule : rules) {
            if (_rule.isSelected()) {
                rule = _rule;
                break;
            }
        }
        if (rule == null) {
            logger.error("价格区间配置有问题, 0x530080");
            throw new RuntimeTrackingException(new TrackingException("0x530080"));
        }
        doFacetPriceRange(query, searchResult, rule);
        logger.debug("##doSearch##baseQuery after {}: {}", getClass().getSimpleName(), baseQuery);
        String afterQuery = baseQuery.toString();
        if (!afterQuery.equals(beforeQuery)) {
            logger.error("{}#doSearch() changed baseQuery!");
        }
    }

    private void doFacetPriceRange(SolrQuery query, SearchResult searchResult, IntervalContentPojo rule) {
        List<IntervalDetailPojo> ruleDetails;
        try {
            ruleDetails = this.rangeRuleService.listRangeRuleDetails(rule.getSid());
        } catch (Exception e) {
            logger.error("加载排序规则[{}]明细失败, 0x530081", rule.getSid(), e);
            throw new RuntimeTrackingException(new TrackingException(e, "0x530081"));
        }
        List<RangeDisplayPojo> availableRanges;
        try {
            availableRanges = this.itemIAO.facetPriceRange(query, rule, ruleDetails);
        } catch (SolrSearchException e) {
            String msg = "facet" + rule.getShowText() + "区间失败";
            logger.error(msg + ", 0x530082", e);
            throw new RuntimeTrackingException(new TrackingException(e, "0x530082"));
        }
        if (availableRanges != null && availableRanges.size() > 0) {
            searchResult.getFilters().getAvailableRanges().getAvailables().addAll(availableRanges);
        }
    }
}
