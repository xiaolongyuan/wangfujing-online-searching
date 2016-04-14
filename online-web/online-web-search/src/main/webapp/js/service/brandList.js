$(function () {
    jQuery(function ($) {
        var o = $("#brand");
        ~function (e) {
            e.fn.fixedBox = function (opt) {
                this.each(function () {
                    var i = e.extend({
                        startTop: null,
                        endTop: null,
                        fixedClass: "box-fixed",
                        targetEle: null,
                        range: 0,
                        onStart: function () {
                        },
                        onEnd: function () {
                        }
                    }, opt), s = e(this), a = s.height(), o = s.width(), n = e('<div class="fixed-box-wrap"/>');
                    s.css({
                        height: a,
                        width: o
                    }), s.parent().hasClass("fixed-box-wrap") || s.wrap(n.css("height", a)), e(window).bind("scroll", function () {
                        var t = e(document).scrollTop(), o = i.startTop || s.parent(".fixed-box-wrap").offset().top, n = i.targetEle ? e(i.targetEle).offset().top : 1e4;
                        t > o && (i.endTop || n) - i.range > t ? (s.addClass(i.fixedClass), i.onStart && i.onStart()) : (s.removeClass(i.fixedClass), i.onEnd && i.onEnd())
                    })
                });
                return this;
            }
        }
        (jQuery);
        $(".letterFilter .letters").fixedBox({targetEle: ".footer", range: 0});
        o.find(".letterFilter .item").click(function (e) {
            var theObj = $(this), txt = $.trim(theObj.text()), top = $("a[name='" + txt + "']", ".brand-list").length > 0 ? $("a[name='" + txt + "']", ".brand-list").offset().top : 0;
            theObj.addClass("current").siblings().removeClass("current");
            $("html,body").animate({scrollTop: top}, 0);
        }).end().find(".brand-section").prepend(function () {
            var txt = $(this).find(".letter").text();
            txt = $.trim(txt);
            return ['<a name="' + txt + '">', "</a>",].join(" ");
        }).end().find(".cats-bname").on({
            "click": function (e) {
                var theObj = $(this);
                catid = theObj.attr("data-catid");
                if ($(this).parents(".cats-item").hasClass("current")) {
                    return;
                }
                o.find(".letterFilter .item").removeClass("current");
                theObj.parents(".cats-item").addClass("hover current").siblings().removeClass("hover current");
                o.find(".letterFilter .item").hide().end().find(".brand-list").hide().find(".item").hide().filter(function () {
                    var theObj = $(this), theCatid = theObj.attr("data-catid");
                    if (!theCatid) {
                        return false;
                    }
                    tmpArr = theCatid.split(",");
                    t = jQuery.inArray(catid, tmpArr) > -1 ? true : false;
                    if (t) {
                        var letter = theObj.parents(".brand-list").show().find("a:first-child").attr("name");
                        o.find(".letterFilter .item a.letter:contains(" + letter + ")").parent().show()
                    }
                    return t;
                }).show();
                o.find(".brand-list").each(function () {
                    var o = $(this), l = o.find(".bd .item:visible").length;
                    o.find(".hd span.red").text(l);
                }).end().find(".letterFilter .item:visible:first").click();
                e.preventDefault();
            }, "mouseover mouseout": function (e) {
                var p = $(this).parents(".cats-item");
                if (p.hasClass("current")) {
                    return;
                }
                if (e.type == "mouseover") {
                    p.addClass("hover");
                } else if (e.type == "mouseout") {
                    p.removeClass("hover").siblings(".current").addClass("hover");
                }
            }
        });
        var n = (window.location).hash.slice(1) * 1;
        if (n) {
            $(".cats-bname[data-catid='" + n + "']").triggerHandler("click");
        }
        $(".brand-list .item[data-catinfo] a").on("mouseover click", function () {
            var t = $(this);
            t.attr("href", function () {
                var catid = $(".cats-list>.current>.cats-bname").data("catid");
                if (!catid) {
                    return;
                }
                return t.parents(".item").data("catinfo")[catid]
            });
        });
    });
})