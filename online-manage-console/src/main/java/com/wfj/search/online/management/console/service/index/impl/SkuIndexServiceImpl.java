package com.wfj.search.online.management.console.service.index.impl;

import com.alibaba.fastjson.JSONObject;
import com.wfj.platform.util.zookeeper.discovery.SpringMvcServiceProvider;
import com.wfj.search.online.management.console.service.index.ISkuIndexService;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <br/>create at 15-11-2
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
@Service("skuIndexService")
public class SkuIndexServiceImpl implements ISkuIndexService {
    private static final Logger logger = LoggerFactory.getLogger(SkuIndexServiceImpl.class);
    @Autowired
    private SpringMvcServiceProvider springMvcServiceProvider;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private MediaType mediaTypeJson = MediaType.parse("application/json; charset=utf-8");

    @Override
    public JSONObject refreshItems(String message) {
        String url;
        try {
            url = springMvcServiceProvider.provideServiceAddress("online-ops-indexSku");
            if (url == null) {
                throw new RuntimeException("根据sku刷新专柜商品服务未发现！");
            }
            RequestBody requestBody = RequestBody.create(mediaTypeJson, message);
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                return JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("根据sku刷新专柜商品操作请求返回非2xx响应");
            }
        } catch (Exception e) {
            JSONObject json = new JSONObject();
            json.put("success", false);
            json.put("errorCode", 500);
            json.put("message", "根据sku刷新专柜商品服务异常，请稍后再试！");
            logger.error("根据sku刷新专柜商品服务异常 SkuItemIndexServiceImpl.refreshItems", e);
            return json;
        }
    }

    @Override
    public JSONObject removeItems(String message) {
        String url;
        try {
            url = springMvcServiceProvider.provideServiceAddress("online-ops-removeSku");
            if (url == null) {
                throw new RuntimeException("根据sku移除专柜商品服务未发现！");
            }
            RequestBody requestBody = RequestBody.create(mediaTypeJson, message);
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                return JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("根据sku移除专柜商品操作请求返回非2xx响应");
            }
        } catch (Exception e) {
            JSONObject json = new JSONObject();
            json.put("success", false);
            json.put("errorCode", 500);
            json.put("message", "根据sku移除专柜商品服务异常，请稍后再试！");
            logger.error("根据sku移除专柜商品服务异常 SkuItemIndexServiceImpl.removeItems", e);
            return json;
        }
    }
}
