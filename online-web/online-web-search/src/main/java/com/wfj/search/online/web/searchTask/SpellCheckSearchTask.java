package com.wfj.search.online.web.searchTask;

import com.wfj.search.online.common.RuntimeTrackingException;
import com.wfj.search.online.common.TrackingException;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.iao.IItemIAO;
import com.wfj.search.online.web.iao.SolrSearchException;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.SpellingParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>create at 15-12-17</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("spellCheckSearchTask")
public class SpellCheckSearchTask implements SearchTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IItemIAO itemIAO;

    @Override
    public void doSearch(SearchResult searchResult, SolrQuery baseQuery) {
        logger.debug("##doSearch##baseQuery before {}: {}", getClass().getSimpleName(), baseQuery);
        String beforeQuery = baseQuery.toString();
        SolrQuery query = new SolrQuery("NNNNNEEEEEVVVVVEEEEERRRRR").setStart(0).setRows(0);
        query.set("spellcheck", true)
                .set(SpellingParams.SPELLCHECK_COUNT, 5)
                .set(SpellingParams.SPELLCHECK_Q, searchResult.getParams().getQ());
        try {
            this.itemIAO.spellCheckSuggest(query, searchResult);
        } catch (SolrSearchException e) {
            logger.error("拼写查检失败, 0x530060", e);
            throw new RuntimeTrackingException(new TrackingException(e, "0x530060"));
        }
        logger.debug("##doSearch##baseQuery after {}: {}", getClass().getSimpleName(), baseQuery);
        String afterQuery = baseQuery.toString();
        if (!afterQuery.equals(beforeQuery)) {
            logger.error("{}#doSearch() changed baseQuery!");
        }
    }
}
