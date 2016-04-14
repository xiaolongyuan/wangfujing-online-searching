package com.wfj.search.online.index.service.impl;

import com.wfj.search.online.index.mapper.SearchConfigMapper;
import com.wfj.search.online.index.service.IEdismaxConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.wfj.search.online.common.constant.ConfigurationParameters.*;
import static com.wfj.search.online.web.common.constant.CacheableAware.*;

/**
 * <p>create at 16-2-26</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service("edismaxConfigService")
public class EdismaxConfigService implements IEdismaxConfigService {
    private static final StringConverter STRING_CONVERTER = new StringConverter();
    private static final IntegerConverter INTEGER_CONVERTER = new IntegerConverter();
    @Autowired
    private SearchConfigMapper searchConfigMapper;
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
            return converter.convert(defaultValue);
        }
        return value;
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_CONFIG_TIE)
    public String getTie() {
        return getConfig(CONFIG_TIE, defaultTie, this.searchConfigMapper, STRING_CONVERTER, "0");
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_CONFIG_QF)
    public String getQf() {
        return getConfig(CONFIG_QF, defaultQf, this.searchConfigMapper, STRING_CONVERTER, "0")
                .replaceAll("\\{channel\\}", "0");
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_CONFIG_QS)
    public int getQs() {
        return getConfig(CONFIG_QS, defaultQs, this.searchConfigMapper, INTEGER_CONVERTER, "0");
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_CONFIG_MM)
    public String getMm() {
        return getConfig(CONFIG_MM, defaultMm, this.searchConfigMapper, STRING_CONVERTER, "0");
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_CONFIG_BQ)
    public String getBq() {
        return getConfig(CONFIG_BQ, defaultBq, this.searchConfigMapper, STRING_CONVERTER, "0");
    }

    private static interface ValueConverter<T> {
        T convert(String stringValue);
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
}
