package com.wfj.search.online.web.controller;

import com.wfj.member.sdk.client.MemberClient;
import com.wfj.member.sdk.common.Config;
import com.wfj.member.sdk.common.MemberCollectDto;
import com.wfj.member.sdk.common.MsgReturnDto;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>create at 15-12-24</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/member")
public class MemberController implements ServletContextAware {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${member.domainName}")
    private String domainName;
    @Value("${member.url}")
    private String memberUrl;
    private ServletContext servletContext;
    @Value("${cookie.domain}")
    private String cookieDomain;

    @PostConstruct
    public void initConfig() throws Exception {
        String realPath = servletContext.getRealPath("/");
        Config.setPrivateKey(realPath + "/WEB-INF/classes/member_privateKey.dat");
        Config.setDomainName(domainName);
        Config.setMemberUrl(memberUrl);
    }

    @RequestMapping(value = "validate")
    @ResponseBody
    public JSONObject validate(@CookieValue(value = "ticket", required = false) String ticket) {
        JSONObject json = new JSONObject();
        if (StringUtils.isBlank(ticket)) {
            json.put("login", false);
        } else {
            MsgReturnDto msgReturnDto = null;
            try {
                msgReturnDto = MemberClient.valLoginStatus(ticket.trim());
            } catch (Exception e) {
                logger.error("调用MemberSdk失败", e);
            }
            if (msgReturnDto == null) {
                json.put("login", false);
            } else {
                boolean login = "1".equals(msgReturnDto.getCode());
                json.put("login", login);
                if (login) {
                    //noinspection unchecked
                    Map<String, Object> logInfoObj = (Map<String, Object>) msgReturnDto.getObject();
                    JSONObject logInfo = new JSONObject();
                    logInfo.put("sid", logInfoObj.get("sid"));
                    logInfo.put("username", logInfoObj.get("username"));
                    try {
                        Object email = logInfoObj.get("email");
                        if (email != null && !"".equals(email.toString().trim())) {
                            logInfo.put("email", email);
                        }
                    } catch (Exception e) {
                        logger.info("会员信息中不包含email", e);
                    }
                    try {
                        Object mobile = logInfoObj.get("mobile");
                        if (mobile != null && !"".equals(mobile.toString().trim())) {
                            logInfo.put("mobile", mobile);
                        }
                    } catch (Exception e) {
                        logger.info("会员信息中不包含mobile", e);
                    }
                    json.put("loginInfo", logInfo);
                }
            }
        }
        return json;
    }

    @RequestMapping(value = "login")
    @ResponseBody
    public JSONObject login(@RequestParam("username") String username,
            @RequestParam("password") String password, HttpServletRequest request) {
        JSONObject json = new JSONObject();
        MsgReturnDto msgReturnDto = null;
        try {
            msgReturnDto = MemberClient.quickLogin(username, password, request);
        } catch (Exception e) {
            logger.error("调用MemberSdk失败", e);
        }
        if (msgReturnDto == null) {
            json.put("login", false);
            json.put("message", "登录失败，请稍候再试。");
        } else {
            boolean login = "1".equals(msgReturnDto.getCode());
            json.put("login", login);
            if (!login) {
                json.put("message", msgReturnDto.getDesc());
            } else {
                Cookie ticket = new Cookie("ticket", msgReturnDto.getObject().toString());
                ticket.setMaxAge(-1);
                ticket.setDomain(cookieDomain);
                json.put("ticketCookie", ticket);
            }
        }
        return json;
    }

    @RequestMapping(value = "/concern")
    @ResponseBody
    public JSONObject concern(@CookieValue(value = "ticket", required = false) String ticket,
            @RequestParam(value = "sid", required = true) String sid,
            @RequestParam(value = "type", defaultValue = "0") Integer type) {
        JSONObject json = new JSONObject();
        Long memberSid = null;
        if (StringUtils.isBlank(ticket)) {
            json.put("ok", false);
            json.put("login", false);
        } else {
            MsgReturnDto msgReturnDto = null;
            try {
                msgReturnDto = MemberClient.valLoginStatus(ticket.trim());
            } catch (Exception e) {
                logger.error("调用MemberSdk失败", e);
            }
            if (msgReturnDto == null) {
                json.put("ok", false);
                json.put("login", false);
            } else {
                boolean login = "1".equals(msgReturnDto.getCode());
                json.put("login", login);
                if (login) {
                    Object object = msgReturnDto.getObject();
                    try {
                        JSONObject jsonObject = JSONObject.fromObject(object);
                        memberSid = jsonObject.getLong("sid");
                    } catch (Exception e) {
                        logger.error("memberSdk响应格式与接口文档不符", e);
                    }
                }
            }
        }
        if (memberSid == null) {
            json.put("ok", false);
            json.put("login", false);
        } else {
            MsgReturnDto msgReturnDto = null;
            try {
                MemberCollectDto memberCollectDto = new MemberCollectDto();
                memberCollectDto.setContentSid(sid);
                memberCollectDto.setMemberSid(memberSid);
                memberCollectDto.setType(type);
                msgReturnDto = MemberClient.addMemberCollect(memberCollectDto);
            } catch (Exception e) {
                logger.error("调用MemberSdk失败", e);
                json.put("ok", false);
            }
            if (msgReturnDto == null) {
                json.put("ok", false);
            } else {
                boolean success = "1".equals(msgReturnDto.getCode());
                json.put("ok", success);
            }
        }
        return json;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
