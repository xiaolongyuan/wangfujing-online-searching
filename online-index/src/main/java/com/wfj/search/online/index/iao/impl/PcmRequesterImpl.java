package com.wfj.search.online.index.iao.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wfj.search.online.common.pojo.*;
import com.wfj.search.online.index.iao.IPcmRequester;
import com.wfj.search.online.index.iao.RequestException;
import com.wfj.search.online.index.util.PcmUrlConfig;
import com.wfj.search.online.index.util.PojoUtils;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <br/>create at 15-9-8
 *
 * @author liuxh
 * @author liufl / 2015-1013
 * @since 1.0.0
 */
@Component("pcmRequester")
public class PcmRequesterImpl implements IPcmRequester {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private PcmUrlConfig pcmUrlConfig;
    private OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build();
    private MediaType mediaTypeJson = MediaType.parse("application/json; charset=utf-8");

    @SuppressWarnings("StringBufferMayBeStringBuilder")
    @Override
    public List<ItemPojo> listItems(List<String> itemIdList) throws RequestException {
        try {
            if (itemIdList == null || itemIdList.size() == 0) {
                return Collections.emptyList();
            }
            StringBuffer itemIds = new StringBuffer();
            itemIdList.stream().filter(itemId -> itemId != null && !itemId.isEmpty())
                    .forEach(itemId -> itemIds.append(itemId).append(","));
            itemIds.deleteCharAt(itemIds.length() - 1);
            JSONObject params = new JSONObject();
            params.put("itemIds", itemIds.toString());
            String url = pcmUrlConfig.getUrlListItemsByItemId();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【分页列出参加活动商品信息】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                JSONArray jsonArray = json.getJSONArray("list");
                List<ItemPojo> itemList = Lists.newArrayList();
                for (int i = 0; i < jsonArray.size(); i++) {
                    itemList.add(PojoUtils.json2Item(jsonArray.getJSONObject(i)));
                }
                return itemList;
            } else {
                throw new RequestException("调用PCM【分页列出参加活动商品信息】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【分页列出参加活动商品信息】接口失败", e);
        }
    }

    @Override
    public Optional<ItemPojo> getItemsByItemId(String itemId) throws RequestException {
        if (StringUtils.isBlank(itemId)) {
            return Optional.empty();
        }
        List<ItemPojo> list = this.listItems(Collections.singletonList(itemId));
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    @Override
    public int countItems() throws RequestException {
        try {
            JSONObject params = new JSONObject();
            params.put("start", 0);
            params.put("fetch", 0);
            String url = pcmUrlConfig.getUrlListItems();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【分页列出专柜商品信息】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                Integer total = json.getInteger("total");
                if (total != null) {
                    return total;
                }
            }
            throw new RequestException("调用PCM【分页列出专柜商品信息】接口返回结果为：\n\r" + json.toString());
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【分页列出专柜商品信息】接口失败", e);
        }
    }

