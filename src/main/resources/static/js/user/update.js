//@sourceURL=/js/user/update.js
requirejs.config({
    baseUrl: '/',
    paths: {
        jquery: 'js/lib/jquery.min',
        layui: 'layui/layui',
        http: 'js/common/http'
    },
    shim: {
        layui: {exports: "layui"}
    }
});

requirejs(['jquery', 'layui', 'http'], function($, layui, http) {

    var form = layui.form;

    // 监听提交
    form.on('submit(user-update-submit)', function(data) {
        var that = $(this);
        if (!that.hasClass('layui-btn-disabled')) {
            that.addClass('layui-btn-disabled'); // 禁用提交按钮
            http.post('/user/doUpdate', data.field, function() {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
                window.location.href = "/user/index";
            }, function() {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
            });
        }
        return false;
    });

});
