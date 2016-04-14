package com.wfj.search.online.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wfj.search.online.common.TrackingException;
import com.wfj.search.online.web.common.pojo.BrandDisplayPojo;
import com.wfj.search.online.web.common.pojo.CategoryDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.param.SortParamRestorer;
import com.wfj.search.online.web.service.ISearchConfigService;
import com.wfj.search.online.web.service.ISearchService;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>create at 16-1-16</p>
 *
 * @author liufl
 * @since 1.0.0
 */
@Controller
public class IndexController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${search.host.searchHost}")
    private String searchHost;
    @Value("${search.host.brandHost}")
    private String brandHost;
    @Value("${search.host.listHost}")
    private String listHost;
    @Autowired
    private ISearchConfigService searchConfigService;
    @Autowired
    private ISearchService searchService;
    @Autowired
    private SortParamRestorer sortParamRestorer;

    private HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat(); // 拼音转接输出格式

    @RequestMapping("/index")
    public String index(Model model, @RequestHeader(HttpHeaders.HOST) String host,
            @RequestParam(value = "channel", required = false) String channel,
            HttpServletRequest request) {
        model.addAttribute("brandHost", brandHost);
        model.addAttribute("listHost", listHost);
        if (this.searchHost.equals(host)) {
            return "forward:/jumpTo3w.html";
        } else if (this.brandHost.equals(host)) {
            try {
                this.availableCategoriesAndBrands(model, channel, true);
            } catch (TrackingException e) {
                request.setAttribute("errorCode", e.getErrorCode());
                throw new RuntimeException(e);
            } catch (Exception e) {
                logger.error("搜索异常, 0x530007", e);
                request.setAttribute("errorCode", "0x530007");
                throw e;
            }
            return "brands-index";
        } else if (this.listHost.equals(host)) {
            try {
                this.availableCategoriesAndBrands(model, channel, false);
            } catch (TrackingException e) {
                request.setAttribute("errorCode", e.getErrorCode());
                throw new RuntimeException(e);
            } catch (Exception e) {
                logger.error("搜索异常, 0x530008", e);
                request.setAttribute("errorCode", "0x530008");
                throw e;
            }
            return "lists-index";
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private void availableCategoriesAndBrands(Model model, String channel, boolean facetBrandCategories)
            throws TrackingException {
        SearchParams params = this.emptyParam(channel);
        SearchResult result = this.searchService.doListAllCategories(params);
        List<CategoryDisplayPojo> topLevelCategories = result.getFilters().getQueryMatchedCategoryTree();
        model.addAttribute("categories", topLevelCategories);
        params.setFacetBrandCategories(facetBrandCategories);
        result = this.searchService.doListAvailableBrands(params);
        List<BrandDisplayPojo> availableBrands = result.getFilters().getAvailableBrands().getAvailables();
        LinkedHashMap<String, List<BrandDisplayPojo>> groupedBrands = Maps.newLinkedHashMap();
        for (char c = 'A'; c <= 'Z'; c++) {
            groupedBrands.put("" + c, Lists.newArrayList());
        }
        groupedBrands.put("0-9", Lists.newArrayList());
        groupedBrands.put("#", Lists.newArrayList());
        for (BrandDisplayPojo brand : availableBrands) {
            char dc = brand.getDisplay().toUpperCase().charAt(0);
            if (dc >= 'A' && dc <= 'Z') {
                groupedBrands.get("" + dc).add(brand);
            } else if (dc >= '0' && dc <= '9') {
                groupedBrands.get("0-9").add(brand);
            } else {
                Collection<String> abbre = null;
                try {
                    abbre = this.getAbbre(brand.getDisplay());
                } catch (BadHanyuPinyinOutputFormatCombination ignored) {
                }
                if (abbre != null) {
                    final AtomicBoolean added = new AtomicBoolean(false);
                    abbre.stream().filter(StringUtils::isNotBlank).forEach(s -> {
                        char c = s.trim().toUpperCase().charAt(0);
                        if (c >= 'A' && c <= 'Z') {
                            groupedBrands.get("" + c).add(brand);
                            added.set(true);
                        } else if (c >= '0' && c <= '9') {
                            groupedBrands.get("0-9").add(brand);
                            added.set(true);
                        }
                    });
                    if (!added.get()) {
                        groupedBrands.get("#").add(brand);
                    }
                }
            }
        }
        for (List<BrandDisplayPojo> brandDisplayPojos : groupedBrands.values()) {
            brandDisplayPojos.sort(new Comparator<BrandDisplayPojo>() {
                @Override
                public int compare(BrandDisplayPojo o1, BrandDisplayPojo o2) {
                    return o1.getDisplay().compareTo(o2.getDisplay());
                }
            });
        }
        model.addAttribute("brands", groupedBrands);
    }

    private SearchParams emptyParam(String channel) {
        SearchParams searchParams = new SearchParams();
        searchParams.setChannel(StringUtils.isEmpty(channel) ? this.searchConfigService.getChannel() : channel);
        this.sortParamRestorer.restore(searchParams, null);
        return searchParams;
    }

    /**
     * 获取拼音缩写
     *
     * @param chinese 含中文的字符串，若不含中文，原样输出
     * @return 转换后的文本
     * @throws net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination
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
}
