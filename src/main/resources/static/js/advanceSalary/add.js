//@sourceURL=/js/advanceSalary/add.js
requirejs.config({
    baseUrl: '/',
    paths: {
        jquery: 'easyui/jquery.min',
        layui: 'layui/layui.all',
        http: 'js/common/http',
        common: 'js/common/common'
    },
    shim: {
        "layui": {exports: "layui"}
    }
});

requirejs(['jquery', 'layui', 'http', 'common'], function($, layui, http, common) {

    var form = layui.form;
    var laydate = layui.laydate;

    // 监听提交
    form.on('submit(advanceSalary-add-submit)', function(data) {
        var that = $(this);
        if (!that.hasClass('layui-btn-disabled')) {
            that.addClass('layui-btn-disabled'); // 禁用提交按钮
            http.post('/advanceSalary/doAdd', data.field, function() {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
                window.parent.mainFrm.location.href = "../../../../java/com/oven/core/advanceSalary/index";
            }, function() {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
            });
        }
        return false;
    });

    // 初始化日期选择框
    laydate.render({
        elem: '#advanceTime',
        format: 'yyyy-MM-dd',
        max: common.getNowFormatDate()
    });

    http.get('/employee/getAll', {}, function(data) {
        common.initEmployeeSelectBox(data, $("#employeeSelect"), form);
    });

});