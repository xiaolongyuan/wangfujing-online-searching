$(function(){
    $("#js_brand_sub .item").each(function(){
        $(this).click(function(){
            $("#js_brand_sub .item").find(".b-name b").text("+");
            $(this).addClass("on").siblings().removeClass("on");
            $(this).find(".b-name b").text("-");
        })
    });
})