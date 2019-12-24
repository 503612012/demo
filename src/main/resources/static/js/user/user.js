//@sourceURL=/js/user/user.js

layui.use(['table', 'jquery'], function() {

    var table = layui.table;
    var $ = layui.jquery;

    /**
     * 重新加载表格
     */
    var reload = function() {
        var nickNameReload = $('#nickNameReload');
        var userNameReload = $('#userNameReload');
        var phoneReload = $('#phoneReload');
        // 执行重载
        table.reload('userReload', {
            page: {
                curr: 1 // 重新从第 1 页开始
            }
            , where: {
                nickName: nickNameReload.val(),
                userName: userNameReload.val(),
                phone: phoneReload.val()
            }
        });
    };

    table.render({
        elem: '#user-list'
        , url: '/user/getByPage/'
        , toolbar: '#userListToolBar'
        , id: 'userReload'
        , even: true
        , cols: [[
            {type: 'numbers'}
            , {field: 'userName', title: '用户名', sort: true}
            , {field: 'nickName', title: '昵称'}
            , {field: 'age', title: '年龄'}
            , {field: 'email', title: '邮箱'}
            , {field: 'phone', title: '手机号'}
            , {
                field: 'gender', title: '性别', templet: function(d) {
                    return d.gender == 1 ? '男' : '<span style="color: #F581B1;">女</span>';
                }
            }
            , {field: 'createTime', title: '创建时间'}
            , {field: 'createName', title: '创建人'}
            , {field: 'lastModifyTime', title: '最后修改时间'}
            , {field: 'lastModifyName', title: '最后修改人'}
            , {
                field: 'status', title: '状态', templet: function(d) {
                    if (d.status == 1) {
                        return '<div><div class="layui-unselect layui-form-checkbox layui-form-checked user-status" data-id="' + d.id + '" data-status="' + d.status + '"><span>锁定</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    } else if (d.status == 0) {
                        return '<div><div class="layui-unselect layui-form-checkbox user-status" data-id="' + d.id + '" data-status="' + d.status + '"><span>锁定</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    }
                }
            }
            , {title: '操作', width: 200, align: 'center', toolbar: '#userListBar'}
        ]]
        , page: true
    });

    /**
     * 查询按钮点击事件绑定
     */
    $('.userTable .user-search').on('click', function() {
        reload();
    });

    /**
     * 重置按钮点击事件绑定
     */
    $('.userTable .user-reset').on('click', function() {
        $('#nickNameReload').val('');
        $('#userNameReload').val('');
        $('#phoneReload').val('');
        reload();
    });

    /**
     * 更改用户状态
     */
    var updateUserStatus = function(userId, status) {
        $.ajax({
            url: "/user/updateStatus",
            type: "POST",
            data: {
                userId: userId,
                status: status
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
                reload();
            }
        });
    };

    /**
     * 绑定用户状态更改点击事件
     */
    $("body").on("click", ".user-status", function() {
        if (hasPermission(hasChangeUserStatusPermission)) {
            var id = $(this).attr("data-id");
            var status = $(this).attr("data-status");
            if (status == 0) {
                status = 1;
            } else if (status == 1) {
                status = 0;
            }
            if (status == 1) { // 锁定
                layer.confirm('确认锁定该用户吗？', {anim: 6}, function(index) {
                    updateUserStatus(id, 1);
                    layer.close(index);
                });
            }
            if (status == 0) { // 取消锁定
                layer.confirm('确认取消锁定该用户吗？', {anim: 6}, function(index) {
                    updateUserStatus(id, 0);
                    layer.close(index);
                });
            }
        }
    });

    /**
     * 设置角色复选框选中事件
     */
    $("body").on("click", ".user-set-role-checkbox", function() {
        $(this).parent().toggleClass("layui-form-checked");
    });

    /**
     * 设置用户角色
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     */
    var setUserRole = function(userId, roleIds) {
        $.ajax({
            url: '/user/setUserRole',
            type: 'POST',
            data: {
                'userId': userId,
                'roleIds': roleIds
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
            }
        });
    };

    // 监听工具条
    table.on('tool(user-list)', function(obj) {
        var data = obj.data;
        var userid = data.id;
        if (obj.event == 'detail') {
            $.ajax({
                url: '/user/getRoleByUserId',
                type: 'POST',
                data: {
                    id: userid
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
                        return;
                    }
                    var data = result.data;
                    var html = '<div style="padding: 15px;">';
                    for (var i = 0; i < data.length; i++) {
                        html += '<div style="margin-top: 7px;" data-role-id="' + data[i].role.id + '" class="layui-unselect layui-form-checkbox ' + (data[i].checked == true ? 'layui-form-checked' : '') + '" lay-skin="primary"><span>' + data[i].role.roleName + '</span><i class="layui-icon layui-icon-ok user-set-role-checkbox"></i></div>';
                    }
                    html += '</div>';
                    layer.open({
                        title: '分配角色',
                        area: [$(window).width() <= 750 ? '60%' : '600px', '450px'],
                        btn: ['保存', '关闭'],
                        type: 1,
                        content: html,
                        yes: function() {
                            var roleIds = [];
                            $("body .layui-form-checked").each(function() {
                                roleIds.push($(this).attr("data-role-id"));
                            });
                            setUserRole(userid, roleIds.toString());
                        },
                        btn2: function() {
                            layer.closeAll();
                        }
                    });
                }
            });
        } else if (obj.event == 'del') {
            layer.confirm('真的删除此条记录么？', {anim: 6}, function(index) {
                $.ajax({
                    url: '/user/delete',
                    type: 'POST',
                    data: {
                        id: data.id
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
                            return;
                        }
                        layer.close(index);
                        reload();
                    }
                });
            });
        } else if (obj.event == 'edit') {
            window.parent.mainFrm.location.href = "/user/update?id=" + data.id;
        }
    });

    // 头工具栏事件
    table.on('toolbar(user-list)', function(obj) {
        if (obj.event == 'user-add-btn') {
            window.parent.mainFrm.location.href = "/user/add";
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
