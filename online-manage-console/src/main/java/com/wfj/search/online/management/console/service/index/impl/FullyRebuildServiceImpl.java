package com.wfj.search.online.management.console.service.index.impl;

import com.alibaba.fastjson.JSONObject;
import com.wfj.platform.util.zookeeper.discovery.SpringMvcServiceProvider;
import com.wfj.search.online.management.console.service.index.IFullyRebuildService;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <br/>create at 15-11-4
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
@Service("fullyRebuildService")
public class FullyRebuildServiceImpl implements IFullyRebuildService {
    private static final Logger logger = LoggerFactory.getLogger(FullyRebuildServiceImpl.class);
    @Autowired
    private SpringMvcServiceProvider springMvcServiceProvider;
    private OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build();
    private MediaType mediaTypeJson = MediaType.parse("application/json; charset=utf-8");

    @Override
    public JSONObject refreshEsData(String message) {
        String url;
        try {
            url = springMvcServiceProvider.provideServiceAddress("online-fullyRebuildEs");
            if (url == null) {
                throw new RuntimeException("全量刷新ES数据服务未发现！");
            }
            RequestBody requestBody = RequestBody.create(mediaTypeJson, message);
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                return JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("全量刷新ES数据操作请求返回非2xx响应");
            }
        } catch (Exception e) {
            JSONObject json = new JSONObject();
            json.put("success", false);
            json.put("errorCode", 500);
            json.put("message", "全量刷新ES数据异常，请稍后再试！");
            logger.error("全量刷新ES数据异常", e);
            return json;
        }
    }

    @Override
    public JSONObject refreshItems(String message) {
        String url;
        try {
            url = springMvcServiceProvider.provideServiceAddress("online-fullyRebuildIndex");
            if (url == null) {
                throw new RuntimeException("全量刷新专柜商品服务未发现！");
            }
            RequestBody requestBody = RequestBody.create(mediaTypeJson, message);
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                return JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("全量刷新专柜商品索引操作请求返回非2xx响应");
            }
        } catch (Exception e) {
            JSONObject json = new JSONObject();
            json.put("success", false);
            json.put("errorCode", 500);
            json.put("message", "全量刷新专柜商品服务，请稍后再试！");
            logger.error("全量刷新专柜商品服务异常 FullyRebuildIndexServiceImpl.refreshItems", e);
            return json;
        }
    }
}
