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
            <li class="rlikes">
                <a href="${concernItemPageUrl}" target="_blank">
                    <i id="js_like" class="posa icon_28"></i>
                </a>
            </li>
            <li class="totop">
            <#--noinspection HtmlUnknownAnchorTarget-->
                <a href="#toolbar"><i class="posa icon_33"></i></a>
            </li>
        </ul>
    </div>
</div>