var itemUrlPrefix = itemUrlPrefix || 'http://www.wangfujing.com/item';
var itemUrlPostfix = itemUrlPostfix || '';
$(function () {
    //noinspection JSUnresolvedFunction
    var history_list = getBrowseList();
    if (history_list.length) {
        var $historyList = $('#history_list');
        var idSet = [];
        $historyList.html('');
        for (var i = history_list.length - 1; i >= 0; i--) {
            var hi = history_list[i];
            var contain = false;
            for (var j = 0; j < idSet.length; j++) {
                if (idSet[j] == hi.code) {
                    contain = true;
                    break;
                }
            }
            if (contain) {
                continue;
            }
            idSet.push(hi.code);
            var li = '<li class="fl item"><a title="' + hi.name + '" target="_blank" href="' +
                itemUrlPrefix + hi.code + itemUrlPostfix + '"><img alt="' + hi.name + '" class="item_pic" src="'
                + hi.imgurl + '" style="width:60px;height:60px;"></a></li>';
            $historyList.append(li);
        }
        $('#history_view').show();
    }
});