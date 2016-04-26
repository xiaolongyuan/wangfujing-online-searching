<#-- @ftlvariable name="random" type="java.util.Random" -->
<#-- @ftlvariable name="brandLocation" type="java.lang.String" -->
<#-- @ftlvariable name="searchLocation" type="java.lang.String" -->
<#-- @ftlvariable name="memberHomeUrl" type="java.lang.String" -->
<#-- @ftlvariable name="registerPageUrl" type="java.lang.String" -->
<#-- @ftlvariable name="loginPageUrl" type="java.lang.String" -->
<#-- @ftlvariable name="logoutPageUrl" type="java.lang.String" -->
<#-- @ftlvariable name="context" type="org.springframework.web.servlet.support.RequestContext" -->
<#-- @ftlvariable name="itemUrlPostfix" type="java.lang.String" -->
<#-- @ftlvariable name="itemUrlPrefix" type="java.lang.String" -->
<#-- @ftlvariable name="imageHosts" type="java.util.List<String>" -->
<#-- @ftlvariable name="imageLocationTemplate" type="java.lang.String" -->
<#-- @ftlvariable name="cssLocationTemplate" type="java.lang.String" -->
<#-- @ftlvariable name="cssHosts" type="java.util.List<String>" -->
<#-- @ftlvariable name="tbarContent" type="java.lang.String" -->
<#-- @ftlvariable name="headerContent" type="java.lang.String" -->
<#-- @ftlvariable name="navigationContent" type="java.lang.String" -->
<#-- @ftlvariable name="wwwLocation" type="java.lang.String" -->
<#-- @ftlvariable name="result" type="com.wfj.search.online.web.common.pojo.SearchResult" -->
<#-- @ftlvariable name="footerContent" type="java.lang.String" -->
<#-- @ftlvariable name="jsHosts" type="java.util.List<String>" -->
<#-- @ftlvariable name="wwwJsHosts" type="java.util.List<String>" -->
<#-- @ftlvariable name="jsLocationTemplate" type="java.lang.String" -->
<#-- @ftlvariable name="wwwJsLocationTemplate" type="java.lang.String" -->
<#include "../macro/randomLocation.ftl">
<#assign condLimit=1/>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="ie6" lang="zh-CN"> <![endif]-->
<!--[if IE 7]>         <html class="ie7" lang="zh-CN"> <![endif]-->
<!--[if IE 8]>         <html class="ie8" lang="zh-CN"> <![endif]-->
<!--[if gt IE 8]><!-->
<html lang="zh-CN"> <!--<![endif]-->
<head>
    <meta charset="UTF-8">
    <meta property="qc:admins" content="2410213673671676521676375"/>
    <meta name="keywords" content="王府井,王府井网上商城,王府井百货"/>
    <meta name="description"
          content="王府井网上商城依托王府井百货品牌优势，是集化妆品、服装、鞋帽、箱包、珠宝、配饰、户外、运动、家居生活等产品，定位于面向品位、品质、时尚和追求购物乐趣消费者的B2C精品购物平台。"/>
<#--noinspection HtmlUnknownTarget-->
    <link href="<@randomLocation hosts=cssHosts template=cssLocationTemplate/>/base.css" rel="stylesheet">
    <link href="<@randomLocation hosts=cssHosts template=cssLocationTemplate/>/brand.css" rel="stylesheet">
    <title>${result.params.selectedBrands.selected[0].display}</title>
