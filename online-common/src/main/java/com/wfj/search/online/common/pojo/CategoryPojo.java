package com.wfj.search.online.common.pojo;

/**
 * 分类信息POJO
 * <br/>create at 15-6-29
 *
 * @author liufl
 * @since 1.0.0
 */
public class CategoryPojo {
    private String categoryId;
    private String categoryName;
    private boolean leafLevel = false;
    private boolean selfBuilt;
    private String rootCategoryId;
    private String parentCategoryId;
    private int level;
    private int order;
    private String channel;

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
     * 分类名
     *
     * @return 分类名
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * 分类名
     *
     * @param categoryName 分类名
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * 是否叶子分类
     *
     * @return {@code true} 是，不能再挂子级分类； {@code false} 不是，可以有子级分类
     */
    public boolean isLeafLevel() {
        return leafLevel;
    }

    /**
     * 设置是否叶子分类
     *
     * @param leafLevel {@code true} 是，不能再挂子级分类； {@code false} 不是，可以有子级分类
     */
    public void setLeafLevel(boolean leafLevel) {
        this.leafLevel = leafLevel;
    }

    /**
     * 是否自建分类
     *
     * @return {@code true} 自建分类； {@code false} 非自建分类
     */
    public boolean isSelfBuilt() {
        return selfBuilt;
    }

    /**
     * 设置是否自建分类
     *
     * @param selfBuilt {@code true} 自建分类； {@code false} 非自建分类
     */
    public void setSelfBuilt(boolean selfBuilt) {
        this.selfBuilt = selfBuilt;
    }

    /**
     * 顶级分类编码
     *
     * @return 顶级分类编码
     */
    public String getRootCategoryId() {
        return rootCategoryId;
    }

    /**
     * 顶级分类编码
     *
     * @param rootCategoryId 顶级分类编码
     */
    public void setRootCategoryId(String rootCategoryId) {
        this.rootCategoryId = rootCategoryId;
    }

    /**
     * 父级分类编码
     *
     * @return 父级分类编码
     */
    public String getParentCategoryId() {
        return parentCategoryId;
    }

    /**
     * 父级分类编码
     *
     * @param parentCategoryId 父级分类编码
     */
    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    /**
     * 分类等级
     *
     * @return 分类等级
     */
    public int getLevel() {
        return level;
    }

    /**
     * 分类等级
     *
     * @param level 分类等级
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * 同级别展示顺序
     *
     * @return 同级别展示顺序
     */
    public int getOrder() {
        return order;
    }

    /**
     * 同级别展示顺序
     *
     * @param order 同级别展示顺序
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
}
