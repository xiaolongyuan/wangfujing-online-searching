<#-- @ftlvariable name="cartPageUrl" type="java.lang.String" -->
<#-- @ftlvariable name="concernItemPageUrl" type="java.lang.String" -->
<div class="aside" id="j_aside">
    <div class="channel_nav fixed">
        <ul id="j_side_bar" class="nav_list">
            <li class="cart">
                <a href="${cartPageUrl}" target="_blank">
                    <i class="posa icon_6"></i>
                    <b class="posa num">0</b>
                </a>
            </li>
            <li class="posr rlikes">
                <a href="${concernItemPageUrl}" target="_blank">
                    <i class="posa icon_28" id="js_like" style="display: block;"></i>
                </a>
                <div class="posa addshow">
                    已成功加入收藏
                </div>
            </li>
            <li class="totop">
            <#--noinspection HtmlUnknownAnchorTarget-->
                <a href="#toolbar"><i class="posa icon_33"></i></a>
            </li>
        </ul>
    </div>
</div>