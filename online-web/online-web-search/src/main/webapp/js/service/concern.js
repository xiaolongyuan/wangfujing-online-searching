var searchLocation = searchLocation || 'http://search.wangfujing.com';
var loginForm = $('#ui-login-page');
function addConcern(sid, type, event) {
    $.ajax({
        url: searchLocation + '/member/concern.json',
        data: {
            sid: sid,
            type: type
        },
        type: 'POST',
        dataType: "jsonp",
        jsonp: "callback",
        success: function (data) {
            /** @namespace data.ok */
            if (data.ok) {
                var div = $(event.target);
                div.removeClass('icon_28');
                div.attr('data-sku-id', '');
                div.html('已关注');
                var offset = $("#js_like").offset();
                var flyer = $('<div class="icon_28 flyer-img"></div>');//抛物体对象
                flyer.fly({
                    start: {
                        left: event.clientX, //抛物体起点横坐标
                        top: event.clientY////抛物体起点纵坐标
                    },
                    end: {
                        left: offset.left + 10, //抛物体终点横坐标
                        top: offset.top + 10 //抛物体终点纵坐标
                    },
                    speed: 0.9,
                    vertex_Rtop: 30,
                    onEnd: function () {
                        $(".addshow").show().animate({
                            left: '-100px',
                            width: '100px'
                        }, 300).fadeOut(500).animate({left: '0px', width: '53px'}, 300);//成功加入收藏动画效果
                        this.destory();//销毁抛物体
                    }
                });
            } else {
                if (data.login === false) {
                    loginForm.show();
                    $('#LogonForm').submit(function (e) {
                        e.preventDefault();
                        var username = $('#logonId').val();
                        var password = $('#logonPassword').val();
                        $.ajax({
                            url: searchLocation + '/member/login.json',
                            method: 'POST',
                            data: {
                                username:username,
                                password:password
                            },
                            type: 'POST',
                            dataType: "jsonp",
                            jsonp: "callback",
                            success: function(data) {
                                if (data.login) {
                                    /** @namespace data.ticketCookie */
                                    var _cookie = data.ticketCookie.name + '=' + data.ticketCookie.value;
                                    _cookie += ';domain=' + data.ticketCookie.domain;
                                    _cookie += ';maxAge=' + data.ticketCookie.maxAge;
                                    _cookie += ';path=/';
                                    //noinspection JSValidateTypes
                                    document.cookie = _cookie;
                                    validateLoginInfo();
                                    loginForm.hide();
                                    addConcern(sid, type, event);
                                } else {
                                    var $logonErrorMsg = $('#logonErrorMsg');
                                    $logonErrorMsg.html(data.message);
                                    $logonErrorMsg.show();
                                }
                            }
                        });
                    });
                }
            }
        }
    });
}
$(function () {
    $(".list_likes").unbind('click').click(function (event) {
        if (event.target && event.target.getAttribute('data-sku-id')) {
            var sid = event.target.getAttribute('data-sku-id');
            if (sid) {
                var type = 0;
                addConcern(sid, type, event);
            }
        }
    });
    $('.block-close').click(function () {
        loginForm.hide();
    });
});