</head>
<body>
${tbarContent}
${headerContent}
${navigationContent}
<!--品牌馆中心内容-->
<div class="brandMain clearfix">
    <div class="wrap">
        <div class="crumbs clearfix">
            <strong>您现在的位置：</strong>
        <#--noinspection HtmlUnknownTarget-->
            <a class="link" href="${wwwLocation}"><strong>首页</strong></a>&gt;
            <span><a href="${context.contextPath}${result.params.selectedBrands.selected[0].url}.html"
            >${result.params.selectedBrands.selected[0].display}</a>
            </span><#if result.params.selectedCategories?size gt 0>&gt;</#if>
        <#list result.params.selectedCategories as cat>
            <span><a href="${context.contextPath}${cat.url}.html">${cat.display}</a></span><#if cat_has_next>&gt;</#if>
        </#list>
            <span class="fr">找到相关商品<b class="red">${result.pagination.totalResults}</b>件</span>
        </div>
        <div class="brand-bd clearfix">
            <div class="brand-sub" id="js_brand_sub">
            <#list result.filters.queryMatchedCategoryTree as rootCategory>
                <div class="hd">
                    <h2 class="tit-name">${rootCategory.display}<span>(${rootCategory.facetCount})</span></h2>

                    <div class="bd">
                        <#list rootCategory.children as _2ndCategory>
                            <#assign _c = false>
                            <#if result.params.selectedCategories?size gt 1>
                                <#assign _c = result.params.selectedCategories[1].id == _2ndCategory.id>
                            <#elseif rootCategory_index == 0>
                                <#assign _c = true>
                            </#if>
                            <div class="item<#if _c> on</#if>">
                                <div class="b-name">
                                    <span>${_2ndCategory.display}(${_2ndCategory.facetCount})</span>
                                    <b class="fr"><#if _c>-<#else>+</#if></b>
                                </div>
                                <div class="b-names">
                                    <ul class="clearfix">
                                        <#list _2ndCategory.children as _3rdCategory>
                                            <li>
                                                <a href="${context.contextPath}${_3rdCategory.url}.html">${_3rdCategory.display}
                                                    <span>(${_3rdCategory.facetCount})</span></a></li>
                                        </#list>
                                    </ul>
                                </div>
                            </div>
                        </#list>
                    </div>
                </div>
            </#list>
            </div>
            <div class="brand-main">
            <#if result.params.selectedBrands.selected[0].picture??>
                <#if result.params.selectedBrands.selected[0].picture != "">
                    <div class="brand-banner">
                        <img src="<@randomLocation hosts=imageHosts template=imageLocationTemplate/>/brand/${result.params.selectedBrands.selected[0].picture}">
                    </div>
                </#if>
            </#if>
                <div class="p20 goods-filter">
                    <table cellpadding="0" cellspacing="0" border="0" class="goodsTable">
                        <colgroup>
                            <col class="w15">
                            <col class="w10">
                            <col class="w75">
                        </colgroup>
                    <#if result.filters.availableRanges.availables?size gt condLimit>
                        <tr>
                            <td align="right">价格：</td>
                            <td align="center">
                                <#if result.params.selectedRange??>
                                    <#assign sr=result.params.selectedRange.min + "_" + result.params.selectedRange.max>
                                    <div class="gray-btn"><a
                                            href="${context.contextPath}${result.params.selectedRange.url}.html">不限</a>
                                    </div>
                                <#else>
                                    <#assign sr="">
                                    <div class="gray-btn">不限</div>
                                </#if>
                            </td>
                            <td>
                                <ul class="goodsList">
                                    <#list result.filters.availableRanges.availables as price>
                                        <#if sr == price.min + "_" + price.max>
                                            <li class="on"><a>${price.display}</a></li>
                                        <#else>
                                            <li><a href="${context.contextPath}${price.url}.html">${price.display}</a>
                                            </li>
                                        </#if>
                                    </#list>
                                </ul>
                            </td>
                        </tr>
                    </#if>
                    <#if result.filters.availableColors.availables?size gt condLimit>
                        <tr>
                            <td align="right">颜色：</td>
                            <td align="center">
                                <#if result.params.selectedColors.selected?size gt 0>
                                    <div class="gray-btn"><a
                                            href="${context.contextPath}${result.params.selectedColors.url}.html">不限</a>
                                    </div>
                                <#else>
                                    <div class="gray-btn">不限</div>
                                </#if>
                            </td>
                            <td>
                                <ul class="goodsList">
                                    <#list result.filters.availableColors.availables as color>
                                        <#assign _c = false>
                                        <#list result.params.selectedColors.selected as sc>
                                            <#if sc.id = color.id>
                                                <#assign _c = true>
                                                <#break>
                                            </#if>
                                        </#list>
                                        <#if _c>
                                            <li class="on"><a>${color.display}</a></li>
                                        <#else>
                                            <li><a href="${context.contextPath}${color.url}.html">${color.display}</a>
                                            </li>
                                        </#if>
                                    </#list>
                                </ul>
                            </td>
                        </tr>
                    </#if>
                    <#assign showPropCondCount=0/>
                    <#list result.filters.availableProperties as availableProperties>
                        <#if showPropCondCount < 3 && availableProperties?? && availableProperties.availables?size gt condLimit>
                            <#assign showPropCondCount = showPropCondCount + 1/>
                            <tr>
                                <td align="right">${availableProperties.display}：</td>
                                <td align="center">
                                    <#list result.params.selectedProperties as sp>
                                        <#if availableProperties.id == sp.id>
                                            <#assign _sp = sp>
                                            <#break>
                                        </#if>
                                    </#list>
                                    <#if _sp??>
                                        <div class="gray-btn"><a href="${context.contextPath}${_sp.url}.html">不限</a>
                                        </div>
                                    <#else>
                                        <div class="gray-btn">不限</div>
                                    </#if>
                                </td>
                                <td>
                                    <ul class="goodsList">
                                        <#list availableProperties.availables as pv>
                                            <#assign _c = false>
                                            <#if _sp??>
                                                <#list _sp.values as sv>
                                                    <#if sv.id = pv.id>
                                                        <#assign _c = true>
                                                        <#break>
                                                    </#if>
                                                </#list>
                                            </#if>
                                            <#if _c>
                                                <li class="on"><a>${pv.display}</a></li>
                                            <#else>
                                                <li><a href="${context.contextPath}${pv.url}.html">${pv.display}</a>
                                                </li>
                                            </#if>
                                        </#list>
                                    </ul>
                                </td>
                            </tr>
                        </#if>
                    </#list>
                    </table>
                </div>
                <div class="goods-list-section">
                    <div class="toobar-tit">
                        <div class="fl">商品排序按：</div>
                        <div class="fl sort">
                        <#list result.sorts as sort>
                            <#assign _on = false/>
                            <#assign _arr = ""/>
                            <#assign sortUrl=sort.url/>
                            <#if sort.selected>
                                <#assign sortUrl=sort.opposite.url/>
                                <#if sort.order == sort.opposite.order>
                                    <#assign _on = true/>
                                </#if>
                                <#if sort.order != sort.opposite.order>
                                    <#if sort.order == "ASC">
                                        <#assign _on = true/>
                                        <#assign _arr = "&nbsp;&uarr;"/>
                                    </#if>
                                    <#if sort.order == "DESC">
                                        <#assign _on = true/>
                                        <#assign _arr = "&nbsp;&darr;"/>
                                    </#if>
                                </#if>
                            </#if>
                            <#if !sort.selected>
                                <#if sort.order != sort.opposite.order>
                                    <#if sort.order == "ASC">
                                        <#assign _arr = "&nbsp;&uarr;"/>
                                    </#if>
                                    <#if sort.order == "DESC">
                                        <#assign _arr = "&nbsp;&darr;"/>
                                    </#if>
                                </#if>
                            </#if>
                            <div class="item<#if _on> on</#if>"><a
                                    href="${context.contextPath}${sortUrl}.html">${sort.display}${_arr}</a>
                            </div>
                        </#list>
                        </div>
                        <div class="fr r-page">
                            <div class="fl">
                                <span>${result.pagination.currentPage}</span>/<span>${result.pagination.totalPage}</span>
                            </div>
                        <#if result.pagination.prePage??>
                            <a href="${result.pagination.prePage.url}" class="fl">上一页</a>
                        <#else>
                            <a class="fl">上一页</a>
                        </#if>
                        <#if result.pagination.postPage??>
                            <a href="${result.pagination.postPage.url}" class="fl">下一页</a>
                        <#else>
                            <a class="fl">下一页</a>
                        </#if>
                        </div>
                    </div>
                    <div id="Search_Result_div" class="goods-list">
                        <ul id="WC_CatalogSearchResultDisplay_div_5" class="grid-view clearfix 8">
                        <#list result.successList as pro>
                            <li class="col item">
                                <div class="p-img">
                                    <div class="pic-box ${pro.spuId}">
                                        <a class="itemhover" id="catalogEntry_img${pro.spuId}"
                                           href="${itemUrlPrefix}${pro.spuId}${itemUrlPostfix}" target="_blank"
                                           title="${pro.title}">
                                            <#assign pic = pro.colorMasterPicture/>
                                            <#if pro.colorMasterPictureOfPix['220x220']??>
                                                <#assign pic = pro.colorMasterPictureOfPix['220x220']/>
                                            </#if>
                                            <#assign imgLocation=imageLocationTemplate?replace('{host}', imageHosts[random.nextInt(imageHosts?size)])>
                                            <img id="spu-big-pic-${pro.spuId}"
                                            <#--id="img_${pro.spuId}-->
                                                 style="display: inline;"
                                                 data-lazyload="${imgLocation}/${pic}"
                                                 class="lazy-pic"
                                                 data-src="${imgLocation}/${pic}" data-original="${imgLocation}/${pic}"
                                                 src="${imgLocation}/${pic}"
                                                 alt="${pro.title}">
                                        </a>
                                    </div>
                                    <div class="posa pin-ico ico6_tag"></div>
                                </div>
                                <div class="posr item-wrap">
                                    <div class="posr thumb-list">
                                        <div class="hd" id="act-thumb-btn"><a href="javascript:void(0);"
                                                                              class="btn-pre prevStop"><span
                                                class="arrow arrowL "><s class="s"></s></span></a> <a
                                                href="javascript:void(0);" class="btn-next nextStop"><span
                                                class="arrow arrowR "><s class="s"></s></span></a></div>
                                        <ul class="bd thumb-items clearfix">
                                            <#list pro.colorItems as ci>
                                                <#assign bigPic = ci.colorMasterPicture/>
                                                <#assign smallPic = ci.colorMasterPicture/>
                                                <#if ci.colorMasterPictureOfPix['220x220']??>
                                                    <#assign bigPic = ci.colorMasterPictureOfPix['220x220']/>
                                                </#if>
                                                <#if ci.colorMasterPictureOfPix['60x60']??>
                                                    <#assign smallPic = ci.colorMasterPictureOfPix['60x60']/>
                                                </#if>
                                                <li style="float: left; width: 40px;"
                                                    data-burl="${itemUrlPrefix}${ci.skuId}${itemUrlPostfix}"
                                                    data-bimg="<@randomLocation hosts=imageHosts template=imageLocationTemplate/>/${bigPic}"
                                                    class="fl thumb-pic thumb-selected">
                                                    <a href="${itemUrlPrefix}${ci.skuId}${itemUrlPostfix}" class="fl"
                                                       target="_blank">
                                                        <img data-spu-id="${ci.spuId}"
                                                             data-sku-id="${ci.skuId}"
                                                             data-big-pic="<@randomLocation hosts=imageHosts template=imageLocationTemplate/>/${bigPic}"
                                                             class="lazy-pic"
                                                             data-lazyload="<@randomLocation hosts=imageHosts template=imageLocationTemplate/>/${smallPic}"
                                                             src="<@randomLocation hosts=imageHosts template=imageLocationTemplate/>/${smallPic}"
                                                             alt="${ci.title}"
                                                             style="display: inline-block;"
                                                             onmouseover="switchSpuBigPic(this)"
                                                             onmouseout="resetSpuBigPic(this)">
                                                    </a>
                                                </li>
                                            </#list>
                                        </ul>
                                    </div>
                                    <div class="p-price">
                                        <span class="price"><strong
                                                class="red">￥${pro.currentPrice?string("0.00")}</strong></span>
                                    </div>
                                    <div class="p-name">
                                        <a target="_blank" title="${pro.title}"
                                           href="${itemUrlPrefix}${pro.spuId}${itemUrlPostfix}"><#if !pro.longDesc?? || pro.longDesc.trim().isEmpty()>${pro.title} ${pro.shortDesc!}<#else>${pro.longDesc} ${pro.title}</#if></a>
                                    </div>
                                    <p class="red p-words"></p>

                                    <div class="p-promote">
                                    </div>
                                </div>
                                <div class="brand-name clearfix dn">
                                    <div class="brand-wrap">
                                        <a href="${brandLocation}/0-0-0-${pro.brandId}-0-0-0-0-0-1_1-1-0.html"
                                           target="_blank" class="fl">${pro.brandName}</a>
                                    </div>
                                </div>
                            </li>
                        </#list>
                        </ul>
                    </div>
                    <div class="pagination-pages clearfix" id="pager">
                        <div class="bk30"></div>
                        <div class="fr pagination-box">
                            <div class="fl pagination-count"><span>共</span><span
                                    class="red">${result.pagination.totalResults}</span><span>个商品</span>
                            </div>
                            <div class="fl pagination-wrap">
                            <#if result.pagination.currentPage == 1>
                                <a href="" class="page-btn next page-disabled">上一页</a>
                            </#if>
                            <#if result.pagination.currentPage gt 1>
                                <a href="${context.contextPath}${result.pagination.prePage.url}.html"
                                   class="page-btn next">上一页</a>
                            </#if>
                            <#if result.pagination.currentPage gt 4>
                                <a href="${context.contextPath}${result.pagination.firstPage.url}.html">1</a> ...
                            </#if>
                            <#list result.pagination.pages as page>
                                <#if result.pagination.currentPage == page.pageNum >
                                    <a class="current">${page.display}</a>
                                </#if>
                                <#if result.pagination.currentPage != page.pageNum >
                                    <a href="${context.contextPath}${page.url}.html">${page.display}</a>
                                </#if>
                            </#list>
                            <#if result.pagination.currentPage lt result.pagination.totalPage - 3>
                                ... <a
                                    href="${context.contextPath}${result.pagination.lastPage.url}.html">${result.pagination.lastPage.pageNum}</a>
                            </#if>
                            <#if result.pagination.currentPage lt result.pagination.totalPage>
                                <a href="${context.contextPath}${result.pagination.postPage.url}.html"
                                   class="page-btn next">下一页</a>
                            </#if>
                            <#if result.pagination.currentPage == result.pagination.totalPage>
                                <a class="page-btn next page-disabled">下一页</a>
                            </#if>
                            </div>
                        </div>
                        <div class="bk30"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--end 品牌馆中心内容-->
