//@sourceURL=/js/user/add.js

layui.use(['form', 'layedit', 'laydate'], function() {
    var form = layui.form
        , layer = layui.layer
        , layedit = layui.layedit
        , laydate = layui.laydate;

    // 自定义验证规则
    form.verify({
        pass: [/(.+){6,16}$/, '密码必须6到16位'],
        confirmPass: function() {
            var pass = $("input[name=password]").val();
            var confirmPass = $("input[name=confirmPassword]").val();
            if (pass !== confirmPass) {
                return "两次输入的密码不一致，请重新输入！";
            }
        },
        userName: function(value) {
            // 判断用户名是否已经存在
            var isExist = true;
            $.ajax({
                url: '/user/isExist',
                type: 'POST',
                data: {
                    userName: value
                },
                async: false,
                dataType: 'json',
                success: function(result) {
                    if (result.code !== 200) {
                        layer.open({
                            title: '系统提示',
                            content: result.data,
                            btnAlign: 'c'
                        });
                        isExist = true;
                    }
                    isExist = result.data;
                }
            });
            if (isExist) {
                return '该用户已存在！';
            }
        }
    });

    // 监听提交
    form.on('submit(user-add-submit)', function(data) {
        $.ajax({
            url: '/user/doAdd',
            type: 'POST',
            data: data.field,
            dataType: 'json',
            success: function(result) {
                if (result.code !== 200) {
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
        return false;
    });
});
