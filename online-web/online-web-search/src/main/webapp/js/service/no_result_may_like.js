var searchLocation = searchLocation || 'http://search.wangfujing.com';
var getNoResultMayLikeDiv = function () {
    $.ajax({
        url: searchLocation + '/service/may-like/randoms.jsonp',
        dataType: "jsonp",
        jsonp: "callback",
        success: function (data) {
            if (data.list.length) {
                var $jLike = $('#j_like');
                $jLike.html('');
                $jLike.append('<div class="hd"><h2 class="hd_tit">猜你喜欢</h2></div>');
                $jLike.append('<div class="bd "><ul id="history_maybe_like" class="like_list clearfix"></ul></div>');
                $jLike.append('<a href="javascript:" class="posa icon_30 prev"></a>');
                $jLike.append('<a href="javascript:" class="posa icon_29 next"></a>');
                var lis = '';
                for (var i = 0; i < data.list.length; i++) {
                    var item = data.list[i];
                    var itemUrl = itemUrlPrefix + item.spuId + itemUrlPostfix;
                    lis += '<li>' +
                    '<a target="_blank" href="' + itemUrl + '" class="imgs">' +
                    '<img alt="' + item.title + '" class="item_pic" src="' + picLocation + '/';
                    if (item.colorMasterPictureOfPix && item.colorMasterPictureOfPix['220x220']) {
                        lis += item.colorMasterPictureOfPix['220x220'];
                    } else {
                        lis += item.colorMasterPicture;
                    }
                    lis += '"style="width: 220px;height: 220px;">' +
                    '</a><div class="item_txt"><a target="_blank" href="' + itemUrl + '" class="name">' + item.title +
                    '</a><div class="item_price"><strong>￥' + item.currentPrice.toFixed(2) + '</strong></div>' +
                    '</div></li>';
                }
                var ul = $jLike.find('#history_maybe_like');
                ul.append(lis);
            }
        }
    });
};
$(function () {
    getNoResultMayLikeDiv();
});