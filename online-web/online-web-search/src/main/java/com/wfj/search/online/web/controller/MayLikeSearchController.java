package com.wfj.search.online.web.controller;

import com.google.common.collect.Maps;
import com.wfj.search.online.web.common.pojo.SpuDisplayPojo;
import com.wfj.search.online.web.service.IMayLikeSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <br/>create at 16-1-12
 *
 * @author liuxh
 * @since 1.0.0
 */
@Controller
@RequestMapping("/service/may-like")
public class MayLikeSearchController {
    private static final Logger logger = LoggerFactory.getLogger(MayLikeSearchController.class);
    @Autowired
    private IMayLikeSearchService mayLikeSearchService;

    @RequestMapping(value = "/random")
    public String random(@RequestParam(required = false) String keyWord, @RequestParam(required = false) String cats,
            Model model) {
        logger.debug("根据[keyWord={}, cats={}]查询猜你喜欢列表", keyWord, cats);
        List<SpuDisplayPojo> spus;
        try {
            spus = mayLikeSearchService.randomSearch(keyWord, cats);
        } catch (Exception e) {
            logger.error("根据[keyWord={}, cats={}]查询猜你喜欢列表出错", keyWord, cats, e);
            spus = Collections.emptyList();
        }
        model.addAttribute("ok", true);
        model.addAttribute("list", spus);
        return "may_like";
    }

    @RequestMapping(value = "/randoms")
    @ResponseBody
    public Map<String, Object> random(@RequestParam(required = false) String keyWord,
            @RequestParam(required = false) String cats) {
        logger.debug("根据[keyWord={}, cats={}]查询猜你喜欢列表", keyWord, cats);
        List<SpuDisplayPojo> spus;
        try {
            spus = mayLikeSearchService.randomSearch(keyWord, cats);
        } catch (Exception e) {
            logger.error("根据[keyWord={}, cats={}]查询猜你喜欢列表出错", keyWord, cats, e);
            spus = Collections.emptyList();
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("ok", true);
        map.put("list", spus);
        return map;
    }
}
