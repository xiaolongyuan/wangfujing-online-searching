package com.wfj.search.online.web.interceptor;

import com.wfj.search.online.web.service.ISearchConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

/**
 * <p>create at 15-11-10</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class CommonVariableInjectInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private ISearchConfigService searchConfigService;
    private static Random random = new Random();
    @Value("${concern.itemPageUrl}")
    private String concernItemPageUrl;
    @Value("${cart.cartPageUrl}")
    private String cartPageUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        request.setAttribute("random", random);
        request.setAttribute("wwwLocation", this.searchConfigService.getWwwLocation());
        request.setAttribute("searchLocation", this.searchConfigService.getSearchLocation());
        request.setAttribute("brandLocation", this.searchConfigService.getBrandLocation());
        request.setAttribute("listLocation", this.searchConfigService.getListLocation());
        request.setAttribute("cssHosts", this.searchConfigService.getCssHosts());
        request.setAttribute("cssLocationTemplate", this.searchConfigService.getCssLocationTemplate());
        request.setAttribute("imageHosts", this.searchConfigService.getImageHosts());
        request.setAttribute("imageLocationTemplate", this.searchConfigService.getImageLocationTemplate());
        request.setAttribute("jsHosts", this.searchConfigService.getJsHosts());
        request.setAttribute("wwwJsHosts", this.searchConfigService.getWwwJsHosts());
        request.setAttribute("jsLocationTemplate", this.searchConfigService.getJsLocationTemplate());
        request.setAttribute("wwwJsLocationTemplate", this.searchConfigService.getWwwJsLocationTemplate());
        request.setAttribute("itemUrlPrefix", this.searchConfigService.getItemUrlPrefix());
        request.setAttribute("itemUrlPostfix", this.searchConfigService.getItemUrlPostfix());
        request.setAttribute("concernItemPageUrl", this.concernItemPageUrl);
        request.setAttribute("cartPageUrl", this.cartPageUrl);
        return super.preHandle(request, response, handler);
    }
}
