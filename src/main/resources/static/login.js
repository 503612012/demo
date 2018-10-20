//@sourceURL=/js/login.js
$(function () {

    $("#login-checkbox-icon").on("click", function () {
        $("#login-chechbox-div").toggleClass("layui-form-checked");
    });

    $("#login-submit").on("click", function () {
        var userName = $("input[name=userName]").val();
        var password = $("input[name=password]").val();
        var vercode = $("input[name=vercode]").val();
        if (userName == null || userName == '' || password == null || password == '' || vercode == null || vercode == '') {
            return;
        }
        $.ajax({
            url: "/doLogin",
            type: "POST",
            data: {
                userName: userName,
                pwd: password
            },
            dataType: "json",
            success: function (result) {
                if (result.code != 200) {
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
    });

});