//@sourceURL=/js/fundBillCharts/fundBillCharts.js
requirejs.config({
    baseUrl: '/',
    paths: {
        echarts: 'js/lib/echarts.min',
        jquery: 'easyui/jquery.min',
        common: 'js/common/common',
        layui: 'layui/layui.all',
        http: 'js/common/http'
    },
    shim: {
        "layui": {exports: "layui"},
        "echarts": {exports: "echarts"}
    }
});

requirejs(['jquery', 'echarts', 'layui', 'http', 'common'], function($, echarts, layui, http, common) {

    var laydate = layui.laydate;
    var myLine = echarts.init(document.getElementById('fundBillChartsLine'));

    // 获取累计收益
    http.get('/fundBillCharts/getTotal', {}, function(data) {
        $('#totalProfit').html('累计收益：' + '<span style="color: red;">' + common.numberFormat(data == null ? 0 : data, 2, '.', ',') + '</span>');
    });

    if ($(window).width() < 750) {
        $(".countDateLabel").hide();
    }

    // 初始化日期选择框
    laydate.render({
        elem: '#fundBillChartsDateYear',
        value: new Date(),
        type: 'year',
        format: 'yyyy',
        done: function(value) {
            var employeeId = $("#employeeSelect").val();
            loadCharts(value, 1, employeeId);
        },
        max: common.getNowFormatDate()
    });

    // 初始化日期选择框
    laydate.render({
        elem: '#fundBillChartsDateMonth',
        value: new Date(),
        type: 'month',
        format: 'yyyy-MM',
        done: function(value) {
            var employeeId = $("#employeeSelect").val();
            loadCharts(value, 2, employeeId);
        },
        max: common.getNowFormatDate()
    });

    /**
     * 加载图表
     */
    function loadCharts(date, dateType) {
        $.get('/fundBillCharts/getChartsData?date=' + date + '&dateType=' + dateType).done(function(result) {
            if (result.code != 200) {
                layer.open({
                    title: '系统提示',
                    anim: 6,
                    content: result.data,
                    btnAlign: 'c'
                });
                return;
            }
            var data = result.data;
            myLine.dispose();
            myLine = echarts.init(document.getElementById('fundBillChartsLine'));
            myLine.setOption({
                legend: {},
                tooltip: {
                    trigger: 'axis'
                },
                dataset: {
                    source: data.source
                },
                xAxis: {
                    type: 'category'
                },
                yAxis: {},
                series: data.series
            });
            myLine.resize();
        });
    }

    loadCharts(common.getCurrentDate(2), 2, '');

    /**
     * 绑定数据类型切换事件
     */
    $("#fundBillChartsDataType button").on('click', function() {
        $("#fundBillChartsDataType button").toggleClass("active");
        var dateType = $("#fundBillChartsDataType .active").attr("data-type");
        if (dateType == 1) {
            $("#fundBillChartsDateMonth").addClass('hide');
            $("#fundBillChartsDateYear").removeClass('hide');
            loadCharts(common.getCurrentDate(1), dateType);

            // 初始化日期选择框
            laydate.render({
                elem: '#fundBillChartsDateYear',
                value: common.getCurrentDate(1),
                type: 'year',
                format: 'yyyy',
                done: function(value) {
                    loadCharts(value, 1);
                },
                max: common.getNowFormatDate()
            });
        } else if (dateType == 2) {
            $("#fundBillChartsDateYear").addClass('hide');
            $("#fundBillChartsDateMonth").removeClass('hide');
            loadCharts(common.getCurrentDate(2), dateType);

            // 初始化日期选择框
            laydate.render({
                elem: '#fundBillChartsDateMonth',
                value: common.getCurrentDate(2),
                type: 'month',
                format: 'yyyy-MM',
                done: function(value) {
                    loadCharts(value, 2);
                },
                max: common.getNowFormatDate()
            });
        }
    });

});
