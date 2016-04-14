package com.wfj.search.online.web.service;

import com.wfj.search.online.web.common.pojo.TagDisplayPojo;

/**
 * <p>create at 15-9-15</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public interface ITagService {
    TagDisplayPojo restoreTag(String tagId);
}
