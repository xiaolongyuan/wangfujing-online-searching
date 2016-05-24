package com.wfj.search.online.index.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.solr.client.solrj.beans.Field;

/**
 * 索引用商品分类POJO
 * <br/>create at 15-7-16
 *
 * @author liufl
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryIndexPojo {
    @Field("categoryId")
    private String categoryId;
    @Field("categoryName")
    private String categoryName;
    @Field("leafLevel")
    private boolean leafLevel;
    @Field("selfBuilt")
    private boolean selfBuilt;
    @Field("rootCategoryId")
    private String rootCategoryId;
    @Field("parentCategoryId")
    private String parentCategoryId;
    @Field("level")
    private int level;
    @Field("order")
    private int order;
    @Field("channel")
    private String channel;
    @Field("operationSid")
    private Long operationSid;

    private CategoryIndexPojo parent;

    /**
     * 分类编码
     *
     * @return 分类编码
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * 分类编码
     *
     * @param categoryId 分类编码
     */
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 分类名称
     *
     * @return 分类名称
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * 分类名称
     *
     * @param categoryName 分类名称
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * 是否叶子节点
     *
     * @return 是否叶子节点
     */
    @SuppressWarnings("unused")
    public boolean isLeafLevel() {
        return leafLevel;
    }

    /**
     * 是否叶子节点
     *
     * @param leafLevel 是否叶子节点
     */
    public void setLeafLevel(boolean leafLevel) {
        this.leafLevel = leafLevel;
    }

    /**
     * 是否自建分类
     *
     * @return 是否自建分类
     */
    @SuppressWarnings("unused")
    public boolean isSelfBuilt() {
        return selfBuilt;
    }

    /**
     * 是否自建分类
     *
     * @param selfBuilt 是否自建分类
     */
    public void setSelfBuilt(boolean selfBuilt) {
        this.selfBuilt = selfBuilt;
    }

    /**
     * 所属顶级分类编码
     *
     * @return 所属顶级分类编码
     */
    @SuppressWarnings("unused")
    public String getRootCategoryId() {
        return rootCategoryId;
    }

    /**
     * 所属顶级分类编码
     *
     * @param rootCategoryId 所属顶级分类编码
     */
    public void setRootCategoryId(String rootCategoryId) {
        this.rootCategoryId = rootCategoryId;
    }

    /**
     * 所属父级分类编码
     *
     * @return 所属父级分类编码
     */
    public String getParentCategoryId() {
        return parentCategoryId;
    }

    /**
     * 所属父级分类编码
     *
     * @param parentCategoryId 所属父级分类编码
     */
    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    /**
     * 分类层级
     *
     * @return 分类层级
     */
    public int getLevel() {
        return level;
    }

    /**
     * 分类层级
     *
     * @param level 分类层级
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * 同级展示顺序
     *
     * @return 同级展示顺序
     */
    public int getOrder() {
        return order;
    }

    /**
     * 同级展示顺序
     *
     * @param order 同级展示顺序
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * 所属渠道编码
     *
     * @return 所属渠道编码
     */
    public String getChannel() {
        return channel;
    }

    /**
     * 所属渠道编码
     *
     * @param channel 所属渠道编码
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * 写入操作记录号
     *
     * @return 写入操作记录号
     */
    public Long getOperationSid() {
        return operationSid;
    }

    /**
     * 写入操作记录号
     *
     * @param operationSid 写入操作记录号
     */
    public void setOperationSid(Long operationSid) {
        this.operationSid = operationSid;
    }

    /**
     * 判断父级分类编号是否有效：当父级编号是null或0时无效
     *
     * @param parentCategoryId 父级分类编号
     * @return 有效性
     */
    @SuppressWarnings("unused")
    public static boolean isValid4ParentCategory(String parentCategoryId) {
        return !(parentCategoryId == null || "0".equals(parentCategoryId));
    }

    public CategoryIndexPojo getParent() {
        return parent;
    }

    public void setParent(CategoryIndexPojo parent) {
        this.parent = parent;
    }
}
