package com.wfj.search.online.management.console.controller.caller;

import com.wfj.platform.util.httpclient.HttpRequestException;
import com.wfj.platform.util.httpclient.HttpRequester;
import com.wfj.platform.util.signature.json.JsonSigner;
import com.wfj.platform.util.signature.keytool.RsaKeyLoader;
import com.wfj.platform.util.zookeeper.discovery.SpringMvcServiceProvider;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.PrivateKey;
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
    protected PrivateKey privateKey;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public abstract Logger getLogger();

    public abstract String getCaller();

    public abstract String getPrivateKeyFile();

    public abstract SpringMvcServiceProvider getServiceProvider();

    protected void loadPrivateKey() throws IOException, InvalidKeySpecException {
        this.privateKey = RsaKeyLoader
                .base64String2PriKey(IOUtils.toString(
                        this.resourceLoader.getResource("classpath:" + getPrivateKeyFile()).getInputStream()));
    }

    @PostConstruct
    public void afterPropertiesSet() throws IOException, InvalidKeySpecException {
        loadPrivateKey();
    }

    protected String requestWithSignature(String url, JSONObject messageBody)
            throws URISyntaxException, IOException, HttpRequestException {
        String signed = JsonSigner.wrapSignature(messageBody, privateKey, getCaller());
        return HttpRequester.getSimpleHttpRequester().httpPostString(url, signed);
    }

    protected void testTemplate(String serviceName, JSONObject messageBody) {
        try {
            String url = this.getServiceProvider().provideServiceAddress(serviceName);
            this.getLogger().info("request url: {}", url);
            String resp = requestWithSignature(url, messageBody);
            assertNotNull(resp);
            this.getLogger().info(resp);
            JSONObject res = JSONObject.fromObject(resp);
            assertTrue(res.getBoolean("success"));
        } catch (Exception e) {
            this.getLogger().error("调用失败", e);
            fail(e.getMessage());
        }
    }
}
