package com.wfj.search.online.index.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wfj.search.online.common.pojo.*;
import com.wfj.search.online.index.iao.IPcmRequester;
import com.wfj.search.online.index.iao.RequestException;
import com.wfj.search.online.index.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <br/>create at 15-7-15
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
public class PojoUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(PojoUtils.class);

    private PojoUtils() {
    }

    public static ItemPojo json2Item(JSONObject json) {
        ItemPojo item = new ItemPojo();
        item.setItemId(Validate.notBlank(json.getString("itemId"), "商品编码为空").trim());
        item.setSkuId(Validate.notBlank(json.getString("skuId"), "SKU编码为空").trim());
//        item.setSupplierId(Validate.notBlank(json.getString("supplierId"), "供应商编码为空").trim());
        item.setSupplierId(json.getString("supplierId"));
        List<String> channels = Lists.newArrayList();
        try {
            JSONArray channel = json.getJSONArray("channel");
            for (int i = 0; i < channel.size(); i++) {
                String channelId = channel.getString(i);
                if (StringUtils.isNotBlank(channelId)) {
                    channels.add(channelId.trim());
                }
            }
        } catch (Exception ignored) {
        }
        item.setChannels(Validate.notEmpty(channels, "可售渠道列表为空"));
        JSONArray active = json.getJSONArray("active");
        if (active != null) {
            for (int i = 0; i < active.size(); i++) {
                try {
                    JSONObject activity = active.getJSONObject(i);
                    ActivityPojo activityPojo = new ActivityPojo();
                    activityPojo.setActiveId(Validate.notBlank(activity.getString("activeId")).trim());
                    activityPojo.setActiveName(Validate.notBlank(activity.getString("activeName")).trim());
                    item.getActivities().add(activityPojo);
                } catch (Exception ignored) {
                }
            }
        }
        item.setStockMode(Validate.notNull(json.getInteger("stockMode"), "库存方式为空"));
        item.setInventory(json.getInteger("inventory"));
        String originalPrice = Validate.notBlank(json.getString("originalPrice"), "原价为空").trim();
        item.setOriginalPrice(new BigDecimal(originalPrice));
        String currentPrice = Validate.notBlank(json.getString("currentPrice"), "现价为空").trim();
        item.setCurrentPrice(new BigDecimal(currentPrice));
        if (item.getOriginalPrice() != null && item.getCurrentPrice() != null) {
            double op = item.getOriginalPrice().doubleValue();
            item.setDiscountRate(op == 0 ? 0 : item.getCurrentPrice().doubleValue() / op);
        }
        return item;
    }

    public static SkuPojo json2Sku(JSONObject json) {
        SkuPojo sku = new SkuPojo();
        sku.setSkuId(Validate.notBlank(json.getString("skuId"), "SKU编码为空").trim());
        sku.setSpuId(Validate.notBlank(json.getString("spuId"), "SPU编码为空").trim());
        sku.setType(Validate.notNull(json.getInteger("type"), "商品类型为空"));
        sku.setOnSell(Validate.notNull(json.getBoolean("onSell"), "线上可售状态为空"));
        sku.setTitle(Validate.notBlank(json.getString("title"), "SKU标题为空").trim());
        sku.setSubTitle(json.getString("subTitle"));
        JSONArray activeKeywords = json.getJSONArray("activeKeywords");
        if (activeKeywords != null) {
            for (int i = 0; i < activeKeywords.size(); i++) {
                String activeKeyword = activeKeywords.getString(i);
                if (StringUtils.isNotBlank(activeKeyword)) {
                    sku.getActiveKeywords().add(activeKeyword.trim());
                }
            }
        }
        sku.setColorId(Validate.notBlank(json.getString("colorId"), "色系编码为空").trim());
        sku.setColorName(Validate.notBlank(json.getString("colorName"), "色系名称为空").trim());
        sku.setColorAlias(json.getString("colorAlias"));
        StandardPojo standard = new StandardPojo();
        try {
            // 由于pcm端对规格id不做限制
            // url保留字";/?:@=&"中"/"和"@"即使转码也会出现400错误
            // 将两个替换为url安全字符"$-_.+!*'()"中的$与!，并进行转码
            String standardId = json.getString("standardId");
            standardId = standardId.replaceAll("/", "\\$");
            standardId = standardId.replaceAll("@", "!");
            standard.setStandardId(URLEncoder.encode(standardId, "UTF-8"));
        } catch (UnsupportedEncodingException ignored) {
        }
        Validate.notBlank(standard.getStandardId(), "规格编码为空");
        standard.setStandardName(Validate.notBlank(json.getString("standardName"), "规格名称为空").trim());
        sku.setStandardPojo(standard);
        JSONArray pictureArray = json.getJSONArray("pictures");
        sku.getPictures().clear();
        if (pictureArray != null) {
            for (int i = 0; i < pictureArray.size(); i++) {
                JSONObject pictureJO = pictureArray.getJSONObject(i);
                if (pictureJO != null) {
                    PicturePojo picturePojo = json2Picture(pictureJO);
                    if (picturePojo != null) {
                        sku.getPictures().add(picturePojo);
                    }
                }
            }
        }
        Validate.notEmpty(sku.getPictures(), "SKU图片列表为空");
        boolean hasMaster = false;
        for (PicturePojo picturePojo : sku.getPictures()) {
            if (picturePojo.isColorMaster()) {
//                if (hasMaster) {
//                    throw new IllegalArgumentException("包含超过一张颜色主图");
//                }
                hasMaster = true;
            }
        }
        if (!hasMaster) {
            throw new IllegalArgumentException("缺少颜色主图");
        }
        try {
            sku.setUpTime(trans2Date(Validate.notBlank(json.getString("upTime"), "上架时间为空").trim()));
        } catch (Exception e) {
            throw new IllegalArgumentException("上架时间格式无法解析", e);
        }
        return sku;
    }

    private static PicturePojo json2Picture(JSONObject json) {
        PicturePojo picture = new PicturePojo();
        picture.setPictureSid(json.getString("pictureSid"));
        picture.setPicture(Validate.notBlank(json.getString("picture"), "图片地址为空").trim());
        picture.setColorId(Validate.notBlank(json.getString("colorId"), "图片色系编码为空").trim());
        try {
            Integer order = json.getInteger("order");
            picture.setOrder(order == null ? 0 : order);
        } catch (Exception e) {
            LOGGER.error("图片Order提取失败, picJson:{}", json, e);
        }
        picture.setColorAlias(json.getString("colorAlias"));
        Boolean colorMaster = json.getBoolean("colorMaster");
        picture.setColorMaster(colorMaster == null ? false : colorMaster);
        try {
            String size = json.getString("size");
            size = size.replaceAll("\\*", "x");
            size = size.replaceAll("\\.", "_dot_");
            size = size.toLowerCase();
            picture.setSize(size);
        } catch (Exception e) {
            LOGGER.error("提取图片分辨率失败，PicJSON:{}", json, e);
        }
        return picture;
    }

    public static SpuPojo json2Spu(JSONObject json) throws ParseException {
        SpuPojo spu = new SpuPojo();
        spu.setSpuId(Validate.notBlank(json.getString("spuId"), "SPU编码为空").trim());
        spu.setSpuName(Validate.notBlank(json.getString("spuName"), "SPU名称为空").trim());
        spu.setModel(Validate.notBlank(json.getString("model"), "款号为空").trim());
        spu.setBrandId(Validate.notBlank(json.getString("brandId"), "品牌编码为空").trim());
        spu.setActiveBit(json.getInteger("activeBit"));
        spu.setOnSell(Validate.notNull(json.getBoolean("onSell"), "SPU上架状态为空"));
        spu.setPageDescription(json.getString("pageDescription"));
        JSONArray aliasArray = json.getJSONArray("aliases");
        for (int i = 0; i < aliasArray.size(); i++) {
            String alias = aliasArray.getString(i);
            if (StringUtils.isNotBlank(alias)) {
                spu.getAliases().add(alias.trim());
            }
        }
        Date onSellSince = null;
        try {
            onSellSince = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                    json.getString("onSellSince"));
        } catch (Exception ignored) {
        } // 无法解析上市时间，忽略， 为null
        spu.setOnSellSince(onSellSince);
        JSONArray categoryIdArray = json.getJSONArray("categoryIds");
        if (categoryIdArray != null) {
            for (int i = 0; i < categoryIdArray.size(); i++) {
                String categoryId = categoryIdArray.getString(i);
                if (StringUtils.isNotBlank(categoryId)) {
                    spu.getCategoryIds().add(categoryId.trim());
                }
            }
        }
        Validate.notEmpty(spu.getCategoryIds(), "所属分类ID列表为空");
        JSONArray tagArray = json.getJSONArray("tags");
        if (tagArray != null) {
            for (int i = 0; i < tagArray.size(); i++) {
                JSONObject tagJo = tagArray.getJSONObject(i);
                if (tagJo != null) {
                    spu.getTags().add(json2Tag(tagJo));
                }
            }
        }
        JSONArray propertyValueArray = json.getJSONArray("propertyValues");
        spu.getPropertyValues().clear();
        if (propertyValueArray != null) {
            for (int i = 0; i < propertyValueArray.size(); i++) {
                JSONObject propertyValueInfo = propertyValueArray.getJSONObject(i);
                if (propertyValueInfo != null) {
                    PropertyValuePojo propertyValue = json2PropertyValue(propertyValueInfo);
                    PropertyPojo property = json2Property(propertyValueInfo);
                    propertyValue.setProperty(property);
                    spu.getPropertyValues().add(propertyValue);
                }
            }
        }
        return spu;
    }

    private static PropertyPojo json2Property(JSONObject json) {
        PropertyPojo property = new PropertyPojo();
        property.setPropertyId(Validate.notBlank(json.getString("propertyId"), "属性编码为空").trim());
        property.setPropertyName(Validate.notBlank(json.getString("propertyName"), "属性名称为空").trim());
        Integer propertyOrder = json.getInteger("propertyOrder");
        property.setPropertyOrder(propertyOrder == null ? 0 : propertyOrder);
        property.setEnumProperty(Validate.notNull(json.getBoolean("enumProperty"), "属性是否枚举为空"));
        return property;
    }

    private static PropertyValuePojo json2PropertyValue(JSONObject json) {
        PropertyValuePojo propertyValue = new PropertyValuePojo();
        propertyValue.setCategoryId(Validate.notBlank(json.getString("categoryId"), "属性值所属分类编码为空").trim());
//        propertyValue.setPropertyValueId(Validate.notBlank(json.getString("propertyValueId"), "属性值编码为空").trim());
        propertyValue.setPropertyValueId(json.getString("propertyValueId"));
        propertyValue.setPropertyValue(Validate.notBlank(json.getString("propertyValue"), "属性值名称为空").trim());
        return propertyValue;
    }

    private static TagPojo json2Tag(JSONObject json) {
        TagPojo tag = new TagPojo();
        tag.setTagId(Validate.notBlank(json.getString("tagId"), "标签编码为空").trim());
        tag.setTag(Validate.notBlank(json.getString("tag"), "标签名称为空").trim());
        return tag;
    }

    public static BrandPojo json2Brand(JSONObject json) {
        BrandPojo brand = new BrandPojo();
        brand.setBrandId(Validate.notBlank(json.getString("brandId"), "品牌编码为空").trim());
        brand.setBrandName(Validate.notBlank(json.getString("brandName"), "品牌名称为空").trim());
        try {
            brand.setBrandDesc(json.getString("brandDesc"));
        } catch (Exception ignored) {
        }
//        brand.setBrandLogo(Validate.notBlank(json.getString("brandLogo"), "品牌LOGO为空").trim());
        brand.setBrandLogo(json.getString("brandLogo"));
        try {
            brand.setBrandPicture(json.getString("brandPicture"));
        } catch (Exception ignored) {
        }
        JSONArray brandAliasesInfo = json.getJSONArray("brandAliases");
        if (brandAliasesInfo != null) {
            for (int i = 0; i < brandAliasesInfo.size(); i++) {
                String brandAlias = brandAliasesInfo.getString(i);
                if (StringUtils.isNotBlank(brandAlias)) {
                    brand.getBrandAliases().add(brandAlias.trim());
                }
            }
        }
        return brand;
    }

    public static CategoryPojo json2Category(JSONObject json) {
        CategoryPojo category = new CategoryPojo();
        category.setCategoryId(Validate.notBlank(json.getString("categoryId"), "分类编码为空").trim());
        category.setCategoryName(Validate.notBlank(json.getString("categoryName"), "分类名称为空").trim());
        category.setLeafLevel(Validate.notNull(json.getBoolean("leafLevel"), "是否叶子级分类标记为空"));
        category.setSelfBuilt(json.getBoolean("selfBuilt"));
        category.setRootCategoryId(Validate.notBlank(json.getString("rootCategoryId"), "根分类编码为空").trim());
        category.setParentCategoryId(json.getString("parentCategoryId"));
        category.setLevel(Validate.notNull(json.getInteger("level"), "分类Level为空"));
        category.setOrder(Validate.notNull(json.getInteger("order"), "同级分类序号为空"));
        category.setChannel(Validate.notBlank(json.getString("channel"), "分类所属销售渠道编码为空").trim());
        return category;
    }

    public static CommentPojo json2Comment(JSONObject json) throws ParseException {
        CommentPojo comment = new CommentPojo();
        comment.setSid(Validate.notBlank(json.getString("sid"), "评论SID为空").trim());
        JSONObject memberJson = Validate.notNull(json.getJSONObject("member"), "会员信息为空");
        MemberPojo member = new MemberPojo();
        member.setMemberId(Validate.notBlank(memberJson.getString("memberId"), "会员号为空").trim());
        member.setMemberName(Validate.notBlank(memberJson.getString("memberName"), "会员名称为空").trim());
        member.setMemberLevel(Validate.notBlank(memberJson.getString("memberLevel"), "会员级别为空").trim());
        member.setMemberImg(Validate.notBlank(memberJson.getString("memberImg"), "会员头像地址为空").trim());
        comment.setMember(member);
        comment.setSpuId(Validate.notBlank(json.getString("spuId"), "SPU编码为空").trim());
        comment.setSkuId(Validate.notBlank(json.getString("skuId"), "SKU编码为空").trim());
        comment.setItemId(Validate.notBlank(json.getString("itemId"), "专柜商品编码为空").trim());
        comment.setTitle(json.getString("title"));
        comment.setContent(json.getString("content"));
        comment.setScore(Validate.notNull(json.getInteger("score"), "评论打分为空"));
        String createTime = json.getString("createTime");
        try {
            trans2Date(createTime);// 用于验证时间格式
        } catch (Exception e) {
            throw new IllegalArgumentException("评论时间非法", e);
        }
        comment.setCreateTime(createTime);
        comment.setOrderNo(Validate.notBlank(json.getString("orderNo"), "订单号为空").trim());
        comment.setProductName(Validate.notBlank(json.getString("productName"), "商品名称为空").trim());
        String orderCreatedTime = json.getString("orderCreatedTime");
        try {
            trans2Date(orderCreatedTime);// 用于验证时间格式
        } catch (Exception e) {
            throw new IllegalArgumentException("订单创建时间非法", e);
        }
        comment.setOrderCreatedTime(orderCreatedTime);
        comment.setIsAnonymity(Validate.notNull(json.getInteger("isAnonymity"), "是否匿名评论为空"));// 是否匿名：0匿名，1非匿名
        comment.setMind(json.getString("mind"));
        JSONArray picsArray = json.getJSONArray("pics");
        if (picsArray != null) {
            for (int i = 0; i < picsArray.size(); i++) {
                String pic = picsArray.getString(i);
                if (StringUtils.isNotBlank(pic)) {
                    comment.getPics().add(pic);
                }
            }
        }
        return comment;
    }

    public static BrandIndexPojo toIndexPojo(BrandPojo brand) {
        BrandIndexPojo indexPojo = new BrandIndexPojo();
        indexPojo.setBrandId(brand.getBrandId());
        indexPojo.setBrandName(brand.getBrandName());
        indexPojo.setBrandDesc(brand.getBrandDesc());
        indexPojo.setBrandLogo(brand.getBrandLogo());
        indexPojo.setBrandPicture(brand.getBrandPicture());
        indexPojo.getBrandAliases().addAll(brand.getBrandAliases());
        return indexPojo;
    }

    public static CategoryIndexPojo toIndexPojo(CategoryPojo category, IPcmRequester pcmRequester,
            Long operationSid) throws RequestException {
        CategoryIndexPojo index = new CategoryIndexPojo();
        index.setCategoryId(category.getCategoryId());
        index.setCategoryName(category.getCategoryName());
        index.setLeafLevel(category.isLeafLevel());
        index.setSelfBuilt(category.isSelfBuilt());
        index.setRootCategoryId(category.getRootCategoryId());
        index.setParentCategoryId(category.getParentCategoryId());
        index.setLevel(category.getLevel());
        index.setOrder(category.getOrder());
        index.setChannel(category.getChannel());
        Deque<String> categoryIdStack = new LinkedList<>();
        categoryIdStack.push(category.getCategoryId());
        String parentCategoryId = category.getParentCategoryId();
        while (StringUtils.isNotBlank(parentCategoryId) && !"0".equals(parentCategoryId.trim())) {
            categoryIdStack.push(parentCategoryId);
            CategoryPojo parent = pcmRequester.getCategoryInfo(parentCategoryId);
            parentCategoryId = parent.getParentCategoryId();
        }
        String idPath = "";
        while (!categoryIdStack.isEmpty()) {
            idPath += categoryIdStack.pop();
        }
        index.setIdPath(idPath);
        index.setOperationSid(operationSid);
        return index;
    }

    public static CategoryIndexPojo toIndexPojo(CategoryPojo category, IPcmRequester pcmRequester)
            throws RequestException {
        CategoryIndexPojo index = new CategoryIndexPojo();
        index.setCategoryId(category.getCategoryId());
        index.setCategoryName(category.getCategoryName());
        index.setLeafLevel(category.isLeafLevel());
        index.setSelfBuilt(category.isSelfBuilt());
        index.setRootCategoryId(category.getRootCategoryId());
        index.setParentCategoryId(category.getParentCategoryId());
        index.setLevel(category.getLevel());
        index.setOrder(category.getOrder());
        index.setChannel(category.getChannel());
        String parentCategoryId = category.getParentCategoryId();
        if (StringUtils.isNotBlank(parentCategoryId) && !"0".equals(parentCategoryId.trim())) {
            CategoryPojo parent = pcmRequester.getCategoryInfo(parentCategoryId);
            index.setParent(toIndexPojo(parent, pcmRequester));
        }
        List<String> idList = Lists.newArrayList();
        CategoryIndexPojo cat = index;
        while (cat != null) {
            idList.add(cat.getCategoryId());
            cat = cat.getParent();
        }
        Collections.reverse(idList);
        index.setIdPath(StringUtils.join(idList.toArray(), "_"));
        return index;
    }

    public static StandardIndexPojo toIndexPojo(StandardPojo standard) {
        StandardIndexPojo index = new StandardIndexPojo();
        index.setStandardId(standard.getStandardId());
        index.setStandardName(standard.getStandardName());
        return index;
    }

    public static ItemIndexPojo toIndexPojo(ItemPojo itemPojo, SkuPojo skuPojo, SpuPojo spuPojo,
            BrandPojo brandPojo, Map<String, Map<String, CategoryIndexPojo>> spuCategoriesOfChannels) {
        ItemIndexPojo indexPojo = new ItemIndexPojo();
        indexPojo.setItemId(itemPojo.getItemId());
        indexPojo.setSupplierId(itemPojo.getSupplierId());
        indexPojo.setChannels(itemPojo.getChannels());
        indexPojo.setActiveId(
                itemPojo.getActivities().stream().map(ActivityPojo::getActiveId).collect(Collectors.toList()));
        indexPojo.setActiveName(
                itemPojo.getActivities().stream().map(ActivityPojo::getActiveName).collect(Collectors.toList()));
        itemPojo.getChannels().forEach(channel -> indexPojo.getBoost_().put("boost_" + channel, 1.0F));
        indexPojo.setStockMode(itemPojo.getStockMode());
        indexPojo.setInventory(itemPojo.getInventory());
        indexPojo.setOriginalPrice(itemPojo.getOriginalPrice().doubleValue());
        indexPojo.setCurrentPrice(itemPojo.getCurrentPrice().doubleValue());
        indexPojo.setDiscountRate(itemPojo.getDiscountRate());
        indexPojo.setSkuId(itemPojo.getSkuId());
        indexPojo.setTitle(skuPojo.getTitle());
        indexPojo.setSubTitle(skuPojo.getSubTitle());
        indexPojo.setActiveKeywords(skuPojo.getActiveKeywords());
        indexPojo.setColorId(skuPojo.getColorId());
        indexPojo.setColorName(skuPojo.getColorName());
        indexPojo.setColorAlias(skuPojo.getColorAlias());
        final AtomicReference<String> masterPicMatchSize = new AtomicReference<>();
        final AtomicReference<String> masterPicMatchFormat = new AtomicReference<>();
        skuPojo.getPictures().forEach(pic -> {
            if (pic.isColorMaster()) {
                if ("1000x1000".equals(pic.getSize())) {
                    masterPicMatchSize.set(pic.getPicture());
                }
                if (StringUtils.isBlank(masterPicMatchSize.get()) && !pic.getPicture()
                        .matches(".+\\.\\d+[xX]\\d+\\..+")) {
                    masterPicMatchFormat.set(pic.getPicture());
                }
                indexPojo.getColorMasterPictureOfPix()
                        .put("colorMasterPictureOfPix_" + pic.getSize(), pic.getPicture());
            }
            indexPojo.getPictures().add(
                    pic.getOrder() + "-" + (pic.isColorMaster() ? "1" : "0") + "-" + pic.getPicture());
        });
        if (StringUtils.isNotBlank(masterPicMatchSize.get())) {
            indexPojo.setColorMasterPicture(masterPicMatchSize.get());
        } else {
            indexPojo.setColorMasterPicture(masterPicMatchFormat.get());
        }
        indexPojo.setStandardId(skuPojo.getStandardPojo().getStandardId());
        indexPojo.setStandardName(skuPojo.getStandardPojo().getStandardName());
        indexPojo.setType(skuPojo.getType());
        indexPojo.setUpTime(skuPojo.getUpTime());
        indexPojo.setSpuId(skuPojo.getSpuId());
        indexPojo.setSpuName(spuPojo.getSpuName());
        indexPojo.setModel(spuPojo.getModel());
        indexPojo.setActiveBit(spuPojo.getActiveBit());
        indexPojo.setPageDescription(spuPojo.getPageDescription());
        indexPojo.setAliases(spuPojo.getAliases());
        indexPojo.setOnSellSince(spuPojo.getOnSellSince());
        indexPojo.setBrandId(spuPojo.getBrandId());
        indexPojo.setBrandName(brandPojo.getBrandName());
        indexPojo.setBrandAliases(brandPojo.getBrandAliases());
        spuCategoriesOfChannels.forEach((channel, spuCategories) -> {
            indexPojo.getAllLevelCategoryIds().put("allLevelCategoryIds_" + channel,
                    Lists.newArrayList(spuCategories.keySet()));
            indexPojo.getAllLevelCategoryNames().put("allLevelCategoryNames_" + channel,
                    spuCategories.values().stream().map(CategoryIndexPojo::getCategoryName).collect(
                            Collectors.toList()));
            spuCategories.forEach((cid, cat) -> {
                String pCid = cat.getParentCategoryId();
                pCid = (StringUtils.isBlank(pCid) || "0".equals(pCid.trim())) ? "0" : pCid.trim();
                String key = "categoryIdUnderCategory_" + pCid;
                List<String> childCats = indexPojo.getCategoryIdUnderCategory().get(key);
                if (childCats == null) {
                    childCats = Lists.newArrayList();
                    indexPojo.getCategoryIdUnderCategory().put(key, childCats);
                }
            });
            spuCategories.forEach((cid, cat) -> {
                String pCid = cat.getParentCategoryId();
                pCid = (StringUtils.isBlank(pCid) || "0".equals(pCid.trim())) ? "0" : pCid.trim();
                String key = "categoryIdUnderCategory_" + pCid;
                List<String> childCats = indexPojo.getCategoryIdUnderCategory().get(key);
                childCats.add(cid);
            });

        });
        indexPojo.getTagIds().addAll(spuPojo.getTags().stream().map(TagPojo::getTagId).collect(Collectors.toList()));
        indexPojo.getTags().addAll(spuPojo.getTags().stream().map(TagPojo::getTag).collect(Collectors.toList()));
        spuPojo.getPropertyValues().forEach(pv -> {
            indexPojo.getPropertyValues().put("propertyValues_" + pv.getChannel(),
                    Collections.synchronizedList(Lists.newArrayList()));
            indexPojo.getPropertyIds().put("propertyIds_" + pv.getChannel(),
                    Collections.synchronizedList(Lists.newArrayList()));
            indexPojo.getPropertyValueIdOfPropertyId()
                    .put("propertyValueIdOfPropertyId_" + pv.getProperty().getPropertyId() + "_" + pv.getChannel(),
                            pv.getPropertyValueId());
        });
        spuPojo.getPropertyValues().forEach(pv -> {
            String channel = pv.getChannel();
            List<String> channelPropValues = indexPojo.getPropertyValues().get("propertyValues_" + channel);
            channelPropValues.add(pv.getPropertyValue());
            List<String> channelPropIds = indexPojo.getPropertyIds().get("propertyIds_" + channel);
            channelPropIds.add(pv.getProperty().getPropertyId());
        });
        return indexPojo;
    }

    /**
     * @param sku SKU对象
     * @return SKU索引对象数据
     */
    public static SkuIndexPojo toIndexPojo(SkuPojo sku) {
        SkuIndexPojo index = new SkuIndexPojo();
        index.setSkuId(sku.getSkuId());
        index.setSpuId(sku.getSpuId());
        index.setType(sku.getType());
        index.setOnSell(sku.isOnSell());
        index.setTitle(sku.getTitle());
        index.setSubTitle(sku.getSubTitle());
        index.setActiveKeywords(sku.getActiveKeywords());
        index.setColorId(sku.getColorId());
        index.setColorName(sku.getColorName());
        index.setColorAlias(sku.getColorAlias());
        index.setStandardId(sku.getStandardPojo().getStandardId());
        index.setStandardName(sku.getStandardPojo().getStandardName());
        index.setPictures(sku.getPictures().stream()
                .map(pic -> pic.getOrder() + "-" + (pic.isColorMaster() ? "1" : "0") + "-" + pic.getPicture())
                .collect(Collectors.toList()));
        index.setUpTime(sku.getUpTime());
        return index;
    }

    /**
     * @param spu SPU对象
     * @return SPU索引对象数据
     */
    public static SpuIndexPojo toIndexPojo(SpuPojo spu) {
        SpuIndexPojo index = new SpuIndexPojo();
        index.setSpuId(spu.getSpuId());
        index.setSpuName(spu.getSpuName());
        index.setModel(spu.getModel());
        index.setBrandId(spu.getBrandId());
        index.setActiveBit(spu.getActiveBit());
        index.setOnSell(spu.isOnSell());
        index.setPageDescription(spu.getPageDescription());
        index.getAliases().addAll(spu.getAliases());
        index.setOnSellSince(spu.getOnSellSince());
        index.getCategoryIds().addAll(spu.getCategoryIds());
        index.getTagIds().addAll(spu.getTags().stream().map(TagPojo::getTagId).collect(Collectors.toSet()));
        spu.getTags().forEach(tag -> index.getTagIndexPojos().add(PojoUtils.toIndexPojo(tag)));
        index.getPropertyValueIds().addAll(spu.getPropertyValues().stream().map(PropertyValuePojo::getPropertyValueId)
                .collect(Collectors.toList()));
        index.getSpuPropertyValues().addAll(spu.getPropertyValues().stream().map(PropertyValuePojo::getPropertyValue)
                .collect(Collectors.toSet()));
        index.getPropertyIndexPojos().addAll(spu.getPropertyValues().stream().map(PropertyValuePojo::getProperty)
                .map(PojoUtils::toIndexPojo).collect(Collectors.toSet()));
        index.getPropertyValueIndexPojos().addAll(spu.getPropertyValues().stream()
                .map(PojoUtils::toIndexPojo).collect(Collectors.toSet()));
        return index;
    }

    private static PropertyIndexPojo toIndexPojo(PropertyPojo property) {
        PropertyIndexPojo index = new PropertyIndexPojo();
        index.setPropertyId(property.getPropertyId());
        index.setPropertyName(property.getPropertyName());
        index.setEnumProperty(property.isEnumProperty());
        index.setPropertyOrder(property.getPropertyOrder());
        return index;
    }

    private static PropertyValueIndexPojo toIndexPojo(PropertyValuePojo propertyValue) {
        PropertyValueIndexPojo indexPojo = new PropertyValueIndexPojo();
        indexPojo.setPropertyValueId(propertyValue.getPropertyValueId());
        indexPojo.setPropertyValue(propertyValue.getPropertyValue());
        indexPojo.setPropertyId(propertyValue.getProperty().getPropertyId());
        indexPojo.setChannel(propertyValue.getChannel());
        return indexPojo;
    }

    public static TagIndexPojo toIndexPojo(TagPojo tag) {
        TagIndexPojo indexPojo = new TagIndexPojo();
        indexPojo.setTagId(tag.getTagId());
        indexPojo.setTagName(tag.getTag());
        return indexPojo;
    }

    public static CommentIndexPojo toIndexPojo(CommentPojo comment, long versionNo) {
        CommentIndexPojo indexPojo = new CommentIndexPojo();
        indexPojo.setCommentId(comment.getSid());
        indexPojo.setMemberId(comment.getMember().getMemberId());
        indexPojo.setMemberName(comment.getMember().getMemberName());
        indexPojo.setMemberLevel(comment.getMember().getMemberLevel());
        indexPojo.setMemberImg(comment.getMember().getMemberImg());
        indexPojo.setSpuId(comment.getSpuId());
        indexPojo.setSkuId(comment.getSkuId());
        indexPojo.setItemId(comment.getItemId());
        indexPojo.setTitle(comment.getTitle());
        indexPojo.setContent(comment.getContent());
        indexPojo.setUserScore(comment.getScore());
        indexPojo.setCreateTime(comment.getCreateTime());
        indexPojo.setOrderNo(comment.getOrderNo());
        indexPojo.setProductName(comment.getProductName());
        indexPojo.setOrderCreatedTime(comment.getOrderCreatedTime());
        indexPojo.setIsAnonymity(comment.getIsAnonymity());
        indexPojo.setMind(comment.getMind());
        indexPojo.getPics().addAll(comment.getPics());
        indexPojo.setOperationSid(versionNo);
        return indexPojo;
    }

    public static Date trans2Date(String s) throws ParseException {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(s);
    }
}
