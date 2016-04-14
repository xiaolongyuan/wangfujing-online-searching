package com.wfj.search.online.web.controller.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * <br/>create at 15-12-24
 *
 * @author liuxh
 * @since 1.0.0
 */
@ControllerAdvice(basePackages = "com.wfj.search.online.web.controller")
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {
    public JsonpAdvice() {
        super("callback", "jsonp");
    }
}