${footerContent}
<#include "no-pic-script.ftl">
<#--noinspection HtmlUnknownTarget-->
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/plugin/jquery-1.9.1.min.js"></script>
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/plugin/jquery.slide.js"></script>
<#--noinspection HtmlUnknownTarget-->
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/plugin/jquery.fly.min.js"></script>
<!--[if lte IE 9]>
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/plugin/requestAnimationFrame.js"></script>
<![endif]-->
<#--noinspection HtmlUnknownTarget-->
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/public.js"></script>
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/brand.js"></script>
<script type="text/javascript">
    var searchLocation = '${searchLocation}';
    var picLocation = '<@randomLocation hosts=imageHosts template=imageLocationTemplate/>';
    var memberHome = '${memberHomeUrl}';
    var logoutPageUrl = '${logoutPageUrl}';
    var loginPageUrl = '${loginPageUrl}';
    var registerPageUrl = '${registerPageUrl}';
    var itemUrlPrefix = '${itemUrlPrefix}';
    var itemUrlPostfix = '${itemUrlPostfix}';
</script>
<#--noinspection HtmlUnknownTarget-->
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/search_form.js"></script>
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/search_hot_word.js"></script>
<#--noinspection HtmlUnknownTarget-->
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/logon_info.js"></script>
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/cart_num.js"></script>
<#--noinspection HtmlUnknownTarget-->
<script src="<@randomLocation hosts=wwwJsHosts template=wwwJsLocationTemplate/>/script/js/service/cookieopt.js"></script>
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/history_list.js"></script>
<#--noinspection HtmlUnknownTarget-->
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/search_list_pic_switch.js"></script>

</body>
</html>