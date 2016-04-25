<#-- @ftlvariable name="searchLocation" type="java.lang.String" -->
<#-- @ftlvariable name="memberHomeUrl" type="java.lang.String" -->
<#-- @ftlvariable name="logoutPageUrl" type="java.lang.String" -->
<#-- @ftlvariable name="loginPageUrl" type="java.lang.String" -->
<#-- @ftlvariable name="registerPageUrl" type="java.lang.String" -->
<#-- @ftlvariable name="itemUrlPrefix" type="java.lang.String" -->
<#-- @ftlvariable name="itemUrlPostfix" type="java.lang.String" -->
<#-- @ftlvariable name="cssHosts" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="cssLocationTemplate" type="java.lang.String" -->
<#-- @ftlvariable name="tbarContent" type="java.lang.String" -->
<#-- @ftlvariable name="headerContent" type="java.lang.String" -->
<#-- @ftlvariable name="navigationContent" type="java.lang.String" -->
<#-- @ftlvariable name="wwwLocation" type="java.lang.String" -->
<#-- @ftlvariable name="categories" type="java.util.List<com.wfj.search.online.web.common.pojo.CategoryDisplayPojo>" -->
<#-- @ftlvariable name="brands" type="java.util.LinkedHashMap<java.util.String, java.util.List<com.wfj.search.online.web.common.pojo.BrandDisplayPojo>>" -->
<#-- @ftlvariable name="imageHosts" type="java.util.List<String>" -->
<#-- @ftlvariable name="imageLocationTemplate" type="java.lang.String" -->
<#-- @ftlvariable name="brandLocation" type="java.lang.String" -->
<#-- @ftlvariable name="footerContent" type="java.lang.String" -->
<#-- @ftlvariable name="jsHosts" type="java.util.List<String>" -->
<#-- @ftlvariable name="jsLocationTemplate" type="java.lang.String" -->
<#include "../macro/randomLocation.ftl">
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="ie6" lang="zh-CN"> <![endif]-->
<!--[if IE 7]>         <html class="ie7" lang="zh-CN"> <![endif]-->
<!--[if IE 8]>         <html class="ie8" lang="zh-CN"> <![endif]-->
<!--[if gt IE 8]><!-->
<html lang="zh-CN"> <!--<![endif]-->

<head>
    <meta charset="UTF-8">
    <meta property="qc:admins" content="2410213673671676521676375" />
    <meta name="keywords" content="王府井,王府井网上商城,王府井百货"/>
    <meta name="description" content="王府井网上商城依托王府井百货品牌优势，是集化妆品、服装、鞋帽、箱包、珠宝、配饰、户外、运动、家居生活等产品，定位于面向品位、品质、时尚和追求购物乐趣消费者的B2C精品购物平台。"/>
    <#--noinspection HtmlUnknownTarget-->
    <link href="<@randomLocation hosts=cssHosts template=cssLocationTemplate/>/base.css" rel="stylesheet">
    <link href="<@randomLocation hosts=cssHosts template=cssLocationTemplate/>/brandList.css" rel="stylesheet">
    <title>品牌馆</title>
</head>
<body>
${tbarContent}
${headerContent}
${navigationContent}
<div class="clearfix" id="brand">
    <div class="wrap">
        <div class="crumbs clearfix">
            <strong>您现在的位置：</strong>
            <a href="${wwwLocation}" class="link"><strong>首页</strong></a>&gt;
            <span>品牌馆</span>
        </div>
        <div class="posr channel-container clearfix">
            <div class="channel-sub">
                <div class="related-channel">
                    <div class="hd col-name">
                        <h2><a href="#" onclick="filterCat(this)" data-cid="">全部品牌</a></h2>
                    </div>
                    <ul class="bd cats-list">
                        <#list categories as category>
                            <li><a href="#" onclick="filterCat(this)" data-cid="${category.id}">${category.display}</a></li>
                        </#list>
                    </ul>
                </div>
            </div>
            <div class="channel-main">
                <div class="letterFilter">
                    <div class="fixed-box-wrap">
                        <dl class="letters clearfix">
                            <#list brands?keys as group>
                                <dd class="fl item<#if group_index == 0> current</#if>"><a href="#${group}" class="letter">${group}</a></dd>
                            </#list>
                        </dl>
                    </div>
                </div>
                <div class="brand-list-wrap">
                    <#list brands?keys as group>
                        <div class="brand-section brand-list<#if brands[group]?size == 0> dn</#if>"><a name="${group}"> </a>
                            <div class="hd"><h2 class="posr ">相关品牌<span class="red">${brands[group]?size}</span>个<span class="posa letter">${group}</span></h2></div>
                            <div class="bd">
                                <ul class="img-list clearfix">
                                    <#list brands[group] as brand>
                                        <li data-catinfo="{}" data-catid="<#list brand.availableCategoryIds as cid>${cid},</#list>" class="fl item">
                                            <div class="posr pic-logo clearfix">
                                                <a class="posr fl" href="${brandLocation}${brand.url}.html" target="_blank">
                                                    <img alt="${brand.display}logo图片" src="<@randomLocation hosts=imageHosts template=imageLocationTemplate/>/brand/${brand.logo!}">
                                                </a>
                                                <div class="posa line"></div>
                                            </div>
                                            <p class="name"><a href="${brandLocation}${brand.url}.html" target="_blank">${brand.display}</a></p>
                                        </li>
                                    </#list>
                                </ul>
                            </div>
                        </div>
                    </#list>
                </div>
            </div>
        </div>
    </div>
</div>
${footerContent}
<#include "no-pic-script.ftl">
<#--noinspection HtmlUnknownTarget-->
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/plugin/jquery-1.9.1.min.js"></script>
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/brandList.js"></script>
<#--noinspection HtmlUnknownTarget-->
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/brandIndex.js"></script>
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
</body>
</html>