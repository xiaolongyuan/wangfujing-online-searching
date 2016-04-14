<#-- @ftlvariable name="imageLocationTemplate" type="java.lang.String" -->
<#-- @ftlvariable name="imageHosts" type="java.util.List<String>" -->
<#-- @ftlvariable name="searchLocation" type="java.lang.String" -->
<#-- @ftlvariable name="itemUrlPostfix" type="java.lang.String" -->
<#-- @ftlvariable name="itemUrlPrefix" type="java.lang.String" -->
<#-- @ftlvariable name="memberHomeUrl" type="java.lang.String" -->
<#-- @ftlvariable name="registerPageUrl" type="java.lang.String" -->
<#-- @ftlvariable name="loginPageUrl" type="java.lang.String" -->
<#-- @ftlvariable name="logoutPageUrl" type="java.lang.String" -->
<#-- @ftlvariable name="context" type="org.springframework.web.servlet.support.RequestContext" -->
<#-- @ftlvariable name="tbarContent" type="java.lang.String" -->
<#-- @ftlvariable name="cssHosts" type="java.util.List<String>" -->
<#-- @ftlvariable name="cssLocationTemplate" type="String" -->
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

    <title>未找到${result.params.inputQuery}相关商品</title>
</head>
<body>
${tbarContent}
${headerContent}
${navigationContent}
<div id="j_search" class="search-main">
    <div class="wrap">
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
        </div>
        <!-- 无结果 -->
        <div id="no_results">
            <h1 class="result_msg"><i class="msg1"></i>抱歉!没有找到与"<span
                    class="red">${result.params.inputQuery}</span>"相关的商品。
            </h1>
            <div class="result_text">
                <p><strong>您可以尝试用以下方式再找一次：</strong></p>
                <p>缩短或更改搜索关键字（例：将“兰蔻再生青春眼霜”改成“兰蔻眼霜”后重新搜索）</p>
            </div>
            <div id="re_form">
                <div class="posr brds search_from">
                    <form id="CatalogSearchForm2" name="CatalogSearchForm">
                        <input type="hidden" name="pattern" value="3">

                        <div class="search_inputBox"><input type="text" value="${result.params.inputQuery}"
                                                            id="CatalogSearchForm2_SearchTerm"
                                                            placeholder="请输入商品名称" class="ele_text" maxlength="50"></div>
                        <button type="submit" class="posa ui_btn btn_search" id="j_reBtn"><i class="posa icon_7"></i>搜索
                        </button>
                    </form>
                </div>
            </div>
        </div>
    <#include "history_like.ftl">
    </div>
<#include "aside.ftl">
</div>
${footerContent}
<#include "no-pic-script.ftl">
<#--noinspection HtmlUnknownTarget-->
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/plugin/jquery-1.9.1.min.js"></script>
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/plugin/jquery.slide.js"></script>
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
        $('#CatalogSearchForm2').submit(function (e) {
            e.preventDefault();
            var val = $('#CatalogSearchForm2_SearchTerm').val();
            if (val == "") {
                return;
            }
            var _win = window;
            while (_win != _win.parent) {
                _win = _win.parent;
            }
            _win.open(searchLocation = "/search/" + encodeURIComponent(val) + ".html", "_blank");
        });
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
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/no_result_may_like.js"></script>
<script src="<@randomLocation hosts=wwwJsHosts template=wwwJsLocationTemplate/>/script/js/service/cookieopt.js"></script>
<#--noinspection HtmlUnknownTarget-->
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/history_list.js"></script>

</body>
</html>