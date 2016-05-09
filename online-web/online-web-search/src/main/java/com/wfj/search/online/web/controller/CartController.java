package com.wfj.search.online.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.wfj.member.sdk.client.MemberClient;
import com.wfj.member.sdk.common.Config;
import com.wfj.member.sdk.common.MsgReturnDto;
import com.wfj.search.utils.http.OkHttpOperator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

/**
 * <p>create at 16-1-11</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Controller
@RequestMapping("/cart")
public class CartController implements ServletContextAware {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${member.domainName}")
    private String domainName;
    @Value("${member.url}")
    private String memberUrl;
    private ServletContext servletContext;
    @Value("${cart.getCartNumUrl}")
    private String getCartNumUrl;
    @Autowired
    private OkHttpOperator okHttpOperator;

    @PostConstruct
    public void initConfig() throws Exception {
        String realPath = servletContext.getRealPath("/");
        Config.setPrivateKey(realPath + "/WEB-INF/classes/member_privateKey.dat");
        Config.setDomainName(domainName);
        Config.setMemberUrl(memberUrl);
    }

    @RequestMapping(value = "/num")
    @ResponseBody
    public JSONObject getCartNum(@CookieValue(value = "ticket", required = false) String ticket,
            @CookieValue(value = "cart_id", required = false) String cartId) {
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isBlank(ticket) && StringUtils.isBlank(cartId)) {
            jsonObject.put("cartNum", 0);
        } else {
            String loginUid = null;
            String cartUid = null;
            if (StringUtils.isNotBlank(ticket)) {
                MsgReturnDto msgReturnDto = null;
                try {
                    msgReturnDto = MemberClient.valLoginStatus(ticket.trim());
                } catch (Exception e) {
                    logger.error("调用MemberSdk失败", e);
                }
                if (msgReturnDto != null) {
                    boolean login = "1".equals(msgReturnDto.getCode());
                    if (login) {
                        Object object = msgReturnDto.getObject();
                        net.sf.json.JSONObject loginJson = net.sf.json.JSONObject.fromObject(object);
                        cartUid = loginJson.getString("sid");
                        loginUid = cartUid;
                    }
                }
            } else {
                cartUid = cartId;
            }
            if (StringUtils.isBlank(cartUid)) {
                jsonObject.put("cartNum", 0);
            } else {
                String cartNumResp;
                JSONObject param = new JSONObject();
                param.put("uid", StringUtils.isBlank(loginUid) ? "" : loginUid);
                param.put("cart_id", StringUtils.isBlank(cartId) ? "" : cartId);
                String url = this.getCartNumUrl + cartUid;
                String jsonText = param.toJSONString();
                try {
                    cartNumResp = this.okHttpOperator.postJsonTextForTextResp(url, jsonText);
                } catch (Exception e) {
                    logger.error("请求购物车失败", e);
                    cartNumResp = null;
                }
                if (StringUtils.isBlank(cartNumResp)) {
                    jsonObject.put("cartNum", 0);
                } else {
                    try {
                        jsonObject.put("cartNum", Integer.parseInt(cartNumResp));
                    } catch (NumberFormatException e) {
                        logger.error("购物车响应非数字", e);
                        jsonObject.put("cartNum", 0);
                    }
                }
            }
        }
        return jsonObject;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
