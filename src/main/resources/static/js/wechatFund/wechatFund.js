//@sourceURL=/js/wechatFund/wechatFund.js
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
    var form = layui.form;

    if ($(window).width() < 750) {
        $(".countDateLabel").hide();
    }

    // 初始化日期选择框
    laydate.render({
        elem: '#wechatFundDateMonth',
        value: new Date(),
        type: 'month',
        format: 'yyyy-MM',
        showBottom: false,
        change: function(value) {
            $("#wechatFundDateMonth").val(value);
            if ($(".layui-laydate").length) {
                $(".layui-laydate").remove();
            }
            loadCharts(value);
        },
        max: common.getNowFormatDate()
    });

    // 初始化日期选择框
    laydate.render({
        elem: '#totalWechatFundDateMonth',
        value: new Date(),
        type: 'month',
        format: 'yyyy-MM',
        showBottom: false,
        change: function(value) {
            $("#totalWechatFundDateMonth").val(value);
            if ($(".layui-laydate").length) {
                $(".layui-laydate").remove();
            }
            loadTotalCharts(value);
        },
        max: common.getNowFormatDate()
    });

    $("#wechat-fund-add-btn").on('click', function() {
        // 初始化日期选择框
        laydate.render({
            elem: '#dataDate',
            value: new Date(),
            showBottom: false,
            ready: function(value) {
                common.disabledDate(value, '7,1')
            },
            change: function(value) {
                common.disabledDate(value, '7,1')
            },
            trigger: 'click',
            format: 'yyyy-MM-dd'
        });
        $('#money').val('');

        layer.open({
            title: '录入收益',
            id: 'input-wechat-fund',
            type: 1,
            content: $('#input-webchat-fund-box'),
            area: [$(window).width() <= 750 ? '80%' : '400px', '280px'],
            resize: false,
            cancel: function() {
                $(document).off("click");
            }
        });
    });

    var reload = function() {
        loadCharts($('#wechatFundDateMonth').val());
        loadTotalCharts($('#totalWechatFundDateMonth').val());
    };

    // 监听提交
    form.on('submit(input-wechat-fund-submit)', function(data) {
        common.commitForm($(this), layer, '/wechatFund/doAdd', data.field, reload);
        return false;
    });

    /**
     * 加载图表
     */
    function loadCharts(date) {
        var myLine = echarts.init(document.getElementById('wechatFundLine'));
        myLine.setOption({
            tooltip: {
                trigger: "axis"
            },
            color: ["#029688"],
            legend: {
                data: ["当日收益"]
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
                    name: "当日收益",
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
        http.get('/wechatFund/getChartsData?date=' + date, {}, function(data) {
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
                        data: data.data
                    }
                ]
            });
            myLine.resize();
        });
    }

    /**
     * 加载图表
     */
    function loadTotalCharts(date) {
        var myLine = echarts.init(document.getElementById('totalWechatFundLine'));
        myLine.setOption({
            tooltip: {
                trigger: "axis"
            },
            color: ["#1f13dc"],
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
        http.get('/wechatFund/getTotalChartsData?date=' + date, {}, function(data) {
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
                        data: data.data
                    }
                ]
            });
            myLine.resize();
        });
    }

    loadCharts(common.getCurrentDate(2));
    loadTotalCharts(common.getCurrentDate(2));

});
