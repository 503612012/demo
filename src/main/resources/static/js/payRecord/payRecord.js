//@sourceURL=/js/payRecord/payRecord.js
requirejs.config({
    baseUrl: '/',
    paths: {
        jquery: 'easyui/jquery.min',
        layui: 'layui/layui.all',
        common: 'js/common/common'
    },
    shim: {
        "layui": {exports: "layui"}
    }
});

requirejs(['jquery', 'layui', 'common'], function($, layui, common) {

    var table = layui.table;

    /**
     * 重新加载表格
     */
    var reload = function() {
        var employeeName = $('#employeeName');
        var worksiteName = $('#worksiteName');
        var workDate = $('#workDate');
        // 执行重载
        table.reload('payRecordReload', {
            page: {
                curr: 1 // 重新从第 1 页开始
            }
            , where: {
                employeeName: employeeName.val(),
                worksiteName: worksiteName.val(),
                workDate: workDate.val()
            }
        });
    };

    table.render({
        elem: '#payRecord-list'
        , url: '/payRecord/getByPage/'
        , id: 'payRecordReload'
        , even: true
        , cols: [[
            {type: 'numbers'}
            , {field: 'employeeName', title: '员工名称'}
            , {field: 'payDate', title: '发薪日期'}
            , {field: 'totalHour', title: '总工时'}
            , {
                field: 'totalMoney', title: '总薪资', templet: function(d) {
                    return '<span class="totalMoney" data-value="' + d.totalMoney + '" style="cursor: pointer;">***</span>';
                }
            }
            , {field: 'payerName', title: '发薪人'}
            , {
                field: 'remark', title: '备注', templet: function(d) {
                    if (d.remark == '' || d.remark == null) {
                        return '无';
                    } else {
                        return d.remark;
                    }
                }
            }
        ]]
        , page: true
        , done: function(res) {
            if (hasPermission(hasShowSalaryPayRecordTotalMoneyPermission)) {
                $('#layui-table-page1').css("display", "flex");
                var totalSalary = 0;
                for (var i = 0; i < res.data.length; i++) {
                    totalSalary += res.data[i].totalMoney;
                }
                var html = "<div style='margin-left: 20px; font-weight: bold; color: red;'>本页发薪总额为：<span style='cursor: pointer;' data-value='" + totalSalary + "元' class='totalSalary'>***</span></div>";
                $('#layui-table-page1').append(html);
            }
        }
    });

    /**
     * 显示/隐藏总金额
     */
    $("body").on("click", "span.totalSalary", function() {
        common.showOrHide($(this), hasShowSalaryPayRecordTotalMoneyPermission);
    });

    /**
     * 显示/隐藏金额
     */
    $("body").on("click", "span.totalMoney", function() {
        common.showOrHide($(this), hasShowSalaryPayRecordMoneyPermission);
    });

    /**
     * 查询按钮点击事件绑定
     */
    $('.payRecordTable .payRecord-search').on('click', function() {
        reload();
    });

    /**
     * 重置按钮点击事件绑定
     */
    $('.payRecordTable .payRecord-reset').on('click', function() {
        $('#employeeName').val('');
        $('#worksiteName').val('');
        $('#workDate').val('');
        reload();
    });

    common.cacheMousedown();

});
