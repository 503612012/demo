//@sourceURL=/js/role/add.js
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
    form.on('submit(role-add-submit)', function(data) {
        var that = $(this);
        that.addClass('layui-btn-disabled'); // 禁用提交按钮
        http.post('/role/doAdd', data.field, function() {
            that.removeClass('layui-btn-disabled'); // 释放提交按钮
            window.parent.mainFrm.location.href = "/role/index";
        });
        return false;
    });

});
