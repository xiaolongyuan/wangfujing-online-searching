$(function(){
    /*home-banneer滚动**/
    $("#j_slide_banner").slide($.extend({},slide_opt,{
        autoPlay:true
    }));

    //服务
    $("#j_service>.item").hover(function(){
        var o=$(this);
        o.addClass("hover").find(".pic_service_off ").addClass("pic_service_on").end().find(".msg_box").show();
    },function(){
        var o=$(this);
        o.removeClass("hover").find(".pic_service_off ").removeClass("pic_service_on").end().find(".msg_box").hide();
    });
    //全局图片移动效果
    $(".img-wrap").hover(function(){
        $(this).find(".ac-pic").animate({right:0},"5")
    },function(){
        $(this).find(".ac-pic").animate({right:-20+"px"},"5")
    });
    /*楼层tab导航切换*/
    $(".floor-nav-list li").each(function(k,v){
        var index=$(this).index();
        $(this).click(function(){
            $(".floor-nav-list li").find(".icon_17").removeClass("icon_18");
            $(this).addClass("on").siblings().removeClass("on").end().find(".icon_17").addClass("icon_18");
            $(this).parents(".main_hd").next().find(".bd").hide();
            $(this).parents(".main_hd").next().find(".bd").eq(index).show();
        });
    });
    //页脚底部评论
    $("#j_slide_comment").slide($.extend({},slide_opt,{
        effect:"topLoop",
        autoPlay:true,
        vis:2
    }));
})