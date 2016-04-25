package com.wfj.search.online.index.controller.caller;

import com.alibaba.fastjson.JSONObject;
import com.wfj.platform.util.zookeeper.discovery.SpringMvcServiceProvider;
import com.wfj.search.utils.signature.json.rsa.JsonSigner;
import com.wfj.search.utils.signature.json.rsa.StandardizingUtil;
import com.wfj.search.utils.signature.ras.KeyUtils;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.Assert.*;

/**
 * <p>create at 16-3-28</p>
 *
 * @author liufl
 * @since 1.0.19
 */
public abstract class CallerBase implements ResourceLoaderAware {
    private ResourceLoader resourceLoader;
    private PrivateKey privateKey;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public abstract Logger getLogger();

    public abstract String getCaller();

    public abstract String getPrivateKeyFile();

    public abstract SpringMvcServiceProvider getServiceProvider();

    private void loadPrivateKey() throws IOException, InvalidKeySpecException {
        this.privateKey = KeyUtils.base64String2RSAPrivateKey(IOUtils.toString(
                this.resourceLoader.getResource("classpath:" + getPrivateKeyFile()).getInputStream()));
    }

    @PostConstruct
    public void afterPropertiesSet() throws IOException, InvalidKeySpecException {
        loadPrivateKey();
    }

    private String requestWithSignature(String url, JSONObject messageBody)
            throws URISyntaxException, IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String signed = StandardizingUtil
                .standardize(JsonSigner.wrapSignature(messageBody, privateKey, getCaller(), ""));
        MediaType mediaTypeJson = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder().url(url)
                .post(RequestBody.create(mediaTypeJson, signed)).build();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor).build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    protected void testTemplate(String serviceName, JSONObject messageBody) {
        try {
            String url = this.getServiceProvider().provideServiceAddress(serviceName);
            String resp = requestWithSignature(url, messageBody);
            assertNotNull(resp);
            JSONObject res = JSONObject.parseObject(resp);
            assertTrue(res.getBoolean("success"));
        } catch (Exception e) {
            this.getLogger().error("调用失败", e);
            fail(e.getMessage());
        }
    }
}
