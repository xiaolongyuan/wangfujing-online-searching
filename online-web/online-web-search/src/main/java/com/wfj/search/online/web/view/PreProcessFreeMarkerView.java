package com.wfj.search.online.web.view;

import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

/**
 * <p>create at 15-10-15</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class PreProcessFreeMarkerView extends FreeMarkerView {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String CONTEXT_PATH = "base";

    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        model.put(CONTEXT_PATH, request.getContextPath());
        super.exposeHelpers(model, request);
    }

    @Override
    protected void doRender(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            super.doRender(model, request, response);
        } catch (TemplateException e) {
            logger.error("processTemplate fail, 0x530001", e);
            request.setAttribute("errorCode", "0x530001");
            throw e;
        }
    }

    @Override
    protected void processTemplate(Template template, SimpleHash model,
            HttpServletResponse response) throws IOException, TemplateException {
        // 输出到RAM缓存
        ByteArrayOutputStream cacheOut = new ByteArrayOutputStream();
        String charsetName = this.getEncoding() == null ? "UTF-8" : this.getEncoding();
        Writer writer = new OutputStreamWriter(cacheOut, charsetName);
        template.process(model, writer);
        // 至此若未出现异常，取出写入响应
        String content = new String(cacheOut.toByteArray(), charsetName);
        response.getWriter().write(content);
    }
}
