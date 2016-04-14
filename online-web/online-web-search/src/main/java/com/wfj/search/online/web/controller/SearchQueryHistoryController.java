package com.wfj.search.online.web.controller;

import com.wfj.search.online.common.pojo.SearchQueryHistoryRecord;
import com.wfj.search.online.web.pojo.PagedResult;
import com.wfj.search.online.web.service.ISearchConfigService;
import com.wfj.search.online.web.service.ISearchHistoryService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 搜索查询历史
 * <p>create at 16-1-21</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/search-history")
public class SearchQueryHistoryController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ISearchHistoryService searchHistoryService;
    @Autowired
    private ISearchConfigService searchConfigService;

    @RequestMapping("load")
    @ResponseBody
    public PagedResult<SearchQueryHistoryRecord> load(
            @CookieValue(value = "wfj-st", required = false) String trackId,
            @RequestParam(value = "prefix", required = false) String prefix,
            @RequestParam(value = "channel", required = false) String channel) {
        if (StringUtils.isBlank(channel)) {
            channel = this.searchConfigService.getChannel();
        }
        return this.searchHistoryService.loadHistory(trackId, prefix, channel, 10);
    }

    @RequestMapping("remove")
    @ResponseBody
    public JSONObject remove(
            @CookieValue(value = "wfj-st", required = false) String trackId,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "channel", required = false) String channel) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", true);
        if (!StringUtils.isBlank(trackId)) {
            query = query == null ? "" : query;
            if (StringUtils.isBlank(channel)) {
                channel = this.searchConfigService.getChannel();
            }
            try {
                this.searchHistoryService.markHistoryRemoved(trackId, query, channel);
            } catch (Exception e) {
                logger.error("修改历史数据失败", e);
                jsonObject.put("success", false);
                jsonObject.put("msg", "修改历史数据失败," + e.toString());
            }
        }
        return jsonObject;
    }
}
