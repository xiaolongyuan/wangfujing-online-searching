$(function () {
    var heights = [0, 0, 0, 0, 0];
    $('.all-brand .brand-widget').each(function () {
        if ($(this).find('a').length) {
            var idx = widgetIdx(heights);
            var left = 125 + idx * 200;
            $(this).offset({top: heights[idx], left: left});
            heights[idx] += $(this).height();
            $(this).removeClass('dn');
        }
    });
});
function widgetIdx(heights) {
    var idx = 0;
    var min = heights[0];
    for(var i = 1; i < 5; i++) {
        if (min > heights[i]) {
            idx = i;
            min = heights[i];
        }
    }
    return idx;
}