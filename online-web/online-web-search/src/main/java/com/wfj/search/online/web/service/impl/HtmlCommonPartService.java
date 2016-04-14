package com.wfj.search.online.web.service.impl;

import com.wfj.platform.util.httpclient.HttpRequestException;
import com.wfj.platform.util.httpclient.HttpRequester;
import com.wfj.search.online.web.service.IHtmlCommonPartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

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

    @Override
    @Cacheable(VALUE_KEY_SEARCH_WEB_HTML_COMMON_PART)
    public String htmlContent(String url) {
        try {
            String content = HttpRequester.getSimpleHttpRequester().httpGetString(url);
            return content == null ? "" : content;
        } catch (IOException | HttpRequestException | URISyntaxException e) {
            logger.error("获取公共Html块失败,{}", url, e);
            return "";
        }
    }
}
