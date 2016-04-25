package com.wfj.search.online.web.service.impl;

import com.google.common.collect.Lists;
import com.wfj.search.online.web.common.pojo.ItemDisplayPojo;
import com.wfj.search.online.web.common.pojo.SpuDisplayPojo;
import com.wfj.search.online.web.iao.IItemIAO;
import com.wfj.search.online.web.service.IMayLikeSearchService;
import com.wfj.search.online.web.service.ISearchConfigService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.DisMaxParams;
import org.apache.solr.common.params.GroupParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <br/>create at 16-1-12
 *
 * @author liuxh
 * @since 1.0.0
 */
@Service("likeSearchService")
public class MayLikeSearchServiceImpl implements IMayLikeSearchService {
    private static final Logger logger = LoggerFactory.getLogger(MayLikeSearchServiceImpl.class);
    @Autowired
    private ISearchConfigService searchConfigService;
    @Autowired
    private IItemIAO itemIAO;

    @Override
    public List<SpuDisplayPojo> randomSearch(String inputQuery, String categories) {
        int rows = searchConfigService.getMayLikeRows();
        final String channel = this.searchConfigService.getChannel();
        SolrQuery query = new SolrQuery();
        query
                .setStart(0)
                .setRows(rows)
                .addFilterQuery("channels:" + channel)
                .addFilterQuery("allLevelCategoryIds_" + channel + ":*")
                        // eDisMax参数
                .set("defType", "edismax")
                .set(DisMaxParams.QF, this.searchConfigService.getQf())
                .set(DisMaxParams.MM, this.searchConfigService.getMm())
                .set(DisMaxParams.QS, this.searchConfigService.getQs())
                .set(DisMaxParams.TIE, this.searchConfigService.getTie())
                .set("boost", "boost_" + channel)
                .set(CommonParams.SORT, "random_" + UUID.randomUUID().toString() + " " + SolrQuery.ORDER.desc)
                        // group参数
                .set(GroupParams.GROUP, true)
                .set(GroupParams.GROUP_FIELD, "spuId")
                .set(GroupParams.GROUP_TOTAL_COUNT, true)
                .set(GroupParams.GROUP_FACET, true)
                .set(GroupParams.GROUP_TRUNCATE, true);
        if (StringUtils.isBlank(inputQuery)) {
            query.setQuery("*");
            if (StringUtils.isNotBlank(categories)) {
                Set<String> catIds = Arrays.asList(categories.split("-"))
                        .stream()
                        .filter(catId -> StringUtils.isNotBlank(catId) && !"0".equals(catId))
                        .collect(Collectors.toSet());
                query.addFilterQuery("allLevelCategoryIds_" + channel + ":(" + StringUtils.join(catIds, " ") + ")");
            }
        } else {
            query.setQuery(inputQuery);
        }
        QueryResponse response;
        try {
            response = itemIAO.query(query);
        } catch (Exception e) {
            logger.error("猜你喜欢-随机查询失败", e);
            return Collections.emptyList();
        }
        if (response == null) {
            logger.error("猜你喜欢-随机查询出现异常情况", new RuntimeException());
            return Collections.emptyList();
        }
        List<SpuDisplayPojo> spus = Collections.synchronizedList(new ArrayList<>());
        GroupResponse groupResponse = response.getGroupResponse();
        List<GroupCommand> groups = groupResponse.getValues();
        assert groups.size() == 1;// 只定义对spuId进行group计算
        GroupCommand groupBySpuId = groups.get(0);
        assert "spuId".equals(groupBySpuId.getName());// 只定义对spuId进行group计算
        List<Group> spuItems = groupBySpuId.getValues();
        spuItems.forEach(group -> {
            SolrDocumentList itemDocs = group.getResult();
            if (itemDocs.size() > 0) {
                // 如果有数据，只会返回1条。(因为group.limit默认值1，所以没有特殊设置，此处最多只会返回一条。)
                SolrDocument itemDoc = itemDocs.get(0);
                ItemDisplayPojo item = new ItemDisplayPojo();
                item.setItemId((String) itemDoc.getFieldValue("itemId"));
                item.setSupplierId((String) itemDoc.getFieldValue("supplierId"));
                item.setStockMode((Integer) itemDoc.getFieldValue("stockMode"));
                item.setInventory((Integer) itemDoc.getFieldValue("inventory"));
                item.setOriginalPrice((Double) itemDoc.getFieldValue(("originalPrice")));
                item.setCurrentPrice((Double) itemDoc.getFieldValue("currentPrice"));
                item.setDiscountRate((Double) itemDoc.getFieldValue("discountRate"));
                item.setSkuId((String) itemDoc.getFieldValue("skuId"));
                item.setColorId((String) itemDoc.getFieldValue("colorId"));
                item.setColorName((String) itemDoc.getFieldValue("colorName"));
                item.setColorMasterPicture((String) itemDoc.getFieldValue("colorMasterPicture"));
                item.getColorMasterPictureOfPix().put("220x220",
                        (String) itemDoc.getFieldValue("colorMasterPictureOfPix_220x220"));
                item.getColorMasterPictureOfPix().put("60x60",
                        (String) itemDoc.getFieldValue("colorMasterPictureOfPix_60x60"));
                item.setType((Integer) itemDoc.getFieldValue("type"));
                item.setSpuId((String) itemDoc.getFieldValue("spuId"));
                item.setSpuName((String) itemDoc.getFieldValue("spuName"));
                item.setModel((String) itemDoc.getFieldValue("model"));
                item.setActiveBit((Integer) itemDoc.getFieldValue("activeBit"));
                item.setOnSellSince((Date) itemDoc.getFieldValue("onSellSince"));
                item.setBrandId((String) itemDoc.getFieldValue("brandId"));
                item.setBrandName((String) itemDoc.getFieldValue("brandName"));
                item.setLongDesc((String) itemDoc.getFieldValue("longDesc"));
                item.setShortDesc((String) itemDoc.getFieldValue("shortDesc"));
                List<String> activeName = Lists.newArrayList();
                try {
                    activeName = itemDoc.getFieldValues("activeName").stream().map(Object::toString)
                            .collect(Collectors.toList());
                } catch (Exception ignored) {
                }
                item.setActiveName(activeName);
                item.setTitle((String) itemDoc.getFieldValue("title"));
                Object subTitle = itemDoc.getFieldValue("subTitle");
                Collection<Object> channelCategoryIds = itemDoc.getFieldValues("allLevelCategoryIds_" + channel);
                if (channelCategoryIds != null) {
                    item.setCategoryIds(channelCategoryIds.stream().map(Object::toString).collect(Collectors.toList()));
                }
                item.setSubTitle(subTitle == null ? "" : subTitle.toString());
                spus.add(SpuDisplayPojo.copyOf(item));
            }
        });
        return spus;
    }
}
