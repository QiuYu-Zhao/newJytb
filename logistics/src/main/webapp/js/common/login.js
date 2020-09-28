jQuery(document).ready(function() {
    App.init();
    //登陆背景图片
    $.backstretch([
            "/image/bg/banner.jpg"

        ], {
            fade: 100,
            duration: 8000
        }
    );
});

function changeImg() {
    var imgSrc = $("#imgObj");
    var src = imgSrc.attr("src");
    imgSrc.attr("src", chgUrl(src));
}
function chgUrl(url) {
    var timestamp = (new Date()).valueOf();
    url = url.substring(0, 17);
    if ((url.indexOf("&") >= 0)) {
        url = url + "×tamp=" + timestamp;
    } else {
        url = url + "?timestamp=" + timestamp;
    }
    return url;
}

function submitCheck() {
    var username = $("#username").val();
    var password = $("#password").val();
    var code = $("#code").val();

    if(username == ""){
        layer.tips('请输入用户名', '#username', {
            tips: [1, '#e85555'],
            anim : 6,
            time: 2000
        });
        return;
    }
    if(password == ""){
        layer.tips('请输入密码', '#password', {
            tips: [1, '#e85555'],
            anim : 6,
            time: 2000
        });
        return;
    }
    $("#loginForm").submit();
};
$(document).keypress(function(e) {
    // 回车键事件
    if(e.which == 13) {
        submitCheck();
    }
});