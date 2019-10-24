//@sourceURL=/js/salary/salary.js

layui.use(['laydate', 'element'], function() {

    var laydate = layui.laydate;
    var element = layui.element;

    // 初始化日期选择框
    laydate.render({
        elem: '#salaryDateMonth',
        value: getCurrentDate(1),
        type: 'year',
        format: 'yyyy',
        done: function(value) {
            loadCharts(value, 1);
            loadUpData(value, 1);
        },
        max: getNowFormatDate()
    });

    // 初始化日期选择框
    laydate.render({
        elem: '#salaryDateDay',
        value: getCurrentDate(2),
        type: 'month',
        format: 'yyyy-MM',
        done: function(value) {
            loadCharts(value, 2);
            loadUpData(value, 2);
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
    function loadCharts(date, dateType) {
        // 基于准备好的dom，初始化echarts实例
        var myLine = echarts.init(document.getElementById('salaryLine'));

        // 显示标题，图例和空的坐标轴
        myLine.setOption({
            "tooltip": {
                "trigger": "axis"
            },
            "color": ["#029688", "#0d9ac3", "#1f13dc"],
            "legend": {
                "data": ["录入薪资", "发放薪资", "预支薪资"]
            },
            "xAxis": [
                {
                    "type": "category",
                    "boundaryGap": false,
                    "data": []
                }
            ],
            "yAxis": {
                "type": "value",
                "axisLabel": {
                    "formatter": "{value} 元"
                }
            },
            "series": [
                {
                    "name": "录入薪资",
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
                    "name": "发放薪资",
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
                },
                {
                    "name": "预支薪资",
                    "type": "line",
                    "smooth": true,
                    "itemStyle": {
                        "normal": {
                            "areaStyle": {
                                "color": "#1f179e"
                            },
                            "lineStyle": {
                                "color": "#1f13dc"
                            }
                        }
                    },
                    "data": []
                }
            ]
        });

        myLine.showLoading();
        $.get('/salary/getSalaryData?date=' + date + '&dateType=' + dateType).done(function(data) {
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
                        "data": data.salaryIn
                    },
                    {
                        "data": data.salaryOut
                    },
                    {
                        "data": data.advanceSalary
                    }
                ]
            });
        });
    }

    /**
     * 加载同比增长数据
     */
    function loadUpData(date, dateType) {
        $.ajax({
            url: '/salary/getUpData',
            type: 'GET',
            data: {
                "date": date,
                "dateType": dateType
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
                var data = result.data;
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
                $("#inSalaryProgress").attr("lay-percent", ((number_format(data.inSalary == null ? 0 : data.inSalary, 2, '.', ',') + '%')));
                $("#outSalaryProgress").attr("lay-percent", ((number_format(data.outSalary == null ? 0 : data.outSalary, 2, '.', ',') + '%')));
                $("#advanceSalaryProgress").attr("lay-percent", ((number_format(data.advanceSalary == null ? 0 : data.advanceSalary, 2, '.', ',') + '%')));

                $("#inSalaryProgress span").html(number_format(data.inSalary == null ? 0 : data.inSalary, 2, '.', ',') + '%');
                $("#outSalaryProgress span").html(number_format(data.outSalary == null ? 0 : data.outSalary, 2, '.', ',') + '%');
                $("#advanceSalaryProgress span").html(number_format(data.advanceSalary == null ? 0 : data.advanceSalary, 2, '.', ',') + '%');

                if (parseFloat(data.inSalary) < 0) {
                    $("#inSalaryProgress").addClass('layui-bg-red');
                    element.progress("inSalaryProgress", -data.inSalary);
                    $("#inSalaryProgress").attr("lay-percent", ((number_format(-data.inSalary == null ? 0 : -data.inSalary, 2, '.', ',') + '%')));
                    $("#inSalaryProgress span").html(number_format(-data.inSalary == null ? 0 : -data.inSalary, 2, '.', ',') + '%');
                    $("#inSalary span").html($("#inSalary span").html().replace('增长', '下降'));
                }
                if (parseFloat(data.outSalary) < 0) {
                    $("#outSalaryProgress").addClass('layui-bg-red');
                    element.progress("outSalaryProgress", -data.outSalary);
                    $("#outSalaryProgress").attr("lay-percent", ((number_format(-data.outSalary == null ? 0 : -data.outSalary, 2, '.', ',') + '%')));
                    $("#outSalaryProgress span").html(number_format(-data.outSalary == null ? 0 : -data.outSalary, 2, '.', ',') + '%');
                    $("#outSalary span").html($("#outSalary span").html().replace('增长', '下降'));
                }
                if (parseFloat(data.advanceSalary) < 0) {
                    $("#advanceSalaryProgress").addClass('layui-bg-red');
                    element.progress("advanceSalaryProgress", -data.advanceSalary);
                    $("#advanceSalaryProgress").attr("lay-percent", ((number_format(-data.advanceSalary == null ? 0 : -data.advanceSalary, 2, '.', ',') + '%')));
                    $("#advanceSalaryProgress span").html(number_format(-data.advanceSalary == null ? 0 : -data.advanceSalary, 2, '.', ',') + '%');
                    $("#advanceSalary span").html($("#advanceSalary span").html().replace('增长', '下降'));
                }
                element.init();
            }
        });
    }

    /**
     * 参数说明：
     * number：要格式化的数字
     * decimals：保留几位小数
     * dec_point：小数点符号
     * thousands_sep：千分位符号
     */
    function number_format(number, decimals, dec_point, thousands_sep) {
        number = (number + '').replace(/[^0-9+-Ee.]/g, '');
        var n = !isFinite(+number) ? 0 : +number;
        var prec = !isFinite(+decimals) ? 0 : Math.abs(decimals);
        var sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep;
        var dec = (typeof dec_point === 'undefined') ? '.' : dec_point;
        var s;
        var toFixedFix = function(n, prec) {
            var k = Math.pow(10, prec);
            return '' + Math.ceil(n * k) / k;
        };

        s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
        var re = /(-?\d+)(\d{3})/;
        while (re.test(s[0])) {
            s[0] = s[0].replace(re, "$1" + sep + "$2");
        }

        if ((s[1] || '').length < prec) {
            s[1] = s[1] || '';
            s[1] += new Array(prec - s[1].length + 1).join('0');
        }
        return s.join(dec);
    }

    loadCharts(getCurrentDate(1), 1);
    loadUpData(getCurrentDate(1), 1);

    /**
     * 绑定数据类型切换事件
     */
    $("#salaryDataType button").on('click', function() {
        $("#salaryDataType button").toggleClass("active");
        var dateType = $("#salaryDataType .active").attr("data-type");
        if (dateType == 1) {
            $("#salaryDateDay").addClass('hide');
            $("#salaryDateMonth").removeClass('hide');
            loadCharts(getCurrentDate(1), dateType);
            loadUpData(getCurrentDate(1), dateType);

            // 初始化日期选择框
            laydate.render({
                elem: '#salaryDateMonth',
                value: getCurrentDate(1),
                type: 'year',
                format: 'yyyy',
                done: function(value) {
                    loadCharts(value, 1);
                    loadUpData(value, 1);
                },
                max: getNowFormatDate()
            });
        } else if (dateType == 2) {
            $("#salaryDateMonth").addClass('hide');
            $("#salaryDateDay").removeClass('hide');
            loadCharts(getCurrentDate(2), dateType);
            loadUpData(getCurrentDate(2), dateType);

            // 初始化日期选择框
            laydate.render({
                elem: '#salaryDateDay',
                value: getCurrentDate(2),
                type: 'month',
                format: 'yyyy-MM',
                done: function(value) {
                    loadCharts(value, 2);
                    loadUpData(value, 2);
                },
                max: getNowFormatDate()
            });
        }
    });

});
