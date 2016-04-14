package com.wfj.search.online.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <br/>create at 16-1-25
 *
 * @author liuxh
 * @since 1.0.0
 */
@Controller
public class ErrorPageController {
    @RequestMapping("/404")
    public String resourceNotFound() {
        return "404";
    }

    @RequestMapping("/500")
    public String internalServerError(){
        return "500";
    }
}
