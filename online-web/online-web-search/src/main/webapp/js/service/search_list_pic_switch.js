function switchSpuBigPic(img) {
    var spuId = $(img).attr('data-spu-id');
    var bigPicSrc = $(img).attr("data-big-pic");
    $('#spu-big-pic-' + spuId).attr('src', bigPicSrc);
}

function resetSpuBigPic(img) {
    var spuId = $(img).attr('data-spu-id');
    var bigPicImg = $('#spu-big-pic-' + spuId);
    bigPicImg.attr('src', bigPicImg.attr('data-src'));
}
