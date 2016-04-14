package com.wfj.search.online.web.service.impl;

import com.wfj.search.online.web.mapper.HotWordMapper;
import com.wfj.search.online.web.pojo.HotWord;
import com.wfj.search.online.web.pojo.HotWordsOfChannelPojo;
import com.wfj.search.online.web.service.IHotWordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_HOT_WORDS;
import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_HOT_WORDS_PAGES;

/**
 * <br/>create at 15-12-22
 *
 * @author liuxh
 * @since 1.0.0
 */
@Service("hotWordService")
public class HotWordServiceImpl implements IHotWordService {
    @Autowired
    private HotWordMapper hotWordMapper;

    @Override
    @Cacheable(value = VALUE_KEY_HOT_WORDS, unless = "#result.size() > 0")
    public List<HotWordsOfChannelPojo> listHotWords(String siteId, String channelId) {
        if (StringUtils.isBlank(siteId)) {
            siteId = null;
            channelId = null;
        }
        return hotWordMapper.listHotWords(siteId, channelId);
    }

    @Override
    @Cacheable(VALUE_KEY_HOT_WORDS_PAGES)
    public List<HotWord> listHotWords(String siteId, String channelId, String value, int start, int fetch) {
        if (start < 0) {
            start = 0;
        }
        if (fetch < 0) {
            return Collections.emptyList();
        }
        return hotWordMapper.listHotWordPages(siteId, channelId, value, start, fetch);
    }

    @Override
    @Cacheable(VALUE_KEY_HOT_WORDS_PAGES)
    public int count(String siteId, String channelId, String value) {
        return hotWordMapper.count(siteId, channelId, value);
    }
}
