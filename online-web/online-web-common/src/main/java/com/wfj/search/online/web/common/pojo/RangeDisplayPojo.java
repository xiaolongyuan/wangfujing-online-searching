package com.wfj.search.online.web.common.pojo;

import org.apache.commons.lang3.Validate;

/**
 * 区间
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public class RangeDisplayPojo extends AbstractDisplayPojo {
    private String display;
    private String min;
    private String max;
    private int order;

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = Validate.notBlank(min).trim();
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = Validate.notBlank(max).trim();
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
