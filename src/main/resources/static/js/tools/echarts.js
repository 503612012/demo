//@sourceURL=/js/tools/echarts.js

layui.use('table', function() {

    var $ = layui.$;

    // 基于准备好的dom，初始化echarts实例
    var myBar = echarts.init(document.getElementById('barId'));

    // 显示标题，图例和空的坐标轴
    myBar.setOption({
        title: {
            text: '异步数据加载动态刷新示例'
        },
        tooltip: {},
        legend: {
            data: ['销量']
        },
        xAxis: {
            data: []
        },
        yAxis: {},
        series: [{
            name: '销量',
            type: 'line',
            data: []
        }]
    });

    // 异步加载数据
    // myChart.showLoading();
    setInterval(function() {
        $.get('/tools/echarts/getData').done(function(data) {
            // myChart.hideLoading();
            // 填入数据
            myBar.setOption({
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

    // 基于准备好的dom，初始化echarts实例
    var myLing = echarts.init(document.getElementById('lineId'));

    // 显示标题，图例和空的坐标轴
    myLing.setOption({
        title: {
            text: '异步数据加载动态刷新示例'
        },
        tooltip: {},
        legend: {
            data: ['销量']
        },
        xAxis: {
            data: []
        },
        yAxis: {},
        series: [{
            name: '销量',
            type: 'bar',
            data: []
        }]
    });

    // 异步加载数据
    // myChart.showLoading();
    setInterval(function() {
        $.get('/tools/echarts/getData').done(function(data) {
            // myChart.hideLoading();
            // 填入数据
            myLing.setOption({
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

});
