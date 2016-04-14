package com.wfj.search.online.web.common.pojo;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * 分类
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public class CategoryDisplayPojo extends AbstractDisplayPojo {
    private String id;
    private String display;
    private String parentId;
    private int order;
    private final List<CategoryDisplayPojo> children = Collections.synchronizedList(Lists.newArrayList());
    private CategoryDisplayPojo parent;
    private boolean root;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List<CategoryDisplayPojo> getChildren() {
        return children;
    }

    public CategoryDisplayPojo getParent() {
        return parent;
    }

    public void setParent(CategoryDisplayPojo parent) {
        this.parent = parent;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }
}
