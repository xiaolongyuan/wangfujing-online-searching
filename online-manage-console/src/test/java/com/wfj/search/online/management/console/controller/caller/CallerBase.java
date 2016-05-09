package com.wfj.search.online.management.console.controller.caller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wfj.search.utils.signature.json.rsa.JsonSigner;
import com.wfj.search.utils.signature.json.rsa.StandardizingUtil;
import com.wfj.search.utils.signature.ras.KeyUtils;
import com.wfj.search.utils.zookeeper.discovery.SpringWebMvcServiceProvider;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * <p>create at 16-3-28</p>
 *
 * @author liufl
 * @since 1.0.19
 */
abstract class CallerBase implements ResourceLoaderAware {
    private static final MediaType mediaTypeJson = MediaType.parse("application/json; charset=utf-8");
    private ResourceLoader resourceLoader;
    private PrivateKey privateKey;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public abstract Logger getLogger();

    public abstract String getCaller();

    public abstract String getPrivateKeyFile();

    public abstract SpringWebMvcServiceProvider getServiceProvider();

    private void loadPrivateKey() throws IOException, InvalidKeySpecException {
        this.privateKey = KeyUtils.base64String2RSAPrivateKey(IOUtils.toString(
                this.resourceLoader.getResource("classpath:" + getPrivateKeyFile()).getInputStream()));
    }

    @PostConstruct
    public void afterPropertiesSet() throws IOException, InvalidKeySpecException {
        loadPrivateKey();
    }

    private String requestWithSignature(String url, JSON messageBody, String username)
            throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String signed = StandardizingUtil
                .standardize(JsonSigner.wrapSignature(messageBody, privateKey, getCaller(), username));
        Request request = new Request.Builder().url(url)
                .post(RequestBody.create(mediaTypeJson, signed)).build();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(loggingInterceptor).build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    void testTemplate(String serviceName, JSON messageBody, String username) {
        try {
            String url = this.getServiceProvider().provideServiceAddress(serviceName).orNull();
            if (StringUtils.isBlank(url)) {
                throw new IllegalStateException("无法获取" + serviceName + "地址");
            }
            String resp = requestWithSignature(url, messageBody, username);
            assertNotNull(resp);
            JSONObject res = JSONObject.parseObject(resp);
            assertTrue(res.getBoolean("success"));
        } catch (Exception e) {
            this.getLogger().error("调用失败", e);
            fail(e.getMessage());
        }
    }
}
