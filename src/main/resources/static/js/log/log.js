//@sourceURL=/js/log/log.js

layui.use('table', function() {

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
            }
            , where: {
                title: titleReload.val(),
                content: contentReload.val(),
                operatorId: logSearchSelect.val()
            }
        });
    };

    table.render({
        elem: '#log-list'
        , url: '/log/getByPage/'
        , toolbar: '#logListToolBar'
        , id: 'logReload'
        , even: true
        , title: '日志数据表'
        , cols: [[
            {type: 'numbers'}
            , {field: 'title', title: '标题', sort: true}
            , {field: 'content', title: '内容'}
            , {field: 'operatorName', title: '操作人', sort: true}
            , {field: 'operatorTime', title: '操作时间', sort: true}
            , {field: 'operatorIp', title: '操作IP', sort: true}
        ]]
        , page: true
    });

    var $ = layui.$;

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
    $.ajax({
        url: '/user/getAll',
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
            var html = '<option value="">请选择操作人</option>';
            for (var i=0; i<data.length; i++) {
                html += '<option value="' + data[i].id + '">' + data[i].nickName + '</option>';
            }
            $("#logSearchSelect").html(html);
            form.render("select");
        }
    });

});
