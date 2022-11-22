//@sourceURL=/js/log/log.js
requirejs.config({
    baseUrl: '/',
    paths: {
        jquery: 'js/lib/jquery.min',
        layui: 'layui/layui.all',
        http: 'js/common/http'
    },
    shim: {
        layui: {exports: "layui"}
    }
});

requirejs(['jquery', 'layui', 'http'], function($, layui, http) {

    var table = layui.table;
    var form = layui.form;

    /**
     * 重新加载表格
     */
    var reload = function() {
        var titleReload = $('#titleReload');
        var contentReload = $('#contentReload');
        var logSearchSelect = $('#logSearchSelect');
        // 执行重载
        table.reload('logReload', {
            page: {
                curr: 1 // 重新从第 1 页开始
            },
            where: {
                title: titleReload.val(),
                content: contentReload.val(),
                operatorId: logSearchSelect.val()
            }
        });
    };

    table.render({
        elem: '#log-list',
        url: '/log/getByPage/',
        toolbar: '#logListToolBar',
        id: 'logReload',
        even: true,
        cols: [[
            {type: 'numbers'},
            {field: 'title', title: '标题', sort: true},
            {field: 'request', title: '入参'},
            {field: 'response', title: '出参'},
            {field: 'requestUri', title: '接口地址'},
            {field: 'requestMethod', width: 90, title: '请求方法'},
            {field: 'operatorName', title: '操作人', sort: true},
            {field: 'operatorTime', title: '操作时间', sort: true},
            {field: 'operatorIp', width: 127, title: '操作IP'}
        ]],
        page: true
    });

    /**
     * 查询按钮点击事件绑定
     */
    $('.logTable .log-search').on('click', function() {
        reload();
    });

    /**
     * 重置按钮点击事件绑定
     */
    $('.logTable .log-reset').on('click', function() {
        $('#titleReload').val('');
        $('#contentReload').val('');
        $('#logSearchSelect').val('');
        form.render("select");
        reload();
    });

    // 初始化下拉框
    http.get('/user/getAll', {}, function(data) {
        var html = '<option value="">请选择操作人</option>';
        for (var i = 0; i < data.length; i++) {
            html += '<option value="' + data[i].id + '">' + data[i].nickName + '</option>';
        }
        $("#logSearchSelect").html(html);
        form.render("select");
    });

});
