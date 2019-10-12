//@sourceURL=/js/role/role.js

layui.use('table', function() {
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
            }
            , where: {
                roleName: roleNameReload.val()
            }
        });
    };

    table.render({
        elem: '#role-list'
        , url: '/role/getByPage/'
        , toolbar: '#roleListToolBar'
        , id: 'roleReload'
        , even: true
        , cols: [[
            {type: 'numbers'}
            , {field: 'roleName', title: '角色名', sort: true}
            , {field: 'createTime', title: '创建时间'}
            , {field: 'createName', title: '创建人'}
            , {field: 'lastModifyTime', title: '最后修改时间'}
            , {field: 'lastModifyName', title: '最后修改人'}
            , {
                field: 'status', title: '状态', templet: function(d) {
                    if (d.status == 1) {
                        return '<div><div class="layui-unselect layui-form-checkbox layui-form-checked role-status" data-id="' + d.id + '" data-status="' + d.status + '"><span>锁定</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    } else if (d.status == 0) {
                        return '<div><div class="layui-unselect layui-form-checkbox role-status" data-id="' + d.id + '" data-status="' + d.status + '"><span>锁定</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    }
                }
            }
            , {title: '操作', toolbar: '#roleListBar'}
        ]]
        , page: true
    });

    var $ = layui.$;

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
    var updateUserStatus = function(roleId, status) {
        $.ajax({
            url: "/role/updateStatus",
            type: "POST",
            data: {
                roleId: roleId,
                status: status
            },
            dataType: "json",
            success: function(result) {
                if (result.code != 200) {
                    layer.open({
                        title: '系统提示',
                        content: result.data,
                        btnAlign: 'c'
                    });
                    return;
                }
                reload();
            }
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
                    updateUserStatus(id, 1);
                    layer.close(index);
                });
            }
            if (status == 0) { // 取消锁定
                layer.confirm('确认取消锁定该角色吗？', {anim: 6}, function(index) {
                    updateUserStatus(id, 0);
                    layer.close(index);
                });
            }
        }
    });

    // 监听工具条
    table.on('tool(role-list)', function(obj) {
        var data = obj.data;
        if (obj.event == 'detail') {
            window.parent.mainFrm.location.href = "/role/roleMenu?roleId=" + data.id;
        } else if (obj.event == 'del') {
            layer.confirm('真的删除此条记录么？', {anim: 6}, function(index) {
                $.ajax({
                    url: '/role/delete',
                    type: 'POST',
                    data: {
                        id: data.id
                    },
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
                        layer.close(index);
                        reload();
                    }
                });
            });
        } else if (obj.event == 'edit') {
            window.parent.mainFrm.location.href = "/role/update?id=" + data.id;
        }
    });

    // 头工具栏事件
    table.on('toolbar(role-list)', function(obj) {
        if (obj.event == 'role-add-btn') {
            window.parent.mainFrm.location.href = "/role/add";
        }
    });

    // 缓存当前操作的是哪个表格的哪个tr的哪个td
    $(document).off('mousedown', '.layui-table-grid-down').on('mousedown', '.layui-table-grid-down', function() {
        table._tableTrCurr = $(this).closest('td');
    });

    $(document).off('click', '.layui-table-tips-main [lay-event]').on('click', '.layui-table-tips-main [lay-event]', function() {
        var elem = $(this);
        var tableTrCurr = table._tableTrCurr;
        if (!tableTrCurr) {
            return;
        }
        var layerIndex = elem.closest('.layui-table-tips').attr('times');
        // 关闭当前这个显示更多的tip
        layer.close(layerIndex);
        table._tableTrCurr.find('[lay-event="' + elem.attr('lay-event') + '"]')[0].click();
    });

});
