//@sourceURL=/main.js
layui.use(['element'], function() {
    var $ = layui.jquery;
    var element = layui.element;

    $(".layui-carousel-ind ul li").hover(function() {
        var that = $(this);
        if (!that.hasClass("layui-this")) {
            that.parent().find("li").removeClass("layui-this");
            that.addClass("layui-this");

            that.parent().parent().parent().find("ul").toggleClass("layui-this");
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

    /**
     * 加载首页数据
     */
    function loadData() {
        $.ajax({
            url: '/getMainPageData',
            type: 'GET',
            data: {},
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
                $("#totalPay").html(number_format(data.totalPay == null ? 0 : data.totalPay, 2, '.', ','));
                $("#totalEmployee").html(number_format(data.totalEmployee, 0, '.', ','));
                $("#totalWorkhour").html(number_format(data.totalWorkhour == null ? 0 : data.totalWorkhour, 0, '.', ','));
                $("#totalWorksite").html(number_format(data.totalWorksite, 0, '.', ','));
            }
        });
    }

    /**
     * 加载图表
     */
    function loadCharts() {
        // 基于准备好的dom，初始化echarts实例
        var myLine = echarts.init(document.getElementById('lineId'));

        // 显示标题，图例和空的坐标轴
        myLine.setOption({
            title: {
                text: '薪资TOP5'
            },
            tooltip: {},
            legend: {
                data: ['总薪资/元']
            },
            xAxis: {
                data: []
            },
            yAxis: {},
            series: [{
                name: '总薪资/元',
                label: {
                    normal: {
                        show: true,
                        position: 'top'
                    }
                },
                type: 'bar',
                areaStyle: {},
                smooth: true,
                data: []
            }]
        });
        myLine.resize();

        myLine.showLoading();
        $.get('/getSalaryTopFive').done(function(data) {
            myLine.hideLoading();
            // 填入数据
            myLine.setOption({
                xAxis: {
                    data: data.employeeNames
                },
                series: [{
                    // 根据名字对应到相应的系列
                    name: '总薪资/元',
                    data: data.data
                }]
            });
            myLine.resize();
        });
    }

    /**
     * 获取占比信息
     */
    function loadProportion() {
        $.ajax({
            url: '/getProportionData',
            type: 'GET',
            data: {},
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
                var salaryProportion = data.salaryProportion + "%";
                var workhourProportion = data.workhourProportion + "%";
                element.progress("salaryProportion", salaryProportion);
                element.progress("workhourProportion", workhourProportion);
                $("#salaryProportion").attr("lay-percent", ((parseFloat(data.salaryProportion).toFixed(2)) + "%"));
                $("#workhourProportion").attr("lay-percent", ((parseFloat(data.workhourProportion).toFixed(2)) + "%"));
                element.init();
            }
        });
    }

    loadData(); // 加载首页数据
    loadCharts(); // 加载图表
    loadProportion(); // 加载占比信息

});