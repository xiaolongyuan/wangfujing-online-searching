var memberHome = memberHome || 'http://member.wangfujing.com/home';
var searchLocation = searchLocation || 'http://search.wangfujing.com';
var logoutPageUrl = logoutPageUrl || 'http://member.wangfujing.com/ssoLogout2.do';
var loginPageUrl = loginPageUrl || 'http://member.wangfujing.com/ssoLogin2.do';
var registerPageUrl = registerPageUrl || 'http://member.wangfujing.com/ssoRegister2.do';
var $loginName2 = $('#loginName2');
var loginInfo = null;
$loginName2.hide();
var validateLoginInfo = function () {
    $.ajax({
        url: searchLocation + '/member/validate.jsonp',
        type: 'POST',
        dataType: "jsonp",
        jsonp: "callback",
        success: function (data) {
            /** @namespace data.login */
            if (data.login) {
                /** @namespace data.loginInfo.username */
                loginInfo = data.loginInfo;
                var userName = loginInfo.username;
                $('#loginName').html('Hi,<a href="' + memberHome + '" target="_blank">' + userName + '</a>,欢迎来到王府井!');
                $('#btnLogon').hide();
                $('#btnLogout').show();
                $('#btnReg').hide();
                $loginName2.html('Hi,<a href="' + memberHome + '" target="_blank">' + userName + '</a>,欢迎来到王府井!');
                $loginName2.show();
                $('#btnLogon2').hide();
            }
        }
    });
};
$(document).ready(validateLoginInfo);
$(function() {
    var $btnLogout = $('#btnLogout');
    $btnLogout.attr('href', logoutPageUrl);
    $btnLogout.attr('target', '_blank');
    var $btnLogon = $('#btnLogon');
    $btnLogon.attr('href', loginPageUrl);
    $btnLogon.attr('target', '_blank');
    var $btnReg = $('#btnReg').find('a');
    $btnReg.attr('href', registerPageUrl);
    $btnReg.attr('target', '_blank');
    var $btnLogon2 = $('#btnLogon2');
    $btnLogon2.attr('href', registerPageUrl);
    $btnLogon2.attr('target', '_blank');
});