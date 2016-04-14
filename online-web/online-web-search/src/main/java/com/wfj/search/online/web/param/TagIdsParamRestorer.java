package com.wfj.search.online.web.param;

import com.google.common.collect.Lists;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.TagDisplayPojo;
import com.wfj.search.online.web.service.ITagService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>create at 15-11-4</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("tagIdsParamRestorer")
public class TagIdsParamRestorer implements SearchParamRestorer<String> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ITagService tagService;

    @Override
    public void restore(SearchParams searchParams, String tagIds) {
        if (StringUtils.isBlank(tagIds) || "0".equals(tagIds.trim())) {
            return;
        }
        List<String> tagIdList = Lists.newArrayList(tagIds.split("_"))
                .stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        try {
            tagIdList.stream()
                    .filter(tagId -> StringUtils.isNotBlank(tagId) && !"0".equals(tagId.trim()))
                    .forEach(tagId -> {
                        TagDisplayPojo tagDisplayPojo = this.tagService.restoreTag(tagId);
                        if (tagDisplayPojo != null) {
                            searchParams.getSelectedTags().getSelected().add(tagDisplayPojo);
                        }
                    });
        } catch (Exception e) {
            String msg = "恢复Tag失败";
            logger.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
