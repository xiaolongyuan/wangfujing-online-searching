package com.wfj.search.online.web.param;

import com.wfj.search.online.web.common.pojo.RangeDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 处理价格区间，并将处理结果填充到搜索条件封装对象。
 * <p>
 * priceRangeParam参数格式为 [double|*]TO[double|*]，示例：
 * <ul>
 * <li>*TO1000</li>
 * <li>1000TO2000</li>
 * <li>2000TO*</li>
 * </ul>
 * </p>
 * <p>create at 15-11-4</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("priceRangeParamRestorer")
public class PriceRangeParamRestorer implements SearchParamRestorer<String> {
    @Override
    public void restore(SearchParams searchParams, String price) {
        if (StringUtils.isBlank(price) || "0".equals(price.trim())) {
            return;
        }
        String[] priceRange = price.split("TO");
        if (priceRange.length != 2) {
            throw new IllegalArgumentException("价格区间格式非法");
        }
        RangeDisplayPojo range = new RangeDisplayPojo();
        range.setMin(priceRange[0]);
        range.setMax(priceRange[1]);
        if ("*".equals(range.getMin()) && "*".equals(range.getMax())) {
            return;
        }
        if ("*".equals(range.getMin())) {
            range.setDisplay(range.getMax() + "以下");
        } else if ("*".equals(range.getMax())) {
            range.setDisplay(range.getMin() + "以上");
        } else {
            range.setDisplay(range.getMin() + "-" + range.getMax());
        }
        searchParams.setSelectedRange(range);
    }
}
