//@sourceURL=/js/workhourReport/workhourReport.js

layui.use(['laydate', 'element', 'form'], function() {

    var laydate = layui.laydate;
    var form = layui.form;

    // 初始化日期选择框
    laydate.render({
        elem: '#workhourReportDateMonth',
        value: getCurrentDate(1),
        type: 'year',
        format: 'yyyy',
        done: function(value) {
            var employeeId = $("#employeeSelect").val();
            loadCharts(value, 1, employeeId);
        },
        max: getNowFormatDate()
    });

    // 初始化日期选择框
    laydate.render({
        elem: '#workhourReportDateDay',
        value: getCurrentDate(2),
        type: 'month',
        format: 'yyyy-MM',
        done: function(value) {
            var employeeId = $("#employeeSelect").val();
            loadCharts(value, 2, employeeId);
        },
        max: getNowFormatDate()
    });

    function getCurrentDate(type) {
        var date = new Date();
        if (type == 1) {
            return date.getFullYear();
        } else if (type == 2) {
            var seperator = "-";
            var month = date.getMonth() + 1;
            if (month >= 1 && month <= 9) {
                month = "0" + month;
            }
            return date.getFullYear() + seperator + month;
        }
    }

    function getNowFormatDate() {
        var date = new Date();
        var seperator1 = "-";
        var seperator2 = ":";
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        return date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
    }

    /**
     * 加载图表
     */
    function loadCharts(date, dateType, employeeId) {
        // 基于准备好的dom，初始化echarts实例
        var myLine = echarts.init(document.getElementById('workhourReportLine'));

        // 显示标题，图例和空的坐标轴
        myLine.setOption({
            "tooltip": {
                "trigger": "axis",
                "axisPointer": {
                    "label": {
                        "formatter": "总工时"
                    }
                }
            },
            "legend": {
                "data": ["本期总工时", "上期总工时"]
            },
            "xAxis": [
                {
                    "type": "category",
                    "boundaryGap": false,
                    "data": []
                }
            ],
            "yAxis": {
                "axisLabel": {
                    "formatter": "{value} "
                }
            },
            "series": [
                {
                    "name": "本期总工时",
                    "type": "line",
                    "smooth": true,
                    "itemStyle": {
                        "normal": {
                            "areaStyle": {
                                "color": "#83bcb5"
                            },
                            "lineStyle": {
                                "color": "#029688"
                            }
                        }
                    },
                    "data": []
                },
                {
                    "name": "上期总工时",
                    "type": "line",
                    "smooth": true,
                    "itemStyle": {
                        "normal": {
                            "areaStyle": {
                                "color": "#58b2c2"
                            },
                            "lineStyle": {
                                "color": "#0d9ac3"
                            }
                        }
                    },
                    "data": []
                }
            ]
        });

        myLine.showLoading();
        $.get('/workhourReport/getWorkhourReportData?employeeId=' + employeeId + '&date=' + date + '&dateType=' + dateType).done(function(data) {
            myLine.hideLoading();
            // 填入数据
            myLine.setOption({
                "xAxis": [
                    {
                        "data": data.categories
                    }
                ],
                "series": [
                    {
                        "data": data.workhours
                    },
                    {
                        "data": data.preWorkhours
                    }
                ]
            });
        });
    }

    loadCharts(getCurrentDate(1), 1, '');
    loadSelectBox();

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
            loadCharts(getCurrentDate(1), dateType, employeeId);

            // 初始化日期选择框
            laydate.render({
                elem: '#workhourReportDateMonth',
                value: getCurrentDate(1),
                type: 'year',
                format: 'yyyy',
                done: function(value) {
                    var employeeId = $("#employeeSelect").val();
                    loadCharts(value, 1, employeeId);
                },
                max: getNowFormatDate()
            });
        } else if (dateType == 2) {
            $("#workhourReportDateMonth").addClass('hide');
            $("#workhourReportDateDay").removeClass('hide');
            loadCharts(getCurrentDate(2), dateType, employeeId);

            // 初始化日期选择框
            laydate.render({
                elem: '#workhourReportDateDay',
                value: getCurrentDate(2),
                type: 'month',
                format: 'yyyy-MM',
                done: function(value) {
                    var employeeId = $("#employeeSelect").val();
                    loadCharts(value, 2, employeeId);
                },
                max: getNowFormatDate()
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

    function loadSelectBox() {
        // 初始化员工下拉框
        $.ajax({
            url: '/employee/getAll',
            type: 'GET',
            data: {},
            dataType: 'json',
            success: function(result) {
                if (result.code != 200) {
                    layer.open({
                        title: '系统提示',
                        content: result.data,
                        btnAlign: 'c'
                    });
                    return;
                }
                var data = result.data;
                var html = '<option value="">请选择员工</option>';
                for (var i = 0; i < data.length; i++) {
                    html += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
                }
                $("#employeeSelect").html(html);
                form.render("select");
            }
        });
    }

});
