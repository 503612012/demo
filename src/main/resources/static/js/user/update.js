//@sourceURL=/js/user/update.js
$(function () {

    layui.use(['form', 'layedit', 'laydate'], function() {
        var form = layui.form
            , layer = layui.layer
            , layedit = layui.layedit
            , laydate = layui.laydate;

        // 自定义验证规则
        form.verify({
            pass: [/(.+){6,16}$/, '密码必须6到16位'],
            confirmPass: function(value) {
                var pass = $("input[name=password]").val();
                var confirmPass = $("input[name=confirmPassword]").val();
                if (pass != confirmPass) {
                    return "两次输入的密码不一致，请重新输入！";
                }
            }
        });

        // 监听提交
        form.on('submit(user-update-submit)', function(data) {
            $.ajax({
                url: '/user/doUpdate',
                type: 'POST',
                data: data.field,
                dataType: 'json',
                async: false,
                success: function(result) {
                    if (result.code != 200) {
                        layer.open({
                            title: '系统提示',
                            content: result.data,
                            btnAlign: 'c'
                        });
                        return;
                    }
                    window.parent.mainFrm.location.href= "/user/index";
                }
            });
        });
    });

});