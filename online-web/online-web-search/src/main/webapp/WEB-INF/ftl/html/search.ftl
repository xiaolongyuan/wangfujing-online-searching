<#-- @ftlvariable name="random" type="java.util.Random" -->
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
<#-- @ftlvariable name="tbarContent" type="java.lang.String" -->
<#-- @ftlvariable name="jsHosts" type="java.util.List<String>" -->
<#-- @ftlvariable name="wwwJsHosts" type="java.util.List<String>" -->
<#-- @ftlvariable name="jsLocationTemplate" type="java.lang.String" -->
<#-- @ftlvariable name="wwwJsLocationTemplate" type="java.lang.String" -->
<#-- @ftlvariable name="result" type="com.wfj.search.online.web.common.pojo.SearchResult" -->
<#-- @ftlvariable name="wwwLocation" type="java.lang.String" -->
<#-- @ftlvariable name="footerContent" type="java.lang.String" -->
<#-- @ftlvariable name="navigationContent" type="java.lang.String" -->
<#-- @ftlvariable name="headerContent" type="java.lang.String" -->
<#-- @ftlvariable name="cssLocationTemplate" type="java.lang.String" -->
<#-- @ftlvariable name="cssHosts" type="java.util.List<String>" -->
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
    <link href="<@randomLocation hosts=cssHosts template=cssLocationTemplate/>/search.css" rel="stylesheet">
    <title>商品搜索 - ${result.params.inputQuery}</title>
