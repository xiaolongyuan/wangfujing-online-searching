package com.wfj.search.online.index.iao.impl.boostStrategy;

import com.wfj.search.online.index.es.CommentEsIao;
import com.wfj.search.online.index.es.ItemEsIao;
import com.wfj.search.online.index.pojo.ItemIndexPojo;
import com.wfj.search.online.index.pojo.Spot;
import com.wfj.search.online.index.service.IManualBoostService;
import com.wfj.search.online.index.service.ITopSpotService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>create at 15-11-10</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("boostStrategy")
public class BoostStrategyImpl implements BoostStrategy {
    @Autowired
    private IManualBoostService manualBoostService;
    @Autowired
    private ItemEsIao itemEsIao;
    @Autowired
    private CommentEsIao commentEsIao;
    @Autowired
    private ITopSpotService topSpotService;

    @Override
    public SolrInputDocument boost(SolrClient solrClient, ItemIndexPojo item) {
        String brandId = item.getBrandId();
        for (String channel : item.getChannels()) {
            Map<String, Float> channelManualBoostMap = manualBoostService.listBoosts(channel);
            if (channelManualBoostMap != null) {
                Float boost = channelManualBoostMap.get(item.getSkuId());
                if (boost != null) {
                    item.getBoost_().put("boost_" + channel, boost);
                }
            }
            Map<String, Spot> brandSpots = this.topSpotService.brandSpots(brandId, channel);
            Spot spot = brandSpots.get(item.getSpuId());
            item.getPopSpotWeightBrand().put("popSpotWeightBrand_" + channel, spot == null ? 0 : spot.getOrders());
            List<String> catIds = item.getAllLevelCategoryIds().get("allLevelCategoryIds_" + channel);
            if (catIds != null) {
                for (String catId : catIds) {
                    Map<String, Spot> categorySpots = this.topSpotService.categorySpots(catId, channel);
                    spot = categorySpots.get(catId);
                    item.getPopSpotWeightCategory().put("popSpotWeightCategory_" + catId,
                            spot == null ? 0 : spot.getOrders());
                }
            }
        }
        SolrInputDocument doc = solrClient.getBinder().toSolrInputDocument(item);
        doc.setDocumentBoost(this.natureBoost(item));
        return doc;
    }

    @Override
    public Float natureBoost(ItemIndexPojo item) {
        Date since = item.getOnSellSince();
        double sinceBoost = 0;
        if (since != null) {
            Calendar sinceCalendar = Calendar.getInstance();
            sinceCalendar.setTime(since);
            int dayMax = 365;
            int sinceDay = sinceCalendar.get(Calendar.DAY_OF_YEAR);
            Calendar now = Calendar.getInstance();
            int nowDay = now.get(Calendar.DAY_OF_YEAR);
            int abs = Math.abs(sinceDay - nowDay);
            if (abs > dayMax / 2) {
                abs = Math.abs(dayMax - abs);
            }
            double r = abs / dayMax * Math.PI;
            sinceBoost = (Math.cos(r) + 2)/ 3;
            int sinceYear = sinceCalendar.get(Calendar.YEAR);
            int nowYear = now.get(Calendar.YEAR);
            int years = Math.abs(nowYear - sinceYear);
            if (years > 3) {
                sinceBoost = 0;
            } else {
                sinceBoost *= (3 - years) / 3;
            }
        }
        Integer spuSale = item.getSpuSale();
        double spuSaleBoost = spuSale < 100 ? Math.log(spuSale + 1) / Math.log(100) : 1;
        Integer spuClick = item.getSpuClick();
        double spuClickBoost = spuClick < 1000 ? Math.log(spuClick + 1) / Math.log(1000) : 1;
        List<ItemIndexPojo> spuItems = this.itemEsIao.findBySpuId(item.getSpuId());
        AtomicInteger inv = new AtomicInteger(0);
        spuItems.forEach(item_ -> inv.addAndGet(item_.getInventory()));
        double spuInventoryBoost = inv.get() < 100 ? Math.log(inv.get() + 1) / Math.log(10) / 2 : 1;
        long spuCommentCount = this.commentEsIao.countOfSpuScoreGt(item.getSpuId(), -1);
        long spuGoodCommentCount = this.commentEsIao.countOfSpuScoreGt(item.getSpuId(), 3);
        double spuCommentBoost = spuCommentCount == 0 ? 0 : spuGoodCommentCount / spuCommentCount;
        spuCommentBoost *= spuCommentCount < 100 ? Math.log(spuCommentCount) / Math.log(100) : 1;
        return (float) (sinceBoost * 0.2 + spuSaleBoost * 0.8 + spuClickBoost * 0.2 + spuInventoryBoost * 0.1 + spuCommentBoost * 0.5);
    }
}
