package com.wfj.search.online.web.query;

import com.google.common.collect.Lists;
import com.wfj.platform.util.DateUtils;
import com.wfj.search.online.web.common.pojo.*;
import com.wfj.search.online.web.controller.ResourceNotFoundException;
import com.wfj.search.online.web.service.ISearchConfigService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.DisMaxParams;
import org.apache.solr.common.params.GroupParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>create at 15-11-4</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
@Component("baseMainQueryDecorator")
public class BaseMainQueryDecorator implements QueryDecorator {
    @Autowired
    private ISearchConfigService searchConfigService;

    @Override
    public void decorator(SolrQuery query, SearchParams searchParams) {
        // 基础参数
        SortDisplayPojo sort = searchParams.getSort();
        query
                .setSorts(Lists.newArrayList(
                        SolrQuery.SortClause.create(sort.getField(), sort.getOrder().name().toLowerCase())))
                .setStart((searchParams.getCurrentPage() - 1) * searchParams.getRows())
                .setRows(searchParams.getRows());
        String channel = searchParams.getChannel();
        query
                .addFilterQuery("channels:" + channel)
                .addFilterQuery("allLevelCategoryIds_" + channel + ":*");
        // eDisMax参数
        query
                .setQuery(searchParams.getQ())
                .set("defType", "edismax")
                .set(DisMaxParams.QF, this.searchConfigService.getQf())
                .set(DisMaxParams.MM, this.searchConfigService.getMm())
                .set(DisMaxParams.QS, this.searchConfigService.getQs())
                .set(DisMaxParams.TIE, this.searchConfigService.getTie())
                .set(DisMaxParams.BQ, this.searchConfigService.getBq());
        List<CategoryDisplayPojo> selectedCategories = searchParams.getSelectedCategories();
        if (searchParams.isBrand()) {
            query.set("boost", "popSpotWeightBrand_" + channel);
        } else if (searchParams.isList()) {
            if (!selectedCategories.isEmpty()) {
                query.set("boost",
                        "popSpotWeightCategory_" + selectedCategories.get(selectedCategories.size() - 1).getId());
            } else {
                throw new ResourceNotFoundException();
            }
        } else {
            query.set("boost", "boost_" + channel);
        }
        // group参数
        query
                .set(GroupParams.GROUP, true)
                .set(GroupParams.GROUP_FIELD, "spuId")
                        // group返回数据数量，默认为1。根据spuId分组，恢复spu数据只取第一个item数据，所以最少返回1条即可。
                        // .set(GroupParams.GROUP_LIMIT, 1000)
                .set(GroupParams.GROUP_TOTAL_COUNT, true)
                .set(GroupParams.GROUP_FACET, true)
                .set(GroupParams.GROUP_TRUNCATE, true);
        if ("currentPrice".equals(sort.getField()) && SortDisplayPojo.ORDER.DESC == sort.getOrder()) {
            query.set(GroupParams.GROUP_SORT, "currentPrice desc");
        } else {
            query.set(GroupParams.GROUP_SORT, "currentPrice asc, " + sort.getField() + " " + sort.getOrder().name());
        }
        // 请求参数参数
        if (!selectedCategories.isEmpty()) {
            // 必须满足所有分类ID筛选
            for (CategoryDisplayPojo cat : selectedCategories) {
                query.addFilterQuery(
                        "allLevelCategoryIds_" + channel + ":" + cat.getId());
            }
        }
        if (!searchParams.getSelectedBrands().getSelected().isEmpty()) {
            List<String> brandIds = searchParams.getSelectedBrands().getSelected()
                    .stream().map(BrandDisplayPojo::getId).collect(Collectors.toList());
            query.addFilterQuery("brandId:(" + StringUtils.join(brandIds, " ") + ")");
        }
        RangeDisplayPojo selectedRange = searchParams.getSelectedRange();
        if (selectedRange != null) {
            String prs = "[" + selectedRange.getMin() + " TO " + selectedRange.getMax() + "]";
            query.addFilterQuery("currentPrice:" + prs);
        }
        if (!searchParams.getSelectedStandards().getSelected().isEmpty()) {
            query.addFilterQuery("standardId:(" + StringUtils.join(
                    searchParams.getSelectedStandards().getSelected()
                            .stream().map(StandardDisplayPojo::getId).collect(Collectors.toList()), " "
            ) + ")");
        }
        if (!searchParams.getSelectedColors().getSelected().isEmpty()) {
            query.addFilterQuery("colorId:(" + StringUtils.join(
                    searchParams.getSelectedColors().getSelected()
                            .stream().map(ColorDisplayPojo::getId).collect(Collectors.toList()), " "
            ) + ")");
        }
        if (!searchParams.getSelectedProperties().isEmpty()) {
            for (PropertyDisplayPojo selectedProp : searchParams.getSelectedProperties()) {
                query.addFilterQuery(
                        "propertyValueIdOfPropertyId_" + selectedProp.getId() + "_" + channel + ":(" + StringUtils
                                .join(selectedProp.getValues().stream().map(PropertyValueDisplayPojo::getId)
                                        .collect(Collectors.toList()), " ") + ")");
            }
        }
        if (!searchParams.getSelectedTags().getSelected().isEmpty()) {
            query.addFilterQuery("tagIds:(" + StringUtils.join(
                    searchParams.getSelectedTags().getSelected()
                            .stream().map(TagDisplayPojo::getId).collect(Collectors.toList()), " "
            ) + ")");
        }
        /* 上市时间限制 */
        Date dateFrom = searchParams.getDateFrom();
        Date dateTo = searchParams.getDateTo();
        if (dateFrom != null || dateTo != null) {
            try {
                String dateFromStr = (dateFrom != null) ? DateUtils.mjd2UtcStr(dateFrom) : "*";
                String dateToStr = (dateTo != null) ? DateUtils.mjd2UtcStr(dateTo) : "*";
                query.addFilterQuery("onSellSince:[" + dateFromStr + " TO " + dateToStr + "]");
            } catch (ParseException ignored) {
            }
        }
    }
}
