package com.wfj.search.online.web.mapper;

import com.wfj.search.online.web.common.pojo.UrlCodePojo;

/**
 * <p>create at 15-11-16</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface UrlCodeMapper {
    Long getCodeOfUrl(String url);
    void createCode(UrlCodePojo pojo);
}
