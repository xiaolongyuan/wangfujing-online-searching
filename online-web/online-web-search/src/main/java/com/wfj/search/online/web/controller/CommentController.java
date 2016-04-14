package com.wfj.search.online.web.controller;

import com.wfj.search.online.web.pojo.SearchResult;
import com.wfj.search.online.web.service.ICommentService;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * <br/>create at 16-1-6
 *
 * @author liuxh
 * @since 1.0.0
 */
@Controller
@RequestMapping("/service/comment")
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    @Autowired
    private ICommentService commentService;

    @RequestMapping(value = "/list-comments", method = RequestMethod.POST)
    public JSONObject getCommentByParams(@RequestBody String message) {
        JSONObject json = new JSONObject();
        try {
            JSONObject params = JSONObject.fromObject(message);
            String skuId = params.containsKey("skuId") ? params.getString("skuId") : null;
            int start = params.getInt("start");
            int fetch = params.getInt("fetch");
            SearchResult<Map<String, Object>> result = getCommentByParams(skuId, start, fetch);
            json.put("success", true);
            json.put("total", result.getTotal());
            json.put("pageSize", result.getPageSize());
            json.put("list", result.getList());
        } catch (JSONException e) {
            json.put("success", false);
            json.put("errCode", 400);
            json.put("errMessage", "数据解析出错");
            logger.error("数据解析出错，输入数据：{}", message, e);
        } catch (Exception e) {
            json.put("success", false);
            json.put("errCode", 500);
            json.put("errMessage", "查询用户评论失败");
            logger.error("查询用户评论失败", e);
        }
        return json;
    }

    @RequestMapping("/list-comments")
    @ResponseBody
    public SearchResult<Map<String, Object>> getCommentByParams(String skuId, int start, int fetch) {
        logger.debug("skuId={}, start={}, fetch={}", skuId, start, fetch);
        return commentService.query(start, fetch, skuId);
    }
}
