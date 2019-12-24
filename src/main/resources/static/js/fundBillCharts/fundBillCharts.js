//@sourceURL=/js/fundBillCharts/fundBillCharts.js

layui.use(['laydate', 'element', 'form'], function() {

    var laydate = layui.laydate;
    var form = layui.form;
    var myLine = echarts.init(document.getElementById('fundBillChartsLine'));

    // 获取累计收益
    $.ajax({
        url: '/fundBillCharts/getTotal',
        type: 'GET',
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
            $('#totalProfit').html('累计收益：' + '<span style="color: red;">' + number_format(data == null ? 0 : data, 2, '.', ',') + '</span>');
        }
    });

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
        max: getNowFormatDate()
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

    loadCharts(getCurrentDate(2), 2, '');

    /**
     * 绑定数据类型切换事件
     */
    $("#fundBillChartsDataType button").on('click', function() {
        $("#fundBillChartsDataType button").toggleClass("active");
        var dateType = $("#fundBillChartsDataType .active").attr("data-type");
        if (dateType == 1) {
            $("#fundBillChartsDateMonth").addClass('hide');
            $("#fundBillChartsDateYear").removeClass('hide');
            loadCharts(getCurrentDate(1), dateType);

            // 初始化日期选择框
            laydate.render({
                elem: '#fundBillChartsDateYear',
                value: getCurrentDate(1),
                type: 'year',
                format: 'yyyy',
                done: function(value) {
                    loadCharts(value, 1);
                },
                max: getNowFormatDate()
            });
        } else if (dateType == 2) {
            $("#fundBillChartsDateYear").addClass('hide');
            $("#fundBillChartsDateMonth").removeClass('hide');
            loadCharts(getCurrentDate(2), dateType);

            // 初始化日期选择框
            laydate.render({
                elem: '#fundBillChartsDateMonth',
                value: getCurrentDate(2),
                type: 'month',
                format: 'yyyy-MM',
                done: function(value) {
                    loadCharts(value, 2);
                },
                max: getNowFormatDate()
            });
        }
    });

});
