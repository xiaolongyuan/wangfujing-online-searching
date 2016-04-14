$(function(){
    $(".wxbuy").on("mousemove",function(){
        $(this).addClass("drop_hover");
    }).on("mouseleave",function(){
        $(this).removeClass("drop_hover");
    });

    $("#j_nav_warp li").each(function(k,v){
        $(this).click(function(){
            if($(this).hasClass("lastli")){
                return false;
            }
            var i=$(this).index()+1;
            $(this).parents("#js_tabs").find(".amDiv").hide();
            $(this).addClass("icon_detail12").siblings().removeClass("icon_detail12").parents("#js_tabs").find("#content_top"+i).show();
        });
    });
    //商品信息导航固定
    var tem = false;
    $(window).scroll(function () {
        var navObj = $("#j_nav_warp"),
            nav = navObj.offset().top * 1,
            scrolla = $(this).scrollTop() * 1;
        temp = tem == false ? (tem = nav) : tem;
        if (temp - (scrolla) <= 0) {
            navObj.addClass("fixed-t").find(".lastli").addClass("ui_btn");
        } else {
            navObj.removeClass("fixed-t").find(".lastli").removeClass("ui_btn");
        }
    });

//转到顶部
    $('#j_nav_warp li').on('click', function () {
        var nav = $("#j_nav_warp").offset().top * 1, scrolla = $(this).scrollTop() * 1;
        temp = tem == false ? (tem = nav) : tem;
        if (temp - (scrolla) <= 0) {
            if ($(this).hasClass("lastsp")) {
                return;
            } else {
                $(window).scrollTop($("#js_tabs").offset().top * 1 - 1);
            }
        }
    });

    /**选择颜色--尺码*/
    $(".detail-sale-prop li").click(function(){
        $(this).addClass("selected").append('<i class="posa icon_detail3"></i>').siblings().removeClass("selected").find(".icon_detail3").remove(".icon_detail3");
    });

    //五角星的百分比显示
    var per = parseFloat($("#j_per").text()), n, m, num;
    n = per * 72 / 100;
    n = n.toFixed(2);
    num = per * 5 / 100;
    num = num.toFixed(1);
    if (per == 100) {
        m = (per * 640 / 100) - 30;
    } else {
        m = per * 640 / 100;
    }
    m = m.toFixed(2);
    $("#j_star").css("width", n + "px");
    $("#j_block").css("left", m + "px");
    $("#j_block").find("span").text(num);
    /*end 五角星的百分比显示*/

    /*加入购物车*/
    $("#js_detail_addCart,#j_nav_warp .lastli").click(function(){
        $("#js_Shopping_success").show();
    });
    $("#js_close").click(function(){
        $("#js_Shopping_success").hide();
    });


        //jqZoom
        var jqzoomOpt = {
            lens: true,
            preloadImages: false,
            alwaysOn: false,
            zoomWidth: 450,
            zoomHeight: 450,
            xOffset: 20,
            title: false,
            preloadText: "false"

        }, b = function () {
            $('.jqzoom').jqzoom(jqzoomOpt);
        }
        b();

})