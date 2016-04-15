/*保存个人信息*/
$(function(){
    $(".selectorArea select").area("s_province","s_city","s_county");//调用插件

    $(document).on("mouseenter mouseleave","#js_addressList li",function(){
        $(this).find(".dr-btns").toggle();
    });

    $(document).on("click","#js_addressList li",function(){
        $(this).addClass("on").siblings().removeClass("on");
    });

    jQuery.validator.addMethod("phone", function(value, element) {
        var mobile = $("#rcpt-phone").val();// 手机号码
        var telephone = $("#rcpt-tel2").val();// 固定电话
        var mobileRule = /^1[34578][0-9]{9}$/;
        var telephoneRule = /^\d{3,4}-?\d{7,9}$/;
        // 都没填
        if (isEmpty(mobile) && isEmpty(telephone)) {
            //自定义错误提示
            $(".bError").text("请填写固定电话或手机号码");
            return false;
        }
        var mobilePass = false;
        var telephonePass = false;
        // 手机填了、固定电话没填
        if (!isEmpty(mobile) && isEmpty(telephone)) {
            if (!mobileRule.test(mobile)) {
                //自定义错误提示
                $(".bError").text("手机号码格式不对");
                return false;
            } else {
                $(".bError").text("");
                mobilePass = true;
            }
        }

        // 手机没填、固定电话填了
        if (isEmpty(mobile) && !isEmpty(telephone)) {
            if (!telephoneRule.test(telephone)) {
                //自定义错误提示
                $(".bError").text("固定电话格式不对");
                return false;
            } else {
                $(".bError").text("");
                telephonePass = true;
            }
        }

        if (mobilePass || telephonePass) {
            //自定义成功提示
            //$(".bError").text('成功');
            return true;
        } else {
            return false;
        }
    }, "");

    // 空字符串判断
    function isEmpty(v, allowBlank) {
        return v === null || v === undefined || (!allowBlank ? v === "" : false);
    }


    $("#addressForm_cs").validate({
        rules:{
            lastName:{
                required:true,
                minlength:2
            },
            address1:{
                required:true
            },
            zipCode:{
                rangelength:[6,6]
            },
            mobile : {
                phone : true
            },
            telephone:{
                phone : true
            }
        },
        messages:{
            lastName:{
                required:"名字不能为空",
                minlength:"最少输入2个字符"
            },
            address1:{
                required:"详细地址不能为空"
            },
            zipCode:{
                rangelength:"请输入6位数字"
            }
        },
        errorElement : "lable",
            submitHandler:function(){
                var name=$("#rcpt-name").val(),drs=$("#rcpt-address").val(),tel=$("#rcpt-phone").val(),tel2=$("#rcpt-tel2").val();
                var city_0=$("#s_province").find("option:selected").html(),city_1=$("#s_city").find("option:selected").html(),city_2=$("#s_county").find("option:selected").html();
                var html='<li><div class="dr-item"><span>'+name+'</span><span>'+city_0+'</span></div><div class="dr-detail"><span class="addr-name">'+name+'</span><span class="addr-info"><span data-city0="'+city_0+'">'+city_0+'</span>'+'<span data-city1="'+city_1+'">'+city_1+'</span>'+'<span data-city2="'+city_2+'">'+city_2+'</span>'+'<span data-city3="'+drs+'">'+drs+'</span></span><span class="addr-tel">'+tel+'</span><span class="addr-tel2">'+tel2+'</span></div><div class="dr-btns"><a class="setdefault-consignee" href="#">设为默认地址</a><a class="edit-consignee" href="#">编辑</a><a class="del-consignee" href="#">删除</a></div></li>';
            $("#js_addressList").prepend(html).find("li").first().trigger("click");
            $("#addressForm_cs").slideUp("slow");
        }

    });

    $("#js_add_dr").click(function(){
        $("#addressForm_cs").slideDown("slow");
    });

    $(document).on("click",".del-consignee",function(){
        $(this).parents("li").remove();
    });

    $(document).on("click",".edit-consignee",function(){
        var obj=$(this).parents("li").find(".dr-detail");
        var name=obj.find(".addr-name").text(),city0 =obj.find(".addr-info span").eq(0).data("city0"),city1 =obj.find(".addr-info span").eq(1).data("city1"),city2 =obj.find(".addr-info span").eq(2).data("city2"),city3 =obj.find(".addr-info span").eq(3).data("city3"),tel=obj.find(".addr-tel").text(),tel2=obj.find(".addr-tel2").text();
        $("#rcpt-name").val(name);
        $("#s_province").find("option:selected").text(city0);
        $("#s_city").find("option:selected").text(city1);
        $("#s_county").find("option:selected").text(city2);
        $("#rcpt-address").val(city3);
        $("#rcpt-phone").val(tel);
        $("#rcpt-tel2").val(tel2);
        $("#addressForm_cs").slideDown("slow");
    });

    $("#btnAddCcl").click(function(){
        $("#addressForm_cs").slideUp("slow");
    });

    $("#btnCfmPay").click(function(){
        var payment=$(".barPay input[type='radio']:checked").val();
        var payselect=$('.blockChoice select>option:selected').val();
        $("#payment").text(payment);
        $("#payselect").text(payselect);
        $("#payMethod").slideUp("slow");
        $("#accPayInfo").slideDown("slow");
    });

    $("#js_add_pay").click(function(){
        $("#payMethod").slideDown("slow");
        $("#accPayInfo").slideUp("slow");
    });

    $("#js_ts").hover(function(){
        $(this).find(".tsbox").toggle();
    });
    $("#js_More_display").click(function(){
        var i=$(this).text();
        if(i == "显示更多地址"){
            $("#js_addressList li").show();
            $(this).text("收起更多地址");
            $(this).next().addClass("at");
        }else{
            $("#js_addressList li:first").nextAll().hide();
            $(this).text("显示更多地址");
            $(this).next().removeClass("at");
        }

    });

    $("#btnCoupon,#btnOverage").click(function(){
        $(this).nextAll(".dn").toggle();
    });
    /*$("#j_table .red").click(function(){
        $(this).parents("#j_box").hide().next().show();
    });*/
    $("#edit_box .colgray").click(function(){
        $(this).parents("#edit_box").hide().siblings("#j_box").show();
    });

    /*支付方式切换*/
    $("#js_payList li").click(function(){
        $(this).siblings().find(".payment-item").removeClass("item-selected");
        $(this).find(".payment-item").addClass("item-selected");
    });
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
})
