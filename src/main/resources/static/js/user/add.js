//@sourceURL=/js/user/add.js
requirejs.config({
    baseUrl: '/',
    paths: {
        jquery: 'easyui/jquery.min',
        layui: 'layui/layui.all',
        http: 'js/common/http'
    },
    shim: {
        "layui": {exports: "layui"}
    }
});

requirejs(['jquery', 'layui', 'http'], function($, layui, http) {

    var form = layui.form;
    var layer = layui.layer;

    // 自定义验证规则
    form.verify({
        pass: [/(.+){6,16}$/, '密码必须6到16位'],
        confirmPass: function() {
            var pass = $("input[name=password]").val();
            var confirmPass = $("input[name=confirmPassword]").val();
            if (pass != confirmPass) {
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
                    if (result.code != 200) {
                        layer.open({
                            title: '系统提示',
                            anim: 6,
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
            if (value == null || value == '' || value == undefined) {
                return "用户名不能为空！";
            }
        }
    });

    // 监听提交
    form.on('submit(user-add-submit)', function(data) {
        var that = $(this);
        that.addClass('layui-btn-disabled'); // 禁用提交按钮
        http.post('/user/doAdd', data.field, function() {
            that.removeClass('layui-btn-disabled'); // 释放提交按钮
            window.parent.mainFrm.location.href = "/user/index";
        });
        return false;
    });

});
