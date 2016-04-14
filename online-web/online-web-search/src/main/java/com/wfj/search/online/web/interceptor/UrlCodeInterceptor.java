package com.wfj.search.online.web.interceptor;

import com.wfj.search.online.web.service.IUrlCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * URL代码注入
 * <p>create at 15-11-16</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class UrlCodeInterceptor extends HandlerInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IUrlCodeService urlCodeService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        StringBuffer requestURL = request.getRequestURL();
        try {
            Long urlCode = this.urlCodeService.getCodeOfUrl(requestURL.toString());
            if (urlCode != null) {
                request.setAttribute("suc", urlCode.toString());
            }
        } catch (Exception e) {
            logger.error("添加url码失败", e);
        }
        String fromSuc = request.getParameter("suc");
        request.setAttribute("fromSuc", fromSuc);
        return super.preHandle(request, response, handler);
    }
}
