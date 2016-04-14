package com.wfj.search.online.web.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * <p>create at 15-11-16</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class TrackCookieInterceptor extends HandlerInterceptorAdapter {
    private static final String COOKIE_NAME = "wfj-st";
    @Value("${cookie.domain}")
    private String cookieDomain;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        boolean cookieExists = lookUpCookie(request);
        if (!cookieExists) {
            writeCookie(request, response);
        }
        return super.preHandle(request, response, handler);
    }

    private boolean lookUpCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void writeCookie(HttpServletRequest request, HttpServletResponse response) {
        String value = UUID.randomUUID().toString();
        request.setAttribute("createdTrackId", value);
        Cookie cookie = new Cookie(COOKIE_NAME, value);
        cookie.setDomain(cookieDomain);
        cookie.setMaxAge(60 * 60 * 24 * 365 * 10);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
