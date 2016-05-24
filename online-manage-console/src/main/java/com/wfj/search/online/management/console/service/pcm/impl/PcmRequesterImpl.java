package com.wfj.search.online.management.console.service.pcm.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wfj.search.online.common.pojo.CategoryPojo;
import com.wfj.search.online.management.console.service.pcm.IPcmRequester;
import com.wfj.search.utils.http.OkHttpOperator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <br/>create at 15-8-21
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
@Service("pcmRequester")
public class PcmRequesterImpl implements IPcmRequester {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private OkHttpOperator okHttpOperator;
    @Value("${pcm.address}")
    private String address;
    @Value("${pcm.uri.listBrands}")
    private String listBrandUri;
    @Value("${pcm.uri.listCategoryByChannel}")
    private String uriListCategoryByChannel;
    @Value("${pcm.uri.listSubCategories}")
    private String uriListSubCategories;
    @Value("${pcm.uri.getCategoryInfo}")
    private String uriGetCategoryInfo;
    @Value("${pcm.uri.listChannels}")
    private String urlListChannels;
    @Value("${config.default.channel}")
    private String defaultChannel;

    @Override
    public JSONArray listBrands() {
        //noinspection Duplicates
        try {
            final String url = address + "/" + listBrandUri;
            final String jsonText = "{}";
            final String respStr = this.okHttpOperator.postJsonTextForTextResp(url, jsonText);
            final JSONObject jsonObjectGet = JSONObject.parseObject(respStr);
            final Boolean success = jsonObjectGet.getBoolean("success");
            if (success != null && success) {
                return jsonObjectGet.getJSONArray("list");
            }
        } catch (Exception e) {
            logger.error("请求PCM查询品牌列表", e);
        }
        return new JSONArray();
    }

    @Override
    public List<CategoryPojo> listCategoryByChannel(String channel) {
        final List<CategoryPojo> categories = Lists.newArrayList();
        try {
            final String url = address + "/" + uriListCategoryByChannel;
            final JSONObject jsonBody = new JSONObject();
            if (StringUtils.isNotBlank(channel)) {
                jsonBody.put("channelCode", channel);
            } else {
                jsonBody.put("channelCode", defaultChannel);
            }
            final String jsonText = jsonBody.toJSONString();
            final String respStr = this.okHttpOperator.postJsonTextForTextResp(url, jsonText);
            final JSONObject jsonObjectGet = JSONObject.parseObject(respStr);
            final Boolean success = jsonObjectGet.getBoolean("success");
            if (success != null && success) {
                final JSONArray categoryJsonArray = jsonObjectGet.getJSONArray("list");
                final int size = categoryJsonArray.size();
                for (int i = 0; i < size; i++) {
                    final String categoryId = categoryJsonArray.getString(i);
                    getCategoryInfo(categoryId).ifPresent(categories::add);
                }
            }
        } catch (Exception e) {
            logger.error("请求PCM查询渠道下分类列表失败", e);
        }
        return categories;
    }

    @Override
    public List<CategoryPojo> listDisplayCategoryChildren(String parentId) {
        final List<CategoryPojo> categories = Lists.newArrayList();
        try {
            final String url = address + "/" + uriListSubCategories;
            final JSONObject jsonBody = new JSONObject();
            if (StringUtils.isNotBlank(parentId)) {
                jsonBody.put("categoryId", parentId);
            }
            final String jsonText = jsonBody.toJSONString();
            final String respStr = this.okHttpOperator.postJsonTextForTextResp(url, jsonText);
            final JSONObject jsonObjectGet = JSONObject.parseObject(respStr);
            final Boolean success = jsonObjectGet.getBoolean("success");
            if (success != null && success) {
                final JSONArray categoryJsonArray = jsonObjectGet.getJSONArray("list");
                final int size = categoryJsonArray.size();
                for (int i = 0; i < size; i++) {
                    final JSONObject categoryInfo = categoryJsonArray.getJSONObject(i);
                    final String categoryId = categoryInfo.getString("categoryId");
                    getCategoryInfo(categoryId).ifPresent(categories::add);
                }
            }
        } catch (Exception e) {
            logger.error("请求PCM查询子级分类列表失败", e);
        }
        return categories;
    }

    @Override
    public Optional<CategoryPojo> getCategoryInfo(String categoryId) {
        try {
            final String url = address + "/" + uriGetCategoryInfo;
            final JSONObject jsonBody = new JSONObject();
            if (StringUtils.isNotBlank(categoryId)) {
                jsonBody.put("categoryId", categoryId);
            }
            final String jsonText = jsonBody.toJSONString();
            final String respStr = this.okHttpOperator.postJsonTextForTextResp(url, jsonText);
            final JSONObject jsonObjectGet = JSONObject.parseObject(respStr);
            final Boolean success = jsonObjectGet.getBoolean("success");
            if (success != null && success) {
                final CategoryPojo categoryPOJO = new CategoryPojo();
                final JSONObject categoryInfo = jsonObjectGet.getJSONObject("item");
                categoryPOJO.setCategoryId(Validate.notBlank(categoryInfo.getString("categoryId"), "分类编码为空").trim());
                categoryPOJO
                        .setCategoryName(Validate.notBlank(categoryInfo.getString("categoryName"), "分类名称为空").trim());
                categoryPOJO.setLeafLevel(Validate.notNull(categoryInfo.getBoolean("leafLevel"), "是否叶子级分类标记为空"));
                categoryPOJO.setSelfBuilt(categoryInfo.getBoolean("selfBuilt"));
                categoryPOJO.setRootCategoryId(
                        Validate.notBlank(categoryInfo.getString("rootCategoryId"), "根分类编码为空").trim());
                categoryPOJO.setParentCategoryId(categoryInfo.getString("parentCategoryId"));
                categoryPOJO.setLevel(Validate.notNull(categoryInfo.getInteger("level"), "分类Level为空"));
                categoryPOJO.setOrder(Validate.notNull(categoryInfo.getInteger("order"), "同级分类序号为空"));
                categoryPOJO
                        .setChannel(Validate.notBlank(categoryInfo.getString("channel"), "分类所属销售渠道编码为空").trim());
                return Optional.of(categoryPOJO);
            }
        } catch (Exception e) {
            logger.error("请求PCM查询分类信息失败", e);
        }
        return Optional.empty();
    }

    @Override
    public JSONArray listChannels() {
        //noinspection Duplicates
        try {
            final String url = address + "/" + urlListChannels;
            final String jsonText = "{}";
            final String respStr = this.okHttpOperator.postJsonTextForTextResp(url, jsonText);
            final JSONObject jsonObjectGet = JSONObject.parseObject(respStr);
            final Boolean success = jsonObjectGet.getBoolean("success");
            if (success != null && success) {
                return jsonObjectGet.getJSONArray("list");
            }
        } catch (Exception e) {
            logger.error("请求PCM查询渠道列表失败", e);
        }
        return new JSONArray();
    }
}
