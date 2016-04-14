package com.wfj.search.online.web.service.impl;

import com.google.common.collect.Lists;
import com.wfj.search.online.web.iao.IItemIAO;
import com.wfj.search.online.web.iao.SolrSearchException;
import com.wfj.search.online.web.service.ISuggestionKeyWordService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SimpleParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_DEFAULT_SUGGESTION_KEY_WORD;
import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_SUGGESTION_KEY_WORD;

/**
 * <br/>create at 15-12-18
 *
 * @author liuxh
 * @since 1.0.0
 */
@Service("suggestionKeyWordService")
public class SuggestionKeyWordServiceImpl implements ISuggestionKeyWordService {
    private static final Logger logger = LoggerFactory.getLogger(SuggestionKeyWordServiceImpl.class);
    public static final String SUGGESTION_FIELD = "keywords";
    /**
     * 在索引中，单词转拼音匹配到的拼音与单词之间通过6个反引号间隔，比如：西藏 -> xizang``````西藏
     */
    public static final String SEPARATOR_BETWEEN_PY_CN = "``````";
    @Autowired
    private IItemIAO itemIAO;

    @Override
    @Cacheable(VALUE_KEY_DEFAULT_SUGGESTION_KEY_WORD)
    public List<String> prefix() {
        return this.prefix(null);
    }

    @Override
    @Cacheable(VALUE_KEY_SUGGESTION_KEY_WORD)
    public List<String> prefix(String prefix, String... words) {
        StringBuilder qParams = new StringBuilder("*:*");
        if (StringUtils.isBlank(prefix)) {
            qParams.append(" ")
                    .append(SimpleParams.AND_OPERATOR)
                    .append(SUGGESTION_FIELD).append(":").append(prefix);
        }
        for (String word : words) {
            qParams.append(" ")
                    .append(SimpleParams.AND_OPERATOR)
                    .append(SUGGESTION_FIELD).append(":").append(word);
        }
        SolrQuery query = new SolrQuery();
        query.set(CommonParams.Q, qParams.toString());
        List<FacetField.Count> keyWordFFC;
        try {
            keyWordFFC = this.itemIAO.facetField(query, SUGGESTION_FIELD);
            List<String> keywords = Collections.synchronizedList(Lists.newArrayList());
            keyWordFFC.stream()
                    .map(FacetField.Count::getName)
                    .filter(StringUtils::isNotBlank)
                    .forEach(keyword -> {
                        StringBuilder w = new StringBuilder();
                        w.append(StringUtils.join(words, " ")).append(" ");
                        if (keyword.contains(SEPARATOR_BETWEEN_PY_CN)) {
                            String[] pyWord = keyword.split(SEPARATOR_BETWEEN_PY_CN);
                            if (pyWord.length == 2) {
                                w.append(pyWord[1]);
                            } else {
                                return;
                            }
                        } else {
                            w.append(keyword);
                        }
                        keywords.add(w.toString());
                    });
            return keywords;
        } catch (SolrSearchException e) {
            logger.error("facet属性失败", e);
            return Collections.emptyList();
        }
    }
}
