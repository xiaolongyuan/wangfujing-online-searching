package com.wfj.search.online.web.searchTask;

import com.google.common.collect.Lists;
import com.wfj.search.online.common.RuntimeTrackingException;
import com.wfj.search.online.common.TrackingException;
import com.wfj.search.online.common.pojo.rule.SortRulePojo;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.common.pojo.SortDisplayPojo;
import com.wfj.search.online.web.param.SortParamRestorer;
import com.wfj.search.online.web.service.ISortService;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>create at 15-11-23</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
@Component("sortsSearchTask")
public class SortsSearchTask implements SearchTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ISortService sortService;

    @Override
    public void doSearch(SearchResult searchResult, SolrQuery baseQuery) {
        logger.debug("##doSearch##baseQuery before {}: {}", getClass().getSimpleName(), baseQuery);
        String beforeQuery = baseQuery.toString();
        List<SortRulePojo> sortRulePojos;
        try {
            sortRulePojos = this.sortService.listAllSort(searchResult.getParams().getChannel());
        } catch (Exception e) {
            logger.error("列出所有分类规则失败, 0x530059", e);
            throw new RuntimeTrackingException(new TrackingException(e, "0x530059"));
        }
        List<SortDisplayPojo> sorts = Lists.newArrayList();
        // 在拼接参数时已经保证当sortId为空或0时，添加默认排序对象
        String sortId = searchResult.getParams().getSort().getId();
        for (SortRulePojo sortRulePojo : sortRulePojos) {
            SortDisplayPojo sortDefault = SortParamRestorer.toDisplayPojo(sortRulePojo);// 获取默认的排序对象
            if (sortId.split("_")[0].equals(sortRulePojo.getSid())) {
                // selected
                if (sortId.equals(sortDefault.getId())) {// 当前选择为该条排序规则中的默认排序
                    sortDefault.setSelected(true);
                    sorts.add(sortDefault);
                } else {// 当前选择为该条排序规则中的非默认排序，即默认排序的相对排序
                    SortDisplayPojo opposite = sortDefault.getOpposite();
                    sortDefault.setOpposite(null);
                    opposite.setOpposite(sortDefault);
                    opposite.setSelected(true);
                    sorts.add(opposite);
                }
            } else {
                // not selected, add default sorting rule
                sorts.add(sortDefault);
            }
        }
        searchResult.getSorts().clear();
        searchResult.getSorts().addAll(sorts);
        logger.debug("##doSearch##baseQuery after {}: {}", getClass().getSimpleName(), baseQuery);
        String afterQuery = baseQuery.toString();
        if (!afterQuery.equals(beforeQuery)) {
            logger.error("{}#doSearch() changed baseQuery!");
        }
    }
}
