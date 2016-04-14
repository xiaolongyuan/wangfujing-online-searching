package com.wfj.search.online.web.controller;

import com.wfj.search.online.web.service.ISearchConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 直接访问跳转到主站
 * <p>create at 15-11-9</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Controller
public class JumpTo3wController {
    @Autowired
    private ISearchConfigService searchConfigService;
    @RequestMapping("/jumpTo3w.html")
    public void jumpTo3W(HttpServletResponse response) throws IOException {
        response.setHeader(HttpHeaders.LOCATION, searchConfigService.getWwwLocation());
        response.sendError(HttpStatus.MOVED_PERMANENTLY.value());
    }
}
