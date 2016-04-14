package com.wfj.search.online.index.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>create at 16-3-31</p>
 *
 * @author liufl
 * @since 1.0.21
 */
public class MessageBodyChooser {
    public static JSONObject getJsonObject(String message, String messageGet) {
        if(StringUtils.isBlank(message)) {
            message = messageGet;
        }
        JSONObject signed = JSONObject.parseObject(message);
        return signed.getJSONObject("messageBody");
    }

    @SuppressWarnings("unused")
    public static JSONArray getJsonArray(String message, String messageGet) {
        if(StringUtils.isBlank(message)) {
            message = messageGet;
        }
        JSONObject signed = JSONObject.parseObject(message);
        return signed.getJSONArray("messageBody");
    }
}
