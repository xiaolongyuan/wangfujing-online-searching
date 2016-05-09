package com.wfj.search.script;

import com.alibaba.fastjson.JSONObject;
import com.wfj.search.online.index.controller.caller.CallerBase;
import com.wfj.search.utils.zookeeper.discovery.SpringWebMvcServiceProvider;
import okhttp3.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.concurrent.TimeUnit;

/**
 * <p>create at 16-4-24</p>
 *
 * @author liufl
 * @since 1.0.34
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-script.xml"})
public class RequestFailureItemRebuild extends CallerBase {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${pcm.address}")
    private String pcmAddress;
    @Value("${pcm.uri.listItems}")
    private String pcmUriListItems;
    @Value("${caller.caller}")
    private String caller;
    @Value("${caller.privateKeyFile}")
    private String privateKeyFile;
    @Autowired
    private SpringWebMvcServiceProvider serviceProvider;

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public String getCaller() {
        return caller;
    }

    @Override
    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    @Override
    public SpringWebMvcServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    @Test
    public void rebuild() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(15, TimeUnit.SECONDS).build();
        // 从PCM找出start&fetch对应的商品编码
        LineNumberReader reader = new LineNumberReader(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("fail-lines.txt")));
        String line;
        MediaType mediaTypeJson = MediaType.parse("application/json; charset=utf-8");
        while ((line = reader.readLine()) != null) {
            Request request = new Request.Builder().url(pcmAddress + "/" + pcmUriListItems)
                    .post(RequestBody.create(mediaTypeJson, "{\"start\":" + line + ",\"fetch\":1}")).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                JSONObject resp = JSONObject.parseObject(response.body().string());
                if (resp.getBoolean("success")) {
                    String itemId = resp.getJSONArray("list").getJSONObject(0).getString("itemId");
                    String serviceName = "online-ops-indexItem";
                    JSONObject messageBody = new JSONObject();
                    messageBody.put("itemId", itemId);
                    testTemplate(serviceName, messageBody);
                } else {
                    logger.error("request pcm start {} fail", line);
                }
            } else {
                logger.error("request pcm start {} fail", line);
            }
        }
    }
}
