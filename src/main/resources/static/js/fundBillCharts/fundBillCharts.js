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
        showBottom: false,
        change: function(value) {
            $("#fundBillChartsDateYear").val(value);
            if ($(".layui-laydate").length) {
                $(".layui-laydate").remove();
            }
            loadCharts(value, 1);
        },
        max: common.getNowFormatDate()
    });

    // 初始化日期选择框
    laydate.render({
        elem: '#fundBillChartsDateMonth',
        value: new Date(),
        type: 'month',
        format: 'yyyy-MM',
        showBottom: false,
        change: function(value) {
            $("#fundBillChartsDateMonth").val(value);
            if ($(".layui-laydate").length) {
                $(".layui-laydate").remove();
            }
            loadCharts(value, 2);
        },
        max: common.getNowFormatDate()
    });

    // 初始化日期选择框
    laydate.render({
        elem: '#totalFundBillChartsDateYear',
        value: new Date(),
        type: 'year',
        format: 'yyyy',
        showBottom: false,
        change: function(value) {
            $("#totalFundBillChartsDateYear").val(value);
            if ($(".layui-laydate").length) {
                $(".layui-laydate").remove();
            }
            loadTotalCharts(value, 1);
        },
        max: common.getNowFormatDate()
    });

    // 初始化日期选择框
    laydate.render({
        elem: '#totalFundBillChartsDateMonth',
        value: new Date(),
        type: 'month',
        format: 'yyyy-MM',
        showBottom: false,
        change: function(value) {
            $("#totalFundBillChartsDateMonth").val(value);
            if ($(".layui-laydate").length) {
                $(".layui-laydate").remove();
            }
            loadTotalCharts(value, 2);
        },
        max: common.getNowFormatDate()
    });

    /**
     * 加载图表
     */
    function loadCharts(date, dateType) {
        http.get('/fundBillCharts/getChartsData?date=' + date + '&dateType=' + dateType, {}, function(data) {
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

    /**
     * 加载全部累计
     */
    function loadTotalCharts(date, dataType) {
        var myLine = echarts.init(document.getElementById('totalFundBillChartsLine'));
        myLine.setOption({
            tooltip: {
                trigger: "axis"
            },
            color: ["#029688"],
            legend: {
                data: ["累计收益"]
            },
            xAxis: [
                {
                    type: "category",
                    boundaryGap: false,
                    data: []
                }
            ],
            yAxis: {
                type: "value",
                axisLabel: {
                    formatter: "{value} 元",
                    rotate: 30,
                    showMinLabel: false
                }
            },
            series: [
                {
                    name: "累计收益",
                    type: "line",
                    smooth: true,
                    itemStyle: {
                        normal: {
                            areaStyle: {
                                color: "#83bcb5"
                            },
                            lineStyle: {
                                color: "#029688"
                            }
                        }
                    },
                    data: []
                }
            ]
        });
        myLine.resize();

        myLine.showLoading();
        http.get('/fundBillCharts/getTotalChartsData?date=' + date + '&dateType=' + dataType, {}, function(data) {
            myLine.hideLoading();
            // 填入数据
            myLine.setOption({
                xAxis: [
                    {
                        data: data.data.categories
                    }
                ],
                series: [
                    {
                        data: data.data.data
                    }
                ]
            });
            myLine.resize();
        });
    }

    loadCharts(common.getCurrentDate(2), 2);
    loadTotalCharts(common.getCurrentDate(2), 2);

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
                showBottom: false,
                change: function(value) {
                    $("#fundBillChartsDateYear").val(value);
                    if ($(".layui-laydate").length) {
                        $(".layui-laydate").remove();
                    }
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
                showBottom: false,
                change: function(value) {
                    $("#fundBillChartsDateMonth").val(value);
                    if ($(".layui-laydate").length) {
                        $(".layui-laydate").remove();
                    }
                    loadCharts(value, 2);
                },
                max: common.getNowFormatDate()
            });
        }
    });

    /**
     * 绑定数据类型切换事件
     */
    $("#totalFundBillChartsDataType button").on('click', function() {
        $("#totalFundBillChartsDataType button").toggleClass("active");
        var dateType = $("#totalFundBillChartsDataType .active").attr("data-type");
        if (dateType == 1) {
            $("#totalFundBillChartsDateMonth").addClass('hide');
            $("#totalFundBillChartsDateYear").removeClass('hide');
            loadTotalCharts(common.getCurrentDate(1), dateType);

            // 初始化日期选择框
            laydate.render({
                elem: '#totalFundBillChartsDateYear',
                value: common.getCurrentDate(1),
                type: 'year',
                format: 'yyyy',
                showBottom: false,
                change: function(value) {
                    $("#totalFundBillChartsDateYear").val(value);
                    if ($(".layui-laydate").length) {
                        $(".layui-laydate").remove();
                    }
                    loadTotalCharts(value, 1);
                },
                max: common.getNowFormatDate()
            });
        } else if (dateType == 2) {
            $("#totalFundBillChartsDateYear").addClass('hide');
            $("#totalFundBillChartsDateMonth").removeClass('hide');
            loadTotalCharts(common.getCurrentDate(2), dateType);

            // 初始化日期选择框
            laydate.render({
                elem: '#totalFundBillChartsDateMonth',
                value: common.getCurrentDate(2),
                type: 'month',
                format: 'yyyy-MM',
                showBottom: false,
                change: function(value) {
                    $("#totalFundBillChartsDateMonth").val(value);
                    if ($(".layui-laydate").length) {
                        $(".layui-laydate").remove();
                    }
                    loadTotalCharts(value, 2);
                },
                max: common.getNowFormatDate()
            });
        }
    });

});
