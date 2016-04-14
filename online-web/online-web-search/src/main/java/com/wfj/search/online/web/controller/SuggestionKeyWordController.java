package com.wfj.search.online.web.controller;

import com.wfj.search.online.web.service.ISuggestionKeyWordService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <br/>create at 15-12-18
 *
 * @author liuxh
 * @since 1.0.0
 */
@Controller("/suggestion/key-word")
public class SuggestionKeyWordController {
    private static final Logger logger = LoggerFactory.getLogger(SuggestionKeyWordController.class);
    @Autowired
    private ISuggestionKeyWordService suggestionKeyWordService;

    @RequestMapping("/list")
    public String list(@RequestParam(value = "prefix", required = false) String prefix, Model model) {
        logger.debug("the prefix word is {}", prefix);
        List<String> keyWords;
        if (StringUtils.isBlank(prefix)) {
            // 当输入前缀词为空，提取默认词频排序词
            keyWords = suggestionKeyWordService.prefix();
        } else {
            // 当输入前缀词不为空，根据空格切分单词
            String[] words = prefix.split("\\s+");
            int length = words.length - 1;
            String[] placeholders = new String[length];
            System.arraycopy(words, 0, placeholders, 0, length);
            // 最后一个词为前缀词，其他词为占位词
            keyWords = suggestionKeyWordService.prefix(words[length], placeholders);
        }
        model.addAttribute("keyWords", keyWords);
        return "jsonView";
    }
}
