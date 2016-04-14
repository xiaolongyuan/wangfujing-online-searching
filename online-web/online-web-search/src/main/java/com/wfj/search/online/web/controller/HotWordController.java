package com.wfj.search.online.web.controller;

import com.google.common.collect.Lists;
import com.wfj.search.online.web.pojo.HotWord;
import com.wfj.search.online.web.pojo.HotWordsOfChannelPojo;
import com.wfj.search.online.web.service.IHotWordService;
import com.wfj.search.online.web.service.ISearchConfigService;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <br/>create at 15-12-22
 *
 * @author liuxh
 * @since 1.0.0
 */
@Controller
@RequestMapping("/service/hot-word")
public class HotWordController {
    private static final Logger logger = LoggerFactory.getLogger(HotWordController.class);
    @Autowired
    private IHotWordService hotWordService;
    @Autowired
    private ISearchConfigService searchConfigService;

    @RequestMapping(value = "/list-hot-word-pages", method = RequestMethod.POST)
    public JSONObject list(@RequestBody String message) {
        JSONObject json = new JSONObject();
        try {
            JSONObject param = JSONObject.fromObject(message);
            String siteId = param.containsKey("siteId") ? param.getString("siteId") : null;
            String channelId = param.containsKey("channelId") ? param.getString("channelId") : null;
            String value = param.containsKey("value") ? param.getString("value") : null;
            int start = param.getInt("start");
            int fetch = param.getInt("fetch");
            List<HotWord> list = hotWordService.listHotWords(siteId, channelId, value, start, fetch);
            int total = hotWordService.count(siteId, channelId, value);
            json.put("success", true);
            json.put("total", total);
            json.put("list", list);
        } catch (JSONException e) {
            json.put("success", false);
            json.put("errCode", 400);
            json.put("errMessage", "数据解析出错");
            logger.error("数据解析出错，输入数据：{}", message, e);
        } catch (Exception e) {
            json.put("success", false);
            json.put("errCode", 500);
            json.put("errMessage", "查询热词失败");
            logger.error("查询热词失败", e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("查询热词输入数据{}，返回数据{}", message, json.toString());
        }
        return json;
    }

    @RequestMapping(value = "/list-hot-word", method = RequestMethod.POST)
    public JSONObject listHotWord(@RequestBody String message) {
        JSONObject json = new JSONObject();
        try {
            JSONObject param = JSONObject.fromObject(message);
            String siteId = param.containsKey("siteId") ? param.getString("siteId") : null;
            String channelId = param.containsKey("channelId") ? param.getString("channelId") : null;
            json.put("success", true);
            json.put("data", hotWords(siteId, channelId));
        } catch (Exception e) {
            json.put("success", false);
            json.put("errCode", 500);
            json.put("errMessage", "查询热词失败");
            logger.error("查询热词失败", e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("查询热词输入数据{}，返回数据{}", message, json.toString());
        }
        return json;
    }

    @RequestMapping(value = "/list-hot-word")
    @ResponseBody
    public List<HotWordsOfChannelPojo> hotWords(String siteId, String channelId) {
        if (StringUtils.isBlank(siteId) || StringUtils.isBlank(channelId)) {
            siteId = this.searchConfigService.getDefaultSite();
            channelId = this.searchConfigService.getDefaultChannel();
            logger.info("siteId或channelId为空，使用默认siteId和channelId");
        }
        logger.debug("siteId={}, channelId={}", siteId, channelId);
        List<HotWordsOfChannelPojo> list = Lists.newArrayList();
        try {
            list.addAll(hotWordService.listHotWords(siteId, channelId));
        } catch (Exception e) {
            logger.error("查询热词失败，输入站点id={}，频道id={}", siteId, channelId, e);
        }
        return list;
    }
}
