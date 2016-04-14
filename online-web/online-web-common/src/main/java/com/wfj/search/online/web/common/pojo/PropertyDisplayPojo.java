package com.wfj.search.online.web.common.pojo;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * 属性
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public class PropertyDisplayPojo extends AbstractDisplayPojo {
    private String id;
    private String display;
    private int propertyOrder;
    private final List<PropertyValueDisplayPojo> values = Collections.synchronizedList(Lists.newArrayList());

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public int getPropertyOrder() {
        return propertyOrder;
    }

    public void setPropertyOrder(int propertyOrder) {
        this.propertyOrder = propertyOrder;
    }

    public List<PropertyValueDisplayPojo> getValues() {
        return values;
    }
}
