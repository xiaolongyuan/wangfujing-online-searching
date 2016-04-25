<#-- @ftlvariable name="memberLocation" type="java.lang.String" -->
<#-- @ftlvariable name="wwwLocation" type="java.lang.String" -->
<div class="" id="toolbar">
    <div class="wrap clearfix">
        <ul class="fl toolbar_fl">
            <li class="item first">
                <a id="j_add_favorite" href="javascript:"><i class="posa icon_5"></i>收藏王府井</a>
            </li>
            <li class="item">
                <a id="guanzhu" target="_blank" href="http://weibo.com/wangfujingcom"><i class="posa icon_1"></i></a>
            </li>
            <li id="j_weixin" class="item last ">
                <a href="javascript:" class=""><i class="posa icon_2"></i></a>

                <div class="posa pic_weixinB dn">
                    <img alt="王府井微信帐号" src="http://img.wfjimg.com/wfj_ec2/images/pic_weixnB.png">
                    <i class="arrow arrowT posa"></i>
                </div>
            </li>
        </ul>
        <ul class="fr toolbar_fr">
            <li class="item first">
                <span id="loginName">Hi,欢迎来到王府井!</span>
            </li>
            <li class="item link_top_login" id="btnLogin">
            <#--noinspection HtmlUnknownTarget-->
                <a href="${wwwLocation?replace("http://", "https://")}/webapp/wcs/stores/servlet/LogonForm?catalogId=10101&amp;myAcctMain=1&amp;langId=-7&amp;storeId=10154"
                   class="" id="btnLogon"><i class="posa icon_3"></i>请登录</a>
                <a href="${wwwLocation}/webapp/wcs/stores/servlet/Logoff?catalogId=10101&amp;myAcctMain=1&amp;langId=-7&amp;storeId=10154&amp;URL=LogonForm"
                   class="" id="btnLogout"><i class="posa icon_3"></i>退出</a>
            </li>
            <li class="item link_top_reg" id="btnReg">
            <#--noinspection HtmlUnknownTarget-->
                <a href="${wwwLocation}/webapp/wcs/stores/servlet/WFJURLUserRegistration?catalogId=10101&amp;storeId=10154&amp;langId=-7"
                   class=""><i class="posa icon_4"></i>免费注册</a>
            </li>
            <li class="line">|</li>
            <li id="myorder" class="item">
                <#--noinspection HtmlUnknownTarget-->
                <a href="${memberLocation}/order/myOrder/1.html?status=1">我的订单</a>
            </li>
            <li class="line">|</li>
            <li id="" class="j_drop item my_wfj">
            <#--noinspection HtmlUnknownTarget-->
                <a id="mywfj"
                   href="${memberLocation}/home">我的王府井<i
                        class="arrow arrowB posa"></i></a>
                <span class="posa outer"></span>
                <span class="posa bk_line"></span>

                <div class="posa drop_list">
                    <div class="drop_panel">
                        <h2 class="posr padding_rl">
                            <a id="btnLogon2"
                               <#--noinspection HtmlUnknownTarget-->
                               href="${memberLocation}/ssoLogin2.do">请登录
                                <i class="arrow arrowR posa"></i></a>
                            <span id="loginName2">Hi,欢迎来到王府井!</span>
                        </h2>
                        <ul class="padding_rl menu_list clearfix">
                            <li id="likegoods" class="item"><a
                                    href="${memberLocation}/memberCollect/get/1?type=0">我喜欢的商品&gt;</a>
                            </li>
                            <li id="orderstatus" class="item">
                                <#--noinspection HtmlUnknownTarget-->
                                <a href="${memberLocation}/order/getNoPayOrder/1?status=6">待处理订单&gt;</a>
                            </li>
                            <li id="likePinPai" class="item">
                                <#--noinspection HtmlUnknownTarget-->
                                <a href="${memberLocation}/memberCollect/get/1?type=1">我喜欢的品牌&gt;</a>
                            </li>
                            <li id="myOrder1" class="item">
                            <#--noinspection HtmlUnknownTarget-->
                                <a href="${memberLocation}/order/myOrder/1.html?status=1">我的订单&gt;</a>
                            </li>
                            <li id="loyalty" class="item">
                                <a href="${memberLocation}/memberScore/getScore/1.html?status=1">我的积分&gt;</a>
                            </li>
                            <li id="youhuijuan" class="item">
                            <#--noinspection HtmlUnknownTarget-->
                                <a href="${memberLocation}/asset/getCoupon/1">我的优惠券&gt;</a>
                            </li>
                        </ul>
                        <ul id="loginName3" class="padding_rl recently_view clearfix" style="display: none;">
                            <#--noinspection HtmlUnknownTag-->
                            <div class="hd">最近浏览的商品</div>
                            <li class="item">
                                <a target="_blank" title="MG 美即 海洋冰泉补水面膜  25G"
                                   <#--noinspection HtmlUnknownTarget-->
                                   href="${wwwLocation}/item/000000000010011755"
                                   class="hot-product-ref"><img
                                        src="http://img2.wfjimg.com/catalog/10011755/10011755_01_45x45.jpg"
                                        title="MG 美即 海洋冰泉补水面膜  25G"></a>
                            </li>
                            <li class="item">
                            <#--noinspection HtmlUnknownTarget-->
                                <a target="_blank" title="PORTS 时尚撞色收腰长袖连衣裙 灰色 2"
                                   href="${wwwLocation}/item/000000030044022001"
                                   class="hot-product-ref"><img
                                        src="http://img2.wfjimg.com/catalog/30044022001/30044022001_01_mt_45x45.jpg"
                                        title="PORTS 时尚撞色收腰长袖连衣裙 灰色 2"></a>
                            </li>
                            <li class="item">
                                <a target="_blank" title="KIEHL'S 科颜氏 集焕白净润透白保湿霜  50ML"
                                <#--noinspection HtmlUnknownTarget-->
                                   href="${wwwLocation}/item/000000000010015267"
                                   class="hot-product-ref"><img
                                        src="http://img2.wfjimg.com/catalog/10015267/10015267_01_45x45.jpg"
                                        title="KIEHL'S 科颜氏 集焕白净润透白保湿霜  50ML"></a>
                            </li>
                        </ul>
                    </div>
                </div>
            </li>
            <li class="line">|</li>
            <li class="j_drop item service_center">
                <a id="fuwucenter" target="_blank" href="${wwwLocation}/help/helpcenter/index.shtml">服务中心<i
                        class="arrow arrowB posa"></i></a>
                <span class="posa outer"></span>
                <span class="posa bk_line"></span>

                <div class="posa drop_list">
                    <div class="drop_panel">
                        <ul class="padding_rl menu_list clearfix">
                            <li id="helpcenter1" class="item">
                                <a target="_blank"
                                <#--noinspection HtmlUnknownTarget-->
                                   href="${wwwLocation}/help/helpcenter/index.shtml">帮助中心</a>
                            </li>
                            <li id="helpcenter2" class="item">
                                <a target="_blank"
                                   href="${wwwLocation}/help/helpcenter/index.shtml">购物指南</a>
                            </li>
                            <li id="helpcenter3" class="item">
                                <a target="_blank"
                                   <#--noinspection HtmlUnknownTarget-->
                                   href="${wwwLocation}/help/helpcenter/index.shtml">配送中心</a>
                            </li>
                            <li id="helpcenter4" class="item">
                                <a target="_blank"
                                   href="${wwwLocation}/help/helpcenter/index.shtml">如何付款</a>
                            </li>
                            <li id="helpcenter5" class="item">
                                <a target="_blank"
                                   <#--noinspection HtmlUnknownTarget-->
                                   href="${wwwLocation}/help/helpcenter/index.shtml">售后服务</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </li>
        </ul>
    </div>
</div>