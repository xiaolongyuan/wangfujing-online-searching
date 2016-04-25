<#-- @ftlvariable name="list" type="java.util.List<com.wfj.search.online.web.common.pojo.SpuDisplayPojo>" -->
<#-- @ftlvariable name="itemUrlPostfix" type="java.lang.String" -->
<#-- @ftlvariable name="itemUrlPrefix" type="java.lang.String" -->
<#-- @ftlvariable name="imageHosts" type="java.util.List<String>" -->
<#-- @ftlvariable name="imageLocationTemplate" type="java.lang.String" -->
<#include "../macro/randomLocation.ftl">
<#if (list?size > 0)>
<!-- maybe like -->
<div class="posr bottomSlide" id="j_like">
    <div class="hd">
        <h2 class="hd_tit">猜你喜欢</h2>
    </div>
    <div class="bd ">
        <ul id="history_maybe_like" class="like_list clearfix">
            <#list list as pro>
                <li>
                    <a target="_blank" href="${itemUrlPrefix}${pro.spuId}${itemUrlPostfix}" class="imgs">
                        <#assign pic = pro.colorMasterPicture/>
                        <#if pro.colorMasterPictureOfPix['220x220']??>
                            <#assign pic = pro.colorMasterPictureOfPix['220x220']/>
                        </#if>
                        <img alt="" class="item_pic"
                             src="<@randomLocation hosts=imageHosts template=imageLocationTemplate/>/${pic}"
                             style="width: 220px;height: 220px;">
                    </a>
                    <div class="item_txt">
                        <a target="_blank" href="${itemUrlPrefix}${pro.spuId}${itemUrlPostfix}" class="name"><#if pro.longDesc?? || pro.longDesc.trim().isEmpty()>${pro.title} ${pro.shortDesc!}<#else>${pro.longDesc} ${pro.title}</#if></a>
                        <div class="item_price">
                        <#--<del>￥990</del>-->
                            <strong>￥${pro.currentPrice?string("0.00")}</strong>
                        <#--<strong>【2.0折】</strong>-->
                        </div>
                    </div>
                </li>
            </#list>
        </ul>
    </div>
    <a href="javascript:" class="posa icon_30 prev"></a>
    <a href="javascript:" class="posa icon_29 next"></a>
</div>
</#if>