package com.wfj.search.online.index.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wfj.platform.util.analysis.Counter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 索引统计工具
 * <br/>create at 15-7-22
 *
 * @author liufl
 * @since 1.0.0
 */
public class IndexResultCount {
    private final Map<IndexResultType, Counter> itemCounts = Maps.newConcurrentMap();
    private final Counter spuCount = new Counter();
    private final Map<IndexResultType, List<String>> itemIds = Maps.newConcurrentMap();
    private final StringBuffer extraMsg = new StringBuffer();

    private IndexResultCount() {
    }

    public static IndexResultCount emptyCount() {
        IndexResultCount count = new IndexResultCount();
        for (IndexResultType type : IndexResultType.values()) {
            count.itemCounts.put(type, new Counter());
        }
        count.itemIds.put(IndexResultType.NOT_SELLING_SKU, Collections.synchronizedList(Lists.newArrayList()));
        count.itemIds.put(IndexResultType.NOT_SELLING_SPU, Collections.synchronizedList(Lists.newArrayList()));
        count.itemIds.put(IndexResultType.NO_ITEM_DATA, Collections.synchronizedList(Lists.newArrayList()));
        count.itemIds.put(IndexResultType.NO_SKU_DATA, Collections.synchronizedList(Lists.newArrayList()));
        count.itemIds.put(IndexResultType.NO_SPU_DATA, Collections.synchronizedList(Lists.newArrayList()));
        count.itemIds.put(IndexResultType.NO_COLOR_MAIN_PIC, Collections.synchronizedList(Lists.newArrayList()));
        count.itemIds.put(IndexResultType.NO_BRAND_DATA, Collections.synchronizedList(Lists.newArrayList()));
        count.itemIds.put(IndexResultType.NO_CATEGORY_DATA, Collections.synchronizedList(Lists.newArrayList()));
        count.itemIds.put(IndexResultType.EXCEPTION, Collections.synchronizedList(Lists.newArrayList()));
        count.itemIds.put(IndexResultType.FORBIDDEN_ITEM, Collections.synchronizedList(Lists.newArrayList()));
        count.itemIds.put(IndexResultType.FORBIDDEN_SKU, Collections.synchronizedList(Lists.newArrayList()));
        count.itemIds.put(IndexResultType.FORBIDDEN_SPU, Collections.synchronizedList(Lists.newArrayList()));
        count.itemIds.put(IndexResultType.FORBIDDEN_BRAND, Collections.synchronizedList(Lists.newArrayList()));
        return count;
    }

    public void merge(IndexResultCount part) {
        synchronized (this) {
            for (IndexResultType type : IndexResultType.values()) {
                long count = part.getCount(type);
                if (count >= 0) {
                    this.countUp(type, count);
                } else if (type.equals(IndexResultType.LOST)) {
                    this.countDownLost(-count);
                }
            }
            this.countUpSpu(part.getSpuCount());
            for (IndexResultType type : part.itemIds.keySet()) {
                List<String> totalItemIds = this.itemIds.get(type);
                totalItemIds.addAll(part.itemIds.get(type));
            }
        }
    }

    public void countUp(IndexResultType type, long amount) {
        this.itemCounts.get(type).countUp(amount);
    }

    public void countDownLost(long amount) {
        this.itemCounts.get(IndexResultType.LOST).countDown(amount);
    }

    public void countUpSpu(long amount) {
        this.spuCount.countUp(amount);
    }

    public long getCount(IndexResultType type) {
        return itemCounts.get(type).getCount();
    }

    public long getSpuCount() {
        return this.spuCount.getCount();
    }

    public void recordNoIndexItemId(IndexResultType type, String itemId) {
        List<String> itemIds = this.itemIds.get(type);
        if (itemIds != null) {
            itemIds.add(itemId);
        }
    }

