package com.wfj.search.online.statistics.controller;

import com.wfj.search.utils.status.SystemInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * /status.json resolver
 * <br/>create at 15-6-1
 *
 * @author liufl
 * @since 1.0.0
 */
@Controller
public class StatusController {
    @Autowired
    @Qualifier("instanceName")
    private String instanceName;
    @Value("${monitor.register.appName}")
    private String appName;

    @RequestMapping("/status")
    public String status(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("instanceName", instanceName);
        model.addAttribute("system", SystemInfo.getSystemInfo());
        model.addAttribute("jvm", SystemInfo.getJvmInfo());
        return "jsonView"; // no view's name should match this.
    }
}
