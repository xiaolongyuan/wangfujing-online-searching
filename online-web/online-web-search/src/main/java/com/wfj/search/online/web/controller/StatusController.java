package com.wfj.search.online.web.controller;

import com.wfj.search.utils.status.SystemInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>create at 16-3-14</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Controller
public class StatusController {
    @Autowired
    @Qualifier("instanceName")
    private String instanceName;
    @Autowired
    @Qualifier("instanceHost")
    private String instanceHost;
    @Autowired
    @Qualifier("servicePort")
    private Integer servicePort;
    @Value("${monitor.register.appName}")
    private String appName;

    @SuppressWarnings("Duplicates")
    @RequestMapping("/status")
    public String status(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("instanceName", instanceName);
        model.addAttribute("instanceHost", instanceHost);
        model.addAttribute("servicePort", servicePort);
        model.addAttribute("system", SystemInfo.getSystemInfo());
        model.addAttribute("jvm", SystemInfo.getJvmInfo());
        return "jsonView"; // no view's name should match this.
    }
}
