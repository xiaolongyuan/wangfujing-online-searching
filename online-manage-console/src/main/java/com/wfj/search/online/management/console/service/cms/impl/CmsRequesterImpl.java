package com.wfj.search.online.management.console.service.cms.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wfj.search.online.management.console.service.cms.ICmsRequester;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <br/>create at 15-12-22
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
@Service("cmsRequester")
public class CmsRequesterImpl implements ICmsRequester {
    private static final Logger logger = LoggerFactory.getLogger(CmsRequesterImpl.class);
    @Value("${cms.address}")
    private String address;
    @Value("${cms.uri.getSiteList}")
    private String urlGetSiteList;
    @Value("${cms.uri.getChannelListBySid}")
    private String urlGetChannelListBySid;
    private OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build();

    @Override
    public JSONArray getSiteList() {
        try {
            Request request = new Request.Builder().url(address + "/" + urlGetSiteList)
                    .get().build();
            Response response = okHttpClient.newCall(request).execute();
            JSONObject jsonObjectGet;
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                jsonObjectGet = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("请求CMS查询站点列表返回非2xx响应");
            }
            Boolean success = jsonObjectGet.getBoolean("success");
            if (success != null && success) {
                return jsonObjectGet.getJSONArray("list");
            }
        } catch (Exception e) {
            logger.error("查询CMS站点列表失败", e);
        }
        return new JSONArray();
    }

    @Override
    public JSONArray getChannelListBySid(String siteId) {
        try {
            String url = address + "/" + urlGetChannelListBySid;
            if (StringUtils.isNotBlank(siteId)) {
                url += "?_site_id_param=" + siteId;
            }
            JSONObject jsonObjectGet;
            Request request = new Request.Builder().url(url)
                    .get().build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                jsonObjectGet = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("请求CMS查询频道列表返回非2xx响应");
            }
            Boolean success = jsonObjectGet.getBoolean("success");
            if (success != null && success) {
                return jsonObjectGet.getJSONArray("list");
            }
        } catch (Exception e) {
            logger.error("请求CMS查询频道列表失败", e);
        }
        return new JSONArray();
    }
}