    @Override
    public Page<ItemPojo> listItems(int start, int fetch) throws RequestException {
        try {
            if (fetch <= 0) {
                return new PageImpl<>(Collections.emptyList(), 0, fetch);
            }
            JSONObject params = new JSONObject();
            params.put("start", start < 0 ? 0 : start);
            params.put("fetch", fetch);
            String url = pcmUrlConfig.getUrlListItems();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【分页列出专柜商品信息】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                JSONArray jsonArray = json.getJSONArray("list");
                List<ItemPojo> itemList = Lists.newArrayList();
                for (int i = 0; i < jsonArray.size(); i++) {
                    itemList.add(PojoUtils.json2Item(jsonArray.getJSONObject(i)));
                }
                long total = json.getLong("total");
                return new PageImpl<>(itemList, total, fetch);
            } else {
                throw new RequestException("调用PCM【分页列出专柜商品信息】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            logger.error("调用PCM【分页列出专柜商品信息】接口失败", e);
            throw new RequestException("调用PCM【分页列出专柜商品信息】接口失败", e);
        }
    }

    @Override
    @Cacheable(PCM_SKU)
    public SkuPojo getSkuInfo(String skuId) throws RequestException {
        return directGetSkuInfo(skuId);
    }

    @Override
    @CachePut(PCM_SKU)
    public SkuPojo directGetSkuInfo(String skuId) throws RequestException {
        try {
            JSONObject params = new JSONObject();
            params.put("skuId", skuId);
            String url = pcmUrlConfig.getUrlGetSkuInfo();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【获取SKU信息】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                return PojoUtils.json2Sku(json.getJSONObject("item"));
            } else {
                throw new RequestException("调用PCM【获取SKU信息】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【获取SKU信息】接口失败", e);
        }
    }

    @Override
    @Cacheable(PCM_SPU)
    public SpuPojo getSpuInfo(String spuId) throws RequestException {
        return directGetSpuInfo(spuId);
    }

    @Override
    @CachePut(PCM_SPU)
    public SpuPojo directGetSpuInfo(String spuId) throws RequestException {
        try {
            JSONObject params = new JSONObject();
            params.put("spuId", spuId);
            String url = pcmUrlConfig.getUrlGetSpuInfo();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【获取SPU信息】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                return PojoUtils.json2Spu(json.getJSONObject("item"));
            } else {
                throw new RequestException("调用PCM【获取SPU信息】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【获取SPU信息】接口失败", e);
        }
    }

    @Override
    @Cacheable(PCM_BRAND)
    public BrandPojo getBrandInfo(String brandId) throws RequestException {
        return directGetBrandInfo(brandId);
    }

    @Override
    @CachePut(PCM_BRAND)
    public BrandPojo directGetBrandInfo(String brandId) throws RequestException {
        try {
            JSONObject params = new JSONObject();
            params.put("brandId", brandId);
            String url = pcmUrlConfig.getUrlGetBrandInfo();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【获取品牌详细信息】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                return PojoUtils.json2Brand(json.getJSONObject("item"));
            } else {
                throw new RequestException("调用PCM【获取品牌详细信息】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【获取品牌详细信息】接口失败", e);
        }
    }

    @Override
    @Cacheable(PCM_CATEGORY)
    public CategoryPojo getCategoryInfo(String categoryId) throws RequestException {
        return this.directGetCategoryInfo(categoryId);
    }

    @Override
    @CachePut(PCM_CATEGORY)
    public CategoryPojo directGetCategoryInfo(String categoryId) throws RequestException {
        try {
            JSONObject params = new JSONObject();
            params.put("categoryId", categoryId);
            String url = pcmUrlConfig.getUrlGetCategoryInfo();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【获取分类详细信息】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                return PojoUtils.json2Category(json.getJSONObject("item"));
            } else {
                throw new RequestException("调用PCM【获取分类详细信息】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【获取分类详细信息】接口失败", e);
        }
    }

    @Override
    public List<String> listSkuIdBySpuId(String spuId) throws RequestException {
        try {
            JSONObject params = new JSONObject();
            params.put("spuId", spuId);
            String url = pcmUrlConfig.getUrlListSkuIdBySpuId();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【按SPU列出SKU编码】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                JSONArray jsonArray = json.getJSONArray("list");
                List<String> list = Lists.newArrayList();
                for (int i = 0; i < jsonArray.size(); i++) {
                    list.add(jsonArray.getJSONObject(i).getString("skuId"));
                }
                return list;
            } else {
                throw new RequestException("调用PCM【按SPU列出SKU编码】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【按SPU列出SKU编码】接口失败", e);
        }
    }

    @Override
    public List<String> listItemIdBySkuId(String skuId) throws RequestException {
        try {
            JSONObject params = new JSONObject();
            params.put("skuId", skuId);
            String url = pcmUrlConfig.getUrlListItemIdBySkuId();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【按SKU列出专柜商品编码】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                JSONArray jsonArray = json.getJSONArray("list");
                List<String> list = Lists.newArrayList();
                for (int i = 0; i < jsonArray.size(); i++) {
                    list.add(jsonArray.getJSONObject(i).getString("itemId"));
                }
                return list;
            } else {
                throw new RequestException("调用PCM【按SKU列出专柜商品编码】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【按SKU列出专柜商品编码】接口失败", e);
        }
    }

    @Override
    public List<BrandPojo> listBrands() throws RequestException {
        try {
            String url = pcmUrlConfig.getUrlListBrands();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, "{}")).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【列出所有网站品牌】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                JSONArray jsonArray = json.getJSONArray("list");
                List<BrandPojo> list = Lists.newArrayList();
                for (int i = 0; i < jsonArray.size(); i++) {
                    list.add(PojoUtils.json2Brand(jsonArray.getJSONObject(i)));
                }
                return list;
            } else {
                throw new RequestException("调用PCM【列出所有网站品牌】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【列出所有网站品牌】接口失败", e);
        }
    }

    @Override
    public List<String> listSubCategories(String categoryId) throws RequestException {
        try {
            JSONObject params = new JSONObject();
            params.put("categoryId", categoryId);
            String url = pcmUrlConfig.getUrlListSubCategories();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【列出所有子级分类】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                JSONArray jsonArray = json.getJSONArray("list");
                List<String> list = Lists.newArrayList();
                for (int i = 0; i < jsonArray.size(); i++) {
                    list.add(jsonArray.getJSONObject(i).getString("categoryId"));
                }
                return list;
            } else {
                throw new RequestException("调用PCM【列出所有子级分类】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【列出所有子级分类】接口失败", e);
        }
    }

    @Override
    public int totalSpuOfBrandId(String brandId) throws RequestException {
        try {
            JSONObject params = new JSONObject();
            params.put("brandId", brandId);
            params.put("start", 0);
            params.put("fetch", 0);
            String url = pcmUrlConfig.getUrlListSpuIdByBrandId();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【按品牌编码列出SPU编码】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                return json.getInteger("total");
            } else {
                throw new RequestException("调用PCM【按品牌编码列出SPU编码】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【按品牌编码列出SPU编码】接口失败", e);
        }
    }

    @Override
    public List<String> listSpuIdByBrandId(String brandId, int start, int fetch) throws RequestException {
        try {
            JSONObject params = new JSONObject();
            params.put("brandId", brandId);
            params.put("start", start);
            params.put("fetch", fetch);
            String url = pcmUrlConfig.getUrlListSpuIdByBrandId();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【按品牌编码列出SPU编码】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                JSONArray jsonArray = json.getJSONArray("list");
                List<String> list = Lists.newArrayList();
                for (int i = 0; i < jsonArray.size(); i++) {
                    list.add(jsonArray.getJSONObject(i).getString("spuId"));
                }
                return list;
            } else {
                throw new RequestException("调用PCM【按品牌编码列出SPU编码】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【按品牌编码列出SPU编码】接口失败", e);
        }
    }

    @Override
    public int totalSpuOfCategoryId(String categoryId) throws RequestException {
        try {
            JSONObject params = new JSONObject();
            params.put("categoryId", categoryId);
            params.put("start", 0);
            params.put("fetch", 0);
            String url = pcmUrlConfig.getUrlListSpuIdByCategoryId();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【按叶子分类编码列出SPU编码】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                return json.getInteger("total");
            } else {
                throw new RequestException("调用PCM【按叶子分类编码列出SPU编码】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【按叶子分类编码列出SPU编码】接口失败", e);
        }
    }

    @Override
    public List<String> listSpuIdByCategoryId(String categoryId, int start, int fetch) throws RequestException {
        try {
            JSONObject params = new JSONObject();
            params.put("categoryId", categoryId);
            params.put("start", start);
            params.put("fetch", fetch);
            String url = pcmUrlConfig.getUrlListSpuIdByCategoryId();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【按叶子级分类编码列出SPU编码】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                JSONArray jsonArray = json.getJSONArray("list");
                List<String> list = Lists.newArrayList();
                for (int i = 0; i < jsonArray.size(); i++) {
                    list.add(jsonArray.getJSONObject(i).getString("spuId"));
                }
                return list;
            } else {
                throw new RequestException("调用PCM【按叶子级分类编码列出SPU编码】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【按叶子级分类编码列出SPU编码】接口失败", e);
        }
    }

    @Override
    public int countItemPriceChanges(Date after, Date before) throws RequestException {
        try {
            JSONObject params = new JSONObject();
            params.put("after", DATE_FORMAT.format(after));
            params.put("before", DATE_FORMAT.format(before));
            params.put("start", 0);
            params.put("fetch", 1);
            String url = pcmUrlConfig.getUrlListItemPriceChanges();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【分页列出指定时间段内商品变价信息】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                return json.getInteger("total");
            } else {
                throw new RequestException("调用PCM【分页列出指定时间段内商品变价信息】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【分页列出指定时间段内商品变价信息】接口失败", e);
        }
    }

    @Override
    public List<ItemPojo> listItemPriceChanges(Date after, Date before, int start, int fetch) throws RequestException {
        try {
            JSONObject params = new JSONObject();
            params.put("after", DATE_FORMAT.format(after));
            params.put("before", DATE_FORMAT.format(before));
            params.put("start", start);
            params.put("fetch", fetch);
            String url = pcmUrlConfig.getUrlListItemPriceChanges();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【分页列出指定时间段内商品变价信息】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                JSONArray jsonArray = json.getJSONArray("list");
                List<ItemPojo> list = Lists.newArrayList();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    ItemPojo pojo = new ItemPojo();
                    pojo.setItemId(jo.getString("itemId"));
                    pojo.setCurrentPrice(new BigDecimal(jo.getDouble("currentPrice")));
                    list.add(pojo);
                }
                return list;
            } else {
                throw new RequestException("调用PCM【分页列出指定时间段内商品变价信息】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【分页列出指定时间段内商品变价信息】接口失败", e);
        }
    }

    @Override
    public int countActivityItems(String activityId) throws RequestException {
        try {
            JSONObject params = new JSONObject();
            params.put("activeId", activityId);
            params.put("start", 0);
            params.put("fetch", 1);
            String url = pcmUrlConfig.getUrlListItemsOfActivity();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【分页列出指定活动下商品】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                return json.getInteger("total");
            } else {
                throw new RequestException("调用PCM【分页列出指定活动下商品】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【分页列出指定活动下商品】接口失败", e);
        }
    }

    @Override
    public List<ItemPojo> listActivityItems(String activityId, int start, int fetch) throws RequestException {
        try {
            JSONObject params = new JSONObject();
            params.put("activeId", activityId);
            params.put("start", start);
            params.put("fetch", fetch);
            String url = pcmUrlConfig.getUrlListItemsOfActivity();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【分页列出指定活动下商品】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                JSONArray jsonArray = json.getJSONArray("list");
                List<ItemPojo> itemList = Lists.newArrayList();
                for (int i = 0; i < jsonArray.size(); i++) {
                    itemList.add(PojoUtils.json2Item(jsonArray.getJSONObject(i)));
                }
                return itemList;
            } else {
                throw new RequestException("调用PCM【分页列出指定活动下商品】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【分页列出指定活动下商品】接口失败", e);
        }
    }

    @Override
    public int countClosedActivities(Date after, Date before) throws RequestException {
        try {
            JSONObject params = new JSONObject();
            params.put("after", DATE_FORMAT.format(after));
            params.put("before", DATE_FORMAT.format(before));
            params.put("start", 0);
            params.put("fetch", 1);
            String url = pcmUrlConfig.getUrlListClosedActivities();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【分页列出指定时间区间内结束的活动】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                return json.getInteger("total");
            } else {
                throw new RequestException("调用PCM【分页列出指定时间区间内结束的活动】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【分页列出指定时间区间内结束的活动】接口失败", e);
        }
    }

    @Override
    public List<String> listClosedActivities(Date after, Date before, int start, int fetch)
            throws RequestException {
        try {
            JSONObject params = new JSONObject();
            params.put("after", DATE_FORMAT.format(after));
            params.put("before", DATE_FORMAT.format(before));
            params.put("start", 0);
            params.put("fetch", 1);
            String url = pcmUrlConfig.getUrlListClosedActivities();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【分页列出指定时间区间内结束的活动】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                JSONArray jsonArray = json.getJSONArray("list");
                List<String> list = Lists.newArrayList();
                for (int i = 0; i < jsonArray.size(); i++) {
                    list.add(jsonArray.getJSONObject(i).getString("activeId"));
                }
                return list;
            } else {
                throw new RequestException("调用PCM【分页列出指定时间区间内结束的活动】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【分页列出指定时间区间内结束的活动】接口失败", e);
        }
    }

    @Override
    public int countActiveActivities(Date when) throws RequestException {
        try {
            JSONObject params = new JSONObject();
            params.put("time", DATE_FORMAT.format(when));
            params.put("start", 0);
            params.put("fetch", 1);
            String url = pcmUrlConfig.getUrlListActiveActivities();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【分页列出指定时间点活动的活动】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                return json.getInteger("total");
            } else {
                throw new RequestException("调用PCM【分页列出指定时间点活动的活动】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【分页列出指定时间点活动的活动】接口失败", e);
        }
    }

    @Override
    public List<ActivityPojo> listActiveActivities(Date when, int start, int fetch) throws RequestException {
        try {
            JSONObject params = new JSONObject();
            params.put("time", DATE_FORMAT.format(when));
            params.put("start", start);
            params.put("fetch", fetch);
            String url = pcmUrlConfig.getUrlListActiveActivities();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用PCM【分页列出指定时间点活动的活动】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                JSONArray jsonArray = json.getJSONArray("list");
                List<ActivityPojo> list = Lists.newArrayList();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ActivityPojo activityPojo = new ActivityPojo();
                    activityPojo.setActiveId(jsonObject.getString("activeId"));
                    activityPojo.setActiveName(jsonObject.getString("activeName"));
                    list.add(activityPojo);
                }
                return list;
            } else {
                throw new RequestException("调用PCM【分页列出指定时间点活动的活动】接口返回结果为：\n\r" + json.toString());
            }
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用PCM【分页列出指定时间点活动的活动】接口失败", e);
        }
    }
}
