<#-- @ftlvariable name="cartPageUrl" type="java.lang.String" -->
<#-- @ftlvariable name="context" type="org.springframework.web.servlet.support.RequestContext" -->
<#-- DDD 删除此mock ftl -->
<div class="wrap " id="header_section">
    <div class="bd clearfix">
        <div class="fl search_box" id="search" role="search">
            <div class="posr search_wrap">
                <div class="posr brds search_from">
                    <form method="get" action="" id="j_searchForm"
                          name="CatalogSearchForm">
                        <ul class="shelper dn" id="shelper">
                            <!--<li id="d_1" onclick="$o.clickItem(this)" title="手机壳6">
                                <div class="search-item">
                                    手机壳
                                    <strong>6</strong>
                                </div>
                                <div class="search-count">搜索历史</div>
                            </li>
                            <li id="d_2" onclick="$o.clickItem(this)" title="手机壳6">
                                <div class="search-item">
                                    手机壳
                                    <strong>6</strong>
                                </div>
                                <div class="search-count">约715485个商品</div>
                            </li>
                            <li id="d_3" onclick="$o.clickItem(this)" title="手机壳6">
                                <div class="search-item">
                                    手机壳
                                    <strong>6</strong>
                                </div>
                                <div class="search-count">约715485个商品</div>
                            </li>
                            <li onclick="$o.hideTip()" class="close">关闭</li>-->
                        </ul>
                        <div class="search_inputBox">
                            <input type="text" id="key" placeholder="请输入商品名称" name="q"
                                   autofocus="true"
                            <#--noinspection HtmlUnknownAttribute-->
                                   hidefocus="true" class="ele_text" autocomplete="off">
                            <input type="hidden" value="3" name="pattern">
                        </div>
                        <button type="submit" class="posa ui_btn btn_search" id="j_searchBtn"><i class="icon_7"></i>搜索
                        </button>
                    </form>

                </div>
                <div id="search_words">

                    <div class="search_hot clearfix">
                        <span class="fl">热门搜索：</span>

                        <a data-gaevent="{&quot;cats&quot;:&quot;index&quot;,&quot;word&quot;:&quot;SWAROVSKI&quot;}"
                           target="_blank" title="SWAROVSKI" href="http://brand.wangfujing.com/swarovski/?ref=m-1-9_3_4"
                           class="fl search_word ">SWAROVSKI</a>

                        <a data-gaevent="{&quot;cats&quot;:&quot;index&quot;,&quot;word&quot;:&quot;Salvatore Ferragamo&quot;}"
                           target="_blank" title="Salvatore Ferragamo"
                           href="http://brand.wangfujing.com/salvatore-ferragamo/13224-0-0-0-0-1?ref=m-1-9_1_4"
                           class="fl search_word ">Salvatore Ferragamo</a>

                        <a data-gaevent="{&quot;cats&quot;:&quot;index&quot;,&quot;word&quot;:&quot;家纺&quot;}"
                           target="_blank" title="家纺"
                           href="http://list.wangfujing.com/13227_13267_0-0-0-0-0-0-1?ref=m-2_13227-1"
                           class="fl search_word ">家纺</a>

                        <a data-gaevent="{&quot;cats&quot;:&quot;index&quot;,&quot;word&quot;:&quot;COACH女表&quot;}"
                           target="_blank" title="COACH女表"
                           href="http://brand.wangfujing.com/coach/13255-0-0-0-0-1?ref=m-1-9_3_4"
                           class="fl search_word ">COACH女表</a>

                        <a data-gaevent="{&quot;cats&quot;:&quot;index&quot;,&quot;word&quot;:&quot;MICHAEL KORS&quot;}"
                           target="_blank" title="MICHAEL KORS"
                           href="http://brand.wangfujing.com/michael-kors/0-0-0-0-7-1?ref=a-1-9_1_2"
                           class="fl search_word ">MICHAEL KORS</a>

                        <a data-gaevent="{&quot;cats&quot;:&quot;index&quot;,&quot;word&quot;:&quot;LANCOME&quot;}"
                           target="_blank" title="LANCOME" href="http://brand.wangfujing.com/lancome/?ref=m-1-9_2_4"
                           class="fl search_word ">LANCOME</a>

                    </div>
                </div>
            </div>
        </div>
        <!-- 搜索 -->
        <div id="logo_box" class="fl">
            <div class="fl icon_0" id="logo"><a target="" href="${wwwLocation}">王府井网上商城（www.wangfujing.com）logo图片</a>
            </div>
            <div class="fl" id="slogo">
                <#--<a target="_blank" href="http://www.wangfujing.com/activity/20150120/cares.shtml?ref=a-0-2"><img-->
                        <#--alt="更多豪礼大放送" src="${context.contextPath}/images/slogo_03.jpg"></a>-->
            </div>
        </div>
        <div id="header_r" class="fl ">
            <div class=" header_min_cart posr" id="header_cart">
                <a id="shopcounthead" href="${cartPageUrl}"
                   class="brds ui_btn cart_link" target="_blank"><span id="j_headerCartNum"
                                                                       class="posa icon_6 header_cartNum"> <b
                        id="cartnum" class="posa red">0</b></span>去购物车结算</a>
                <!--mini 购物车-->
                <div class="header_cartList posa dn "></div>
            </div>
        </div>
    </div>
</div>