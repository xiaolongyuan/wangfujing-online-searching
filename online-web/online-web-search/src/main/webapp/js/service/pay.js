$(function(){
    $(".titlist li").tab("on","typeBank","bankbox");

    $("#js_toPay").click(function(){
        $("#toPaydiv").show();
    });
    $("#js_goPay").click(function(){
        $("#goPaydiv").show();
    });
})