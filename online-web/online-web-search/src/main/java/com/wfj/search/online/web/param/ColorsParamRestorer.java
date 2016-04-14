package com.wfj.search.online.web.param;

import com.google.common.collect.Lists;
import com.wfj.search.online.web.common.pojo.ColorDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.service.IColorService;
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
@Component("colorsParamRestorer")
public class ColorsParamRestorer implements SearchParamRestorer<String> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IColorService colorService;

    @Override
    public void restore(SearchParams searchParams, String colorIds) {
        if (StringUtils.isBlank(colorIds) || "0".equals(colorIds.trim())) {
            return;
        }
        List<String> colorIdList = Lists.newArrayList(colorIds.split("_"))
                .stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        if (colorIdList.isEmpty()) {
            return;
        }
        try {
            colorIdList.stream()
                    .filter(colorId -> StringUtils.isNotBlank(colorId) && !"0".equals(colorId.trim()))
                    .forEach(colorId -> {
                        ColorDisplayPojo colorDisplayPojo = this.colorService.restoreColor(colorId);
                        if (colorDisplayPojo != null) {
                            searchParams.getSelectedColors().getSelected().add(colorDisplayPojo);
                        }
                    });
        } catch (Exception e) {
            String msg = "恢复色系失败";
            logger.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
