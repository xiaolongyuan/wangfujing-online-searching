package com.wfj.search.online.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 根据server name列表分类或品牌
 * <p>create at 16-1-15</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Controller
public class ListFacadeController {
    @Value("${search.host.searchHost}")
    private String searchHost;
    @Value("${search.host.brandHost}")
    private String brandHost;
    @Value("${search.host.listHost}")
    private String listHost;

    @RequestMapping("/{cat1}-{cat2}-{cat3}-{brandIds}-{price}-{standardIds}-{colors}-{attrs}-{tagIds}-{sortId}-{currentPage}-0.html")
    public String list(@RequestHeader(HttpHeaders.HOST) String host,
            @PathVariable("cat1") String cat1,
            @PathVariable("cat2") String cat2,
            @PathVariable("cat3") String cat3,
            @PathVariable("brandIds") String brandIds,
            @PathVariable("price") String price,
            @PathVariable("standardIds") String standardIds,
            @PathVariable("colors") String colors,
            @PathVariable("attrs") String attrs,
            @PathVariable("tagIds") String tagIds,
            @PathVariable("sortId") String sortId,
            @PathVariable("currentPage") Integer currentPage,
            HttpServletRequest request) {
        String url = "/" + cat1 + "-" + cat2 + "-" + cat3 + "-" + brandIds + "-" + price + "-" + standardIds + "-" + colors + "-" + attrs + "-" + tagIds + "-" + sortId + "-" + currentPage + "-0.html";
        if (this.brandHost.equals(host)) {
            request.setAttribute("urlPrefix", "");
            return "forward:/brandlist" + url;
        } else if (this.listHost.equals(host)) {
            request.setAttribute("urlPrefix", "");
            return "forward:/list" + url;
        } else {
            throw new ResourceNotFoundException();
        }
    }
}
