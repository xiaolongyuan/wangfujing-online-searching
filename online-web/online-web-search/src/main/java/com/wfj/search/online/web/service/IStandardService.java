package com.wfj.search.online.web.service;

import com.wfj.search.online.web.common.pojo.StandardDisplayPojo;

/**
 * <p>create at 15-9-15</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface IStandardService {
    StandardDisplayPojo restoreStandard(String standardId);
}
