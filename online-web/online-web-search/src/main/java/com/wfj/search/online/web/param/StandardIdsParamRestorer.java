package com.wfj.search.online.web.param;

import com.google.common.collect.Lists;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.StandardDisplayPojo;
import com.wfj.search.online.web.service.IStandardService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>create at 15-11-4</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("standardIdsParamRestorer")
public class StandardIdsParamRestorer implements SearchParamRestorer<String> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IStandardService standardService;

    @Override
    public void restore(SearchParams searchParams, String standardIds) {
        if (StringUtils.isBlank(standardIds) || "0".equals(standardIds.trim())) {
            return;
        }
        List<String> standardIdList = Lists.newArrayList(standardIds.split("_"))
                .stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        if (standardIdList.isEmpty()) {
            return;
        }
        try {
            standardIdList.stream()
                    .filter(standardId -> StringUtils.isNotBlank(standardId) && !"0".equals(standardId.trim()))
                    .forEach(standardId -> {
                        try {
                            standardId = URLEncoder.encode(standardId, "UTF-8");
                        } catch (UnsupportedEncodingException ignored) {
                        }
                        StandardDisplayPojo standardDisplayPojo = this.standardService.restoreStandard(standardId);
                        if (standardDisplayPojo != null) {
                            searchParams.getSelectedStandards().getSelected().add(standardDisplayPojo);
                        }
                    });
        } catch (Exception e) {
            String msg = "恢复规格失败";
            logger.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
