//@sourceURL=/main.js
$(function() {

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
        var n = !isFinite(+number) ? 0 : +number,
            prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
            sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
            dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
            s = '',
            toFixedFix = function(n, prec) {
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
                    $("input[name=vercode]").val("");
                    layer.open({
                        title: '系统提示',
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
     * 加载图标
     */
    function loadCharts() {
        // 基于准备好的dom，初始化echarts实例
        var myLine = echarts.init(document.getElementById('lineId'));

        // 显示标题，图例和空的坐标轴
        myLine.setOption({
            title: {
                text: '异步数据加载动态刷新示例'
            },
            tooltip: {},
            legend: {
                data: ['销量']
            },
            xAxis: {
                boundaryGap: false,
                data: []
            },
            yAxis: {},
            series: [{
                name: '销量',
                type: 'line',
                areaStyle: {},
                smooth: true,
                data: []
            }]
        });

        // 异步加载数据
        // myChart.showLoading();
        setInterval(function() {
            $.get('/tools/echarts/getData').done(function(data) {
                // myChart.hideLoading();
                // 填入数据
                myLine.setOption({
                    xAxis: {
                        data: data.categories
                    },
                    series: [{
                        // 根据名字对应到相应的系列
                        name: '销量',
                        data: data.data
                    }]
                });
            });
        }, 1000);
    }

    loadData(); // 加载首页数据
    loadCharts(); // 加载图标

});