package com.wfj.search.online.web.common.pojo;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 品牌
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public class BrandDisplayPojo extends AbstractDisplayPojo {
    private String id;
    private String display;
    private String desc;
    private String logo;
    private String picture;
    private List<String> availableCategoryIds = Lists.newArrayList();

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<String> getAvailableCategoryIds() {
        return availableCategoryIds;
    }

    @SuppressWarnings("unused")
    public void setAvailableCategoryIds(List<String> availableCategoryIds) {
        this.availableCategoryIds = availableCategoryIds;
    }
}
