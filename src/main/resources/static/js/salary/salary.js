//@sourceURL=/js/salary/salary.js
requirejs.config({
    baseUrl: '/',
    paths: {
        jquery: 'easyui/jquery.min',
        layui: 'layui/layui.all',
        echarts: 'js/lib/echarts.min',
        http: 'js/common/http',
        common: 'js/common/common'
    },
    shim: {
        "layui": {exports: "layui"},
        "echarts": {exports: "echarts"}
    }
});

requirejs(['jquery', 'layui', 'http', 'echarts', 'common'], function($, layui, http, echarts, common) {

    var laydate = layui.laydate;
    var element = layui.element;

    if ($(window).width() < 750) {
        $(".countDateLabel").hide();
    }

    // 初始化日期选择框
    laydate.render({
        elem: '#salaryDateMonth',
        value: common.getCurrentDate(1),
        type: 'year',
        format: 'yyyy',
        showBottom: false,
        change: function(value) {
            $("#salaryDateMonth").val(value);
            if ($(".layui-laydate").length) {
                $(".layui-laydate").remove();
            }
            loadCharts(value, 1);
            loadUpData(value, 1);
        },
        max: common.getNowFormatDate()
    });

    // 初始化日期选择框
    laydate.render({
        elem: '#salaryDateDay',
        value: common.getCurrentDate(2),
        type: 'month',
        format: 'yyyy-MM',
        showBottom: false,
        change: function(value) {
            $("#salaryDateDay").val(value);
            if ($(".layui-laydate").length) {
                $(".layui-laydate").remove();
            }
            loadCharts(value, 2);
            loadUpData(value, 2);
        },
        max: common.getNowFormatDate()
    });

    /**
     * 加载图表
     */
    function loadCharts(date, dateType) {
        // 基于准备好的dom，初始化echarts实例
        var myLine = echarts.init(document.getElementById('salaryLine'));

        // 显示标题，图例和空的坐标轴
        myLine.setOption({
            tooltip: {
                trigger: "axis"
            },
            color: ["#029688", "#0d9ac3", "#1f13dc"],
            legend: {
                data: ["录入薪资", "发放薪资", "预支薪资"]
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
                    name: "录入薪资",
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
                },
                {
                    name: "发放薪资",
                    type: "line",
                    smooth: true,
                    itemStyle: {
                        normal: {
                            areaStyle: {
                                color: "#58b2c2"
                            },
                            lineStyle: {
                                color: "#0d9ac3"
                            }
                        }
                    },
                    data: []
                },
                {
                    name: "预支薪资",
                    type: "line",
                    smooth: true,
                    itemStyle: {
                        normal: {
                            areaStyle: {
                                color: "#1f179e"
                            },
                            lineStyle: {
                                color: "#1f13dc"
                            }
                        }
                    },
                    data: []
                }
            ]
        });
        myLine.resize();

        myLine.showLoading();
        http.get('/salary/getSalaryData?date=' + date + '&dateType=' + dateType, {}, function(data) {
            myLine.hideLoading();
            // 填入数据
            myLine.setOption({
                xAxis: [
                    {
                        data: data.categories
                    }
                ],
                series: [
                    {
                        data: data.salaryIn
                    },
                    {
                        data: data.salaryOut
                    },
                    {
                        data: data.advanceSalary
                    }
                ]
            });
            myLine.resize();
        });
    }

    /**
     * 加载同比增长数据
     */
    function loadUpData(date, dateType) {
        var params = {
            "date": date,
            "dateType": dateType
        };
        http.get('/salary/getUpData', params, function(data) {
            if (dateType == 1) { // 按月查看
                $("#inSalary span").html("同比去年增长");
                $("#outSalary span").html("同比去年增长");
                $("#advanceSalary span").html("同去年增长");
            } else if (dateType == 2) { // 按天查看
                $("#inSalary span").html("同比上月增长");
                $("#outSalary span").html("同比上月增长");
                $("#advanceSalary span").html("同比上月增长");
            }

            element.progress("inSalaryProgress", data.inSalary);
            element.progress("outSalaryProgress", data.outSalary);
            element.progress("advanceSalaryProgress", data.advanceSalary);
            $("#inSalaryProgress").attr("lay-percent", ((common.numberFormat(data.inSalary == null ? 0 : data.inSalary, 2, '.', ',') + '%')));
            $("#outSalaryProgress").attr("lay-percent", ((common.numberFormat(data.outSalary == null ? 0 : data.outSalary, 2, '.', ',') + '%')));
            $("#advanceSalaryProgress").attr("lay-percent", ((common.numberFormat(data.advanceSalary == null ? 0 : data.advanceSalary, 2, '.', ',') + '%')));

            $("#inSalaryProgress span").html(common.numberFormat(data.inSalary == null ? 0 : data.inSalary, 2, '.', ',') + '%');
            $("#outSalaryProgress span").html(common.numberFormat(data.outSalary == null ? 0 : data.outSalary, 2, '.', ',') + '%');
            $("#advanceSalaryProgress span").html(common.numberFormat(data.advanceSalary == null ? 0 : data.advanceSalary, 2, '.', ',') + '%');

            if (parseFloat(data.inSalary) < 0) {
                $("#inSalaryProgress").addClass('layui-bg-red');
                element.progress("inSalaryProgress", -data.inSalary);
                $("#inSalaryProgress").attr("lay-percent", ((common.numberFormat(-data.inSalary == null ? 0 : -data.inSalary, 2, '.', ',') + '%')));
                $("#inSalaryProgress span").html(common.numberFormat(-data.inSalary == null ? 0 : -data.inSalary, 2, '.', ',') + '%');
                $("#inSalary span").html($("#inSalary span").html().replace('增长', '下降'));
            }
            if (parseFloat(data.outSalary) < 0) {
                $("#outSalaryProgress").addClass('layui-bg-red');
                element.progress("outSalaryProgress", -data.outSalary);
                $("#outSalaryProgress").attr("lay-percent", ((common.numberFormat(-data.outSalary == null ? 0 : -data.outSalary, 2, '.', ',') + '%')));
                $("#outSalaryProgress span").html(common.numberFormat(-data.outSalary == null ? 0 : -data.outSalary, 2, '.', ',') + '%');
                $("#outSalary span").html($("#outSalary span").html().replace('增长', '下降'));
            }
            if (parseFloat(data.advanceSalary) < 0) {
                $("#advanceSalaryProgress").addClass('layui-bg-red');
                element.progress("advanceSalaryProgress", -data.advanceSalary);
                $("#advanceSalaryProgress").attr("lay-percent", ((common.numberFormat(-data.advanceSalary == null ? 0 : -data.advanceSalary, 2, '.', ',') + '%')));
                $("#advanceSalaryProgress span").html(common.numberFormat(-data.advanceSalary == null ? 0 : -data.advanceSalary, 2, '.', ',') + '%');
                $("#advanceSalary span").html($("#advanceSalary span").html().replace('增长', '下降'));
            }
            element.init();
        });
    }

    loadCharts(common.getCurrentDate(1), 1);
    loadUpData(common.getCurrentDate(1), 1);

    /**
     * 绑定数据类型切换事件
     */
    $("#salaryDataType button").on('click', function() {
        $("#salaryDataType button").toggleClass("active");
        var dateType = $("#salaryDataType .active").attr("data-type");
        if (dateType == 1) {
            $("#salaryDateDay").addClass('hide');
            $("#salaryDateMonth").removeClass('hide');
            loadCharts(common.getCurrentDate(1), dateType);
            loadUpData(common.getCurrentDate(1), dateType);

            // 初始化日期选择框
            laydate.render({
                elem: '#salaryDateMonth',
                value: common.getCurrentDate(1),
                type: 'year',
                format: 'yyyy',
                showBottom: false,
                change: function(value) {
                    $("#salaryDateMonth").val(value);
                    if ($(".layui-laydate").length) {
                        $(".layui-laydate").remove();
                    }
                    loadCharts(value, 1);
                    loadUpData(value, 1);
                },
                max: common.getNowFormatDate()
            });
        } else if (dateType == 2) {
            $("#salaryDateMonth").addClass('hide');
            $("#salaryDateDay").removeClass('hide');
            loadCharts(common.getCurrentDate(2), dateType);
            loadUpData(common.getCurrentDate(2), dateType);

            // 初始化日期选择框
            laydate.render({
                elem: '#salaryDateDay',
                value: common.getCurrentDate(2),
                type: 'month',
                format: 'yyyy-MM',
                showBottom: false,
                change: function(value) {
                    $("#salaryDateDay").val(value);
                    if ($(".layui-laydate").length) {
                        $(".layui-laydate").remove();
                    }
                    loadCharts(value, 2);
                    loadUpData(value, 2);
                },
                max: common.getNowFormatDate()
            });
        }
    });

});
