<#-- @ftlvariable name="errorCode" type="java.lang.String" -->
<#-- @ftlvariable name="imageLocationTemplate" type="java.lang.String" -->
<#-- @ftlvariable name="imageHosts" type="java.util.List<String>" -->
<#-- @ftlvariable name="searchLocation" type="java.lang.String" -->
<#-- @ftlvariable name="itemUrlPostfix" type="java.lang.String" -->
<#-- @ftlvariable name="itemUrlPrefix" type="java.lang.String" -->
<#-- @ftlvariable name="registerPageUrl" type="java.lang.String" -->
<#-- @ftlvariable name="loginPageUrl" type="java.lang.String" -->
<#-- @ftlvariable name="logoutPageUrl" type="java.lang.String" -->
<#-- @ftlvariable name="memberHomeUrl" type="java.lang.String" -->
<#-- @ftlvariable name="tbarContent" type="java.lang.String" -->
<#-- @ftlvariable name="headerContent" type="java.lang.String" -->
<#-- @ftlvariable name="navigationContent" type="java.lang.String" -->
<#-- @ftlvariable name="jsHosts" type="java.util.List<String>" -->
<#-- @ftlvariable name="jsLocationTemplate" type="java.lang.String" -->
<#-- @ftlvariable name="cssLocationTemplate" type="java.lang.String" -->
<#-- @ftlvariable name="cssHosts" type="java.util.List<String>" -->
<#-- @ftlvariable name="wwwLocation" type="java.lang.String" -->
<#include "../macro/randomLocation.ftl">
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="keywords" content="王府井,王府井网上商城,王府井百货"/>
    <meta name="description"
          content="王府井网上商城依托王府井百货品牌优势，是集化妆品、服装、鞋帽、箱包、珠宝、配饰、户外、运动、家居生活等产品，定位于面向品位、品质、时尚和追求购物乐趣消费者的B2C精品购物平台。"/>
    <#--noinspection HtmlUnknownTarget-->
    <link href="<@randomLocation hosts=cssHosts template=cssLocationTemplate/>/base.css" rel="stylesheet">
    <link href="<@randomLocation hosts=cssHosts template=cssLocationTemplate/>/error.css" rel="stylesheet">
    <title>500报错</title>
</head>
<body>
${tbarContent}
<div class="shop-head">
    <div class="wrap">
        <div class="bd clearfix">
            <div class="fl" id="logo_box">
                <div id="logo" class="fl icon_0"><a href="${wwwLocation}" target="">王府井网上商城（www.wangfujing.com）logo图片</a>
                </div>
            </div>
            <div class="fr shopInfo">
                <span><a class="">帮助中心</a></span>
                <span class="posr red shoptel"><i class="posa icon-shop_01"></i>400-890-6600</span>
            </div>
        </div>
    </div>
</div>
<div class="errordiv">
    <div class="wrap">
        <#--noinspection HtmlUnknownTarget-->
        <div class="imgs"><img src="<@randomLocation hosts=cssHosts template=cssLocationTemplate/>/img/500.png" class="er-img"></div>
        <div class="tc er-text">
            <div class="er-e">
                <span class="gray">ERROR</span>
                <span class="red">啊哦～！</span>
            </div>
            <div class="er-c">服务器内部错误，不能执行该请求～！<#if errorCode??>ErrorCode:${errorCode}</#if></div>
            <a href="${wwwLocation}" class="er-btn">返回首页&gt;</a>
        </div>
    </div>
</div>
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
</script>
<#--noinspection HtmlUnknownTarget-->
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/search_form.js"></script>
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/search_hot_word.js"></script>
<#--noinspection HtmlUnknownTarget-->
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/logon_info.js"></script>
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/cart_num.js"></script>
<#--noinspection HtmlUnknownTarget-->
<script src="<@randomLocation hosts=jsHosts template=jsLocationTemplate/>/service/concern.js"></script>
</body>
</html>