//@sourceURL=/js/user/user.js

layui.use('table', function() {
    var table = layui.table;
    var form = layui.form;

    /**
     * 重新加载表格
     */
    var reload = function() {
        var userReload = $('#userReload');
        var nickNameReload = $('#nickNameReload');
        var userNameReload = $('#userNameReload');
        var phoneReload = $('#phoneReload');
        // 执行重载
        table.reload('userReload', {
            page: {
                curr: 1 // 重新从第 1 页开始
            }
            , where: {
                id: userReload.val(),
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
        , title: '用户数据表'
        , cellMinWidth: 80
        , cols: [[
            {field: 'id', title: 'ID', sort: true}
            , {field: 'userName', title: '用户名', sort: true}
            , {field: 'nickName', title: '昵称'}
            , {field: 'age', title: '年龄', sort: true}
            , {field: 'email', title: '邮箱'}
            , {field: 'phone', title: '手机号'}
            , {
                field: 'gender', title: '性别', sort: true, templet: function(d) {
                    return d.gender == 1 ? '男' : '<span style="color: #F581B1;">女</span>';
                }
            }
            , {field: 'createTime', title: '创建时间', sort: true}
            , {field: 'createName', title: '创建人'}
            , {field: 'lastModifyTime', title: '最后修改时间', sort: true}
            , {field: 'lastModifyName', title: '最后修改人'}
            , {
                field: 'status', title: '状态', unresize: true, templet: function(d) {
                    if (d.status == 1) {
                        return '<div><div class="layui-unselect layui-form-checkbox layui-form-checked user-status" data-id="' + d.id + '" data-status="' + d.status + '"><span>锁定</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    } else if (d.status == 0) {
                        return '<div><div class="layui-unselect layui-form-checkbox user-status" data-id="' + d.id + '" data-status="' + d.status + '"><span>锁定</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    }
                }
            }
            , {fixed: 'right', title: '操作', toolbar: '#userListBar'}
        ]]
        , page: true
    });

    var $ = layui.$, active = {
        reload: reload()
    };

    $('.userTable .layui-btn').on('click', function() {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
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
                        content: result.data,
                        btnAlign: 'c'
                    });
                    return;
                }
                reload();
            }
        });
    };

    $("body").on("click", ".user-status", function() {
        if(hasPermission("A1_01_04")) {
            var id = $(this).attr("data-id");
            var status = $(this).attr("data-status");
            if (status == 0) {
                status = 1;
            } else if (status == 1) {
                status = 0;
            }
            if (status == 1) { // 锁定
                layer.confirm('确认锁定该用户吗？', function(index) {
                    updateUserStatus(id, 1);
                    layer.close(index);
                });
            }
            if (status == 0) { // 取消锁定
                layer.confirm('确认取消锁定该用户吗？', function(index) {
                    updateUserStatus(id, 0);
                    layer.close(index);
                });
            }
        }
    });

    // 监听状态操作
    form.on('checkbox(userStatus)', function(obj) {
        var status = obj.elem.checked;
        var userId = $(this).attr("data-id");
        if (status == false) { // 取消锁定
            layer.confirm('确认取消锁定该用户吗？', function(index) {
                updateUserStatus(userId, 0);
                layer.close(index);
            });
        }
        if (status == true) { // 锁定
            layer.confirm('确认锁定该用户吗？', function(index) {
                updateUserStatus(userId, 1);
                layer.close(index);
            });
        }
    });

    // 监听工具条
    table.on('tool(user-list)', function(obj) {
        var data = obj.data;
        if (obj.event === 'detail') {
            // TODO 这里的代码还没有编写
            layer.msg('ID：' + data.id + ' 的分配角色操作');
        } else if (obj.event === 'del') {
            layer.confirm('真的删除此条记录么？', function(index) {
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
                                content: result.data,
                                btnAlign: 'c'
                            });
                            return;
                        }
                        reload();
                    }
                });
            });
        } else if (obj.event === 'edit') {
            window.parent.mainFrm.location.href = "/user/update?id=" + data.id;
        }
    });

    // 头工具栏事件
    table.on('toolbar(user-list)', function(obj) {
        switch (obj.event) {
            case 'user-add-btn':
                // TODO 添加用户的功能还没有编写
                alert("tt");
                var data = checkStatus.data;
                layer.alert(JSON.stringify(data));
                break;
        }
    });
});
