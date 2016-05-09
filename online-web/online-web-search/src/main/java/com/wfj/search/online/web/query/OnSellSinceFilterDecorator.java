package com.wfj.search.online.web.query;

import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.util.DateUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

/**
 * <p>create at 16-1-16</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("onSellSinceFilterDecorator")
public class OnSellSinceFilterDecorator implements QueryDecorator {
    @Override
    public void decorator(SolrQuery query, SearchParams params) {
        Date dateFrom = params.getDateFrom();
        Date dateTo = params.getDateTo();
        //noinspection Duplicates
        if (dateFrom != null || dateTo != null) {
            try {
                String dateFromStr = (dateFrom != null) ? DateUtils.mjd2UtcStr(dateFrom) : "*";
                String dateToStr = (dateTo != null) ? DateUtils.mjd2UtcStr(dateTo) : "*";
                query.addFilterQuery("onSellSince:[" + dateFromStr + " TO " + dateToStr + "]");
            } catch (ParseException ignored) {
            }
        }
    }
}
