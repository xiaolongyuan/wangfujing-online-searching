package com.wfj.search.online.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 模拟WWW提供公共html块
 * DDD delete this mock controller
 * <p>create at 15-12-28</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Controller
public class MockWwwController {
    @Value("${member.location}")
    private String memberLocation;
    @Value("${cart.cartPageUrl}")
    private String cartPageUrl;
    @Value("${search.config.wwwLocation}")
    private String wwwLocation;

    @RequestMapping("/tbar")
    public String tbar(Model model) {
        model.addAttribute("memberLocation", memberLocation);
        return "tbar";
    }

    @RequestMapping("/header")
    public String header(Model model) {
        model.addAttribute("cartPageUrl", cartPageUrl);
        model.addAttribute("wwwLocation", wwwLocation);
        return "header";
    }

    @RequestMapping("/footer")
    public String footer() {
        return "footer";
    }
}