    public List<String> noIndexItemIds(IndexResultType type) {
        List<String> itemIds = this.itemIds.get(type);
        if (itemIds == null) {
            return null;
        }
        return Collections.unmodifiableList(itemIds);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("PCM报告专柜商品数共");
        builder.append(this.getCount(IndexResultType.ALL)).append("个；操作结果：正常索引")
                .append(this.getCount(IndexResultType.OK)).append("个，");
        if (this.getCount(IndexResultType.NOT_SELLING_SKU) > 0) {
            builder.append("非上架SKU").append(this.getCount(IndexResultType.NOT_SELLING_SKU)).append("个，");
        }
        if (this.getCount(IndexResultType.NOT_SELLING_SPU) > 0) {
            builder.append("非上架SPU").append(this.getCount(IndexResultType.NOT_SELLING_SPU)).append("个，");
        }
        if (this.getCount(IndexResultType.NO_ITEM_DATA) > 0) {
            builder.append("获取商品数据失败").append(this.getCount(IndexResultType.NO_ITEM_DATA)).append("个，");
        }
        if (this.getCount(IndexResultType.NO_SKU_DATA) > 0) {
            builder.append("获取SKU数据失败").append(this.getCount(IndexResultType.NO_SKU_DATA)).append("个，");
        }
        if (this.getCount(IndexResultType.NO_SPU_DATA) > 0) {
            builder.append("获取SPU数据失败").append(this.getCount(IndexResultType.NO_SPU_DATA)).append("个，");
        }
        if (this.getCount(IndexResultType.NO_COLOR_MAIN_PIC) > 0) {
            builder.append("缺少商品颜色主图").append(this.getCount(IndexResultType.NO_COLOR_MAIN_PIC)).append("个，");
        }
        if (this.getCount(IndexResultType.NO_BRAND_DATA) > 0) {
            builder.append("获取品牌数据失败").append(this.getCount(IndexResultType.NO_BRAND_DATA)).append("个，");
        }
        if (this.getCount(IndexResultType.NO_CATEGORY_DATA) > 0) {
            builder.append("获取分类数据失败").append(this.getCount(IndexResultType.NO_CATEGORY_DATA)).append("个，");
        }
        if (this.getCount(IndexResultType.EXCEPTION) > 0) {
            builder.append("提交索引失败").append(this.getCount(IndexResultType.EXCEPTION)).append("个，");
        }
        if (this.getCount(IndexResultType.FORBIDDEN_ITEM) > 0) {
            builder.append("被专柜商品黑名单禁止").append(this.getCount(IndexResultType.FORBIDDEN_ITEM)).append("个，");
        }
        if (this.getCount(IndexResultType.FORBIDDEN_SKU) > 0) {
            builder.append("被SKU黑名单禁止").append(this.getCount(IndexResultType.FORBIDDEN_SKU)).append("个，");
        }
        if (this.getCount(IndexResultType.FORBIDDEN_SPU) > 0) {
            builder.append("被SPU黑名单禁止").append(this.getCount(IndexResultType.FORBIDDEN_SPU)).append("个，");
        }
        if (this.getCount(IndexResultType.FORBIDDEN_BRAND) > 0) {
            builder.append("被品牌黑名单禁止").append(this.getCount(IndexResultType.FORBIDDEN_BRAND)).append("个，");
        }
        if (this.getCount(IndexResultType.LOST) > 0) {
            builder.append("统计丢失").append(this.getCount(IndexResultType.LOST)).append("个，");
        }
        builder.setCharAt(builder.length() - 1, '.');
        if (this.getCount(IndexResultType.NO_ITEM_DATA) > 0) {
            builder.append("获取商品数据失败的商品编码有：").append(this.noIndexItemIds(IndexResultType.NO_ITEM_DATA).toString())
                    .append(',');
        }
        if (this.getCount(IndexResultType.NOT_SELLING_SKU) > 0) {
            builder.append("未上架SKU的商品编码有：").append(this.noIndexItemIds(IndexResultType.NOT_SELLING_SKU).toString())
                    .append(',');
        }
        if (this.getCount(IndexResultType.NOT_SELLING_SPU) > 0) {
            builder.append("未上架SPU的商品编码有：").append(this.noIndexItemIds(IndexResultType.NOT_SELLING_SPU).toString())
                    .append(',');
        }
        if (this.getCount(IndexResultType.NO_SKU_DATA) > 0) {
            builder.append("获取SKU数据失败的商品编码有：").append(this.noIndexItemIds(IndexResultType.NO_SKU_DATA).toString())
                    .append(',');
        }
        if (this.getCount(IndexResultType.NO_SPU_DATA) > 0) {
            builder.append("获取SPU数据失败的商品编码有：").append(this.noIndexItemIds(IndexResultType.NO_SPU_DATA).toString())
                    .append(',');
        }
        if (this.getCount(IndexResultType.NO_COLOR_MAIN_PIC) > 0) {
            builder.append("缺少颜色主图的商品编码有：").append(this.noIndexItemIds(IndexResultType.NO_COLOR_MAIN_PIC).toString())
                    .append(',');
        }
        if (this.getCount(IndexResultType.NO_BRAND_DATA) > 0) {
            builder.append("获取品牌数据失败的商品编码有：").append(this.noIndexItemIds(IndexResultType.NO_BRAND_DATA).toString())
                    .append(',');
        }
        if (this.getCount(IndexResultType.NO_CATEGORY_DATA) > 0) {
            builder.append("获取分类数据失败的商品编码有：").append(this.noIndexItemIds(IndexResultType.NO_CATEGORY_DATA).toString())
                    .append(',');
        }
        if (this.getCount(IndexResultType.EXCEPTION) > 0) {
            builder.append("提交索引失败商品编码有：").append(this.noIndexItemIds(IndexResultType.EXCEPTION).toString())
                    .append(',');
        }
        if (this.getCount(IndexResultType.FORBIDDEN_ITEM) > 0) {
            builder.append("被专柜商品黑名单禁止商品编码有：").append(this.noIndexItemIds(IndexResultType.FORBIDDEN_ITEM).toString())
                    .append(',');
        }
        if (this.getCount(IndexResultType.FORBIDDEN_SKU) > 0) {
            builder.append("被SKU黑名单禁止商品编码有：").append(this.noIndexItemIds(IndexResultType.FORBIDDEN_SKU).toString())
                    .append(',');
        }
        if (this.getCount(IndexResultType.FORBIDDEN_SPU) > 0) {
            builder.append("被SPU黑名单禁止商品编码有：").append(this.noIndexItemIds(IndexResultType.FORBIDDEN_SPU).toString())
                    .append(',');
        }
        if (this.getCount(IndexResultType.FORBIDDEN_BRAND) > 0) {
            builder.append("被品牌黑名单禁止商品编码有：").append(this.noIndexItemIds(IndexResultType.FORBIDDEN_BRAND).toString())
                    .append(',');
        }
        builder.setCharAt(builder.length() - 1, '.');
        return builder.toString();
    }

    public StringBuffer getExtraMsg() {
        return extraMsg;
    }
}
