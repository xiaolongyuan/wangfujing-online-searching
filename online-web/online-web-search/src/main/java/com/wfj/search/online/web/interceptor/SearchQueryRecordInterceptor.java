package com.wfj.search.online.web.interceptor;

import com.google.common.collect.Sets;
import com.wfj.search.online.common.pojo.SearchQueryRecord;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.es.SearchQueryRecordEsIao;
import com.wfj.search.online.web.service.ISearchConfigService;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>create at 15-11-16</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class SearchQueryRecordInterceptor extends HandlerInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String COOKIE_NAME = "wfj-st";
    @Autowired
    private SearchQueryRecordEsIao searchQueryRecordEsIao;
    @Autowired
    private ISearchConfigService searchConfigService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
        final String trackId = lookUpCookie(request);
        if (trackId != null) {
            if (modelAndView != null) {
                ModelMap modelMap = modelAndView.getModelMap();
                if (modelMap != null) {
                    final SearchResult result;
                    try {
                        result = (SearchResult) modelMap.get("result");
                    } catch (Exception ignored) {
                        return;
                    }
                    if (result != null) {
                        Thread thread = new Thread(() -> {
                            String channel = request.getParameter("channel");
                            if (StringUtils.isBlank(channel)) {
                                channel = this.searchConfigService.getChannel();
                            }
                            SearchQueryRecord queryRecord = new SearchQueryRecord();
                            String inputQuery = result.getParams().getInputQuery();
                            queryRecord.setQuery(inputQuery);
                            queryRecord.setQueryTime(new Date());
                            queryRecord.setTrackId(trackId);
                            queryRecord.setUuid(UUID.randomUUID().toString());
                            try {
                                queryRecord.setQueryAbbre(Sets.newHashSet(getAbbre(inputQuery)));
                                queryRecord.setQueryPinyin(Sets.newHashSet(GetPyString(inputQuery)));
                            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                                logger.error("拼音转换失败", badHanyuPinyinOutputFormatCombination);
                            }
                            queryRecord.setChannel(channel);
                            this.searchQueryRecordEsIao.upsert(queryRecord);
                        });
                        thread.setDaemon(false);
                        thread.start();
                    }
                }
            }
        }
    }

    private String lookUpCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        Object createdTrackId = request.getAttribute("createdTrackId");
        if (createdTrackId != null) {
            return createdTrackId.toString();
        }
        return null;
    }

    private HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat(); // 拼音转接输出格式

    /**
     * 获取拼音缩写
     *
     * @param chinese 含中文的字符串，若不含中文，原样输出
     * @return 转换后的文本
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    private Collection<String> getAbbre(String chinese)
            throws BadHanyuPinyinOutputFormatCombination {
        List<String[]> pinyinList = new ArrayList<>();
        for (int i = 0; i < chinese.length(); i++) {
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(
                    chinese.charAt(i), this.outputFormat);
            if (pinyinArray != null && pinyinArray.length > 0) {
                pinyinList.add(pinyinArray);
            }
        }
        Set<String> pinyins = new HashSet<>();
        for (String[] array : pinyinList) {
            if (pinyins.isEmpty()) {
                for (String charPinpin : array) {
                    pinyins.add(charPinpin.substring(0, 1));
                }
            } else {
                Set<String> pres = pinyins;
                pinyins = new HashSet<>();
                for (String pre : pres) {
                    for (String charPinyin : array) {
                        pinyins.add(pre + charPinyin.substring(0, 1));
                    }
                }
            }
        }
        return pinyins;
    }

    /**
     * 获取拼音
     *
     * @param chinese 含中文的字符串，若不含中文，原样输出
     * @return 转换后的文本
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    private Collection<String> GetPyString(String chinese)
            throws BadHanyuPinyinOutputFormatCombination {
        List<String[]> pinyinList = new ArrayList<>();
        for (int i = 0; i < chinese.length(); i++) {
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(
                    chinese.charAt(i), this.outputFormat);
            if (pinyinArray != null && pinyinArray.length > 0) {
                pinyinList.add(pinyinArray);
            }
        }
        Set<String> pinyins = new HashSet<>();
        for (String[] array : pinyinList) {
            if (pinyins.isEmpty()) {
                pinyins = new HashSet<>();
                Collections.addAll(pinyins, array);
            } else {
                Set<String> pres = pinyins;
                pinyins = new HashSet<>();
                for (String pre : pres) {
                    for (String charPinyin : array) {
                        pinyins.add(pre + charPinyin);
                    }
                }
            }
        }
        return pinyins;
    }
}
