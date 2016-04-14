package com.wfj.search.online.web.param;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wfj.search.online.web.common.pojo.PropertyDisplayPojo;
import com.wfj.search.online.web.common.pojo.PropertyValueDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.service.IPropertyService;
import com.wfj.search.online.web.service.IPropertyValueService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 处理属性
 * <pre>
 * attrs参数是属性/属性值组合字符串：以";"分割不同属性，属性ID和属性值串以":"分割，属性值串通过"_"分割。
 * 如：10001:10001001_10001002_10001003;10002:10002001_10002002
 * </pre>
 * <p>create at 15-11-4</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("propsParamRestorer")
public class PropsParamRestorer implements SearchParamRestorer<String> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IPropertyService propertyService;
    @Autowired
    private IPropertyValueService propertyValueService;

    @Override
    public void restore(SearchParams searchParams, String attrs) {
        if (StringUtils.isBlank(attrs) || "0".equals(attrs.trim())) {
            return;
        }
        List<String> propertyIds = Lists.newArrayList();
        Map<String, List<String>> pvIdMap = Maps.newHashMap();
        Exception exception = null;
        String msg = null;
        try {
            String[] attrArray = attrs.split(",");
            for (String attr : attrArray) {
                String[] kv = attr.split(":");
                propertyIds.add(kv[0]);
                pvIdMap.put(
                        kv[0], Lists.newArrayList(kv[1].split("_"))
                                .stream().filter(StringUtils::isNotBlank).collect(Collectors.toList())
                );
            }
            List<PropertyDisplayPojo> props = Lists.newArrayList();
            propertyIds.stream()
                    .filter(propertyId -> StringUtils.isNotBlank(propertyId) && !"0".equals(propertyId))
                    .forEach(propertyId -> {
                        PropertyDisplayPojo propertyDisplayPojo = this.propertyService.restoreProperty(propertyId);
                        if (propertyDisplayPojo != null) {
                            props.add(propertyDisplayPojo);
                        }
                    });
            for (PropertyDisplayPojo prop : props) {
                List<String> valueIds = pvIdMap.get(prop.getId());
                List<PropertyValueDisplayPojo> values = Lists.newArrayList();
                valueIds.stream()
                        .filter(propertyValueId -> StringUtils.isNotBlank(propertyValueId) && !"0"
                                .equals(propertyValueId))
                        .forEach(propertyValueId -> {
                            PropertyValueDisplayPojo propertyValueDisplayPojo = this.propertyValueService
                                    .restorePropertyValue(propertyValueId);
                            if (propertyValueDisplayPojo != null) {
                                values.add(propertyValueDisplayPojo);
                            }
                        });
                prop.getValues().addAll(values);
                searchParams.getSelectedProperties().add(prop);
            }
        } catch (Exception e) {
            msg = "恢复属性失败";
            exception = e;
        }
        if (exception != null) {
            logger.error(msg, exception);
            throw new RuntimeException(msg, exception);
        }
    }
}
