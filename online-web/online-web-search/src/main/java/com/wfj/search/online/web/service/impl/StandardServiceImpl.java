package com.wfj.search.online.web.service.impl;

import com.wfj.search.online.index.pojo.StandardIndexPojo;
import com.wfj.search.online.web.common.pojo.StandardDisplayPojo;
import com.wfj.search.online.web.es.StandardEsIao;
import com.wfj.search.online.web.service.IStandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_STANDARD;

/**
 * <p>create at 15-9-15</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Service("standardService")
public class StandardServiceImpl implements IStandardService {
    @Autowired
    private StandardEsIao standardEsIao;

    @Override
    @Cacheable(VALUE_KEY_STANDARD)
    public StandardDisplayPojo restoreStandard(String standardId) {
        StandardIndexPojo indexPojo = this.standardEsIao.get(standardId);
        if (indexPojo == null) {
            return null;
        }
        StandardDisplayPojo displayPojo = new StandardDisplayPojo();
        displayPojo.setId(indexPojo.getStandardId());
        displayPojo.setDisplay(indexPojo.getStandardName());
        return displayPojo;
    }
}
