//@sourceURL=/js/menu/menu.js
layui.config({
    base: '/js/lib/'
});
layui.use(['treetable', 'form'], function() {

    var o = layui.$, treetable = layui.treetable;
    var layer = layui.layer;
    var data = null;

    var loadData = function() {
        o.ajax({
            url: "/menu/getMenuTreeTableData",
            type: "GET",
            data: {},
            dataType: "json",
            async: false,
            success: function(result) {
                if (result.code != 200) {
                    layer.open({
                        title: '系统提示',
                        anim: 6,
                        content: result.data,
                        btnAlign: 'c'
                    });
                    return;
                }
                data = result.data;
            }
        });
        treetable.render({
            elem: '#menuTreeTable',
            data: data,
            field: 'title',
            is_open: true,
            icon_class: "up",
            cols: [
                {
                    field: 'title',
                    title: '标题'
                },
                {
                    field: 'menuCode',
                    title: '菜单编码'
                },
                {
                    field: 'id',
                    title: 'ID'
                },
                {
                    field: 'pid',
                    title: '父ID'
                },
                {
                    title: '类型',
                    template: function(d) {
                        if (d.type == 1) {
                            return '<span style="color: black;">菜单</span>';
                        } else if (d.type == 2) {
                            return '<span style="color: blue;">按钮</span>';
                        } else {
                            return '<span style="color: red;">数据错误！</span>';
                        }
                    }
                },
                {
                    title: '状态',
                    template: function(d) {
                        if (d.status == 1) {
                            return '<div class="layui-unselect layui-form-checkbox menu-status" data-id="' + d.id + '" data-status="' + d.status + '" lay-skin="tag"><div>锁定</div><i class="layui-icon layui-icon-ok"></i></div>';
                        } else if (d.status == 0) {
                            return '<div class="layui-unselect layui-form-checkbox menu-status layui-form-checked" data-id="' + d.id + '" data-status="' + d.status + '" lay-skin="tag"><div>正常</div><i class="layui-icon layui-icon-ok"></i></div>';
                        }
                    }
                },
                {
                    fixed: 'right',
                    field: 'actions',
                    title: '操作',
                    template: function(item) {
                        if (item.type == 1) {
                            if (hasPermission(hasUpdateMenuPermission)) {
                                return '<a lay-filter="edit" class="layui-btn layui-btn-xs">编辑</a>';
                            } else {
                                return '';
                            }
                        } else {
                            return '';
                        }
                    }
                }
            ]
        });
        treetable.all('up');
    };

    treetable.on('treetable(edit)', function(data) {
        var menuName = data.item.menuName;
        var id = data.item.id;
        var html = '<div style="padding: 15px;">';
        html += '<form lay-filter="role-update-from" class="layui-form" style="width: 80%;">';
        html += '<div class="layui-form-item">';
        html += '<label class="layui-form-label">菜单名称</label>';
        html += '<div class="layui-input-inline">';
        html += '<input type="hidden" name="id" value="' + id + '">';
        html += '<input type="text" name="menuName" value="' + menuName + '" lay-verify="required" placeholder="请输入菜单名称" autocomplete="off" class="layui-input">';
        html += '</div>';
        html += '</div>';
        html += '</form>';
        html += '</div>';
        layer.open({
            title: '修改菜单',
            area: [$(window).width() <= 750 ? '60%' : '500px', '300px'],
            btn: ['保存', '关闭'],
            type: 1,
            content: html,
            yes: function() {
                menuName = $("body input[name=menuName]").val();
                $.ajax({
                    url: '/menu/doUpdate',
                    type: 'POST',
                    data: {
                        id: id,
                        menuName: menuName
                    },
                    dataType: 'json',
                    success: function(result) {
                        if (result.code != 200) {
                            layer.open({
                                title: '系统提示',
                                anim: 6,
                                content: result.data,
                                btnAlign: 'c'
                            });
                        }
                        layer.closeAll();
                        loadData();
                    }
                });
                return false;
            },
            btn2: function() {
                layer.closeAll();
            }
        });
    });

    o('.up-all').on('click', function() {
        treetable.all('up');
    });

    o('.down-all').on('click', function() {
        treetable.all('down');
    });

    /**
     * 更改菜单状态
     */
    var updateUserStatus = function(menuId, status) {
        $.ajax({
            url: "/menu/updateStatus",
            type: "POST",
            data: {
                'menuId': menuId,
                'status': status
            },
            dataType: "json",
            success: function(result) {
                if (result.code != 200) {
                    layer.open({
                        title: '系统提示',
                        anim: 6,
                        content: result.data,
                        btnAlign: 'c'
                    });
                    return;
                }
                loadData();
            }
        });
        return false;
    };

    /**
     * 绑定菜单状态更改点击事件
     */
    $("body").on("click", ".menu-status", function() {
        if (hasPermission(hasChangeMenuStatusPermission)) {
            var id = $(this).attr("data-id");
            var status = $(this).attr("data-status");
            if (status == 0) {
                status = 1;
            } else if (status == 1) {
                status = 0;
            }
            if (status == 1) { // 锁定
                layer.confirm('确认锁定该菜单吗？', {anim: 6}, function(index) {
                    updateUserStatus(id, 1);
                    layer.close(index);
                });
            }
            if (status == 0) { // 取消锁定
                layer.confirm('确认取消锁定该菜单吗？', {anim: 6}, function(index) {
                    updateUserStatus(id, 0);
                    layer.close(index);
                });
            }
        }
    });

    loadData();

});