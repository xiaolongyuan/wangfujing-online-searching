var searchLocation = searchLocation || 'http://search.wangfujing.com';
$(document).ready(function () {
    $.ajax({
        url: searchLocation + '/cart/num.jsonp',
        type: 'POST',
        dataType: "jsonp",
        jsonp: "callback",
        success: function (data) {
            /** @namespace data.cartNum */
            var cartNum = data.cartNum;
            $('#cartnum').html(cartNum);
            $('.posa.num').html(cartNum);
        }
    });
});
