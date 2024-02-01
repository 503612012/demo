//@sourceURL=/js/role/role.js
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
        var roleNameReload = $('#roleNameReload');
        // 执行重载
        table.reload('roleReload', {
            page: {
                curr: 1 // 重新从第 1 页开始
            },
            where: {
                roleName: roleNameReload.val()
            }
        });
    };

    table.render({
        elem: '#role-list',
        url: '/role/getByPage/',
        id: 'roleReload',
        even: true,
        cols: [[
            {type: 'numbers'},
            {field: 'roleName', title: '角色名', sort: true},
            {field: 'createTime', title: '创建时间'},
            {field: 'createName', title: '创建人'},
            {field: 'lastModifyTime', title: '最后修改时间'},
            {field: 'lastModifyName', title: '最后修改人'},
            {
                field: 'status', title: '状态', templet: function(d) {
                    if (d.status == 1) {
                        return '<div class="layui-unselect layui-form-checkbox role-status" data-id="' + d.id + '" data-status="' + d.status + '" lay-skin="tag"><div>锁定</div><i class="layui-icon layui-icon-ok"></i></div>';
                    } else if (d.status == 0) {
                        return '<div class="layui-unselect layui-form-checkbox role-status layui-form-checked" data-id="' + d.id + '" data-status="' + d.status + '" lay-skin="tag"><div>正常</div><i class="layui-icon layui-icon-ok"></i></div>';
                    }
                }
            },
            {fixed: 'right', title: '操作', width: 200, align: 'center', toolbar: '#roleListBar'}
        ]],
        page: true
    });

    /**
     * 查询按钮点击事件绑定
     */
    $('.roleTable .role-search').on('click', function() {
        reload();
    });

    /**
     * 重置按钮点击事件绑定
     */
    $('.roleTable .role-reset').on('click', function() {
        $('#roleNameReload').val('');
        reload();
    });

    /**
     * 更改角色状态
     */
    var updateRoleStatus = function(roleId, status) {
        var params = {
            roleId: roleId,
            status: status
        };
        http.post('/role/updateStatus', params, function() {
            reload();
        });
    };

    /**
     * 绑定角色状态更改点击事件
     */
    $("body").on("click", ".role-status", function() {
        if (hasPermission(hasChangeRoleStatusPermission)) {
            var id = $(this).attr("data-id");
            var status = $(this).attr("data-status");
            if (status == 0) {
                status = 1;
            } else if (status == 1) {
                status = 0;
            }
            if (status == 1) { // 锁定
                layer.confirm('确认锁定该角色吗？', {anim: 6}, function(index) {
                    updateRoleStatus(id, 1);
                    layer.close(index);
                });
            }
            if (status == 0) { // 取消锁定
                layer.confirm('确认取消锁定该角色吗？', {anim: 6}, function(index) {
                    updateRoleStatus(id, 0);
                    layer.close(index);
                });
            }
        }
    });

    var openDialog = function(title) {
        layer.open({
            title: title,
            id: "roleDialog",
            type: 1,
            offset: '20px',
            content: $('#roleTips'),
            area: [$(window).width() <= 750 ? '60%' : '500px', '200px'],
            resize: false,
            end: function() {
                $("#roleTips").css("display", 'none');
            }
        });
    };

    form.on('submit(role-submit)', function(data) {
        var url;
        if (data.field.id != null && data.field.id != '' && data.field.id != undefined) { // 修改
            url = '/role/update';
        } else { // 添加
            url = '/role/save';
        }
        common.commitForm($(this), layer, url, data.field, reload);
    });

    // 监听工具条
    table.on('tool(role-list)', function(obj) {
        var data = obj.data;
        if (obj.event == 'detail') {
            window.parent.mainFrm.location.href = "/role/roleMenu?roleId=" + data.id;
        } else if (obj.event == 'del') {
            layer.confirm('真的删除此条记录么？', {anim: 6}, function(index) {
                http.post('/role/delete', {id: data.id}, function() {
                    layer.close(index);
                    reload();
                });
            });
        } else if (obj.event == 'edit') {
            http.get('/role/getById', {id: data.id}, function(result) {
                $('#roleId').val(result.id);
                $('#roleName').val(result.roleName);
                openDialog('修改角色');
            });
        }
    });

    // 头工具栏事件
    $('#role-add-btn').on('click', function() {
        $('#roleId').val('');
        $('#roleName').val('');
        openDialog('添加角色');
    });

    common.cacheMousedown();

});
