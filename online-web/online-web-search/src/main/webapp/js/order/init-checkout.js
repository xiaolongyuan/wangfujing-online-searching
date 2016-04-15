var stateInvoice = false;
var stateInfo = false;
var statePay = false;
var cpChoiceState = false;
var ivChoiceState = false;
var ovChoiceState = false;
$(function () {
    $("#btnCoupon").trigger("click");
});
function clearEmptySpan() {
    $('#accInfo .loadedInfo .barInfo').each(function () {
        $this = $(this);
        if ($this.find('[data-type]').text() == '') {
            $this.hide();
        } else
            $this.show();
    });
}
function scrollToError() {
    if (stateInfo == false) {
        $('html, body').animate({scrollTop: $("#accInfo").offset().top - 20}, 1000);
        $("#accInfo .ui-panel").addClass('itemHover');
        errorTip($("#btnAddCfm"), '请点击“确认收货人信息”');
        errorTip($("#btnAddSav"), '请点击“保存收货人信息”');
        if (statePay == false) {
            errorTip($("#btnPayCcl"), '请点击“确认支付和配送方式”');
        }
    } else if (statePay == false) {
        $('html, body').animate({scrollTop: $("#accPay").offset().top - 20}, 1000);
        $("#accPay .ui-panel").addClass('itemHover');
        errorTip($("#btnPayCcl"), '请点击“确认支付和配送方式”');
    } else {
        $("#accInfo .ui-panel").removeClass('itemHover');
        $("#accPay .ui-panel").removeClass('itemHover');
    }
}
function loadFirstAdd($data) {
    if ($data.length > 0 && !$data.hasClass('newAdd')) {
        fillInfo($data.children("[data-type='person']").text(), $data.children("[data-type='add1']").text(), $data.children("[data-type='add2']").text(), $data.children("[data-type='add3']").text(), $data.children("[data-type='add4']").text(), $data.children("[data-type='code']").text(), $data.children("[data-type='phone']").text(), $data.children("[data-type='tel']").text(), '#888888');
        clearErrorTip();
    } else {
        $('#cbxDefAdd').prop('checked', true);
    }
}
function invoiceInit() {
    if ($("#invoice").is(":checked")) {
        $("#invoiceChoice").find(".widgetCoupon").show();
        ovChoiceState = true;
        if ($("#invoice_amount").text() == "0.00") {
            forbInvoice();
            ovChoiceState = true;
        }
    }
}
function tipsGroup() {
    addTips("flash", $(".tipFlash"), "灵感闪现");
    addTips("vip", $(".tipVip"), "VIP价");
    addTips("vipg", $(".tipVipG"), "VIP金价");
    addTips("pvip", $(".pVip"), "VIP白金价");
    addTips("gift", $(".tipGift"), "赠品");
    addTips("storage", $(".tipStorage"), "库存仅剩" + 5 + "件");
    addTips("redlabel", $(".tip01"), "折上折");
    addTips("redlabel", $(".tip02"), "满减");
    addTips("redlabel", $(".tip03"), "组合惠");
    addTips("redlabel", $(".tip05"), "加价换购");
    addTips("redlabel", $(".tip06"), "赠礼");
    addTips("redlabel", $(".tip07"), "返券");
    addTips("redlabel", $(".tip08"), "免运费");
    addTips("redlabel", $(".tip10"), "优惠券");
    addTips("redlabel", $(".tip00"), "优惠");
    addTips("greylabel", $(".tipx01"), "折上折");
    addTips("greylabel", $(".tipx02"), "满减");
    addTips("greylabel", $(".tipx03"), "组合惠");
    addTips("greylabel", $(".tipx05"), "加价换购");
    addTips("greylabel", $(".tipx06"), "赠礼");
    addTips("greylabel", $(".tipx07"), "返券");
    addTips("greylabel", $(".tipx08"), "免运费");
    addTips("greylabel", $(".tipx10"), "优惠券");
    addTips("greylabel", $(".tipx00"), "优惠");
}
$("input[type='text']").each(function () {
    $(this).val($(this).attr('data-pre')).css('color', '#cccccc');
});
function bindInputText() {
    $("input[type='text']").each(function () {
        var $this = $(this);
        $this.focusin(function (e) {
            var id = this.id, v = $.trim(this.value);
            if ($this.val() == $this.attr('data-pre') && (id != "invoiceTitle" && id != "payCount")) {
                $this.val('');
            }
            if (id == "payCount") {
                if ($this.val() == $this.data('pre') && $this.val() == "0.00") {
                    $this.val('');
                }
            }
            if (id == "invoiceTitle") {
                if ($this.val() == $this.data('pre')) {
                    return;
                }
            }
            $this.css('color', '#888888');
            $this.parent().nextAll(".blockError").hide();
        }).focusout(function (e) {
            var id = this.id, v = $.trim(this.value);
            if (!v) {
                $this.val($this.attr('data-pre'));
                if (id == "invoiceTitle") {
                    $this.val($this.data('pre'));
                }
                $this.css('color', '#cccccc');
            } else {
                if (id == "invoiceTitle" && ($this.val() != $this.data('pre'))) {
                    saveInvoice(1);
                }
            }
        }).bind('change', function () {
            if ($this.val() == '') {
                $this.val($this.attr('data-pre'));
                $this.css('color', '#cccccc');
            }
        });
    });
}
bindInputText();
function updateInfo() {
    var $var = $("#accInfo .loadedInfo");
    $var.find("[data-type='data-name']").text($("#rcpt-name").val());
    $var.find("[data-type='data-add']").text($("#rcpt-area").multilist('val') + '-' + $("#rcpt-city").multilist('val') + '-' + $("#rcpt-zone").multilist('val') + '-' + $("#rcpt-address").val());
    $var.find("[data-type='data-code']").text($("#rcpt-code").val());
    $var.find("[data-type='data-phone']").text($("#rcpt-phone").val());
    $var.find("[data-type='data-tel']").text($("#rcpt-tel1").val() + '-' + $("#rcpt-tel2").val() + '-' + $("#rcpt-tel3").val());
    clearEmptySpan();
}
function addressAhead() {
    var _ar = $("#rcpt-area").multilist('val');
    var _ci = $("#rcpt-city").multilist('val');
    var _zo = $("#rcpt-zone").multilist('val');
    if (_ar == '请选择')
        _ar = '';
    if (_ci == '请选择')
        _ci = '';
    if (_zo == '请选择')
        _zo = '';
    $('#addAhead').hide().text(_ar + ' ' + _ci + ' ' + _zo).show();
}
function panelFocus($this) {
    $this.find('.ui-panel').addClass('bg-grey-f8');
    $this.find('.ui-panel').removeClass('itemHover');
}
function panelNormal($this) {
    $this.find('.ui-panel').removeClass('bg-grey-f8');
    $this.find('.ui-panel').removeClass('itemHover');
}
function fillInfo(_name, _area, _city, _zone, _address, _code, _phone, _tel, _focusbg) {
    $("#rcpt-area").trigger('listup');
    $("#rcpt-city").trigger('listup');
    $("#rcpt-zone").trigger('listup');
    $("#rcpt-area").multilist('setVal', _area);
    $("#rcpt-area").trigger('change', {flag: '1'});
    $("#rcpt-city").multilist('setVal', _city);
    $("#rcpt-city").trigger('change', {flag: '1'});
    $("#rcpt-zone").multilist('setVal', _zone);
    $("#rcpt-zone").trigger('change', {flag: '1'});
    $("#rcpt-name").val(_name).css('color', _focusbg);
    $("#rcpt-address").val(_address).css('color', _focusbg);
    $("#rcpt-phone").val(_phone).css('color', _focusbg);
    $("#rcpt-code").val(_code).css('color', _focusbg);
    if (_tel) {
        var tel = _tel.split("-");
        tel1 = tel[0];
        tel2 = tel[1];
        $("#rcpt-tel1").val(tel1).css('color', _focusbg);
        $("#rcpt-tel2").val(tel2).css('color', _focusbg);
        $("#rcpt-tel3").val('');
        if (tel.length > 2) {
            tel3 = tel[2];
            $("#rcpt-tel3").val(tel3).css('color', _focusbg);
        }
    }
}
function fillEmptyInfo() {
    _focusbg = "#cccccc";
    $("#rcpt-area").trigger('listup');
    $("#rcpt-city").trigger('listup');
    $("#rcpt-zone").trigger('listup');
    $("#rcpt-area").multilist('setDef');
    $("#rcpt-city").multilist('setDef');
    $("#rcpt-zone").multilist('setDef');
    $("#addAhead").hide().text('');
    $("#rcpt-name").val('').css('color', _focusbg).trigger('change');
    $("#rcpt-address").val('').css('color', _focusbg).trigger('change');
    $("#rcpt-phone").val('').css('color', _focusbg).trigger('change');
    $("#rcpt-code").val('').css('color', _focusbg).trigger('change');
    $("#rcpt-tel1").val('').css('color', _focusbg).trigger('change');
    $("#rcpt-tel2").val('').css('color', _focusbg).trigger('change');
    $("#rcpt-tel3").val('').trigger('change');
}
function addTips(type, $target, label) {
    var $tip = $("<div/>");
    var $tiparrow = $("<div/>").addClass("tips-arrow");
    var $tipbody = $("<div/>").addClass("tips-body");
    var tip_wid = label.length * 12 + 20;
    var tip_mgl = 0;
    var $obj;
    var tar_child = "tips-canvas";
    switch (type) {
        case"flash":
            $obj = createTips($tip.addClass("tips-red-down"), $tipbody.text(label), $tiparrow.addClass("tips-arrow-mid"));
            break;
        case"vip":
            $obj = createTips($tip.addClass("tips-yellow-down").addClass("layout-a"), $tipbody.text(label), $tiparrow.addClass("tips-arrow-mid"));
            tip_wid -= 15;
            tip_mgl = 42;
            break;
        case"vipg":
            $obj = createTips($tip.addClass("tips-red-down").addClass("layout-a"), $tipbody.text(label), $tiparrow.addClass("tips-arrow-mid"));
            tip_wid -= 15;
            tip_mgl = 37;
            break;
        case"pvip":
            $obj = createTips($tip.addClass("tips-red-down").addClass("layout-a"), $tipbody.text(label), $tiparrow.addClass("tips-arrow-mid"));
            tip_wid -= 5;
            tip_mgl = 22;
            break;
        case"gift":
            $obj = createTips($tip.addClass("tips-red-down"), $tipbody.text(label), $tiparrow.addClass("tips-arrow-mid"));
            break;
        case"storage":
            $obj = createTips($tip.addClass("tips-red-up").addClass("layout-left"), $tiparrow.addClass("tips-arrow-mid"), $tipbody.text(label));
            tar_child = "tips-bottom";
            break;
        case"redlabel":
            $obj = createTips($tip.addClass("tips-red-right").addClass("layout-left"), $tipbody.addClass("layout-left").text(label), $tiparrow.addClass("layout-left").addClass("tips-arrow-right").addClass("mg-t7"));
            tip_wid += 5;
            break;
        case"greylabel":
            $obj = createTips($tip.addClass("tips-grey-right").addClass("layout-left"), $tipbody.addClass("layout-left").text(label), $tiparrow.addClass("layout-left").addClass("tips-arrow-right").addClass("mg-t7"));
            tip_wid += 5;
            break;
        default:
            alert('error');
            break;
    }
    $obj.css("width", tip_wid).css("margin-left", tip_mgl).appendTo($target.find("." + tar_child)).fadeIn(500);
}
function createTips($_tip, $_part1, $_part2) {
    return $_tip.append($_part1).append($_part2).css("display", "none");
}
function mixHeight() {
    var leftHeight = $(".widgetTotalSub .panelLeft").height();
    var rightHeight = $(".widgetTotalSub .panelRight").height();
    if (leftHeight > rightHeight) {
        $(".widgetTotalSub .panelRight").height(leftHeight - 2);
        $(".widgetTotalSub .panelLeft").height(leftHeight);
    } else {
        $(".widgetTotalSub .panelLeft").height(rightHeight + 2);
        $(".widgetTotalSub .panelRight").height(rightHeight);
    }
}
function matchHeight(n) {
    var hl = $(".widgetTotalSub .panelLeft").height();
    var hr = $(".widgetTotalSub .panelRight").height();
    $(".widgetTotalSub .panelLeft").height(hl + n);
    $(".widgetTotalSub .panelRight").height(hr + n);
}
function checkName() {
    var flag = false;
    var name = $("#rcpt-name").val();
    name = name.replace(/\s/g, '');
    if (name == "请填写收货人的真实姓名") {
        errorTip($("#rcpt-name"), '您还未填写收货人姓名');
        flag = false;
    } else if (!regexName(name)) {
        errorTip($("#rcpt-name"), '收货人姓名不能包含特殊字符');
        flag = false;
    } else {
        if (name.length < 2 || name == '请填写收货人的真实姓名') {
            errorTip($("#rcpt-name"), '收货人姓名不能少于2个字符');
            flag = false;
        } else {
            flag = true;
        }
    }
    return flag;
}
function checkAddr() {
    var _addr = $("#rcpt-address").val().replace(' ', '').replace($("#rcpt-address").attr('data-pre'), '');
    var flag = false;
    if (_addr == '') {
        errorTip($("#rcpt-address"), '请填写正确的地址');
        flag = false;
    } else {
        if ($("#rcpt-address").val().length < 5) {
            errorTip($("#rcpt-address"), '请仔细填写详细地址');
            flag = false;
        } else {
            flag = true;
        }
    }
    return flag;
}
function checkPhone() {
    var flag = true;
    var flag0 = true;
    var flag1 = true;
    var flag2 = true;
    var _ph = $("#rcpt-phone").val();
    var _t1 = $("#rcpt-tel1").val();
    var _t2 = $("#rcpt-tel2").val();
    var _t3 = $("#rcpt-tel3").val().replace(/\s/g, '');
    var _subph = _ph.substr(0, 2);
    if (!_ph.length > 0 && !_t1.length > 0 && !_t2.length > 0 && !_t3.length > 0) {
        flag0 = false;
        errorTip($("#rcpt-tel3"), '手机固话任选其一即可');
    } else {
        if (_ph.length > 0) {
            if (!regexNum(_ph) || _ph.length != 11) {
                errorTip($("#rcpt-tel3"), '请填写11位手机号码');
                flag1 = false;
            } else if (_subph != '13' && _subph != '14' && _subph != '15' && _subph != '17' && _subph != '18') {
                errorTip($("#rcpt-tel3"), '请填写正确的手机号码');
                flag1 = false;
            } else {
                flag1 = true;
            }
        }
        if (_t1.length > 0) {
            if (!regexNum(_t1) || (_t1.length < 3 || _t1.length > 4)) {
                errorTip($("#rcpt-tel3"), '请填写3-4位固话区号');
                flag2 = false;
            } else if (!regexNum(_t2) || (_t2.length < 7 || _t2.length > 8)) {
                errorTip($("#rcpt-tel3"), '请填写7-8位固话号码');
                flag2 = false;
            } else if ((!regexNum(_t3) && _t3.length > 0) || _t3.length > 6) {
                errorTip($("#rcpt-tel3"), '请填写6位以内分机号');
                flag2 = false;
            } else
                flag2 = true;
        } else if (_t2.length > 0 || _t3.length > 0) {
            errorTip($("#rcpt-tel3"), '请填写3-4位固话区号');
            flag2 = false;
        }
    }
    if (flag1 && flag2 && flag0)
        flag = true; else
        flag = false;
    return flag;
}
function checkPaypoint() {
    var flag = false;
    if ($("#payCount").val() == '') {
        flag = false;
    } else if (!regexFloat2($("#payCount").val())) {
        errorTip($("#payCount"), '仅支持2位小数或者整数');
        flag = false;
    } else {
        flag = true;
    }
    return flag;
}
function quickValidate() {
    var _v1 = checkName();
    var _v2 = checkZone();
    var _v3 = checkAddr();
    var _v4 = checkCode();
    var _v5 = checkPhone();
    if (_v1 && _v2 && _v3 && _v4 && _v5)
        flag = true; else
        flag = false;
    return flag;
}
function checkCode() {
    var varCode = $("#rcpt-code").val();
    varCode = varCode.replace(/\s/g, '');
    if ((!regexNum(varCode)) && varCode != '') {
        errorTip($("#rcpt-code"), '请输入正确的邮编');
        return false;
    } else if (varCode.length != 6 && varCode != '') {
        errorTip($("#rcpt-code"), '请输入6位邮编');
    } else
        return true;
}
function checkZone() {
    if ($("#rcpt-zone").multilist('idx') == '-1') {
        errorTip($("#rcpt-zone").find('.ui-droplist-data'), '请先选择省份、城市、区县');
        return false;
    }
    return true;
}
function bindAddrCheck() {
    $("#rcpt-address").focusin(function (e) {
        if ($("#rcpt-zone").multilist('val') == '请选择') {
            errorTip($("#rcpt-zone").find('.ui-droplist-data'), '请先选择省份、城市、区县');
        }
    });
}
bindAddrCheck();
function errorTip($tar, str) {
    $tar.parent().nextAll('.blockError').text(str).fadeIn(200);
}
function clearErrorTip() {
    $("#accInfo .formRecipient .blockError").hide();
    $("#accPay form .blockError").hide();
}
function regexName(str) {
    var rg = /^[\u4E00-\u9FA5A-Za-z0-9_]+$/;
    return rg.test(str);
}
function regexNum(str) {
    var rg = /^[0-9]+$/;
    return rg.test(str);
}
function regexFloat2(str) {
    var rg = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;
    return rg.test(str);
}
function forbInvoice() {
    $('#invoiceChoice input.invoiceTitle').prop('disabled', true).removeClass('font-grey-4').css('background-color', '#f8f8f8');
    $('#invoiceChoice input.invoice').prop('checked', false).prop('disabled', true);
}
function initAddInfo() {
    $(".selectorAdd .barAdd li").each(function (e) {
        var $this = $(this);
        var $data = $this.find('label');
        $this.click(function (e) {
            $this.addClass('selected').children(":radio[name='rbadd']").prop('checked', true);
            $this.siblings().removeClass('selected').children(":radio[name='rbadd']").prop('checked', false);
            if (!$this.hasClass('newAdd')) {
                fillEmptyInfo();
                openAreaDl();
                fillInfo($data.children("[data-type='person']").text(), $data.children("[data-type='add1']").text(), $data.children("[data-type='add2']").text(), $data.children("[data-type='add3']").text(), $data.children("[data-type='add4']").text(), $data.children("[data-type='code']").text(), $data.children("[data-type='phone']").text(), $data.children("[data-type='tel']").text(), '#888888');
                clearErrorTip();
            } else {
                resetAreaDl();
                fillEmptyInfo();
            }
        });
    });
    $('#rcpt-name').focusout(function () {
        checkName();
    });
    $('#rcpt-address').focusout(function () {
        checkAddr();
    }).focusin(function (e) {
    });
    $('#rcpt-phone').focusout(function () {
        checkPhone();
    });
    $('#rcpt-tel1').focusout(function () {
        checkPhone();
    });
    $('#rcpt-tel2').focusout(function () {
        checkPhone();
    });
    $('#rcpt-tel3').focusout(function () {
        checkPhone();
    });
    $('#rcpt-code').focusout(function () {
        checkCode();
    });
    $('#payCount').focusout(function () {
        if (checkPaypoint()) {
            SavepayCount();
        } else {
            $(this).val($(this).attr('data-pre')).css('color', '#cccccc');
        }
    });
}
initAddInfo();
$('#access-delivery').click(function (e) {
    var t = true;
    var $notic = $(this).parent().next('.blockChoice').children('p').children('.notic');
    if (!t) {
        $('#widgetPayChoice').find('ul li:eq(1)').remove();
        $notic.text('只支持现金，不支持POS机刷卡');
    } else {
        $notic.text('支持现金、POS机刷卡');
    }
    if (!$('#access-pos').prop('checked')) {
        $('#access-cash').prop('checked', true);
    }
    $('#widgetPayChoice').toggle(300);
});
$('#access-pay').click(function () {
    $('#widgetPayChoice').hide(300);
});
$('#paySuggest .blockChoice .blockRadio input').click(function () {
    $('#widgetPayChoice').hide(300);
    $("input[name='access-money']").prop('checked', false);
});
$('.shoppingList .datalist .repeatedItem:even').addClass('bg-grey-f8');
btnCouponMth();
function btnCouponMth() {
    $("#btnCoupon").click(function (e) {
        e.stopPropagation();
        $this = $(this);
        if ($("#couponChoice").children('.widgetCoupon').is(":hidden")) {
            $this.text('(-)使用优惠券/返利红包/促销优惠码');
        } else {
            $this.text('(+)使用优惠券/返利红包/促销优惠码');
            $("#couponScroll").css("z-index", "-1").css("display", "none");
        }
        $("#couponChoice").children('.widgetCoupon').toggle(500, function (e) {
            if ($(this).is(":hidden")) {
                cpChoiceState = false;
            } else {
                $("#couponScroll").css("z-index", "0").css("display", "block");
                cpChoiceState = true;
            }
        });
        return false;
    });
    $("#couponScroll .btnUse").click(function (e) {
        ga('send', 'event', 'OrderFill', '优惠券', '使用');
    });
    $("#couponChoice .widgetCoupon .enterCode").click(function (e) {
        ga('send', 'event', 'OrderFill', '优惠券', '使用');
    });
}
btnOverageMth();
function btnOverageMth() {
    $("#btnOverage").click(function () {
        $this = $(this);
        if ($("#overageChoice").children('.widgetCoupon').is(":hidden")) {
            $this.text('(-)账户余额');
        } else {
            $this.text('(+)账户余额');
        }
        $("#overageChoice").children('.widgetCoupon').toggle(300, function () {
            if ($(this).is(":hidden")) {
                ivChoiceState = false;
            } else {
                ivChoiceState = true;
            }
        });
        return false;
    });
}
invoiceMth();
function invoiceMth() {
    $("#invoice").click(function (e) {
        if ($("#invoice_amount").text() != 0) {
            stateInvoice = true;
        } else {
            forbInvoice();
        }
        $("#invoiceChoice").children('.widgetCoupon').toggle(300, function () {
            if ($(this).is(":hidden")) {
                ovChoiceState = false;
                $(this).css("z-index", "-1");
            } else {
                ovChoiceState = true;
                $(this).css("z-index", "0");
            }
        });
        ga('send', 'event', 'OrderFill', '索取发票', '开票/不开票');
        if (stateInvoice) {
            if ($(this).is(":checked")) {
                saveInvoice(1);
            } else {
                saveInvoice(0);
            }
            return true;
        } else {
            return false;
        }
    });
}
$(".iconQuest").hover(function (e) {
    $(this).children('.tooltip').fadeIn(300);
}, function (e) {
    $(this).children('.tooltip').fadeOut(300);
});
$('.widgetCoupon').css('display', 'none');
invoiceInit();
$('#widgetCheckout').click(function (e) {
});
function createBtn(type, targetId) {
    var $btn = $("<a/>").addClass('ui-ribbon-btn');
    switch (type) {
        case'cancel':
            $btn.addClass('btnCollapse').html('[&nbsp;收起&nbsp;]').attr('data-target', targetId).click(function (e) {
                switchState('selected-add');
                return false;
            });
            break;
        case'edit':
            $btn.addClass('btnOpen').html('[&nbsp;编辑&nbsp;]').attr('data-target', targetId).click(function (e) {
                switchState('first-visit');
                ga('send', 'event', 'OrderFill', '收货地址', '编辑');
                return false;
            });
            break;
        case'renew':
            $btn.addClass('btnOpen').html('[&nbsp;编辑&nbsp;]').attr('data-target', targetId).click(function (e) {
                switchState('has-address');
                ga('send', 'event', 'OrderFill', '收货地址', '编辑');
                return false;
            });
            break;
        case'editpay':
            $btn.addClass('btnOpen').html('[&nbsp;编辑&nbsp;]').attr('data-target', targetId).click(function (e) {
                switchState('choice-pay');
                ga('send', 'event', 'OrderFill', '支付及配送', '编辑');
                return false;
            });
            break;
        case'cancelpay':
            $btn.addClass('btnCollapse').html('[&nbsp;收起&nbsp;]').attr('data-target', targetId).click(function (e) {
                switchState('has-pay');
                return false;
            });
            break;
        default:
            alert('按钮类型错误！');
            break;
    }
    return $btn;
}
$("#btnAddCfm").click(function (e) {
    $("#btnAddSav").trigger('click');
});
function bindBtnCancel() {
    $("#btnAddCcl").click(function (e) {
        switchState('selected-add');
        $('html, body').animate({scrollTop: $("#accInfo").offset().top - 20}, 1000);
    });
}
bindBtnCancel();
$("#btnPayCcl").click(function (e) {
    switchState('has-pay');
});
$("#btnCfmPay").click(function (e) {
    payLoadedInfo();
    WFJConfirmPayMethod();
    ga('send', 'event', 'OrderFill', '支付及配送', '确认');
});
$("#btnAddToCart").click(function (e) {
    ga('send', 'event', 'OrderFill', '商品信息', '返回购物车');
});
$("#btnAddSav").click(function (e) {
    if (quickValidate()) {
        alert("defd");
        /*setPrimaryValue();
        updateInfo();*/
       /* var name=$("#rcpt-name").val(),drs=$("#rcpt-address").val(),tel=$("#rcpt-phone").val();
        var city_0=$("#s_province").find("option:selected").html(),city_1=$("#s_city").find("option:selected").html(),city_2=$("#s_county").find("option:selected").html();
        var html='<li><div class="dr-item"><span>'+name+'</span><span>'+city_0+'</span></div><div class="dr-detail"><span class="addr-name">'+name+'</span><span class="addr-info">'+city_0+city_1+city_2+drs+'</span><span class="addr-tel">'+tel+'</span></div><div class="dr-btns"><a class="setdefault-consignee" href="#">设为默认地址</a><a class="ftx-05 edit-consignee" href="#">编辑</a><a class="del-consignee" href="#">删除</a></div></li>';
        $("#js_addressList").prepend(html).find("li").first().trigger("click");*/
        $('#state').val($('#rcpt-area').multilist('val'));
        $('#city').val($('#rcpt-city').multilist('val'));
        $('#address2').val($('#rcpt-zone').multilist('val'));
        if ($('#addressForm_cs').find('[name="addressId"]').val()) {
            eidetAdrsInfo();
        } else {
            submitAddressinfo();
        }
        stateInfo = true;
    }
    ga('send', 'event', 'OrderFill', '收货地址', '确认');
});
function switchState(str, _trans) {
    if (str == 'first-visit') {
        $("#accInfo .loadedInfo").hide();
        $("#accInfo .selectorAdd").hide();
        $("#accInfo .btnCollapse").remove();
        $("#barAddEdit").hide();
        $('#accInfo form').show(200);
        $("#accInfo #barAddCfm").show(200);
        $("#accInfo #barAddEdit").hide();
        $("#accInfo .btnOpen").remove();
        $('#cbxDefAdd').prop('checked', true);
        panelFocus($('#accInfo'));
        stateInfo = false;
    }
    if (str == 'add-new') {
        $('#accInfo form').hide(300);
        $('#accInfo .loadedInfo').show(300, function () {
            if (_trans) {
                $('html, body').animate({scrollTop: $("#accPay").offset().top - 20}, 1000);
            }
        });
        $('#accInfo .ui-ribbon').append(createBtn('edit', 'accInfo'));
        panelNormal($('#accInfo'));
        stateInfo = true;
    }
    if (str == 'has-address') {
        panelFocus($('#accInfo'));
        $("#accInfo .loadedInfo").hide();
        $("#accInfo #barAddCfm").hide();
        $("#accInfo .btnOpen").remove();
        $('#accInfo .ui-ribbon').append(createBtn('cancel', 'accInfo'));
        $('#accInfo form').show(200);
        $("#accInfo .selectorAdd").show(200);
        $("#barAddEdit").show();
        panelFocus($('#accInfo'));
        loadFirstAdd($(".barAdd").children('li.selected').find('label'));
        stateInfo = false;
    }
    if (str == 'has-address2') {
        panelFocus($('#accInfo'));
        $("#accInfo .loadedInfo").hide();
        $("#accInfo .ui-ribbon-btn").remove();
        $('#accInfo form').show(200);
        $("#accInfo .selectorAdd").show(200);
        $("#barAddEdit").hide();
        $("#barAddCfm").show();
        panelFocus($('#accInfo'));
        $(".barAdd").children('li:first').trigger('click');
        stateInfo = false;
    }
    if (str == 'selected-add') {
        $("#accInfo form").hide(300);
        $("#accInfo .loadedInfo").show(300, function () {
            if (_trans) {
                $('html, body').animate({scrollTop: $("#accPay").offset().top - 20}, 1000);
            }
        });
        $("#accInfo .btnCollapse").remove();
        $('#accInfo .ui-ribbon').append(createBtn('renew', 'accInfo'));
        panelNormal($('#accInfo'));
        stateInfo = true;
    }
    if (str == 'first-pay') {
        $("#accPay .payNotic").show();
        $("#accPay .btnCollapse").remove();
        $('#accPay .loadedInfo').hide();
        $('#accPay form').hide();
        panelNormal($('#accPay'));
        statePay = false;
    }
    if (str == 'has-pay') {
        $("#accPay .btnCollapse").remove();
        $("#accPay .btnOpen").remove();
        $('#accPay form').hide(300);
        $('#accPay .loadedInfo').show(300, function () {
            if (_trans == true) {
                $('html, body').animate({scrollTop: $("#blockCheckout").offset().top - 20}, 1000);
            }
        });
        $('#accPay .ui-ribbon').append(createBtn('editpay', 'accPay'));
        $('#accPay .payNotic').hide();
        panelNormal($('#accPay'));
        statePay = true;
    }
    if (str == 'choice-pay') {
        $("#accPay .btnOpen").remove();
        $('#accPay .loadedInfo').hide(300);
        $(".payNotic").hide();
        $('#accPay form').fadeIn(300);
        if (!$('#accPay .ui-ribbon .ui-ribbon-btn').length > 0)
            $('#accPay .ui-ribbon').append(createBtn('cancelpay', 'accPay'));
        $('#btnPayCcl').show();
        panelFocus($('#accPay'));
        statePay = false;
    }
    if (str == 'choice-pay2') {
        $("#accPay .btnOpen").remove();
        $('#accPay .loadedInfo').hide(300);
        $('#accPay form').fadeIn(300);
        $("#accPay .ui-ribbon-btn").remove();
        $("#btnPayCcl").hide();
        $(".payNotic").hide();
        panelFocus($('#accPay'));
        statePay = false;
    }
}
function resetAreaDl() {
    $("#rcpt-area").multilist('toggleDisabled', false);
    $("#rcpt-city").multilist('toggleDisabled', true);
    $("#rcpt-zone").multilist('toggleDisabled', true);
}
function openAreaDl() {
    $("#rcpt-city").multilist('toggleDisabled', false);
    $("#rcpt-zone").multilist('toggleDisabled', false);
}
$("#rcpt-area").bind('droplistOpen', function () {
    $("#rcpt-city").trigger('listup');
    $("#rcpt-zone").trigger('listup');
});
$("#rcpt-city").bind('droplistOpen', function () {
    $("#rcpt-area").trigger('listup');
    $("#rcpt-zone").trigger('listup');
});
$("#rcpt-zone").bind('droplistOpen', function () {
    $("#rcpt-city").trigger('listup');
    $("#rcpt-area").trigger('listup');
});
function payLoadedInfo() {
    $var = $('#accPay .loadedInfo');
    var str = "";
    if ($("#access-delivery:checked").length > 0)
        str = $("input[name='access-money']:checked").val();
    $var.find("[data-type='data-pay']").text($("input[name='access-pay']:checked").val() + '    ' + str);
    $var.find("[data-type='data-trans']").html('普通快递&nbsp;送货上门');
    $var.find("[data-type='data-time']").text($('#widgetTimeChoice').multilist('val'));
}
function InitPCAElscs(sltP, sltC, sltA) {
    var siP = 0, siC = 0;
    for (var i = 0, il = AreaA.length; i < il; i++) {
        sltP.find('.ui-droplist-list').append('<li><a idx="' + (i + 1) + '" data-code="' + AreaA[i].code + '" href="#">' + AreaA[i].name + '</a></li>');
    }
    sltP.trigger('bindListClick');
    sltP.change(function (e, data) {
        siP = sltP.multilist('idx');
        var areaIdx = sltP.multilist('dcode');
        var cA = AreaA[siP - 1].cA;
        if (siP) {
            sltC.multilist('setIndex', -1);
            sltA.multilist('setIndex', -1);
            sltC.find('.ui-droplist-list').empty();
            sltA.find('.ui-droplist-list').empty();
            var cA = AreaA[siP - 1].cA;
            for (var i = 0, il = cA.length; i < il; i++) {
                sltC.find('.ui-droplist-list').append('<li><a idx="' + (i + 1) + '" data-code="' + cA[i].code + '" href="#">' + cA[i].name + '</a></li>');
            }
            sltC.trigger('bindListClick');
            if (areaIdx == '01' || areaIdx == '02' || areaIdx == '03' || areaIdx == '04') {
                $("#rcpt-city").multilist('setIndex', 1);
                $("#rcpt-city").multilist('toggleDisabled', true);
                $("#rcpt-zone").multilist('toggleDisabled', false);
                sltC.trigger('change', {flag: '1'});
                if (data.flag == '0') {
                    $("#rcpt-zone").trigger('listdown');
                }
            } else {
                $("#rcpt-city").multilist('toggleDisabled', false);
                if (data.flag == '0') {
                    $("#rcpt-city").trigger('listdown');
                }
            }
        }
        addressAhead();
    });
    sltC.change(function (e, data) {
        siP = sltP.multilist('idx');
        if (!siP) {
            return;
        }
        siC = sltC.multilist('idx');
        if (siC) {
            sltA.find('.ui-droplist-list').empty();
            var cA = AreaA[siP - 1].cA[siC - 1].cA;
            for (var i = 0, il = cA.length; i < il; i++) {
                sltA.find('.ui-droplist-list').append('<li><a idx="' + (i + 1) + '" href="#">' + cA[i].name + '</a></li>');
            }
            sltA.trigger('bindListClick');
            $("#rcpt-zone").multilist('toggleDisabled', false);
            if (data.flag == '0') {
                $("#rcpt-zone").trigger('listdown');
            }
        }
        addressAhead();
        sltA.multilist('setIndex', -1);
    });
    sltA.change(function () {
        addressAhead();
        $("#rcpt-zone").next(".blockError").empty();
    });
}
$('.droplist').multilist();
InitPCAElscs($('#rcpt-area'), $('#rcpt-city'), $('#rcpt-zone'));
$("#rcpt-zone").multilist('toggleDisabled', true);
$("#rcpt-city").multilist('toggleDisabled', true);
tipsGroup();
$('#widgetTimeChoice').multilist('setIndex', 3);
function openCouponWidget() {
    if (cpChoiceState) {
        $("#btnCoupon").text('(-)使用优惠券/返利红包/促销优惠码');
        $("#couponChoice .widgetCoupon").show(function () {
            matchHeight($(this).height() + 4);
            cpChoiceState = true;
        });
    } else {
        $("#btnCoupon").text('(+)使用优惠券/返利红包/促销优惠码');
    }
    ;
    if (ivChoiceState) {
        $("#btnOverage").text('(-)账户余额');
        $("#overageChoice .widgetCoupon").show(function () {
            matchHeight($(this).height() + 4);
            ivChoiceState = true;
        });
    } else {
        $("#btnOverage").text('(+)账户余额');
    }
    ;
    if (ovChoiceState) {
        if ($("#invoice_amount").text() == "0.00") {
            forbInvoice();
        } else {
            $("#invoice").prop("checked", true);
        }
        $("#invoiceChoice .widgetCoupon").show(function () {
            matchHeight($(this).height() + 4);
            ovChoiceState = true;
        });
    }
    ;
}
$(function () {
    d = setInterval(function () {
        var height_l = $(".panelLeft").height();
        $(".panelRight").css("height", (height_l - 2) + "px");
        var height_r = $(".panelRight").height() - 2;
        if (height_l == height_r) {
            clearInterval(d);
        }
    }, 500);
});