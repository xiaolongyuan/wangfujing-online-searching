package com.wfj.search.online.common.pojo;

/**
 * 标签POJO
 * <br/>create at 15-7-2
 *
 * @author liufl
 * @since 1.0.0
 */
public class TagPojo {
    private String tagId;
    private String tag;

    /**
     * 标签ID
     * @return 标签ID
     */
    public String getTagId() {
        return tagId;
    }

    /**
     * 标签ID
     * @param tagId 标签ID
     */
    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    /**
     * 标签内容
     * @return 标签内容
     */
    public String getTag() {
        return tag;
    }

    /**
     * 标签内容
     * @param tag 标签内容
     */
    public void setTag(String tag) {
        this.tag = tag;
    }
}
