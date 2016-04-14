package com.wfj.search.online.web.common.pojo;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * 可用的过滤条件
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public class AvailableFilters {
    // 无筛选分类树，仅与query输入相关
    private List<CategoryDisplayPojo> queryMatchedCategoryTree = Collections.synchronizedList(Lists.newArrayList());
    private List<CategoryDisplayPojo> categoryTree = Collections.synchronizedList(Lists.newArrayList());
    private AvailableDisplayPojo<BrandDisplayPojo> availableBrands = new AvailableDisplayPojo<>(); // 可选品牌
    private AvailableDisplayPojo<RangeDisplayPojo> availableRanges = new AvailableDisplayPojo<>(); // 可选价格区间
    private AvailableDisplayPojo<StandardDisplayPojo> availableStandards = new AvailableDisplayPojo<>(); // 可选规格
    private AvailableDisplayPojo<ColorDisplayPojo> availableColors = new AvailableDisplayPojo<>(); // 可选色系
    // 可选属性
    private List<AvailableDisplayPojo<PropertyValueDisplayPojo>> availableProperties = Collections
            .synchronizedList(Lists.newArrayList());
    private AvailableDisplayPojo<TagDisplayPojo> availableTags = new AvailableDisplayPojo<>(); // 可选标签

    public List<CategoryDisplayPojo> getQueryMatchedCategoryTree() {
        return queryMatchedCategoryTree;
    }

    public List<CategoryDisplayPojo> getCategoryTree() {
        return categoryTree;
    }

    public AvailableDisplayPojo<BrandDisplayPojo> getAvailableBrands() {
        return availableBrands;
    }

    public AvailableDisplayPojo<RangeDisplayPojo> getAvailableRanges() {
        return availableRanges;
    }

    public AvailableDisplayPojo<StandardDisplayPojo> getAvailableStandards() {
        return availableStandards;
    }

    public AvailableDisplayPojo<ColorDisplayPojo> getAvailableColors() {
        return availableColors;
    }

    public List<AvailableDisplayPojo<PropertyValueDisplayPojo>> getAvailableProperties() {
        return availableProperties;
    }

    public AvailableDisplayPojo<TagDisplayPojo> getAvailableTags() {
        return availableTags;
    }

    /**
     * 仅用于缓存反序列化，不要手工调用！
     */
    @SuppressWarnings("unused")
    public void setQueryMatchedCategoryTree(List<CategoryDisplayPojo> queryMatchedCategoryTree) {
        this.queryMatchedCategoryTree = queryMatchedCategoryTree;
    }

    /**
     * 仅用于缓存反序列化，不要手工调用！
     */
    @SuppressWarnings("unused")
    public void setCategoryTree(List<CategoryDisplayPojo> categoryTree) {
        this.categoryTree = categoryTree;
    }

    /**
     * 仅用于缓存反序列化，不要手工调用！
     */
    @SuppressWarnings("unused")
    public void setAvailableBrands(AvailableDisplayPojo<BrandDisplayPojo> availableBrands) {
        this.availableBrands = availableBrands;
    }

    /**
     * 仅用于缓存反序列化，不要手工调用！
     */
    @SuppressWarnings("unused")
    public void setAvailableRanges(AvailableDisplayPojo<RangeDisplayPojo> availableRanges) {
        this.availableRanges = availableRanges;
    }

    /**
     * 仅用于缓存反序列化，不要手工调用！
     */
    @SuppressWarnings("unused")
    public void setAvailableStandards(AvailableDisplayPojo<StandardDisplayPojo> availableStandards) {
        this.availableStandards = availableStandards;
    }

    /**
     * 仅用于缓存反序列化，不要手工调用！
     */
    @SuppressWarnings("unused")
    public void setAvailableColors(AvailableDisplayPojo<ColorDisplayPojo> availableColors) {
        this.availableColors = availableColors;
    }

    /**
     * 仅用于缓存反序列化，不要手工调用！
     */
    @SuppressWarnings("unused")
    public void setAvailableProperties(List<AvailableDisplayPojo<PropertyValueDisplayPojo>> availableProperties) {
        this.availableProperties = availableProperties;
    }

    /**
     * 仅用于缓存反序列化，不要手工调用！
     */
    @SuppressWarnings("unused")
    public void setAvailableTags(AvailableDisplayPojo<TagDisplayPojo> availableTags) {
        this.availableTags = availableTags;
    }
}
