package com.wfj.search.online.web.service.impl;

import com.wfj.search.online.web.service.IHtmlCommonPartService;
import com.wfj.search.utils.http.OkHttpOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_SEARCH_WEB_HTML_COMMON_PART;

/**
 * <p>create at 15-12-28</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service
public class HtmlCommonPartService implements IHtmlCommonPartService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private OkHttpOperator okHttpOperator;

    @Override
    @Cacheable(VALUE_KEY_SEARCH_WEB_HTML_COMMON_PART)
    public String htmlContent(String url) {
        try {
            return this.okHttpOperator.getForTextResp(url);
        } catch (Exception e) {
            logger.error("获取公共Html块失败,{}", url, e);
            return "";
        }
    }
}
