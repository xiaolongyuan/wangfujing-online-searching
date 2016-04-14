$(function () {
    /*鼠标划过显示*/
    $(".itemList li").hover(function(){
        $(this).find(".showinfo").toggle();
    });
    /*判断每行最后一个换位置*/
    $(".itemList li").each(function(k,v){
        if(k%5==4){
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
    /*/
    $("#j_classTable tr:gt(1)").hide();
    var t=1;
    $(".mor_t").click(function(){
        if(t){
            $("#j_classTable tr:gt(1)").show();
            $(this).html('<i class="arroB arroT"></i>收起');
            t = 0;
        }else{
            $("#j_classTable tr:gt(1)").hide();
            $(this).html('<i class="arroB"></i>更多');
            t = 1;
        }
        return false;
    });
    /*/
    $(".sctdn").hide();
    var t=1;
    $(".mor_t").click(function(){
        if(t){
            $(".sctdn").show();
            $(this).html('<i class="arroB arroT"></i>收起');
            t = 0;
        }else{
            $(".sctdn").hide();
            $(this).html('<i class="arroB"></i>更多');
            t = 1;
        }
        return false;
    });
    //*/
})
