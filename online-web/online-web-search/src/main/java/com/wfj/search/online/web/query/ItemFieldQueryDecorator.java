package com.wfj.search.online.web.query;

import com.wfj.search.online.web.common.pojo.SearchParams;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.stereotype.Component;

/**
 * <p>create at 15-11-5</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("itemFieldQueryDecorator")
public class ItemFieldQueryDecorator implements QueryDecorator {
    @Override
    public void decorator(SolrQuery query, SearchParams searchParams) {
        query
                .addField("itemId")
                .addField("supplierId")
                .addField("stockMode")
                .addField("inventory")
                .addField("originalPrice")
                .addField("currentPrice")
                .addField("discountRate")
                .addField("skuId")
                .addField("colorId")
                .addField("colorName")
                .addField("colorMasterPicture")
                .addField("colorMasterPictureOfPix_220x220")
                .addField("colorMasterPictureOfPix_60x60")
                .addField("type")
                .addField("spuId")
                .addField("spuName")
                .addField("model")
                .addField("activeBit")
                .addField("onSellSince")
                .addField("brandId")
                .addField("brandName")
                .addField("activeName")
                .addField("title")
                .addField("subTitle")
                .addField("allLevelCategoryIds_" + searchParams.getChannel())
                .addField("longDesc")
                .addField("shortDesc");
    }
}
