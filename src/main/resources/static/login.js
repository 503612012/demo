//@sourceURL=/login.js
$(function() {

    // 跳出iframe
    if (window !== top) {
        top.location.href = location.href;
    }

    /**
     * 绑定记住我勾选事件
     */
    $("#login-checkbox-icon").on("click", function() {
        $("#login-chechbox-div").toggleClass("layui-form-checked");
    });

    /**
     * 登录操作
     */
    function doLogin() {
        var userName = $("input[name=userName]").val();
        var password = $("input[name=password]").val();
        var vercode = $("input[name=vercode]").val();
        var rememberMe = false;
        if ($(".layui-form-checked").length === 1) {
            rememberMe = true;
        }
        if (userName == null || userName === '' || password == null || password === '' || vercode == null || vercode === '') {
            return;
        }

        var key = $("input[name=key]").val();
        key = CryptoJS.enc.Utf8.parse(key);
        var srcs = CryptoJS.enc.Utf8.parse(password);
        var encrypted = CryptoJS.AES.encrypt(srcs, key, {mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7});
        password = encrypted.toString();

        $.ajax({
            url: "/doLogin",
            type: "POST",
            data: {
                "userName": userName,
                "pwd": password,
                "inputCode": vercode,
                "rememberMe": rememberMe
            },
            dataType: "json",
            success: function(result) {
                if (result.code !== 200) {
                    $("input[name=vercode]").val("");
                    layer.open({
                        title: '系统提示',
                        content: result.data,
                        btnAlign: 'c'
                    });
                    return;
                }
                window.location.href = "/";
            }
        });
    }

    document.onkeydown = function(event) {
        var e = event || window.event;
        if (e && e.keyCode === 13) { //回车键的键值为13
            $("#login-submit").click();
        }
    };

    /**
     * 绑定登录按钮点击事件
     */
    $("#login-submit").on("click", function() {
        doLogin();
    });

});

function changeCode() {
    document.getElementById("LAY-user-get-vercode").src = "/getGifCode?" + Math.random();
}