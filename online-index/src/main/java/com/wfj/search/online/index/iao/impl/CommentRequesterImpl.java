package com.wfj.search.online.index.iao.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wfj.search.online.common.pojo.CommentPojo;
import com.wfj.search.online.index.iao.ICommentRequester;
import com.wfj.search.online.index.iao.RequestException;
import com.wfj.search.online.index.util.CommentUrlConfig;
import com.wfj.search.online.index.util.PojoUtils;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <br/>create at 15-12-28
 *
 * @author liuxh
 * @since 1.0.0
 */
@Component("commentRequester")
public class CommentRequesterImpl implements ICommentRequester {
    @Autowired
    private CommentUrlConfig commentUrlConfig;
    private OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build();
    private MediaType mediaTypeJson = MediaType.parse("application/json; charset=utf-8");

    @Override
    public List<CommentPojo> listCommentsWithPage(int start, int fetch) throws RequestException {
        return listCommentsWithPage(null, null, null, null, null, start, fetch);
    }

    @Override
    public List<CommentPojo> listCommentsWithPage(String memberId, String spuId, String skuId, String itemId,
            String orderNo, int start, int fetch) throws RequestException {
        try {
            JSONObject params = new JSONObject();
            if (start < 0) {
                start = 0;
            }
            if (fetch < 0) {
                fetch = 0;
            }
            params.put("start", start);
            params.put("fetch", fetch);
            if (StringUtils.isNotBlank(memberId)) {
                params.put("memberId", memberId);
            }
            if (StringUtils.isNotBlank(spuId)) {
                params.put("spuId", spuId);
            }
            if (StringUtils.isNotBlank(skuId)) {
                params.put("skuId", skuId);
            }
            if (StringUtils.isNotBlank(itemId)) {
                params.put("itemId", itemId);
            }
            if (StringUtils.isNotBlank(orderNo)) {
                params.put("orderNo", orderNo);
            }
            String url = commentUrlConfig.getUrlListCommentsWithPage();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用评论接口【分页列出评论信息】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                JSONArray jsonArray = json.getJSONArray("list");
                List<CommentPojo> list = Lists.newArrayList();
                for (int i = 0; i < jsonArray.size(); i++) {
                    list.add(PojoUtils.json2Comment(jsonArray.getJSONObject(i)));
                }
                return list;
            }
            throw new RequestException("调用评论接口【分页列出评论信息】返回结果为：\n" + json.toString());
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用评论接口【分页列出评论信息】失败", e);
        }
    }

    @Override
    public int count() throws RequestException {
        return count(null, null, null, null, null);
    }

    @Override
    public int count(String memberId, String spuId, String skuId, String itemId, String orderNo)
            throws RequestException {
        try {
            JSONObject params = new JSONObject();
            params.put("start", 0);
            params.put("fetch", 0);
            if (StringUtils.isNotBlank(memberId)) {
                params.put("memberId", memberId);
            }
            if (StringUtils.isNotBlank(spuId)) {
                params.put("spuId", spuId);
            }
            if (StringUtils.isNotBlank(skuId)) {
                params.put("skuId", skuId);
            }
            if (StringUtils.isNotBlank(itemId)) {
                params.put("itemId", itemId);
            }
            if (StringUtils.isNotBlank(orderNo)) {
                params.put("orderNo", orderNo);
            }
            String url = commentUrlConfig.getUrlListCommentsWithPage();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用评论接口【分页列出评论信息】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                Integer total = json.getInteger("total");
                if (total != null) {
                    return total;
                }
            }
            throw new RequestException("调用评论接口【分页列出评论信息】返回结果为：\n" + json.toString());
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用评论接口【分页列出评论信息】失败", e);
        }
    }

    @Override
    public List<CommentPojo> listCommentsByIds(List<String> commentIds) throws RequestException {
        try {
            if (commentIds == null || commentIds.isEmpty()) {
                return Collections.emptyList();
            }
            JSONObject params = new JSONObject();
            params.put("commentIds", commentIds);
            String url = commentUrlConfig.getUrlListCommentsByIds();
            JSONObject json;
            Request request = new Request.Builder().url(url)
                    .post(RequestBody.create(mediaTypeJson, params.toJSONString())).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String respStr = response.body().string();
                json = JSONObject.parseObject(respStr);
            } else {
                throw new RuntimeException("调用评论接口【根据评论id查询评论信息】返回非2xx响应");
            }
            Boolean success = json.getBoolean("success");
            if (success != null && success) {
                JSONArray jsonArray = json.getJSONArray("list");
                List<CommentPojo> list = Lists.newArrayList();
                for (int i = 0; i < jsonArray.size(); i++) {
                    list.add(PojoUtils.json2Comment(jsonArray.getJSONObject(i)));
                }
                return list;
            }
            throw new RequestException("调用评论接口【根据评论id查询评论信息】返回结果为：\n" + json.toString());
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RequestException("调用评论接口【根据评论id查询评论信息】失败", e);
        }
    }

    @Override
    public Optional<CommentPojo> getCommentById(String commentId) throws RequestException {
        if (StringUtils.isBlank(commentId)) {
            return Optional.empty();
        }
        List<CommentPojo> commentList = listCommentsByIds(Collections.singletonList(commentId));
        if (commentList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(commentList.get(0));
    }
}
