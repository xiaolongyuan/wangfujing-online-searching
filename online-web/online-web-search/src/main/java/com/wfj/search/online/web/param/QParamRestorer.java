package com.wfj.search.online.web.param;

import com.wfj.search.online.web.common.pojo.SearchParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("qParamRestorer")
public class QParamRestorer implements SearchParamRestorer<String> {
    @Override
    public void restore(SearchParams searchParams, String inputQuery) {
        searchParams.setInputQuery(inputQuery);
        if (StringUtils.isNotBlank(inputQuery)) {
            searchParams.setQ(inputQuery.trim().replaceAll("\\s+", " "));
        }
    }
}
