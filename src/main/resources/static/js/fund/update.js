//@sourceURL=/js/fund/update.js
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

    // 监听提交
    form.on('submit(fund-update-submit)', function(data) {
        var that = $(this);
        if (!that.hasClass('layui-btn-disabled')) {
            that.addClass('layui-btn-disabled'); // 禁用提交按钮
            http.post('/fund/doUpdate', data.field, function() {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
                window.location.href = "/fund/index";
            }, function() {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
            });
        }
        return false;
    });

});
