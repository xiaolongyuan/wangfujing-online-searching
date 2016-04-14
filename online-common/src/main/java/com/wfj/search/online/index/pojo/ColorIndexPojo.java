package com.wfj.search.online.index.pojo;

import org.apache.solr.client.solrj.beans.Field;

/**
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class ColorIndexPojo {
    @Field("colorId")
    private String colorId;
    @Field("colorName")
    private String colorName;

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
}
