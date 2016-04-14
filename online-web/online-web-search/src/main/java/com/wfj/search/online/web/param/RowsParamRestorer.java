package com.wfj.search.online.web.param;

import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.service.ISearchConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>create at 15-11-4</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("rowsParamRestorer")
public class RowsParamRestorer implements SearchParamRestorer<Integer> {
    @Autowired
    private ISearchConfigService searchConfigService;
    @Override
    public void restore(SearchParams searchParams, Integer rows) {
        if (rows != null) {
            searchParams.setRows(rows);
        } else {
            searchParams.setRows(this.searchConfigService.getRows());
        }
    }
}
