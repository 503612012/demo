//@sourceURL=/js/workhour/add.js
var worksiteName = '';
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
    var layer = layui.layer;
    var laydate = layui.laydate;

    form.on('select(worksiteSelect)', function(data) {
        worksiteName = data.elem[data.elem.selectedIndex].text;
    });

    // 自定义验证规则
    // noinspection JSUnusedGlobalSymbols
    form.verify({
        isInputedEmployee: function() {
            // 判断该员工该日期该工地是否有录入过
            var isInputed = false;
            var employeeId = $("#employeeSelect").val();
            var workDate = $("#workDate").val();
            var worksiteId = $("#worksiteSelect").val();
            if (employeeId == null || employeeId == '' || employeeId == undefined) {
                return '请选择员工！';
            }
            if (workDate == null || workDate == '' || workDate == undefined) {
                return;
            }
            if (worksiteId == null || worksiteId == '' || worksiteId == undefined) {
                return;
            }
            $.ajax({
                url: '/workhour/isInputed',
                type: 'POST',
                async: false,
                data: {
                    "employeeId": employeeId,
                    "workDate": workDate,
                    "worksiteId": worksiteId
                },
                dataType: 'json',
                success: function(result) {
                    if (result.code != 200) {
                        layer.open({
                            title: '系统提示',
                            anim: 6,
                            content: result.data,
                            btnAlign: 'c'
                        });
                        return;
                    }
                    isInputed = result.data;
                }
            });
            if (isInputed) {
                return '该员工在[' + worksiteName + ']工地[' + workDate + ']的工时已经录入过，请删除后重新录入！';
            }
        }
    });

    form.on('select(employeeSelect)', function(data) {
        if (hasPermission(hasShowEmployeeMoneyStatusPermission)) {
            var employeeId = data.value;
            if (employeeId == '') {
                $("#hourSalary").val('');
                return;
            }
            http.post('/employee/getHourSalaryByEmployeeId', {"employeeId": employeeId}, function(data) {
                $("#hourSalary").val(data);
            });
        }
    });

    // 初始化日期选择框
    laydate.render({
        elem: '#workDate',
        format: 'yyyy-MM-dd',
        max: common.getNowFormatDate()
    });

    // 监听提交
    form.on('submit(workhour-add-submit)', function(data) {
        var that = $(this);
        if (!that.hasClass('layui-btn-disabled')) {
            that.addClass('layui-btn-disabled'); // 禁用提交按钮
            http.post('/workhour/doAdd', data.field, function() {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
                window.parent.mainFrm.location.href = "/workhour/index";
            }, function() {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
            });
        }
        return false;
    });

    // 初始化员工下拉框
    http.get('/employee/getAll', {}, function(data) {
        common.initEmployeeSelectBox(data, $("#employeeSelect"), form);
    });

    // 初始化下工地拉框
    http.get('/worksite/getAll', {}, function(data) {
        common.initWorksiteSelectBox(data, $("#worksiteSelect"), form);
    });

});

function checkNum() {
    var workhour = $("#workhour").val();
    if (workhour == null || workhour == '' || workhour == undefined) {
        return;
    }
    if (parseInt(workhour) > 24) {
        $("#workhour").val('24');
    }
}