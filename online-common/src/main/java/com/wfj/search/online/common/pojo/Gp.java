package com.wfj.search.online.common.pojo;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * <p>create at 16-5-11</p>
 *
 * @author liufl
 * @since 1.0.36
 */
public class Gp {
    private String gp;
    private String title;
    private List<String> itemIds = Lists.newArrayList();
    private boolean confirmed = false;
    private boolean delete = false;

    public String getGp() {
        return gp;
    }

    public void setGp(String gp) {
        this.gp = gp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getItemIds() {
        return itemIds;
    }

    @SuppressWarnings("unused")
    public void setItemIds(List<String> itemIds) {
        this.itemIds = itemIds;
    }

    @SuppressWarnings("unused")
    public boolean isConfirmed() {
        return confirmed;
    }

    @SuppressWarnings("unused")
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    @SuppressWarnings("unused")
    public boolean isDelete() {
        return delete;
    }

    @SuppressWarnings("unused")
    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}
