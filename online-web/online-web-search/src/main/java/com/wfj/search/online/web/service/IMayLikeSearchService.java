package com.wfj.search.online.web.service;

import com.wfj.search.online.web.common.pojo.SpuDisplayPojo;

import java.util.List;

/**
 * <br/>create at 16-1-12
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface IMayLikeSearchService {
    List<SpuDisplayPojo> randomSearch(String inputQuery, String categories);
}
