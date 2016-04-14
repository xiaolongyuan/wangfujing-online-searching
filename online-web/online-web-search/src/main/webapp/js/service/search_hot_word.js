var searchLocation = searchLocation || 'http://search.wangfujing.com';
$(function () {
    $("#search_words").find("> div > a").remove();
    $.ajax({
        url: searchLocation + '/service/hot-word/list-hot-word.jsonp',
        type: 'GET',
        dataType: "jsonp",
        jsonp: "callback",
        success: function (data) {
            var html = '';
            if (data.length) {
                var channelHotWords = data[0];
                var hotWords = channelHotWords.hotWords;
                /** @namespace data.hotWords */
                for (var i = 0; i < hotWords.length; i++) {
                    var hotWord = hotWords[i];
                    html += '<a data-gaevent="{&quot;cats&quot;:&quot;index&quot;,&quot;word&quot;:&quot;' + hotWord.value + '&quot;}" ' +
                    'target="_blank" title="' + hotWord.value + '" href="' + hotWord.link + '" class="fl search_word ">' + hotWord.value + '</a>'
                }
                var $searchWords = $("#search_words");
                var oHtml = $searchWords.find("> div").html();
                html = oHtml + html;
                $searchWords.find("> div").html(html);
            }
        }
    });
});