</head>
<body>
${tbarContent}
${headerContent}
${navigationContent}
<div id="j_search" class="search-main">
    <div class="wrap">
        <!--面包屑-->
        <div class="crumbs clearfix">
            <div class="propertys fl">
            <#--noinspection HtmlUnknownTarget-->
                <a class="link" href="${wwwLocation}"><strong>首页</strong></a>&gt;
            <#list result.params.selectedCategories as cat>
                <#if cat_has_next>
                    <span><a href="${context.contextPath}${cat.url}.html">${cat.display}</a></span>&gt;
                </#if>
            </#list>
            <#if result.params.selectedCategories?size gt 0 >
                <span id="j_carbd" class="posr">${result.params.selectedCategories?last.display}<i
                        class="posa arrow arrowB"></i>
                <#--noinspection HtmlUnknownTag-->
                    <div class="subitem posa" style="display: none;">
                        <#list result.filters.queryMatchedCategoryTree as rootCategory>
                            <#if rootCategory.children?? && rootCategory.children?size gt 0>
                                <#list rootCategory.children as cat>
                                    <#if cat.children?? && cat.children?size gt 0>
                                        <dl<#if !cat_has_next> class="borderno"</#if>>
                                            <dt>
                                                <a href="${context.contextPath}${cat.url}.html">${cat.display}</a>
                                            </dt>
                                            <dd>
                                                <#list cat.children as child>
                                                    <span><a
                                                            href="${context.contextPath}${child.url}.html">${child.display}</a></span>
                                                </#list>
                                            </dd>
                                        </dl>
                                    </#if>
                                </#list>
                            </#if>
                        </#list>
                    <#--noinspection HtmlUnknownTag-->
                    </div>
                </span>
            </#if>
            </div>
            <div class="fl posr listSearch">
            <#--noinspection HtmlFormInputWithoutLabel-->
                <input type="text" value="${result.params.inputQuery}" id="keyWordMesg" maxlength="50">
                <i class="posa icon_26" id="keyWordMesgIcon"></i>
            </div>
            <div class="condition">
            <#if result.params.selectedBrands.selected?size gt 0>
                <label class="cons">
                    <a href="${context.contextPath}${result.params.selectedBrands.url}.html">
                        品牌:<#list result.params.selectedBrands.selected as brand>${brand.display}<#if brand_has_next>
                        、</#if></#list>
                        <span class="closeCons"></span>
                    </a>
                </label>
            </#if>
            <#if result.params.selectedColors.selected?size gt 0>
                <label class="cons">
                    <a href="${context.contextPath}${result.params.selectedColors.url}.html">
                        颜色:<#list result.params.selectedColors.selected as color>${color.display}<#if color_has_next>
                        、</#if></#list>
                        <span class="closeCons"></span>
                    </a>
                </label>
            </#if>
            <#if result.params.selectedRange??>
                <label class="cons">
                    <a href="${context.contextPath}${result.params.selectedRange.url}.html">
                        价格:${result.params.selectedRange.display}<span class="closeCons"></span>
                    </a>
                </label>
            </#if>
            <#list result.params.selectedProperties as sp>
                <#if sp?? && sp.values?size gt condLimit>
                    <label class="cons">
                        <a href="${context.contextPath}${sp.url}.html">
                        ${sp.display}:<#list sp.values as pv>${pv.display}<#if pv_has_next>、</#if></#list><span
                                class="closeCons"></span>
                        </a>
                    </label>
                </#if>
            </#list>
            </div>
        </div>
        <!-- 分类导航 -->
        <#assign tr1s = 0/>
        <table cellspacing="0" cellpadding="0" class="select br4">
            <colgroup>
                <col class="td10">
                <col class="td80">
                <col class="td10">
            </colgroup>
        <#if result.filters.availableBrands.availables?size gt condLimit>
            <#assign tr1s = tr1s + 1/>
            <tr>
                <td valign="top" class="oneTd">品牌</td>
                <td class="listTd">
                    <ul class="posr td_list col clearfix">
                        <#list result.filters.availableBrands.availables as brand>
                            <li><a href="${context.contextPath}${brand.url}.html" class="fil-name">${brand.display}</a>
                            </li>
                        </#list>
                    </ul>
                </td>
                <td class="more">
                    <div class="vb mvb"><span>更多</span><i class="arrb"></i></div>
                </td>
            </tr>
        </#if>
        <#if result.filters.availableRanges.availables?size gt condLimit>
            <#assign tr1s = tr1s + 1/>
            <tr>
                <td valign="top" class="oneTd">价格</td>
                <td class="listTd">
                    <ul class="posr td_list col clearfix">
                        <#list result.filters.availableRanges.availables as price>
                            <li><a href="${context.contextPath}${price.url}.html" class="fil-name">${price.display}</a>
                            </li>
                        </#list>
                    </ul>
                </td>
                <td class="more">
                    <div class="vb mvb"><span>更多</span><i class="arrb"></i></div>
                </td>
            </tr>
        </#if>
        <#assign showCatCount = 0>
        <#if result.params.selectedCategories?size lte 2>
            <#if result.params.selectedCategories?size gt 0>
                <#list result.filters.categoryTree as rootCategory>
                    <#if rootCategory.id == result.params.selectedCategories[0].id>
                        <#assign selectableRootCategories = [rootCategory]>
                    </#if>
                </#list>
            <#else>
                <#assign selectableRootCategories = result.filters.categoryTree>
            </#if>
            <#list selectableRootCategories as rootCategory>
                <#if rootCategory.children?? && rootCategory.children?size gt 0>
                    <#if result.params.selectedCategories?size gt 1>
                        <#list rootCategory.children as _2ndCategory>
                            <#if _2ndCategory.id == result.params.selectedCategories[1].id>
                                <#assign selectable2ndCategories = [_2ndCategory]>
                            </#if>
                        </#list>
                    <#else>
                        <#assign selectable2ndCategories = rootCategory.children>
                    </#if>
                    <#list selectable2ndCategories as _2ndCategory>
                        <#if _2ndCategory.children?? && _2ndCategory.children?size gt 0>
                            <#if result.params.selectedCategories?size lt 3>
                                <#assign showCatCount = showCatCount + 1>
                                <tr>
                                    <td valign="top" class="oneTd"><a
                                            href="${context.contextPath}${_2ndCategory.url}.html">${_2ndCategory.display}</a>
                                    </td>
                                    <td class="listTd">
                                        <ul class="posr td_list col clearfix ">
                                            <#list _2ndCategory.children as child>
                                                <li><a href="${context.contextPath}${child.url}.html">${child.display}
                                                    <span
                                                            class="sec-tit">(${child.facetCount})</span></a></li>
                                            </#list>
                                        </ul>
                                    </td>
                                    <td class="more">
                                        <div class="vb mvb"><span>更多</span><i class="arrb"></i></div>
                                    </td>
                                </tr>
                            </#if>
                        </#if>
                    </#list>
                </#if>
            </#list>
        </#if>
        <#if showCatCount gt 0>
            <#assign tr1s = tr1s + 1/>
        </#if>
        </table>
    <#assign tr2s = 0>
        <!-- 筛选条件 -->
        <table cellspacing="0" cellpadding="0" id="j_classTable" class="select">
            <colgroup>
                <col class="td10">
                <col class="td80">
                <col class="td10">
            </colgroup>
        <#if result.filters.availableColors.availables?size gt condLimit>
            <tr<#if tr1s + tr2s gt 2> class="sctdn"</#if>>
                <td valign="top" class="oneTd">颜色</td>
                <td class="listTd">
                    <ul class="posr td_list col clearfix">
                        <#list result.filters.availableColors.availables as color>
                            <li><a href="${context.contextPath}${color.url}.html" class="fil-name">${color.display}</a>
                            </li>
                        </#list>
                    </ul>
                </td>
                <td class="more">
                    <div class="vb mvb"><span>更多</span><i class="arrb"></i></div>
                </td>
            </tr>
            <#assign tr2s = tr2s + 1/>
        </#if>
        <#list result.filters.availableProperties as availableProperties>
            <#if availableProperties?? && availableProperties.availables?size gt condLimit>
                <tr<#if tr1s + tr2s gt 2> class="sctdn"</#if>>
                    <td valign="top" class="oneTd">${availableProperties.display}</td>
                    <td class="listTd">
                        <ul class="posr td_list col clearfix">
                            <#list availableProperties.availables as prop>
                                <li><a href="${context.contextPath}${prop.url}.html"
                                       class="fil-name">${prop.display}</a></li>
                            </#list>
                        </ul>
                    </td>
                    <td class="more">
                        <div class="vb mvb"><span>更多</span><i class="arrb"></i></div>
                    </td>
                </tr>
                <#assign tr2s = tr2s + 1/>
            </#if>
        </#list>
        </table>
    <#if tr1s + tr2s gt 2>
        <div class="mor_b">
            <div class="mor_t"><i class="arroB"></i><span>更多</span></div>
        </div>
    </#if>
        <!-- 商品排序 -->
        <div class="filterForm clearfix">
            <div class="filtdlt">商品排序</div>
            <ul>
            <#list result.sorts as sort>
                <#assign sortClass = " class=\"\""/>
                <#assign sortUrl=sort.url/>
                <#if sort.selected>
                    <#assign sortUrl=sort.opposite.url/>
                    <#if sort.order == sort.opposite.order>
                        <#assign sortClass = " class=\"filtetselect\""/>
                    </#if>
                    <#if sort.order != sort.opposite.order>
                        <#if sort.order == "ASC">
                            <#assign sortClass = " class=\"filtetselect up_white\""/>
                        </#if>
                        <#if sort.order == "DESC">
                            <#assign sortClass = " class=\"filtetselect down_white\""/>
                        </#if>
                    </#if>
                </#if>
                <#if !sort.selected>
                    <#if sort.order != sort.opposite.order>
                        <#if sort.order == "ASC">
                            <#assign sortClass = " class=\"up\""/>
                        </#if>
                        <#if sort.order == "DESC">
                            <#assign sortClass = " class=\"down\""/>
                        </#if>
                    </#if>
                </#if>
                <li${sortClass}>
                    <a href="${context.contextPath}${sortUrl}.html">${sort.display}</a>
                </li>
            </#list>
            </ul>

            <div class="paging">
                <label>共<span class="red">${result.pagination.totalResults}</span>个商品</label>
                <label><span id="firstPage" class="red">${result.pagination.currentPage}</span>/<span
                        id="Tpages">${result.pagination.totalPage}</span></label>
            <#if result.pagination.currentPage == 1>
                <label class="page p_left col"><a href="javascript:void(0);" class="col">&lt;上一页</a></label>
            </#if>
            <#if result.pagination.currentPage gt 1>
                <label class="page p_left "><a href="${context.contextPath}${result.pagination.prePage.url}.html"
                                               class="col">
                    &lt;上一页</a></label>
            </#if>
            <#if result.pagination.currentPage lt result.pagination.totalPage>
                <label class="page p_right"><a href="${context.contextPath}${result.pagination.postPage.url}.html">下一页&gt;</a></label>
            </#if>
            <#if result.pagination.currentPage == result.pagination.totalPage>
                <label class="page p_right col"><a href="javascript:void(0);" class="col">下一页&gt;</a></label>
            </#if>
            </div>
        </div>
        <!-- 商品展示 -->
        <div class="listing">
            <ul class="itemList clearfix">
            <#list result.successList as pro>
                <li>
                    <dl>
                        <dt class="p-img">
                            <#assign pic = pro.colorMasterPicture/>
                            <#if pro.colorMasterPictureOfPix['220x220']??>
                                <#assign pic = pro.colorMasterPictureOfPix['220x220']/>
                            </#if>
                            <#assign imgLocation=imageLocationTemplate?replace('{host}', imageHosts[random.nextInt(imageHosts?size)])>
                            <a href="${itemUrlPrefix}${pro.spuId}${itemUrlPostfix}" target="_blank">
                                <img id="spu-big-pic-${pro.spuId}" class="lazy-pic" data-src="${imgLocation}/${pic}" data-original="${imgLocation}/${pic}" src="${imgLocation}/${pic}">
                            </a>
                        </dt>
                    <#--
                    <#if pro.activeName?? && pro.activeName?size gt 0>
                        <dd class="p-promotion">${pro.activeName[0]}</dd>
                    </#if>
                    -->
                        <dd class="p-name"><a href="${itemUrlPrefix}${pro.spuId}${itemUrlPostfix}"
                                              target="_blank"><#if !pro.longDesc?? || pro.longDesc.trim().isEmpty()>${pro.title} ${pro.shortDesc!}<#else>${pro.longDesc} ${pro.title}</#if></a></dd>
                        <dd class="p-price">￥${pro.currentPrice?string("0.00")}</dd>
                    </dl>
                    <div class="posa showinfo">
                        <div class="posa icon_28 list_likes" data-sku-id="${pro.skuId}">关注</div>
                        <div class="smalldiv clearfix">
                            <div class="posr colorBox">
                                <div class="bd-data" style="display: none;">
                                    <ul>
                                        <#list pro.colorItems as colorItem>
                                            <#assign bigPic = colorItem.colorMasterPicture/>
                                            <#assign smallPic = colorItem.colorMasterPicture/>
                                            <#if colorItem.colorMasterPictureOfPix['220x220']??>
                                                <#assign bigPic = colorItem.colorMasterPictureOfPix['220x220']/>
                                            </#if>
                                            <#if colorItem.colorMasterPictureOfPix['60x60']??>
                                                <#assign smallPic = colorItem.colorMasterPictureOfPix['60x60']/>
                                            </#if>
                                            <li>
                                                <a href="${itemUrlPrefix}${colorItem.skuId}${itemUrlPostfix}"
                                                   target="_blank">
                                                    <img data-spu-id="${colorItem.spuId}"
                                                         data-sku-id="${colorItem.skuId}"
                                                         data-big-pic="<@randomLocation hosts=imageHosts template=imageLocationTemplate/>/${bigPic}"
                                                         src="<@randomLocation hosts=imageHosts template=imageLocationTemplate/>/${smallPic}"
                                                         onmouseover="switchSpuBigPic(this)"
                                                         onmouseout="resetSpuBigPic(this)">
                                                </a>
                                            </li>
                                        </#list>
                                    </ul>
                                </div>
                                <div class="bd">
                                </div>
                                <a class="posa prev" href="javascript:;"></a>
                                <a class="posa next" href="javascript:;"></a>
                            </div>
                        </div>
                        <a class="posa scanmore"
                           href="${context.contextPath}${pro.moreUrl}.html">查看更多${pro.brandName}<#if pro.leafCategories?? && pro.leafCategories?size gt 0>${pro.leafCategories[0].display}</#if></a>
                    </div>
                </li>
            </#list>
            </ul>
            <div class="pagin-box">
                <ul id="j_page">
                    <li class="pronum">共<span class="red">${result.pagination.totalResults}</span>个商品</li>
                <#if result.pagination.currentPage == 1>
                    <li id="j_prev" class="item prev prev-disabled col">&lt;上一页</li>
                </#if>
                <#if result.pagination.currentPage gt 1>
                    <li id="j_prev" class="item prev prev-disabled"><a
                            href="${context.contextPath}${result.pagination.prePage.url}.html">
                        &lt;上一页</a></li>
                </#if>
                <#if result.pagination.currentPage gt 4>
                    <li class="item"><span class="num"><a
                            href="${context.contextPath}${result.pagination.firstPage.url}.html">1</a></span>
                    </li>
                    <li class="item dot">...</li>
                </#if>
                <#list result.pagination.pages as page>
                    <#if result.pagination.currentPage == page.pageNum >
                        <li class="item active">${page.pageNum}</li>
                    </#if>
                    <#if result.pagination.currentPage != page.pageNum >
                        <li class="item"><span class="num"><a
                                href="${context.contextPath}${page.url}.html">${page.pageNum}</a></span></li>
                    </#if>
                </#list>
                <#if result.pagination.currentPage lt result.pagination.totalPage - 3>
                    <li class="item dot">...</li>
                    <li class="item"><span class="num"><a
                            href="${context.contextPath}${result.pagination.lastPage.url}.html">${result.pagination.lastPage.pageNum}</a></span>
                    </li>
                </#if>
                <#if result.pagination.currentPage lt result.pagination.totalPage>
                    <li id="next" class="item next next-disabled"><a
                            href="${context.contextPath}${result.pagination.postPage.url}.html">下一页&gt;</a>
                    </li>
                </#if>
                <#if result.pagination.currentPage == result.pagination.totalPage>
                    <li id="next" class="item next next-disabled col">下一页&gt;</li>
                </#if>
                </ul>
            </div>
        </div>
    <#include "history_like.ftl">
    </div>
