package com.wfj.search.online.management.console.service.cms.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wfj.search.online.management.console.service.cms.ICmsRequester;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring-test-service-cms.xml")
@Ignore
public class CmsRequesterImplTest {
    private static final Logger logger = LoggerFactory.getLogger(CmsRequesterImplTest.class);
    @Autowired
    private ICmsRequester cmsRequester;

    @Test
    public void testGetSiteList() throws Exception {
        JSONArray jsonArray = cmsRequester.getSiteList();
        assertNotEquals(0, jsonArray.size());
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            assertNotNull(json.getString("id"));
            assertNotNull(json.getString("name"));
            logger.debug("siteId={}， siteName={}", json.getString("id"), json.getString("name"));
        }
    }

    @Test
    public void testGetChannelListBySid() throws Exception {
        JSONArray jsonArray = cmsRequester.getSiteList();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject site = jsonArray.getJSONObject(i);
            String siteId = site.getString("id");
            JSONArray channelArray = cmsRequester.getChannelListBySid(siteId);
            for (int j = 0; j < channelArray.size(); j++) {
                JSONObject channel = channelArray.getJSONObject(j);
                assertNotNull(channel.getString("id"));
                assertNotNull(channel.getString("name"));
                logger.debug("siteId={}, channelId={}, channelName={}", site.getString("id"), channel.getString("id"),
                        channel.getString("name"));
            }
        }
    }
}