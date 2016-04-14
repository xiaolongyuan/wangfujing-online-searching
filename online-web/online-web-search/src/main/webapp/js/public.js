/*版本1.0  丽方**/
/**网站全局公用js---首页--搜索--头尾等*/
    //slide公共部分
    var slide_opt = {
        mainCell : ".bd ul",
        effect : "left",
        interTime : 5e3,
        startFun : function (i, n, s, t, c) {
            c.children().eq(i).find("img[data-lazyload]").trigger("appear");
            if (this.vis < n) {
                s.find(this.prevCell).show().end().find(this.nextCell).show();
            }
        }
    };
/*toolbar下拉*/
$("#toolbar .j_drop").hover(function(){
    $(this).addClass("drop_hover");
},function(){
    $(this).removeClass("drop_hover");
});
/*左侧导航显示/隐藏*/
$("#j_cats .cats_list").hover(function(e){
    var nod = $(this).children().find(".js_area_tabNav");
    if(nod != null){
        $(this).find(".js_tabNav").html($(this).children().find(".js_area_tabNav").val());
        $(this).children().find(".js_area_tabNav").remove();
    }
    $(this).addClass("hover").siblings().removeClass("hover");
    $("#j_cats .cats_list").last().removeClass("hover");
},function(){
    $(this).removeClass("hover");
});
//下拉分类：
$("#j_home").length==0&&$("#j_cats").hover(function(){
    $(this).find(".cats_bd ").show();
},function(){
    $(this).find(".cats_bd ").hide();
});

/*公用tab控件，需要传参，当前样式selected，当前元素的父元素parent，以及控制子div显示隐藏child*/
$.fn.tab=function(selected,parent,child){
    $(this).click(function(){
        var index=$(this).index();
        $(this).addClass(selected).siblings().removeClass(selected);
        console.log($(this).parents("."+parent).find("."+child).html());
        $(this).parents("."+parent).find("."+child).eq(index).show().siblings().hide();
    });
}