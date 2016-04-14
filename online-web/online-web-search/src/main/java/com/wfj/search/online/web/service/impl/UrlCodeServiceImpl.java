package com.wfj.search.online.web.service.impl;

import com.wfj.search.online.web.common.pojo.UrlCodePojo;
import com.wfj.search.online.web.mapper.UrlCodeMapper;
import com.wfj.search.online.web.service.IUrlCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_URL_CODE;

/**
 * <p>create at 15-11-16</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service("urlCodeService")
public class UrlCodeServiceImpl implements IUrlCodeService {
    @Autowired
    private UrlCodeMapper urlCodeMapper;

    @Override
    @Cacheable(value = VALUE_KEY_URL_CODE)
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public Long getCodeOfUrl(String url) {
        if (url == null) {
            return null;
        }
        Long codeOfUrl = this.urlCodeMapper.getCodeOfUrl(url);
        if (codeOfUrl != null) {
            return codeOfUrl;
        }
        UrlCodePojo pojo = new UrlCodePojo();
        pojo.setUrl(url);
        this.urlCodeMapper.createCode(pojo);
        return pojo.getSid();
    }
}
