package com.wfj.search.online.management.console.service.pcm.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wfj.search.online.common.pojo.CategoryPojo;
import com.wfj.search.online.management.console.service.pcm.IPcmRequester;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private OkHttpClient okHttpClient = new OkHttpClient();
    private MediaType mediaTypeJson = MediaType.parse("application/json; charset=utf-8");

    @Override
    public JSONArray listBrands() {
        try {
            Request request = new Request.Builder().url(address + "/" + listBrandUri)
                    .post(RequestBody.create(mediaTypeJson, "{}")).build();
            Response response = okHttpClient.newCall(request).execute();
            JSONObject jsonObjectGet;
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                jsonObjectGet = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("请求PCM查询品牌列表返回非2xx响应");
            }
            Boolean success = jsonObjectGet.getBoolean("success");
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
        List<CategoryPojo> categories = Lists.newArrayList();
        try {
            String url = address + "/" + uriListCategoryByChannel;
            JSONObject jsonBody = new JSONObject();
            if (StringUtils.isNotBlank(channel)) {
                jsonBody.put("channelCode", channel);
            } else {
                jsonBody.put("channelCode", defaultChannel);
            }
            JSONObject jsonObjectGet;
            RequestBody requestBody = RequestBody.create(mediaTypeJson, jsonBody.toJSONString());
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                jsonObjectGet = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("请求PCM查询渠道下分类列表返回非2xx响应");
            }
            Boolean success = jsonObjectGet.getBoolean("success");
            if (success != null && success) {
                JSONArray categoryJsonArray = jsonObjectGet.getJSONArray("list");
                for (int i = 0; i < categoryJsonArray.size(); i++) {
                    getCategoryInfo(categoryJsonArray.getString(i)).ifPresent(categories::add);
                }
            }
        } catch (Exception e) {
            logger.error("请求PCM查询渠道下分类列表失败", e);
        }
        return categories;
    }

    @Override
    public List<CategoryPojo> listDisplayCategoryChildren(String parentId) {
        List<CategoryPojo> categories = Lists.newArrayList();
        try {
            String url = address + "/" + uriListSubCategories;
            JSONObject jsonBody = new JSONObject();
            if (StringUtils.isNotBlank(parentId)) {
                jsonBody.put("categoryId", parentId);
            }
            JSONObject jsonObjectGet;
            RequestBody requestBody = RequestBody.create(mediaTypeJson, jsonBody.toJSONString());
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                jsonObjectGet = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("请求PCM查询子级分类列表返回非2xx响应");
            }
            Boolean success = jsonObjectGet.getBoolean("success");
            if (success != null && success) {
                JSONArray categoryJsonArray = jsonObjectGet.getJSONArray("list");
                for (int i = 0; i < categoryJsonArray.size(); i++) {
                    JSONObject categoryInfo = categoryJsonArray.getJSONObject(i);
                    getCategoryInfo(categoryInfo.getString("categoryId")).ifPresent(categories::add);
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
            String url = address + "/" + uriGetCategoryInfo;
            JSONObject jsonBody = new JSONObject();
            if (StringUtils.isNotBlank(categoryId)) {
                jsonBody.put("categoryId", categoryId);
            }
            JSONObject jsonObjectGet;
            RequestBody requestBody = RequestBody.create(mediaTypeJson, jsonBody.toJSONString());
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                jsonObjectGet = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("请求PCM查询分类信息返回非2xx响应");
            }
            Boolean success = jsonObjectGet.getBoolean("success");
            if (success != null && success) {
                CategoryPojo categoryPOJO = new CategoryPojo();
                JSONObject categoryInfo = jsonObjectGet.getJSONObject("item");
                categoryPOJO.setCategoryId(categoryInfo.getString("categoryId"));
                categoryPOJO.setCategoryName(categoryInfo.getString("categoryName"));
                categoryPOJO.setLeafLevel(categoryInfo.getBoolean("leafLevel"));
                categoryPOJO.setSelfBuilt(categoryInfo.getBoolean("selfBuilt"));
                categoryPOJO.setRootCategoryId(categoryInfo.getString("rootCategoryId"));
                categoryPOJO.setParentCategoryId(categoryInfo.getString("parentCategoryId"));
                categoryPOJO.setLevel(categoryInfo.getInteger("level"));
                categoryPOJO.setOrder(categoryInfo.getInteger("order"));
                categoryPOJO.setChannel(categoryInfo.getString("channel"));
                return Optional.of(categoryPOJO);
            }
        } catch (Exception e) {
            logger.error("请求PCM查询分类信息失败", e);
        }
        return Optional.empty();
    }

    @Override
    public JSONArray listChannels() {
        try {
            String url = address + "/" + urlListChannels;
            JSONObject jsonObjectGet;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, "{}")).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                jsonObjectGet = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("请求PCM查询渠道列表信息返回非2xx响应");
            }
            Boolean success = jsonObjectGet.getBoolean("success");
            if (success != null && success) {
                return jsonObjectGet.getJSONArray("list");
            }
        } catch (Exception e) {
            logger.error("请求PCM查询渠道列表失败", e);
        }
        return new JSONArray();
    }
}
