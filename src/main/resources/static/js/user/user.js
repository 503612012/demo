//@sourceURL=/js/user/user.js
$(function() {

    layui.use('table', function() {
        var table = layui.table;
        var form = layui.form;

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
                    field: 'gender', title: '性别', sort: true, templet: function (d) {
                        return d.gender == 1 ? '男' : '<span style="color: #F581B1;">女</span>';
                    }
                }
                , {field: 'createTime', title: '创建时间', sort: true}
                , {field: 'createName', title: '创建人'}
                , {field: 'lastModifyTime', title: '最后修改时间', sort: true}
                , {field: 'lastModifyName', title: '最后修改人'}
                , {field: 'status', title: '状态', templet: '#userStatusCheckbox', unresize: true}
                , {fixed: 'right', title: '操作', toolbar: '#userListBar'}
            ]]
            , page: true
        });

        var $ = layui.$, active = {
            reload: function() {
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
            }
        };

        $('.userTable .layui-btn').on('click', function() {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });

        // 监听状态操作 TODO 这里需要设置权限，没有更改状态权限的人，点击这个按钮是没有反应的
        form.on('checkbox(userStatus)', function (obj) {
            if (hasPermission("ss")) {
                layer.tips(this.value + ' ' + this.name + '：' + obj.elem.checked, obj.othis);
            }
        });

        // 监听工具条 TODO 这里的代码还没有编写
        table.on('tool(user-list)', function (obj) {
            var data = obj.data;
            if (obj.event === 'detail') {
                layer.msg('ID：' + data.id + ' 的分配角色操作');
            } else if (obj.event === 'del') {
                layer.confirm('真的删除此条记录么？', function (index) {
                    layer.msg('删除的操作');
                });
            } else if (obj.event === 'edit') {
                layer.alert('编辑行：<br>' + JSON.stringify(data))
            }
        });
    });

});