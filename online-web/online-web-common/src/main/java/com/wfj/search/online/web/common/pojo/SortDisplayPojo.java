package com.wfj.search.online.web.common.pojo;

/**
 * 排序
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public class SortDisplayPojo extends AbstractDisplayPojo {
    private String id;
    private String display;
    private String field;
    private ORDER order;
    private boolean isDefault = false;
    private boolean selected = false;
    private SortDisplayPojo opposite;

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

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public ORDER getOrder() {
        return order;
    }

    public void setOrder(ORDER order) {
        this.order = order;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public SortDisplayPojo getOpposite() {
        return opposite;
    }

    public void setOpposite(SortDisplayPojo opposite) {
        this.opposite = opposite;
    }

    public static enum ORDER {
        ASC,
        DESC
    }
}
