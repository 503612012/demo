//@sourceURL=/js/sysdic/index.js
requirejs.config({
    baseUrl: '/',
    paths: {
        jquery: 'js/lib/jquery.min',
        layui: 'layui/layui',
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
        var searchReload = $('#searchReload');
        // 执行重载
        table.reload('sysdicReload', {
            page: {
                curr: 1 // 重新从第 1 页开始
            },
            where: {
                search: searchReload.val(),
                searchColumn: ['_key', '_desc'],
            }
        });
    };

    table.render({
        elem: '#sysdic-list',
        url: '/sysdic/getByPage',
        method: 'POST',
        id: 'sysdicReload',
        even: true,
        contentType: 'application/json',
        request: {
            pageName: 'pageNum',
            limitName: 'pageSize'
        },
        cols: [[
            {field: 'id', width: 80, title: '序号', sort: true, align: 'center'},
            {field: 'key', title: '键', sort: true},
            {field: 'value', title: '值'},
            {
                field: 'desc', title: '描述', templet: function(d) {
                    if (d.desc == null || d.desc == '' || d.desc == undefined) {
                        return '-';
                    }
                    return d.desc;
                }
            },
            {
                field: 'status', title: '状态', width: 120, align: 'center', templet: function(d) {
                    if (d.status == 0) {
                        return '<div class="layui-disabled layui-unselect layui-form-checkbox layui-form-checked sysdic-status" data-id="' + d.id + '" data-status="' + d.status + '" lay-skin="tag"><div>正常</div><i class="layui-icon layui-icon-ok"></i></div>';
                    } else if (d.status == 1) {
                        return '<div class="layui-disabled layui-unselect layui-form-checkbox sysdic-status" data-id="' + d.id + '" data-status="' + d.status + '" lay-skin="tag"><div>停用</div><i class="layui-icon layui-icon-ok"></i></div>';
                    }
                }
            },
            {fixed: 'right', title: '操作', align: 'center', toolbar: '#sysdicListBar'}
        ]],
        page: true
    });

    /**
     * 更改状态
     */
    var updateSysDicStatus = function(id, status) {
        var params = {
            id: id,
            status: status
        };
        http.post('/sysdic/updateStatus', params, function() {
            reload();
        });
    };

    /**
     * 绑定状态更改点击事件
     */
    $("body").on("click", ".sysdic-status", function() {
        // if (hasPermission(hasChangeSysDicStatusPermission)) {
        //     var id = $(this).attr("data-id");
        //     var status = $(this).attr("data-status");
        //     if (status == 0) {
        //         status = 1;
        //     } else if (status == 1) {
        //         status = 0;
        //     }
        //     if (status == 1) { // 禁用
        //         layer.confirm('确认停用该配置吗？', {anim: 6}, function(index) {
        //             updateSysDicStatus(id, 1);
        //             layer.close(index);
        //         });
        //     }
        //     if (status == 0) { // 取消禁用
        //         layer.confirm('确认启用该配置吗？', {anim: 6}, function(index) {
        //             updateSysDicStatus(id, 0);
        //             layer.close(index);
        //         });
        //     }
        // }
    });

    /**
     * 查询按钮点击事件绑定
     */
    $('.sysdicTable .sysdic-search').on('click', function() {
        reload();
    });

    /**
     * 重置按钮点击事件绑定
     */
    $('.sysdicTable .sysdic-reset').on('click', function() {
        $('#searchReload').val('');
        reload();
    });

    var openDialog = function(title) {
        layer.open({
            title: title,
            id: "sysdicDialog",
            type: 1,
            offset: '20px',
            content: $('#sysdicTips'),
            area: [$(window).width() <= 750 ? '60%' : '480px', 'auto'],
            resize: false,
            end: function() {
                $("#sysdicTips").css("display", 'none');
            }
        });
    };

    form.on('submit(sysdic-submit)', function(data) {
        var url;
        if (data.field.id != null && data.field.id != '' && data.field.id != undefined) { // 修改
            url = '/sysdic/update';
        } else { // 添加
            url = '/sysdic/save';
        }
        common.commitForm($(this), layer, url, data.field, reload);
    });

    // 监听工具条
    table.on('tool(sysdic-list)', function(obj) {
        var data = obj.data;
        if (obj.event == 'del') {
            layer.confirm('真的删除此条记录么？', {anim: 6}, function(index) {
                http.post('/sysdic/delete', {id: data.id}, function() {
                    layer.close(index);
                    reload();
                });
            });
        } else if (obj.event == 'edit') {
            http.get('/sysdic/getById', {id: data.id}, function(result) {
                form.val("sysdic-form", {
                    "id": result.id,
                    "key": result.key,
                    "value": result.value,
                    "desc": result.desc
                });
                form.render();
                openDialog('修改数据字典');
            });
        }
    });

    // 头工具栏事件
    $('#sysdic-add-btn').on('click', function() {
        $('#sysdicForm')[0].reset();
        layui.form.render();
        openDialog('添加数据字典');
    });

    $('#sysdic-reload-btn').on('click', function() {
        layer.confirm('确认要重载数据字典么？', {anim: 6}, function(index) {
            layer.close(index);
            http.get('/sysdic/reload', {}, function() {
                layer.msg('重载完成');
            });
        });
    });

    common.cacheMousedown();

});
