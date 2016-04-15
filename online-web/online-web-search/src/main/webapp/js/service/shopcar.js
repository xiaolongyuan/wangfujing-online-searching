$(function(){
    /*清空购物车**/
    $("#clearshopCar").click(function(){
        $("#js_alert").show();
    });
    $("#js_shopyes").click(function(){
        $("#js_shopResult").hide();
        $("#js_shopResultNo").show();
    });
    $("#js_shopno").click(function(){
        $("#js_alert").hide();
    });
    /**/
    $("#js_od_tool li a").click(function(){
        $(this).next().show().parent("li").siblings().find(".bubble-cvs").hide();
    });
    /*历史浏览切换--滚动*/
    $(".history_tab div").tab("current","history","hisbox");
    $('body').hasClass("narrow")?vis = 4:vis = 5;
    $("#js_browse,#js_follow").slide($.extend({},slide_opt,{
        vis:vis,
        scroll:vis,
        pnLoop:false,
        stepEffect:true
    }));

    /*订单增删*/
    $(".ordertool a").click(function(){
        var msg={like:"你确定要移入我的关注吗？",del:"确认要删除该商品吗？"};
        var i=$(this).data("tool"),x=$(this).position().left,y=$(this).position().top;
        $("#js_orderTool").find(".colgray").text(msg[i]).end().show().css({left:x+"px",top:(y-83)+"px"});
    });

    /*促销信息*/
    $(".cxinfo").click(function(){
        $(this).find(".infobox").toggle();
    });
    /*添加数量控制*/
    var num=$(".num").val();
    if(num>=3){
        $("#js_numbox").show();
    }
    $(".block-close").click(function(){
        $(this).parents("#js_numbox").hide();
    });

});