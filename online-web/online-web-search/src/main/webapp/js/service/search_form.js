var searchLocation = searchLocation || "http://search.wangfujing.com";
var keyBlured = false;
function doSearch() {
    var val = $('#key').val();
    if (val == "") {
        return;
    }
    var _win = window;
    while (_win != _win.parent) {
        _win = _win.parent;
    }
    _win.location.assign(searchLocation + "/search/" + encodeURIComponent(val) + ".html");
}
//阻止事件冒泡
function stopEventBubble(event) {
    var e = event || window.event;

    if (e && e.stopPropagation) {
        e.stopPropagation();
    }
    else {
        e.cancelBubble = true;
    }
}
function hidePop() {
    var $shelper = $('#shelper');
    $shelper.html('');
    $shelper.hide();
}
//noinspection JSUnusedGlobalSymbols
function clickPopItem(li) {
    $('#key').val($(li).attr('title'));
    doSearch();
}
function updatePop() {
    var $shelper = $('#shelper');
    $shelper.html('');
    var searchInput = $('#key');
    var val = searchInput.val();
    $.ajax({
        url: searchLocation + "/auto-completion/hh.jsonp",
        data: {
            prefix: val
        },
        type: 'POST',
        dataType: "jsonp",
        jsonp: "callback",
        success: function (data) {
            if (data.list.length) {
                for (var i = 0; i < data.list.length; i++) {
                    var suggestionItem = data.list[i];
                    if (suggestionItem.history) {
                        $shelper.append('<li id="pop-li-' + i + '" title="' + suggestionItem.text + '" onclick="clickPopItem(this)"><div class="search-item">' + suggestionItem.text + '</div><div class="search-count">搜索历史</div></li>');
                    } else {
                        $shelper.append('<li id="pop-li-' + i + '" title="' + suggestionItem.text + '" onclick="clickPopItem(this)"><div class="search-item">' + suggestionItem.text + '</div><div class="search-count">约' + suggestionItem.count + '个商品</div></li>');
                    }
                }
                $shelper.append('<li onclick="hidePop()" class="close">关闭</li>');
                $shelper.find('li').hover(function () {
                    $(this).addClass("on");
                    if ($(this).find('.search-count').text() == '搜索历史') {
                        $(this).find('.search-count').text('').append('<a style="color:#005AA0">删除</a>');
                        $(this).find('a').click(function (e) {
                            var li = $(this).parents('li');
                            var query = li.attr('title');
                            $.ajax({
                                url: searchLocation + '/search-history/remove.jsonp',
                                data: {
                                    query: query
                                },
                                type: 'POST',
                                dataType: 'jsonp',
                                jsonp: 'callback'
                            });
                            li.remove();
                            stopEventBubble(e);
                        });
                    }
                }, function () {
                    $(this).removeClass('on');
                    if ($(this).find('.search-count').find('a').length) {
                        $(this).find('.search-count').text('搜索历史');
                    }
                });
            }
        }
    });
}
$(function () {
    var searchInput = $('#key');
    if (!searchInput.attr('autofocus')) {
        keyBlured = true;
    }
    $('#j_searchForm').submit(function (e) {
        e.preventDefault();
        doSearch();
    });
    searchInput.attr("placeholder", '请输入商品名称');
    searchInput.blur(function () {
        keyBlured = true;
    });
    var $search = $('#search');
    $search.hover(function () {
    }, function () {
        var $shelper = $('#shelper');
        $shelper.html('');
        $shelper.hide();
    });
    searchInput.focus(function (ie) {
        if (keyBlured) {
            var $shelper = $('#shelper');
            $shelper.show();
            updatePop();
        }
        stopEventBubble(ie);
    });
    //noinspection JSUnresolvedFunction
    searchInput.keyup(function (e) {
        var keyCode = e.keyCode;
        var $shelper = $('#shelper');
        if (keyCode == 38 || keyCode == 40) {
            if (!$shelper.is(":visible")) {
                $shelper.show();
                updatePop();
            } else {
                var on = $shelper.find(".on");
                if (on.length) {
                    var index;
                    if (keyCode == 40) {
                        index = on.index() + 1;
                        if (index >= $shelper.find('li').length - 1) {
                            index = 0;
                        }
                    } else {
                        index = on.index() - 1;
                        if (index < 0) {
                            index = $shelper.find('li').length - 2;
                        }
                    }
                    on.removeClass('on');
                    $shelper.find('li:nth-child(' + (index + 1) + ')').addClass('on');
                } else {
                    if ($shelper.find('li:first-child')) {
                        $shelper.find('li:first-child').addClass('on');
                    }
                }
                $('#key').val($shelper.find('.on').attr('title'));
            }
        } else {
            $shelper.show();
            updatePop();
        }
    });
    //noinspection JSUnresolvedFunction
    $(document).keydown(function (e) {
        if (27 == e.keyCode) {
            hidePop();
        }
    });
});