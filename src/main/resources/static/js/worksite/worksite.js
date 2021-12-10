//@sourceURL=/js/worksite/worksite.js
requirejs.config({
    baseUrl: '/',
    paths: {
        jquery: 'js/lib/jquery.min',
        layui: 'layui/layui.all',
        http: 'js/common/http',
        common: 'js/common/common'
    },
    shim: {
        layui: {exports: "layui"}
    }
});

requirejs(['jquery', 'layui', 'http', 'common'], function($, layui, http, common) {

    var form = layui.form;
    var table = layui.table;

    /**
     * 重新加载表格
     */
    var reload = function() {
        var worksiteNameReload = $('#worksiteNameReload');
        // 执行重载
        table.reload('worksiteReload', {
            page: {
                curr: 1 // 重新从第 1 页开始
            },
            where: {
                name: worksiteNameReload.val()
            }
        });
    };

    table.render({
        elem: '#worksite-list',
        url: '/worksite/getByPage/',
        toolbar: '#worksiteListToolBar',
        id: 'worksiteReload',
        even: true,
        cols: [[
            {type: 'numbers'},
            {field: 'name', title: '工地名称', sort: true},
            {
                field: 'desc', title: '工地描述', templet: function(d) {
                    if (d.desc == '' || d.desc == null) {
                        return '-';
                    } else {
                        return d.desc;
                    }
                }
            },
            {field: 'createTime', title: '创建时间'},
            {field: 'createName', title: '创建人'},
            {
                field: 'status', title: '状态', templet: function(d) {
                    if (d.status == 1) {
                        return '<div><div class="layui-unselect layui-form-checkbox layui-form-checked worksite-status" data-id="' + d.id + '" data-status="' + d.status + '"><span>锁定</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    } else if (d.status == 0) {
                        return '<div><div class="layui-unselect layui-form-checkbox worksite-status" data-id="' + d.id + '" data-status="' + d.status + '"><span>正常</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    }
                }
            },
            {title: '操作', width: 120, align: 'center', toolbar: '#worksiteListBar'}
        ]],
        page: true
    });

    /**
     * 查询按钮点击事件绑定
     */
    $('.worksiteTable .worksite-search').on('click', function() {
        reload();
    });

    /**
     * 重置按钮点击事件绑定
     */
    $('.worksiteTable .worksite-reset').on('click', function() {
        $('#worksiteNameReload').val('');
        reload();
    });

    /**
     * 更改工地状态
     */
    var updateWorksiteStatus = function(worksiteId, status) {
        var params = {
            'worksiteId': worksiteId,
            'status': status
        };
        http.post('/worksite/updateStatus', params, function() {
            reload();
        });
    };

    /**
     * 绑定工地状态更改点击事件
     */
    $("body").on("click", ".worksite-status", function() {
        if (hasPermission(hasChangeWorksiteStatusPermission)) {
            var id = $(this).attr("data-id");
            var status = $(this).attr("data-status");
            if (status == 0) {
                status = 1;
            } else if (status == 1) {
                status = 0;
            }
            if (status == 1) { // 锁定
                layer.confirm('确认锁定该工地吗？', {anim: 6}, function(index) {
                    updateWorksiteStatus(id, 1);
                    layer.close(index);
                });
            }
            if (status == 0) { // 取消锁定
                layer.confirm('确认取消锁定该工地吗？', {anim: 6}, function(index) {
                    updateWorksiteStatus(id, 0);
                    layer.close(index);
                });
            }
        }
    });

    var openDialog = function(title) {
        layer.open({
            title: title,
            id: "worksiteDialog",
            type: 1,
            offset: '20px',
            content: $('#worksiteTips'),
            area: [$(window).width() <= 750 ? '60%' : '500px', '300px'],
            resize: false,
            end: function() {
                $("#worksiteTips").css("display", 'none');
            }
        });
    };

    form.on('submit(worksite-submit)', function(data) {
        var url;
        if (data.field.id != null && data.field.id != '' && data.field.id != undefined) { // 修改
            url = '/worksite/update';
        } else { // 添加
            url = '/worksite/add';
        }
        common.commitForm($(this), layer, url, data.field, reload);
    });

    // 监听工具条
    table.on('tool(worksite-list)', function(obj) {
        var data = obj.data;
        if (obj.event == 'del') {
            layer.confirm('真的删除此条记录么？', {anim: 6}, function(index) {
                http.post('/worksite/delete', {id: data.id}, function() {
                    layer.close(index);
                    reload();
                });
            });
        } else if (obj.event == 'edit') {
            http.get('/worksite/getById', {id: data.id}, function(result) {
                $('#worksiteId').val(result.id);
                $('#name').val(result.name);
                $('#desc').val(result.desc);
                openDialog('修改工地');
            });
        }
    });

    // 头工具栏事件
    table.on('toolbar(worksite-list)', function(obj) {
        if (obj.event == 'worksite-add-btn') {
            if (obj.event == 'worksite-add-btn') {
                $('#worksiteId').val('');
                $('#name').val('');
                $('#desc').val('');
                openDialog('添加工地');
            }
        }
    });

    common.cacheMousedown();

});
