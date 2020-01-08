//@sourceURL=/js/workhourReport/workhourReport.js
requirejs.config({
    baseUrl: '/',
    paths: {
        jquery: 'easyui/jquery.min',
        layui: 'layui/layui.all',
        http: 'js/common/http',
        common: 'js/common/common',
        echarts: 'js/lib/echarts.min'
    },
    shim: {
        "layui": {exports: "layui"},
        "echarts": {exports: "echarts"}
    }
});

requirejs(['jquery', 'layui', 'echarts', 'http', 'common'], function($, layui, echarts, http, common) {

    var laydate = layui.laydate;
    var form = layui.form;

    if ($(window).width() < 750) {
        $(".countDateLabel").hide();
    }

    // 初始化日期选择框
    laydate.render({
        elem: '#workhourReportDateMonth',
        value: common.getCurrentDate(1),
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
        elem: '#workhourReportDateDay',
        value: common.getCurrentDate(2),
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
    function loadCharts(date, dateType, employeeId) {
        // 基于准备好的dom，初始化echarts实例
        var myLine = echarts.init(document.getElementById('workhourReportLine'));

        // 显示标题，图例和空的坐标轴
        myLine.setOption({
            tooltip: {
                trigger: "axis",
                axisPointer: {
                    label: {
                        formatter: "总工时"
                    }
                }
            },
            legend: {
                data: ["本期总工时", "上期总工时"]
            },
            xAxis: [
                {
                    type: "category",
                    boundaryGap: false,
                    data: []
                }
            ],
            yAxis: {
                axisLabel: {
                    formatter: "{value} "
                }
            },
            series: [
                {
                    name: "本期总工时",
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
                    name: "上期总工时",
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
                }
            ]
        });
        myLine.resize();

        myLine.showLoading();
        $.get('/workhourReport/getWorkhourReportData?employeeId=' + employeeId + '&date=' + date + '&dateType=' + dateType).done(function(data) {
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
                        data: data.workhours
                    },
                    {
                        data: data.preWorkhours
                    }
                ]
            });
            myLine.resize();
        });
    }

    loadCharts(common.getCurrentDate(1), 1, '');

    /**
     * 绑定数据类型切换事件
     */
    $("#workhourReportDataType button").on('click', function() {
        $("#workhourReportDataType button").toggleClass("active");
        var dateType = $("#workhourReportDataType .active").attr("data-type");
        var employeeId = $("#employeeSelect").val();
        if (dateType == 1) {
            $("#workhourReportDateDay").addClass('hide');
            $("#workhourReportDateMonth").removeClass('hide');
            loadCharts(common.getCurrentDate(1), dateType, employeeId);

            // 初始化日期选择框
            laydate.render({
                elem: '#workhourReportDateMonth',
                value: common.getCurrentDate(1),
                type: 'year',
                format: 'yyyy',
                done: function(value) {
                    var employeeId = $("#employeeSelect").val();
                    loadCharts(value, 1, employeeId);
                },
                max: common.getNowFormatDate()
            });
        } else if (dateType == 2) {
            $("#workhourReportDateMonth").addClass('hide');
            $("#workhourReportDateDay").removeClass('hide');
            loadCharts(common.getCurrentDate(2), dateType, employeeId);

            // 初始化日期选择框
            laydate.render({
                elem: '#workhourReportDateDay',
                value: common.getCurrentDate(2),
                type: 'month',
                format: 'yyyy-MM',
                done: function(value) {
                    var employeeId = $("#employeeSelect").val();
                    loadCharts(value, 2, employeeId);
                },
                max: common.getNowFormatDate()
            });
        }
    });

    $("#employeeSelect").on('change', function() {
        var employeeId = $(this).val();
        var date;
        var dateType = $("#workhourReportDataType .active").attr("data-type");
        if (dateType == 1) {
            date = $("#workhourReportDateMonth").val();
        } else if (dateType == 2) {
            date = $("#workhourReportDateDay").val();
        }
        loadCharts(date, dateType, employeeId)
    });

    // 初始化员工下拉框
    http.get('/employee/getAll', {}, function(data) {
        common.initEmployeeSelectBox(data, $("#employeeSelect"), form);
    });

});
