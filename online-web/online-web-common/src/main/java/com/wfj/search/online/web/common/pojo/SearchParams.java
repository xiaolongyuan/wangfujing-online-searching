package com.wfj.search.online.web.common.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 搜索参数
 * <p>create at 15-11-19</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public class SearchParams {
    private String inputQuery;
    private String q = "*"; // 主查询
    private boolean list = false; // 是否列表页
    private boolean brand = false; // 是否品牌列表页
    private Date dateFrom = null;// 开始时间：用于对时间进行过滤
    private Date dateTo = null;// 结束时间：用于对时间进行过滤，通常情况下，该参数不会赋值
    // 已选分类列表。如果是多个则是按路径排列的各级分类！！注意这不是表示多选参数！！
    private List<CategoryDisplayPojo> selectedCategories = Collections.synchronizedList(Lists.newArrayList());
    private String removeAllNoneCategoryParamUrl; // "全部清除"按钮URL
    private SelectedDisplayPojo<BrandDisplayPojo> selectedBrands = new SelectedDisplayPojo<>();// 品牌
    private RangeDisplayPojo selectedRange;// 价格区间
    private SelectedDisplayPojo<StandardDisplayPojo> selectedStandards = new SelectedDisplayPojo<>();// 规格
    private SelectedDisplayPojo<ColorDisplayPojo> selectedColors = new SelectedDisplayPojo<>();// 颜色
    private List<PropertyDisplayPojo> selectedProperties = Collections
            .synchronizedList(Lists.newArrayList());// 属性
    private SelectedDisplayPojo<TagDisplayPojo> selectedTags = new SelectedDisplayPojo<>();// 标签
    private SortDisplayPojo sort;
    private Integer currentPage = 1;
    private Integer rows = 40;
    private String channel;
    private boolean facetBrandCategories;

    public String getInputQuery() {
        return inputQuery;
    }

    public void setInputQuery(String inputQuery) {
        this.inputQuery = inputQuery;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public boolean isList() {
        return list;
    }

    public void setList(boolean isList) {
        this.list = isList;
    }

    public boolean isBrand() {
        return brand;
    }

    public void setBrand(boolean brand) {
        this.brand = brand;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public List<CategoryDisplayPojo> getSelectedCategories() {
        return selectedCategories;
    }

    public String getRemoveAllNoneCategoryParamUrl() {
        return removeAllNoneCategoryParamUrl;
    }

    public void setRemoveAllNoneCategoryParamUrl(String removeAllNoneCategoryParamUrl) {
        this.removeAllNoneCategoryParamUrl = removeAllNoneCategoryParamUrl;
    }

    public SelectedDisplayPojo<BrandDisplayPojo> getSelectedBrands() {
        return selectedBrands;
    }

    public RangeDisplayPojo getSelectedRange() {
        return selectedRange;
    }

    public SearchParams setSelectedRange(RangeDisplayPojo selectedRange) {
        this.selectedRange = selectedRange;
        return this;
    }

    public SelectedDisplayPojo<StandardDisplayPojo> getSelectedStandards() {
        return selectedStandards;
    }

    public SelectedDisplayPojo<ColorDisplayPojo> getSelectedColors() {
        return selectedColors;
    }

    public List<PropertyDisplayPojo> getSelectedProperties() {
        return selectedProperties;
    }

    public SelectedDisplayPojo<TagDisplayPojo> getSelectedTags() {
        return selectedTags;
    }

    public SortDisplayPojo getSort() {
        return sort;
    }

    public SearchParams setSort(SortDisplayPojo sort) {
        this.sort = Validate.notNull(sort, "排序方式不能为空");
        return this;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public SearchParams setCurrentPage(Integer currentPage) {
        Integer _currentPage = Validate.notNull(currentPage, "页码不能为null");
        if (_currentPage < 1) {
            throw new IllegalArgumentException("页码必须是正整数");
        }
        this.currentPage = _currentPage;
        return this;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        Integer _rows = Validate.notNull(rows, "每页返回数不能为null");
        if (_rows < 1) {
            throw new IllegalArgumentException("每页应至少返回1条");
        }
        this.rows = rows;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public boolean isFacetBrandCategories() {
        return facetBrandCategories;
    }

    public void setFacetBrandCategories(boolean facetBrandCategories) {
        this.facetBrandCategories = facetBrandCategories;
    }

    @JsonIgnore
    @SuppressWarnings("unused")
    public boolean isQueryOnly() {
        if (!selectedCategories.isEmpty()) {
            return false;
        }
        if (!selectedBrands.getSelected().isEmpty()) {
            return false;
        }
        if (selectedRange != null) {
            return false;
        }
        if (!selectedStandards.getSelected().isEmpty()) {
            return false;
        }
        if (!selectedColors.getSelected().isEmpty()) {
            return false;
        }
        if (!selectedProperties.isEmpty()) {
            return false;
        }
        if (!selectedTags.getSelected().isEmpty()) {
            return false;
        }
        if (currentPage > 1) {
            return false;
        }
        return true;
    }

    /**
     * 淺复制
     */
    public SearchParams copy() {
        SearchParams copy = new SearchParams();
        copy.setInputQuery(this.getInputQuery());
        copy.setQ(this.getQ());
        copy.getSelectedCategories().addAll(this.getSelectedCategories());
        copy.setRemoveAllNoneCategoryParamUrl(this.getRemoveAllNoneCategoryParamUrl());
        copy.getSelectedBrands().getSelected().addAll(this.getSelectedBrands().getSelected());
        copy.setSelectedRange(this.getSelectedRange());
        copy.getSelectedStandards().getSelected().addAll(this.getSelectedStandards().getSelected());
        copy.getSelectedColors().getSelected().addAll(this.getSelectedColors().getSelected());
        copy.getSelectedProperties().addAll(this.getSelectedProperties());
        copy.getSelectedTags().getSelected().addAll(this.getSelectedTags().getSelected());
        copy.setSort(this.getSort());
        copy.setCurrentPage(this.getCurrentPage());
        copy.setRows(this.getRows());
        copy.setChannel(this.getChannel());
        copy.setDateFrom(this.getDateFrom());
        copy.setDateTo(this.getDateTo());
        copy.setFacetBrandCategories(this.isFacetBrandCategories());
        return copy;
    }

    /**
     * 根据已有参数拼接url。
     * <p>
     * url格式为：
     * <ul>
     * <li>
     * search.wangfujing.com/search/{inputQuery}/{cat1}-{cat2}-{cat3}-{brandIds}-{price}-{standardIds}-{colors}-{attrs}-{tagIds}-{sortId}-{currentPage}-0
     * </li>
     * <li>
     * list.wangfujing.com/list/{cat1}-{cat2}-{cat3}-{brandIds}-{price}-{standardIds}-{colors}-{attrs}-{tagIds}-{sortId}-{currentPage}-0
     * </li>
     * <li>
     * list.wangfujing.com/{cat1}-{cat2}-{cat3}-{brandIds}-{price}-{standardIds}-{colors}-{attrs}-{tagIds}-{sortId}-{currentPage}-0
     * </li>
     * <li>
     * brand.wangfujing.com/brandlist/{cat1}-{cat2}-{cat3}-{brandIds}-{price}-{standardIds}-{colors}-{attrs}-{tagIds}-{sortId}-{currentPage}-0
     * </li>
     * <li>
     * brand.wangfujing.com/{cat1}-{cat2}-{cat3}-{brandIds}-{price}-{standardIds}-{colors}-{attrs}-{tagIds}-{sortId}-{currentPage}-0
     * </li>
     * </ul>
     * </p>
     *
     * @return 静态链接url公用部分：/{cat1}-{cat2}-{cat3}-{brandIds}-{price}-{standardIds}-{colors}-{attrs}-{tagIds}-{sortId}-{currentPage}-0
     */
    public String toUrl() {
        StringBuilder url = new StringBuilder("/");
        // {cat1}-{cat2}-{cat3}-
        List<CategoryDisplayPojo> selectedCategories = this.getSelectedCategories();
        int ci = 0;
        for (CategoryDisplayPojo sc : selectedCategories) {
            url.append(sc.getId()).append("-");
            ci++;
        }
        for (int i = ci; i < 3; i++) {
            url.append("0-");
        }
        // {brandIds}-
        List<BrandDisplayPojo> selectedBrandList = this.getSelectedBrands().getSelected();
        if (selectedBrandList.isEmpty()) {
            url.append("0-");
        } else {
            url.append(StringUtils.join(
                    selectedBrandList.stream().map(BrandDisplayPojo::getId).collect(Collectors.toList()), "_"
            )).append("-");
        }
        // {price}-
        RangeDisplayPojo range = this.getSelectedRange();
        if (range == null) {
            url.append("0-");
        } else {
            url.append(range.getMin()).append("TO").append(range.getMax()).append("-");
        }
        // {standardIds}-
        List<StandardDisplayPojo> selectedStandardList = this.getSelectedStandards().getSelected();
        if (selectedStandardList.isEmpty()) {
            url.append("0-");
        } else {
            url.append(StringUtils.join(
                    selectedStandardList.stream().map(StandardDisplayPojo::getId).collect(Collectors.toList()), "_"
            )).append("-");
        }
        // {colors}-
        List<ColorDisplayPojo> selectedColorList = this.getSelectedColors().getSelected();
        if (selectedColorList.isEmpty()) {
            url.append("0-");
        } else {
            url.append(StringUtils.join(
                    selectedColorList.stream().map(ColorDisplayPojo::getId).collect(Collectors.toList()), "_"
            )).append("-");
        }
        // {attrs}-
        if (this.getSelectedProperties().isEmpty()) {
            url.append("0-");
        } else {
            // p1:v11_v12,p2:v21
            url.append(StringUtils.join(
                    this.getSelectedProperties().stream().map(selectedProp ->
                            selectedProp.getId() + ":" + StringUtils.join(
                                    selectedProp.getValues().stream().map(PropertyValueDisplayPojo::getId)
                                            .collect(Collectors.toList()), "_"
                            )).collect(Collectors.toList()), ","
            )).append("-");
        }
        // {tagIds}-
        List<TagDisplayPojo> selectedTagList = this.getSelectedTags().getSelected();
        if (selectedTagList.isEmpty()) {
            url.append("0-");
        } else {
            url.append(StringUtils.join(
                    selectedTagList.stream().map(TagDisplayPojo::getId).collect(Collectors.toList()), "_"
            )).append("-");
        }
        // {sortId}-
        if (this.getSort() != null) {
            url.append(this.getSort().getId()).append("-");
        } else {
            url.append(0).append('-');
        }
        // {currentPage}-
        url.append(this.getCurrentPage()).append("-");
        // 0 店铺模式占位
        url.append("0");
        return url.toString();
    }

    @Override
    public String toString() {
        return "SearchParams{" + this.toUrl()
                + "_" + this.getQ()
                + "_" + this.isList()
                + "_" + this.isBrand()
                + "_" + this.getRows()
                + "_" + this.getChannel()
                + ((dateFrom == null) ? "" : "_" + dateFrom.getTime())
                + ((dateTo == null ) ? "" : "_" + dateTo.getTime())
                + this.isFacetBrandCategories()
                + "}";
    }

    @SuppressWarnings("unused")
    public SearchParams setSelectedCategories(List<CategoryDisplayPojo> selectedCategories) {
        this.selectedCategories = selectedCategories;
        return this;
    }

    public SearchParams setSelectedBrands(SelectedDisplayPojo<BrandDisplayPojo> selectedBrands) {
        this.selectedBrands = selectedBrands;
        return this;
    }

    public SearchParams setSelectedStandards(SelectedDisplayPojo<StandardDisplayPojo> selectedStandards) {
        this.selectedStandards = selectedStandards;
        return this;
    }

    public SearchParams setSelectedColors(SelectedDisplayPojo<ColorDisplayPojo> selectedColors) {
        this.selectedColors = selectedColors;
        return this;
    }

    public SearchParams setSelectedProperties(List<PropertyDisplayPojo> selectedProperties) {
        this.selectedProperties = selectedProperties;
        return this;
    }

    public SearchParams setSelectedTags(SelectedDisplayPojo<TagDisplayPojo> selectedTags) {
        this.selectedTags = selectedTags;
        return this;
    }
}