<#include "aside.ftl">
<#include "mini_login.ftl">
</div>
${footerContent}
<#--<#include "no-pic-script.ftl">-->
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
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/search.js"></script>
<script type="text/javascript">
    var searchLocation = '${searchLocation}';
    var picLocation = '<@randomLocation hosts=imageHosts template=imageLocationTemplate/>';
    var memberHome = '${memberHomeUrl}';
    var logoutPageUrl = '${logoutPageUrl}';
    var loginPageUrl = '${loginPageUrl}';
    var registerPageUrl = '${registerPageUrl}';
    var itemUrlPrefix = '${itemUrlPrefix}';
    var itemUrlPostfix = '${itemUrlPostfix}';
    $(function () {
        $('#keyWordMesgIcon').click(function () {
            var val = $('#keyWordMesg').val();
            if (val) {
                var _win = window;
                while (_win != _win.parent) {
                    _win = _win.parent;
                }
                var path = '';
            <#if result.params.selectedCategories?size gt 0>
                <#list result.params.selectedCategories as cat>
                    path += '${cat.id}' + '-';
                </#list>
                var i = 0;
                for (i =${result.params.selectedCategories?size}; i < 3; i++) {
                    path += '0-';
                }
                path += '0-0-0-0-0-0-1_1-1-0';
                path = '/' + path;
            </#if>
                _win.open(searchLocation = "/search/" + encodeURIComponent(val) + path + ".html", "_self");
            }
        });
        $('#key').val('${result.params.inputQuery}');
    });
</script>
<#--noinspection HtmlUnknownTarget-->
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/search_form.js"></script>
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/search_hot_word.js"></script>
<#--noinspection HtmlUnknownTarget-->
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/logon_info.js"></script>
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/cart_num.js"></script>
<#--noinspection HtmlUnknownTarget-->
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/concern.js"></script>
<#--noinspection HtmlUnknownTarget-->
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/may_like.js"></script>
<script src="<@randomLocation hosts=wwwJsHosts template=wwwJsLocationTemplate/>/script/js/service/cookieopt.js"></script>
<#--noinspection HtmlUnknownTarget-->
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/history_list.js"></script>
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/search_list_pic_switch.js"></script>
<#--noinspection HtmlUnknownTarget-->
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/plugin/jquery.lazyload.min.js"></script>
</body>
</html>