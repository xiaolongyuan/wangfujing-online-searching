<#-- @ftlvariable name="wwwLocation" type="java.lang.String" -->
<div class="wrapper" id="ui-login-page" style="display: none;">
    <div id="dialog-box-2" class="block">
        <div class="block-header">
            <h2 class="block-title">
                用户登录
            </h2>
            <span class="block-close">X</span>
        </div>
        <div class="block-bodyer">
            <div class="block-bodyer-left">
                <div class="weight">
                    <div class="weight-bodyer">
                        <form id="LogonForm">
                            <input type="hidden" id="isMerge" value="false" name="orderMove">
                            <input type="hidden" id="checkCode" value="" name="checkCode">

                            <ul class="form-list">
                                <li class="position-r">
                                    <label for="logonId">用户名：</label>
                                    <input type="text" name="logonId" id="logonId">
                                    <div class="userdiv " id="emailOrPhone" style="display: none;">邮箱地址或手机号</div>
                                </li>
                                <li class="margin-t20">
                                    <label for="logonPassword">密码：</label>
                                    <input type="password" name="logonPassword" id="logonPassword">
                                    <#--noinspection HtmlUnknownTarget-->
                                    <a class="left c-gray-1" target="_blank" href="${wwwLocation}/webapp/wcs/stores/servlet/Logoff?URL=WFJURLFindPasswordIndex&amp;catalogId=10101&amp;storeId=10154&amp;langId=-7" id="forgetPassWord">忘记密码？</a><br>
                                    <p class="red p_error" id="logonErrorMsg" style="display: none">提示错误信息</p>
                                </li>
                                <li style="_margin-bottom:-10px; display : none;" id="checkCodeWapper" class="clearfix li-bmg">

                                </li>
                                <li class=" margin-b10 h25" hidden="hidden">
                                    <label for="noForgetPw">&nbsp;</label>

                                    <input type="checkbox" checked="checked" class="checks" name="noForgetPw" id="noForgetPw"><div class="left line-h26 font-grey-4">记住用户名</div>
                                </li>
                                <li class="">
                                    <label>&nbsp;</label>
                                    <input type="submit" value="登录" name="button" class="button width-78" id="l-submit">
                                </li>
                                <li class="">
                                    <label>&nbsp;</label>
                                    <p class="left margin-t10 margin-l5">
                                        使用合作网站账号登录：
                                    </p>

                                </li>
                                <li>
                                    <label>&nbsp;</label>
                                    <p id="partner_link" class="left">
                                        <a href="${wwwLocation}/webapp/wcs/stores/oauth?c=weibo&amp;t=https://www.wangfujing.com/webapp/wcs/stores/servlet/AjaxOrderItemDisplayView?langId=-7&amp;catalogId=10101&amp;storeId=10154" data-url="http://www.wangfujing.com" class="plink"> <b class="icon_sina"></b>新浪微博</a>
                                        <a href="${wwwLocation}/webapp/wcs/stores/oauth?c=alipay&amp;t=https://www.wangfujing.com/webapp/wcs/stores/servlet/AjaxOrderItemDisplayView?langId=-7&amp;catalogId=10101&amp;storeId=10154" class="plink"> <b class="icon_alipay"></b>支付宝</a>
                                        <a href="${wwwLocation}/webapp/wcs/stores/oauth?c=qq&amp;t=https://www.wangfujing.com/webapp/wcs/stores/servlet/AjaxOrderItemDisplayView?langId=-7&amp;catalogId=10101&amp;storeId=10154" class="plink"> <b class="icon_qq"></b>QQ</a>
                                        <a href="${wwwLocation}/webapp/wcs/stores/oauth?c=weixin&amp;t=https://www.wangfujing.com/webapp/wcs/stores/servlet/AjaxOrderItemDisplayView?langId=-7&amp;catalogId=10101&amp;storeId=10154" data-info="weixin" id="j_weixin" class="plink"><b class="icon_weixin"></b>微信</a>
                                    </p>
                                </li>
                            </ul>
                        </form>
                    </div>

                </div>
            </div>
            <div class="block-bodyer-right">
                <div class="p-tb37-lr19">
                    <p class="c-red margin-l10 tc">还没有注册账号?</p>
                    <p class="text-center"><a id="c-res" class="button p-lr20" href="#">10秒快速注册</a></p>
                </div>
            </div>
        </div>
    </div>
</div>