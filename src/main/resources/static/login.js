//@sourceURL=/login.js
$(function() {

    // 跳出iframe
    if (window != top) {
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
        if ($(".layui-form-checked").length == 1) {
            rememberMe = true;
        }
        if (userName == null || userName == '' || password == null || password == '' || vercode == null || vercode == '') {
            return;
        }

        var encrypt = new JSEncrypt();
        encrypt.setPublicKey('-----BEGIN PUBLIC KEY-----\n' + $("input[name=key]").val() + '\n-----END PUBLIC KEY-----');
        var encrypted = encrypt.encrypt(password);
        var that = $(this);
        if (!that.hasClass('layui-btn-disabled')) {
            that.addClass('layui-btn-disabled'); // 禁用提交按钮
            $.ajax({
                url: "/doLogin",
                type: "POST",
                data: {
                    "userName": userName,
                    "pwd": encrypted,
                    "inputCode": vercode,
                    "rememberMe": rememberMe
                },
                dataType: "json",
                success: function(result) {
                    that.removeClass('layui-btn-disabled'); // 释放提交按钮
                    if (result.code != 200) {
                        $("input[name=vercode]").val("");
                        layer.open({
                            title: '系统提示',
                            anim: 6,
                            content: result.data,
                            btnAlign: 'c'
                        });
                        changeCode();
                        return;
                    }
                    window.location.href = "/";
                }
            });
        }
    }

    document.onkeydown = function(event) {
        var e = event || window.event;
        if (e && e.keyCode == 13) { //回车键的键值为13
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
    document.getElementById("user-get-vercode").src = "/getGifCode?" + Math.random();
}