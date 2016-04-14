<#-- @ftlvariable name="itemUrlPostfix" type="java.lang.String" -->
<#-- @ftlvariable name="itemUrlPrefix" type="java.lang.String" -->
<#-- @ftlvariable name="registerPageUrl" type="java.lang.String" -->
<#-- @ftlvariable name="loginPageUrl" type="java.lang.String" -->
<#-- @ftlvariable name="logoutPageUrl" type="java.lang.String" -->
<#-- @ftlvariable name="memberHomeUrl" type="java.lang.String" -->
<#-- @ftlvariable name="imageLocationTemplate" type="java.lang.String" -->
<#-- @ftlvariable name="imageHosts" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="searchLocation" type="java.lang.String" -->
<#-- @ftlvariable name="brandLocation" type="java.lang.String" -->
<#-- @ftlvariable name="brands" type="java.util.LinkedHashMap<java.util.String, java.util.List<com.wfj.search.online.web.common.pojo.BrandDisplayPojo>>" -->
<#-- @ftlvariable name="listLocation" type="java.lang.String" -->
<#-- @ftlvariable name="categories" type="java.util.List<com.wfj.search.online.web.common.pojo.CategoryDisplayPojo>" -->
<#-- @ftlvariable name="tbarContent" type="java.lang.String" -->
<#-- @ftlvariable name="headerContent" type="java.lang.String" -->
<#-- @ftlvariable name="navigationContent" type="java.lang.String" -->
<#-- @ftlvariable name="footerContent" type="java.lang.String" -->
<#-- @ftlvariable name="jsHosts" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="jsLocationTemplate" type="java.lang.String" -->
<#-- @ftlvariable name="cssHosts" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="cssLocationTemplate" type="java.lang.String" -->
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
    <link href="<@randomLocation hosts=cssHosts template=cssLocationTemplate/>/lists.css" rel="stylesheet">
<#--noinspection HtmlUnknownTarget-->
    <link href="<@randomLocation hosts=cssHosts template=cssLocationTemplate/>/sort.css" rel="stylesheet">
    <title>全部商品分类</title>
</head>
<body>
${tbarContent}
${headerContent}
${navigationContent}
<!--商品分类中心内容-->
<div class="brandMain clearfix">
    <div class="wrap">
        <div class="mrgt15 fl">
            <div class="posr clearfix fl">
                <h2 class="all-title fs16 fw fl">全部商品分类 <i class="arrow arrowC"></i></h2>
            </div>
            <!-- begin all-list-->
            <div class="all-list fl">
            <#list categories as category>
                <div class="list-wrap">
                    <p class="list-title fs16 fw"><a
                            href="${listLocation}/${category.id}-0-0-0-0-0-0-0-0-1_1-1-0.html">${category.display}</a>
                    </p>

                    <div class="list-box">
                        <#list category.children as _2ndCat>
                            <div class="mrgt15 disL">
                                <span class="list-name fs14 fw"><a
                                        href="${listLocation}/${category.id}-${_2ndCat.id}-0-0-0-0-0-0-0-1_1-1-0.html">${_2ndCat.display}
                                    ></a></span>

                                <p class="list-cont fs12">
                                    <#list _2ndCat.children as leafCat>
                                        <a href="${listLocation}/${category.id}-${_2ndCat.id}-${leafCat.id}-0-0-0-0-0-0-1_1-1-0.html"
                                           target="_blank">${leafCat.display}</a>
                                    </#list>
                                </p>
                            </div>
                        </#list>
                    </div>
                </div>
            </#list>
            </div>
            <!--end all-list-->
        </div>
        <div class="mrgt15 fl">
            <div class="posr clearfix fl">
                <h2 class="all-title fs16 fw fl">全部品牌 <i class="arrow arrowC"></i></h2>
            </div>
            <!-- begin all-list-->
            <div class="all-brand clearfix posr">
            <#list brands?keys as group>
                <div class="brand-widget dn">
                    <h3 class="brand-title fw">${group}</h3>
                    <#list brands[group] as brand>
                        <a href="${brandLocation}/0-0-0-${brand.id}-0-0-0-0-0-1_1-1-0.html"
                           target="_blank">${brand.display}</a>
                    </#list>
                </div>
            </#list>
            </div>
            <!--end all-list-->
        </div>
    </div>
</div>
<!--end 商品分类中心内容-->
${footerContent}
<#include "no-pic-script.ftl">
<#--noinspection HtmlUnknownTarget-->
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/plugin/jquery-1.9.1.min.js"></script>
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/plugin/jquery.slide.js"></script>
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
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/lists.js"></script>
</body>
</html>
