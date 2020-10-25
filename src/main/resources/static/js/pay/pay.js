//@sourceURL=/js/pay/add.js
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

    var table = layui.table;
    var form = layui.form;
    var layer = layui.layer;

    /**
     * 查询按钮点击事件绑定
     */
    $('.payTable .pay-search').on('click', function() {
        $('.pay-btn').addClass('hide');
        var employeeId = $('#employeeSelect').val();
        var worksiteId = $('#worksiteSelect').val();
        table.render({
            elem: '#pay-list',
            url: '/pay/getWorkhourData/',
            id: 'payReload',
            even: true,
            cols: [[
                {type: 'checkbox'},
                {field: 'employeeName', title: '员工名称'},
                {field: 'worksiteName', title: '工地名称'},
                {field: 'workDate', title: '工时日期'},
                {field: 'workhour', title: '录入工时'},
                {field: 'hourSalary', title: '当日时薪'},
                {field: 'createName', title: '录入人'},
                {
                    field: 'remark', title: '备注', templet: function(d) {
                        if (d.remark == '' || d.remark == null) {
                            return '无';
                        } else {
                            return d.remark;
                        }
                    }
                }
            ]],
            page: {
                layout: []
            },
            where: {
                employeeId: employeeId,
                worksiteId: worksiteId
            },
            done: function(res) {
                if (hasPermission(hasShowSalaryPayTotalMoneyPermission)) {
                    $('#layui-table-page1').css("display", "flex");
                    var totalSalary = 0;
                    for (var i = 0; i < res.data.length; i++) {
                        totalSalary += (res.data[i].workhour * res.data[i].hourSalary);
                    }
                    var html = "<div style='margin-left: 20px; font-weight: bold; color: red;'>本页薪资总额为：<span style='cursor: pointer;' data-value='" + totalSalary + "元' class='totalSalary'>***</span></div>";
                    $('#layui-table-page1').append(html);
                }
            }
        });
    });

    /**
     * 显示/隐藏薪资
     */
    $("body").on("click", "span.totalSalary", function() {
        common.showOrHide($(this), hasShowSalaryPayTotalMoneyPermission);
    });

    /**
     * 监听复选框选择事件
     */
    table.on('checkbox(pay-list)', function() {
        var selectedNum = $(".layui-form-checked").length;
        if (selectedNum <= 0) {
            $('.pay-btn').addClass('hide');
        } else {
            $('.pay-btn').removeClass('hide');
        }
    });

    /**
     * 重置按钮点击事件绑定
     */
    $('.payTable .pay-reset').on('click', function() {
        $('.pay-btn').addClass('hide');
        $('#employeeSelect').val('');
        $('#worksiteSelect').val('');
        loadSelectBox();
        reload();
    });

    /**
     * 重新加载表格
     */
    var reload = function() {
        var employeeId = $('#employeeSelect');
        var worksiteId = $('#worksiteSelect');
        // 执行重载
        table.reload('payReload', {
            where: {
                employeeId: employeeId.val(),
                worksiteId: worksiteId.val()
            }
        });
    };

    var doPay = function(params) {
        http.post('/pay/doPay', params, function() {
            layer.close(index);
            $('.pay-btn').addClass('hide');
            $("#payNoticeText").html('');
            $("#payRemarkBox").addClass("hide");
            $("#payRemarkBox").css("display", "none");
            reload();
        });
    };

    /**
     * 薪资发放按钮点击事件绑定
     */
    $('.payTable .pay-btn').on('click', function() {
        $('#payRemark').val('');
        var list = table.cache.payReload;
        var workhourIds = [];
        var totalWorkhour = 0;
        var totalMoney = 0;
        for (var i = 0; i < list.length; i++) {
            if (list[i].LAY_CHECKED) {
                workhourIds.push(list[i].id);
                var workhour = list[i].workhour;
                var hourSalary = list[i].hourSalary;
                totalWorkhour += workhour;
                totalMoney += (workhour * hourSalary);
            }
        }
        var advanceSalary;
        http.getAsync('/advanceSalary/getTotalAdvanceSalaryByEmployeeId', {"employeeId": list[0].employeeId}, function(result) {
            $("#payNoticeText").html('');
            $("#payRemarkBox").addClass("hide");
            $("#payRemarkBox").css("display", "none");
            advanceSalary = result;
        });
        var notice;
        if (advanceSalary) {
            notice = "本次给【<span style='color: red;'>" + list[0].employeeName + "</span>】" +
                "发放薪资共计【<span style='color: red;'>" + totalWorkhour + "</span>】工时，" +
                "合计【<span style='color: red;' class='auctualPayMoneySpan'>" + totalMoney + "</span>】元，" +
                "该员工曾预支薪资【<span style='color: red;'>" + advanceSalary + "</span>】元，" +
                "将从本次发薪中扣除，扣除后的总发薪金额为【<span style='color: red;'>" + (totalMoney - advanceSalary) + "</span>】元，核对无误后点击确定！";
        } else {
            notice = "本次给【<span style='color: red;'>" + list[0].employeeName + "</span>】发放薪资共计" +
                "【<span style='color: red;'>" + totalWorkhour + "</span>】工时，合计" +
                "【<span style='color: red;' class='auctualPayMoneySpan'>" + totalMoney + "</span>】元，核对无误后点击确定！";
        }
        $("#payNoticeText").html(notice);
        if ($(window).width() < 750) {
            $("#remarkBox").css("width", "80%");
        } else {
            $("#remarkBox").css("width", "400px");
        }
        if (hasPermission(hasChangeSalaryPayMoneyPermission)) {
            // noinspection JSUnusedGlobalSymbols
            layer.open({
                title: '系统提示！',
                skin: 'pay-confirm-box-class',
                type: 1,
                content: $('#payRemarkBox'),
                btn: ['修改金额', '确定', '取消'],
                area: [$(window).width() <= 750 ? '80%' : '500px', '360px'],
                resize: false,
                yes: function() {
                    var actualPayMoney = $(".auctualPayMoneySpan").html();
                    var actualPayMoneyWidth = $(".auctualPayMoneySpan").css('width');
                    var actualPayMoneyInput = "<input type='number' style='color: red; width: " + ((parseInt(actualPayMoneyWidth.replace('px', '')) + 13) + 'px') + ";' name='actualPayMoney' value='" + actualPayMoney + "'>";
                    $(".auctualPayMoneySpan").replaceWith(actualPayMoneyInput);
                },
                btn2: function() {
                    var isModifyPayMoney = false;
                    var actualPayMoney = $("input[name=actualPayMoney]").val();
                    var diffMoney = 0;
                    if (!(actualPayMoney == null || actualPayMoney == '')) { // 修改过金额
                        diffMoney = totalMoney - actualPayMoney;
                        totalMoney = actualPayMoney;
                        isModifyPayMoney = true;
                    }
                    var remark = $('#payRemark').val();
                    var worksiteId = $('#worksiteSelect').val();
                    if (isModifyPayMoney) {
                        if (remark == '' || remark == undefined) {
                            layer.open({
                                title: '系统提示',
                                anim: 6,
                                content: '修改过金额后必须填写备注信息！',
                                btnAlign: 'c'
                            });
                            return false;
                        }
                    }
                    var params = {
                        "workhourIds": workhourIds.join(','),
                        "employeeId": list[0].employeeId,
                        "totalMoney": totalMoney,
                        "totalHour": totalWorkhour,
                        "hasModifyMoney": isModifyPayMoney ? 1 : 0,
                        "changeMoney": diffMoney,
                        "worksiteId": worksiteId,
                        "remark": remark
                    };
                    doPay(params);
                },
                btn3: function() {
                    $("#payNoticeText").html('');
                    $("#payRemarkBox").addClass("hide");
                    $("#payRemarkBox").css("display", "none");
                },
                cancel: function() {
                    $("#payNoticeText").html('');
                    $("#payRemarkBox").addClass("hide");
                    $("#payRemarkBox").css("display", "none");
                }
            });
        } else {
            layer.open({
                title: '系统提示！',
                type: 1,
                content: $('#payRemarkBox'),
                btn: ['确定', '取消'],
                area: [$(window).width() <= 750 ? '60%' : '500px', '300px'],
                resize: false,
                yes: function() {
                    var remark = $('#payRemark').val();
                    var worksiteId = $('#worksiteSelect').val();
                    var params = {
                        "workhourIds": workhourIds.join(','),
                        "employeeId": list[0].employeeId,
                        "totalMoney": totalMoney,
                        "totalHour": totalWorkhour,
                        "worksiteId": worksiteId,
                        "remark": remark
                    };
                    doPay(params);
                },
                btn2: function() {
                    $("#payNoticeText").html('');
                    $("#payRemarkBox").addClass("hide");
                    $("#payRemarkBox").css("display", "none");
                },
                cancel: function() {
                    $("#payNoticeText").html('');
                    $("#payRemarkBox").addClass("hide");
                    $("#payRemarkBox").css("display", "none");
                }
            });
        }
    });

    function loadSelectBox() {
        http.get('/employee/getAll', {}, function(data) {
            common.initEmployeeSelectBox(data, $("#employeeSelect"), form);
        });

        http.get('/worksite/getAll', {}, function(data) {
            common.initWorksiteSelectBox(data, $('#worksiteSelect'), form);
        });
    }

    loadSelectBox();

});