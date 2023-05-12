//@sourceURL=/main.js
requirejs.config({
    baseUrl: '/',
    paths: {
        jquery: 'js/lib/jquery.min',
        layui: 'layui/layui',
        http: 'js/common/http',
        common: 'js/common/common',
        echarts: 'js/lib/echarts.min'
    },
    shim: {
        layui: {exports: "layui"},
        echarts: {exports: "echarts"}
    }
});

requirejs(['jquery', 'echarts', 'layui', 'http', 'common'], function($, echarts, layui, http, common) {

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
     * 加载首页数据
     */
    function loadData() {
        http.get('/getMainPageData', {}, function(data) {
            $("#totalPay").html(common.numberFormat(data.totalPay == null ? 0 : data.totalPay, 2, '.', ','));
            $("#totalEmployee").html(common.numberFormat(data.totalEmployee, 0, '.', ','));
            $("#totalWorkhour").html(common.numberFormat(data.totalWorkhour == null ? 0 : data.totalWorkhour, 0, '.', ','));
            $("#totalWorksite").html(common.numberFormat(data.totalWorksite, 0, '.', ','));
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
        http.get('/getProportionData', {}, function(data) {
            var salaryProportion = data.salaryProportion + "%";
            var workhourProportion = data.workhourProportion + "%";
            element.progress("salaryProportion", salaryProportion);
            element.progress("workhourProportion", workhourProportion);
            $("#salaryProportion").attr("lay-percent", ((parseFloat(data.salaryProportion).toFixed(2)) + "%"));
            $("#workhourProportion").attr("lay-percent", ((parseFloat(data.workhourProportion).toFixed(2)) + "%"));
            element.init();
        });
    }

    loadData(); // 加载首页数据
    loadCharts(); // 加载图表
    loadProportion(); // 加载占比信息

});