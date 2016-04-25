package com.wfj.search.online.web.service.impl;

import com.google.common.collect.Lists;
import com.wfj.search.online.web.service.ISearchConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.wfj.search.online.web.common.constant.CacheableAware.VALUE_KEY_SEARCH_CONFIG_NEW_PRODUCT_DATE_FROM;

/**
 * <p>create at 15-9-21</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
@Service("searchConfigService")
public class SearchConfigServiceImpl implements ISearchConfigService {
    private static final StringListConverter STRING_LIST_CONVERTER = new StringListConverter();
    @Value("${search.config.channel}")
    private String channel;
    @Value("${search.config.default.tie}")
    private String tie;
    @Value("${search.config.default.qf}")
    private String qf;
    @Value("${search.config.default.qs}")
    private String qs;
    @Value("${search.config.default.mm}")
    private String mm;
    @Value("${search.config.default.bq}")
    private String bq;
    @Value("${search.config.webRows}")
    private String rows;
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
    private String simpleRows;
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
    public String getTie() {
        return tie;
    }

    @Override
    public String getQf() {
        return qf.replaceAll("\\{channel\\}", this.getChannel());
    }

    @Override
    public int getQs() {
        return Integer.valueOf(qs);
    }

    @Override
    public String getMm() {
        return mm;
    }

    @Override
    public String getBq() {
        return bq;
    }

    @Override
    public Integer getRows() {
        return Integer.valueOf(rows);
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
    public int getSimpleRows() {
        return Integer.valueOf(simpleRows);
    }

    @Override
    public int getNumDisplayEntries() {
        return Integer.valueOf(numDisplayEntries);
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
    public Integer getMayLikeRows() {
        return Integer.valueOf(mayLikeRows);
    }

    @Override
    public String getDefaultSite() {
        return defaultSite;
    }

    @Override
    public String getDefaultChannel() {
        return defaultChannel;
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_CONFIG_NEW_PRODUCT_DATE_FROM)
    public Date getNewProductsDateFrom() {
        int dateFrom = Integer.valueOf(newProductsDateFrom);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -dateFrom);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private static class StringListConverter {
        List<String> convert(String stringValue) {
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
