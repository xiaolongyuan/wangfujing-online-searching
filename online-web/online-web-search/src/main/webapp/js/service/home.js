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



    function mini_cart_ajax(header_cart){
        var _o=header_cart.find(".header_cartList");
        $.ajax({
            dataType:"JSONP",
            url:"http://www.wangfujing.com/webapp/wcs/stores/servlet/minishopcartCrossdomain?callback=?",
            data:{
                ShoppingCartURL:'http://www.wangfujing.com/webapp/wcs/stores/servlet/OrderCalculate?calculationUsageId=-1&calculationUsageId=-2&calculationUsageId=-3&calculationUsageId=-4&calculationUsageId=-5&calculationUsageId=-7&updatePrices=1&catalogId=10101&errorViewName=AjaxOrderItemDisplayView&orderId=.&langId=-7&storeId=10154&URL=AjaxOrderItemDisplayView',
                LogonFormURL:'https://www.wangfujing.com/webapp/wcs/stores/servlet/LogonForm?catalogId=10101&myAcctMain=1&langId=-7&storeId=10154',
                topUrl:'http://www.wangfujing.com/emall/wfjstore',
                isHttp:'http://www.wangfujing.com/webapp/wcs/stores/servlet/'
            },
            success : function(res){
                var s=res&&res.callbackhtml?($("<div/>").html(res.callbackhtml).text()):"";
                _o.removeClass("load_html").data("isload","2").html(s);
                var car_count = $.trim(_o.find(".js_car_count").text());
                !car_count&&_o.addClass("dn");
                car_count&&$("#goods-num,#goods-top-num,#cartnum,#j_side_cart").html(car_count);
                car_count&&header_cart.attr("totalNum",car_count);
            },
            error:function(){
                _o.removeClass("load_html").addClass("dn").data("isload","0");
            }
        });
    };
    var cart_status={on:null,off:null,flag:!1};
    $("#header_cart").bind("mouseover mouseout",function(e){
        //鼠标触发
        var header_cart=$(this),
            _o=header_cart.find(".header_cartList"),
            isload=_o.data("isload")||"0",
            host=location.host||"";
        host=host.split(".");

        if(e.type=="mouseover"){//over;
            if(cart_status.flag){
                clearTimeout(cart_status.off);
            }else{
                cart_status.on=setTimeout(function(){
                    _o.removeClass("dn").addClass("load_html");
                    cart_status.flag=!0;
                    mini_cart_ajax(header_cart);

                },200);

            }
        }else{//out;
            if(cart_status.flag){
                cart_status.off=setTimeout(function(){
                    _o.addClass("dn") ;
                    cart_status.flag=!1;
                },200);
            }else{
                clearTimeout(cart_status.on);
            }
        }


    });

    //侧边栏
    $("#j_side_bar").on("click","#j_open",function(e){
        var i=$(this).find("a").text();
        if(i=="栏目"){
            $("#j_side_con").slideDown();
            $(this).find("a").text("收起");
        }else{
            $("#j_side_con").slideUp();
            $(this).find("a").text("栏目");
        }

    });
})