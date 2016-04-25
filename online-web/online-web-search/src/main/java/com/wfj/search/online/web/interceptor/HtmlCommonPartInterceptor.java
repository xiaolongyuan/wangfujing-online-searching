package com.wfj.search.online.web.interceptor;

import com.wfj.member.sdk.client.MemberClient;
import com.wfj.member.sdk.common.Config;
import com.wfj.member.sdk.common.DataDto;
import com.wfj.search.online.web.service.IHtmlCommonPartService;
import com.wfj.search.online.web.service.ISearchConfigService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>create at 15-12-28</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class HtmlCommonPartInterceptor extends HandlerInterceptorAdapter implements ServletContextAware {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IHtmlCommonPartService htmlCommonPartService;
    @Autowired
    private ISearchConfigService searchConfigService;
    @Value("${member.domainName}")
    private String domainName;
    @Value("${member.url}")
    private String memberUrl;
    private ServletContext servletContext;
    @Value("${cookie.domain}")
    private String cookieDomain;
    @Value("${member.login}")
    private String memberLoginUrl;
    @Value("${member.logout}")
    private String memberLogoutUrl;
    @Value("${member.register}")
    private String memberRegisterUrl;
    @Value("${member.homeUrl}")
    private String memberHomeUrl;

    @PostConstruct
    public void initConfig() throws Exception {
        String realPath = servletContext.getRealPath("/");
        Config.setPrivateKey(realPath + "/WEB-INF/classes/member_privateKey.dat");
        Config.setDomainName(domainName);
        Config.setMemberUrl(memberUrl);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        if (modelAndView == null) {
            return;
        }
        String tbarUri = this.searchConfigService.getHtmlTbarUri();
        modelAndView.getModelMap().addAttribute("tbarContent", this.htmlCommonPartService.htmlContent(tbarUri));
        String headerUri = this.searchConfigService.getHtmlHeaderUri();
        modelAndView.getModelMap().addAttribute("headerContent", this.htmlCommonPartService.htmlContent(headerUri));
        String navigationUri = this.searchConfigService.getHtmlNavigationUri();
        String nav_ = this.htmlCommonPartService.htmlContent(navigationUri);
        modelAndView.getModelMap()
                .addAttribute("navigationContent", nav_);
        String footerUri = this.searchConfigService.getHtmlFooterUri();
        modelAndView.getModelMap().addAttribute("footerContent", this.htmlCommonPartService.htmlContent(footerUri));
        String returnUrl = request.getScheme() + "://" + request.getServerName() + (request
                .getServerPort() == 80 ? "" : ":" + request.getServerPort()) + request.getRequestURI() + (StringUtils
                .isBlank(request.getQueryString()) ? "" : "?" + request.getQueryString());
        Cookie ticketCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("ticket".equals(cookie.getName())) {
                    ticketCookie = cookie;
                    break;
                }
            }
        }
        modelAndView.getModelMap().addAttribute("logoutPageUrl", this.memberLogoutUrl);
        if (ticketCookie != null) {
            DataDto logoutSignStr = null;
            try {
                logoutSignStr = MemberClient.getLogoutSignStr(returnUrl, ticketCookie.getValue().trim());
            } catch (Exception e) {
                logger.error("调用MemberSdk失败", e);
            }
            if (logoutSignStr != null) {
                modelAndView.getModelMap().addAttribute("logoutPageUrl",
                        this.memberLogoutUrl + "?data=" + logoutSignStr.getData() + "&sign=" + logoutSignStr
                                .getSign() + "&domainName=" + logoutSignStr.getDomainName());
            }
        }
        DataDto loginSignStr = null;
        try {
            loginSignStr = MemberClient.getLoginSignStr(returnUrl);
        } catch (Exception e) {
            logger.error("调用MemberSdk失败", e);
        }
        if (loginSignStr != null) {
            modelAndView.getModelMap().addAttribute("loginPageUrl",
                    this.memberLoginUrl + "?data=" + loginSignStr.getData() + "&sign=" + loginSignStr
                            .getSign() + "&domainName=" + loginSignStr.getDomainName());
        }
        DataDto registerSignStr = null;
        try {
            registerSignStr = MemberClient.getRegisterSignStr(returnUrl);
        } catch (Exception e) {
            logger.error("调用MemberSdk失败", e);
        }
        if (registerSignStr != null) {
            modelAndView.getModelMap().addAttribute("registerPageUrl",
                    this.memberRegisterUrl + "?data=" + registerSignStr.getData() + "&sign=" + registerSignStr
                            .getSign() + "&domainName=" + registerSignStr.getDomainName());
        }
        modelAndView.getModelMap().addAttribute("memberHomeUrl", this.memberHomeUrl);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
