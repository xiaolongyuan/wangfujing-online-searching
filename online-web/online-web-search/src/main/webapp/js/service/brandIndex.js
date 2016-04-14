function syncAnchors() {
    $('.brand-list').each(function() {
        var group = $(this).find('a[name]').attr('name');
        $('a[href="#' + group + '"]').parent().removeClass("dn");
    });
    $('.brand-list.dn').each(function() {
        var group = $(this).find('a[name]').attr('name');
        $('a[href="#' + group + '"]').parent().addClass("dn");
    });
}
function filterCat(a) {
    var cid = $(a).attr('data-cid');
    if (cid) {
        $('.brand-list .bd ul li').attr('data-filtered', 'true');
        $('.brand-list .bd ul li[data-catid*="' + cid + ',"]').attr('data-filtered', 'false');
    } else {
        $('.brand-list .bd ul li').attr('data-filtered', 'false');
    }
    $('li[data-filterd=true]').addClass('dn');
    $('li[data-filterd=false]').removeClass('dn');
    $('.brand-list').each(function () {
        var showBrandCount = $(this).find('.bd ul li[data-filtered=false]').length;
        if (showBrandCount) {
            $(this).removeClass('dn');
        } else {
            $(this).addClass('dn');
        }
    });
    syncAnchors();
}
$(function() {
   syncAnchors();
});