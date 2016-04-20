$(function () {
    /*鼠标划过显示*/

    $(".itemList li").hover(function(){
        var html = $(this).find(".showinfo").find(".bd-data").html();
        $(this).find(".showinfo").show().find(".bd").append(html);
        $(".colorBox").slide({mainCell:".bd ul",autoPage:true,effect:"top",vis:4});
    },function(){
        $(this).find(".showinfo").hide().find(".bd").html("");
    });


    /*判断每行最后一个换位置*/
    $(".itemList li").each(function(k,v){
        var w=$(".itemList").width();
        if(k%5==4 && w ==1200){
            $(this).find(".showinfo").addClass("s-info-right");
        }else if(k%4==3 && w == 1000){
            $(this).find(".showinfo").addClass("s-info-right");
        }else if(k%3==2 && w == 768){
            $(this).find(".showinfo").addClass("s-info-right");
        }else if(k%2==1 && w == 640){
            $(this).find(".showinfo").addClass("s-info-right");
        }
    });
    $(".history .prev").hover(function(){
       $(this).addClass("icon_32")
    });
    $(".history .next").hover(function(){
        $(this).addClass("icon_31")
    });

    //drop j_carbd list
    var timeCarbleave = null,vis = new Number;
    $("#j_carbd").mouseover(function(){
        clearTimeout(timeCarbleave);
        $(".subitem",this).show();
    });
    $(".subitem").mouseover(function(){
        clearTimeout(timeCarbleave);
    });
    $(".subitem").mouseleave(function(e){
        $('.subitem').css("display","none");
    });
    $("#j_carbd").mouseleave(function(e){
        var self = this;
        timeCarbleave = setTimeout(function(){
            $(".subitem",self).hide();
        },50)
    });

    var offset = $("#js_like").offset();
    $(".list_likes").click(function(event) {
        var flyer = $('<div class="icon_28 flyer-img"></div>');//抛物体对象
        flyer.fly({
            start: {
                left: event.clientX, //抛物体起点横坐标
                top: event.clientY////抛物体起点纵坐标
            },
            end: {
                left: offset.left+10, //抛物体终点横坐标
                top: offset.top+10 //抛物体终点纵坐标
            },
            speed:0.9,
            vertex_Rtop:30,
            onEnd: function() {
                $(".addshow").show().animate({left: '-100px',width:'100px'}, 300).fadeOut(500).animate({left: '0px',width:'53px'}, 300);//成功加入收藏动画效果
                this.destory();//销毁抛物体
            }
        });
    });

    $(".td_list").each(function(){
        var that=$(this);
        if($(this).height()>54){
            $(this).addClass("td_list_sel");
            $(this).parent(".listTd").next().find(".vb").removeClass("mvb").on("click",function(){
                if(that.hasClass("td_list_sel")){
                    $(this).find("span").text("收起");
                    $(this).find("i").addClass("arrt");
                    that.removeClass("td_list_sel");
                }else{
                    $(this).find("span").text("更多");
                    $(this).find("i").removeClass("arrt");
                    that.addClass("td_list_sel");
                }
            });
        };
    });
    $("#j_classTable tr:gt(0)").hide();
    var t=1;
    $(".mor_t").click(function(){
        if(t){
            $("#j_classTable tr:gt(0)").show();
            $(this).html('<i class="arroB arroT"></i>收起');
            t = 0;
        }else{
            $("#j_classTable tr:gt(0)").hide();
            $(this).html('<i class="arroB"></i>更多');
            t = 1;
        }
        return false;
    });



})


/*头搜索*/


//
// var $o = function(a){
//     var $b = $("#key"),$a=$("#shelper");
//     var a = '<li id="d_0" title="手机壳6" onclick="clickItem(this)"><div class="search-item">手机壳<strong>6</strong></div><div class="search-count">搜索历史</div></li>',
//         b = '<li id="d_1" title="冰箱" onclick="clickItem(this)"><div class="search-item">冰箱<strong>18</strong></div><div class="search-count">约715485个商品</div></li>',
//         c = '<li onclick="$o.hideTip()" class="close">关闭</li>',
//         d = '<a style="color:#005AA0">删除</a>';
//
//     if(!(b.length<1)){
//         function x(obj){
//             this.init();
//         }
//         x.prototype={
//             constructor :x,
//             init:function(){
//                 $b.focus(function(){
//                     $("#shelper").show().append(a+a+a+a+b+b+b+b+b+b+c).find("li").hover(function(){
//                         if($(this).find(".search-count").text()=="搜索历史"){
//                             $(this).find(".search-count").text("").append(d);
//                             $(this).find("a").click(function(e){
//                                 $(this).parents("li").remove();
//                                 stopEventBubble(e);
//                             });
//                         }
//                     },function(){
//                         if($(this).find(".search-count").text()=="删除"){
//                             $(this).find(".search-count").text("搜索历史");
//                         }
//                     });
//                 });
//             }
//         }
//         new x();
//     }
// }(window);



function clickItem(e){


}

//阻止事件冒泡
function stopEventBubble(event){
    var e=event || window.event;

    if (e && e.stopPropagation){
        e.stopPropagation();
    }
    else{
        e.cancelBubble=true;
    }
}

