package com.wfj.search.online.web.service.impl;

import com.wfj.search.online.index.pojo.TagIndexPojo;
import com.wfj.search.online.web.common.pojo.TagDisplayPojo;
import com.wfj.search.online.web.es.TagEsIao;
import com.wfj.search.online.web.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_TAG;

/**
 * <p>create at 15-9-15</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service("tagService")
public class TagServiceImpl implements ITagService {
    @Autowired
    private TagEsIao tagEsIao;

    @Override
    @Cacheable(VALUE_KEY_TAG)
    public TagDisplayPojo restoreTag(String tagId) {
        TagIndexPojo indexPojo = this.tagEsIao.get(tagId);
        if (indexPojo == null) {
            return null;
        }
        TagDisplayPojo displayPojo = new TagDisplayPojo();
        displayPojo.setId(indexPojo.getTagId());
        displayPojo.setDisplay(indexPojo.getTagName());
        return displayPojo;
    }
}
