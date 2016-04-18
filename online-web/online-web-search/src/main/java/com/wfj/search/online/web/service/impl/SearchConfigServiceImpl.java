package com.wfj.search.online.web.service.impl;

import com.google.common.collect.Lists;
import com.wfj.search.online.web.mapper.SearchConfigMapper;
import com.wfj.search.online.web.service.ISearchConfigService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_SEARCH_CONFIG_WEB_ROWS;
import static com.wfj.search.online.common.constant.ConfigurationParameters.*;
import static com.wfj.search.online.web.common.constant.CacheableAware.*;

/**
 * <p>create at 15-9-21</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
@Service("searchConfigService")
public class SearchConfigServiceImpl implements ISearchConfigService {
    private static final Logger logger = LoggerFactory.getLogger(SearchConfigServiceImpl.class);
    @SuppressWarnings("UnusedDeclaration")
    private static final BooleanConverter BOOLEAN_CONVERTER = new BooleanConverter();
    private static final StringConverter STRING_CONVERTER = new StringConverter();
    private static final IntegerConverter INTEGER_CONVERTER = new IntegerConverter();
    private static final StringListConverter STRING_LIST_CONVERTER = new StringListConverter();
    @Autowired
    private SearchConfigMapper searchConfigMapper;
    @Value("${search.config.channel}")
    private String channel;
    @Value("${search.config.default.tie}")
    private String defaultTie;
    @Value("${search.config.default.qf}")
    private String defaultQf;
    @Value("${search.config.default.qs}")
    private String defaultQs;
    @Value("${search.config.default.mm}")
    private String defaultMm;
    @Value("${search.config.default.bq}")
    private String defaultBq;
    @Value("${search.config.webRows}")
    private String webRows;
    @Value("${search.config.wwwLocation}")
    private String wwwLocation;
    @Value("${search.config.searchLocation}")
    private String searchLocation;
    @Value("${search.config.brandLocation}")
    private String brandLocation;
    @Value("${search.config.listLocation}")
    private String listLocation;
    @Value("${search.config.cssHosts}")
    private String cssHosts;
    @Value("${search.config.cssLocationTemplate}")
    private String cssLocationTemplate;
    @Value("${search.config.imageHosts}")
    private String imageHosts;
    @Value("${search.config.imageLocationTemplate}")
    private String imageLocationTemplate;
    @Value("${search.config.jsHosts}")
    private String jsHosts;
    @Value("${search.config.jsLocationTemplate}")
    private String jsLocationTemplate;
    @Value("${search.config.www.jsHosts}")
    private String wwwJsHosts;
    @Value("${search.config.www.jsLocationTemplate}")
    private String wwwJsLocationTemplate;
    @Value("${search.config.default.simpleRows}")
    private String defaultSimpleRows;
    @Value(("${search.pagination.num_display_entries}"))
    private String numDisplayEntries;
    @Value("${search.config.htmlTbarUri}")
    private String htmlTbarUri;
    @Value("${search.config.htmlHeaderUri}")
    private String htmlHeaderUri;
    @Value("${search.config.htmlNavigationUri}")
    private String htmlNavigationUri;
    @Value("${search.config.htmlFooterUri}")
    private String htmlFooterUri;
    @Value("${search.config.itemUrlPrefix}")
    private String itemUrlPrefix;
    @Value("${search.config.itemUrlPostfix}")
    private String itemUrlPostfix;
    @Value("${search.config.mayLikeRows}")
    private String mayLikeRows;
    @Value("${search.config.web.default.site}")
    private String defaultSite;
    @Value("${search.config.web.default.channel}")
    private String defaultChannel;
    @Value("${search.config.new.products.date.from}")
    private String newProductsDateFrom;

    @Override
    public String getChannel() {
        return this.channel;
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_CONFIG_TIE)
    public String getTie() {
        return getConfig(CONFIG_TIE, defaultTie, this.searchConfigMapper, STRING_CONVERTER, this.getChannel());
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_CONFIG_QF)
    public String getQf() {
        return getConfig(CONFIG_QF, defaultQf, this.searchConfigMapper, STRING_CONVERTER, this.getChannel())
                .replaceAll("\\{channel\\}", this.getChannel());
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_CONFIG_QS)
    public int getQs() {
        return getConfig(CONFIG_QS, defaultQs, this.searchConfigMapper, INTEGER_CONVERTER, this.getChannel());
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_CONFIG_MM)
    public String getMm() {
        return getConfig(CONFIG_MM, defaultMm, this.searchConfigMapper, STRING_CONVERTER, this.getChannel());
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_CONFIG_BQ)
    public String getBq() {
        return getConfig(CONFIG_BQ, defaultBq, this.searchConfigMapper, STRING_CONVERTER, this.getChannel());
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_CONFIG_WEB_ROWS)
    public Integer getRows() {
        return getConfig(CONFIG_WEB_ROWS, webRows, this.searchConfigMapper, INTEGER_CONVERTER, this.getChannel());
    }

    @Override
    public String getWwwLocation() {
        return this.wwwLocation;
    }

    @Override
    public String getSearchLocation() {
        return this.searchLocation;
    }

    @Override
    public String getBrandLocation() {
        return this.brandLocation;
    }

    @Override
    public String getListLocation() {
        return this.listLocation;
    }

    @Override
    public List<String> getCssHosts() {
        return STRING_LIST_CONVERTER.convert(this.cssHosts);
    }

    @Override
    public List<String> getImageHosts() {
        return STRING_LIST_CONVERTER.convert(this.imageHosts);
    }

    @Override
    public List<String> getJsHosts() {
        return STRING_LIST_CONVERTER.convert(this.jsHosts);
    }

    @Override
    public List<String> getWwwJsHosts() {
        return STRING_LIST_CONVERTER.convert(this.wwwJsHosts);
    }

    @Override
    public String getCssLocationTemplate() {
        return this.cssLocationTemplate;
    }

    @Override
    public String getImageLocationTemplate() {
        return this.imageLocationTemplate;
    }

    @Override
    public String getJsLocationTemplate() {
        return this.jsLocationTemplate;
    }

    @Override
    public String getWwwJsLocationTemplate() {
        return this.wwwJsLocationTemplate;
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_CONFIG_SIMPLE_ROWS)
    public int getSimpleRows() {
        return getConfig(CONFIG_SIMPLE_ROWS, defaultSimpleRows, this.searchConfigMapper, INTEGER_CONVERTER,
                this.getChannel());
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_PAGINATION_NUM_DISPLAY_ENTRIES)
    public int getNumDisplayEntries() {
        return getConfig(CONFIG_NUM_DISPLAY_ENTRIES, numDisplayEntries, this.searchConfigMapper, INTEGER_CONVERTER,
                this.getChannel());
    }

    @Override
    public String getHtmlTbarUri() {
        return this.htmlTbarUri;
    }

    @Override
    public String getHtmlHeaderUri() {
        return this.htmlHeaderUri;
    }

    @Override
    public String getHtmlNavigationUri() {
        return this.htmlNavigationUri;
    }

    @Override
    public String getHtmlFooterUri() {
        return this.htmlFooterUri;
    }

    @Override
    public String getItemUrlPrefix() {
        return this.itemUrlPrefix;
    }

    @Override
    public String getItemUrlPostfix() {
        return this.itemUrlPostfix;
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_CONFIG_MAY_LIKE_ROWS)
    public Integer getMayLikeRows() {
        return getConfig(CONFIG_MAY_LIKE_ROWS, mayLikeRows, this.searchConfigMapper, INTEGER_CONVERTER, this.getChannel());
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_CONFIG_DEFAULT_WEB_SITE)
    public String getDefaultSite() {
        return getConfig(CONFIG_WEB_DEFAULT_SITE, this.defaultSite, this.searchConfigMapper, STRING_CONVERTER,
                this.getChannel());
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_CONFIG_DEFAULT_WEB_CHANNEL)
    public String getDefaultChannel() {
        return getConfig(CONFIG_WEB_DEFAULT_CHANNEL, this.defaultChannel, this.searchConfigMapper, STRING_CONVERTER,
                this.getChannel());
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_CONFIG_NEW_PRODUCT_DATE_FROM)
    public Date getNewProductsDateFrom() {
        int dateFrom = getConfig(CONFIG_NEW_PRODUCT_DATE_FROM, this.newProductsDateFrom, this.searchConfigMapper,
                INTEGER_CONVERTER, this.getChannel());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -dateFrom);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private static <T> T getConfig(String config, String defaultValue, SearchConfigMapper searchConfigMapper,
            ValueConverter<T> converter, String channel) {
        String configValue;
        T value;
        try {
            configValue = searchConfigMapper.get(config, channel);
            if (configValue == null) {
                throw new Exception("数据库中无配置值");
            }
            value = converter.convert(configValue);
        } catch (Exception e) {
            logger.warn("查询配置失败, 使用编码默认值" + defaultValue, e);
            try {
                searchConfigMapper.save(config, defaultValue, channel);
            } catch (Exception ignored) {
            }
            return converter.convert(defaultValue);
        }
        return value;
    }

    private interface ValueConverter<T> {
        T convert(String stringValue);
    }

    private static class BooleanConverter implements ValueConverter<Boolean> {
        @Override
        public Boolean convert(String stringValue) {
            return Boolean.parseBoolean(stringValue);
        }
    }

    private static class StringConverter implements ValueConverter<String> {
        @Override
        public String convert(String stringValue) {
            return stringValue;
        }
    }

    private static class IntegerConverter implements ValueConverter<Integer> {
        @Override
        public Integer convert(String stringValue) {
            return Integer.valueOf(stringValue);
        }
    }

    private static class StringListConverter implements ValueConverter<List<String>> {
        @Override
        public List<String> convert(String stringValue) {
            if (StringUtils.isBlank(stringValue)) {
                return Lists.newArrayList();
            }
            return Lists.newArrayList(stringValue.trim().split("[,;\\r\\n]+"))
                    .stream().filter(StringUtils::isNotBlank)
                    .map(String::trim)
                    .collect(Collectors.toList());
        }
    }
}
