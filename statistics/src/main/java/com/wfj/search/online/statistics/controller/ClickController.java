package com.wfj.search.online.statistics.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.common.collect.Maps;
import com.wfj.search.online.statistics.pojo.ClickPojo;
import com.wfj.search.online.statistics.service.IClickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.Map;

/**
 * <p>create at 15-12-9</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Controller
public class ClickController {
    @Autowired
    private IClickService clickService;

    @RequestMapping(value = "clickItem")
    @ResponseBody
    public JSONPObject click(@RequestParam("id") String spuId,
            @RequestParam(value = "pid", required = false) Long pid,
            @CookieValue(name = "wfj-st") String tid,
            @RequestParam("fn") String fn) {
        Timestamp clickTime = new Timestamp(System.currentTimeMillis());
        ClickPojo clickPojo = new ClickPojo(tid, pid, spuId, clickTime);
        this.clickService.onClick(clickPojo);
        Map<String, Object> map = Maps.newHashMap();
        map.put("success", true);
        return new JSONPObject(fn, map);
    }
}
