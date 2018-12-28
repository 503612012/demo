//@sourceURL=/js/employee/employee.js

layui.use('table', function() {
    var table = layui.table;
    var form = layui.form;

    /**
     * 重新加载表格
     */
    var reload = function() {
        var nameReload = $('#nameReload');
        var contactReload = $('#contactReload');
        // 执行重载
        table.reload('employeeReload', {
            page: {
                curr: 1 // 重新从第 1 页开始
            }
            , where: {
                name: nameReload.val(),
                contact: contactReload.val()
            }
        });
    };

    table.render({
        elem: '#employee-list'
        , url: '/employee/getByPage/'
        , toolbar: '#employeeListToolBar'
        , id: 'employeeReload'
        , even: true
        , title: '员工数据表'
        , cols: [[
            {type:'numbers'}
            , {field: 'name', title: '员工姓名', sort: true}
            , {field: 'age', title: '年龄', sort: true}
            , {field: 'contact', title: '手机号'}
            , {
                field: 'gender', title: '性别', sort: true, templet: function(d) {
                    return d.gender == 1 ? '男' : '<span style="color: #F581B1;">女</span>';
                }
            }
            , {
                field: 'daySalary', title: '日薪', sort: true, templet: function(d) {
                    return '<span class="daySalary" data-value="' + d.daySalary + '" style="cursor: pointer;">***</span>';
                }
            }
            , {
                field: 'monthSalary', title: '月薪', sort: true, templet: function(d) {
                    return '<span class="monthSalary" data-value="' + d.monthSalary + '" style="cursor: pointer;">***</span>';
                }
            }
            , {field: 'address', title: '住址'}
            , {field: 'createTime', title: '创建时间', sort: true}
            , {field: 'createName', title: '创建人'}
            , {field: 'lastModifyTime', title: '最后修改时间', sort: true}
            , {field: 'lastModifyName', title: '最后修改人'}
            , {
                field: 'status', title: '状态', templet: function(d) {
                    if (d.status == 1) {
                        return '<div><div class="layui-unselect layui-form-checkbox layui-form-checked employee-status" data-id="' + d.id + '" data-status="' + d.status + '"><span>锁定</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    } else if (d.status == 0) {
                        return '<div><div class="layui-unselect layui-form-checkbox employee-status" data-id="' + d.id + '" data-status="' + d.status + '"><span>锁定</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    }
                }
            }
            , {title: '操作', toolbar: '#employeeListBar'}
        ]]
        , page: true
    });

    var $ = layui.$;

    /**
     * 查询按钮点击事件绑定
     */
    $('.employeeTable .employee-search').on('click', function() {
        reload();
    });

    /**
     * 重置按钮点击事件绑定
     */
    $('.employeeTable .employee-reset').on('click', function() {
        $('#nameReload').val('');
        $('#contactReload').val('');
        reload();
    });

    /**
     * 更改员工状态
     */
    var updateUserStatus = function(employeeId, status) {
        $.ajax({
            url: "/employee/updateStatus",
            type: "POST",
            data: {
                employeeId: employeeId,
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
     * 显示/隐藏金额
     */
    $("body").on("click", "span.daySalary,span.monthSalary", function() {
        if(hasPermission("B1_01_05")) {
            if ($(this).hasClass("red")) { // 隐藏
                $(this).removeClass("red");
                $(this).html("***");
            } else {
                $(this).addClass("red");
                $(this).html($(this).attr("data-value"));
            }
        }
    });

    /**
     * 绑定员工状态更改点击事件
     */
    $("body").on("click", ".employee-status", function() {
        if(hasPermission("B1_01_04")) {
            var id = $(this).attr("data-id");
            var status = $(this).attr("data-status");
            if (status == 0) {
                status = 1;
            } else if (status == 1) {
                status = 0;
            }
            if (status == 1) { // 锁定
                layer.confirm('确认锁定该员工吗？', function(index) {
                    updateUserStatus(id, 1);
                    layer.close(index);
                });
            }
            if (status == 0) { // 取消锁定
                layer.confirm('确认取消锁定该员工吗？', function(index) {
                    updateUserStatus(id, 0);
                    layer.close(index);
                });
            }
        }
    });

    // 监听工具条
    table.on('tool(employee-list)', function(obj) {
        var data = obj.data;
        var employeeid = data.id;
        if (obj.event === 'del') {
            layer.confirm('真的删除此条记录么？', function(index) {
                $.ajax({
                    url: '/employee/delete',
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
        } else if (obj.event === 'edit') {
            window.parent.mainFrm.location.href = "/employee/update?id=" + data.id;
        }
    });

    // 头工具栏事件
    table.on('toolbar(employee-list)', function(obj) {
        switch (obj.event) {
            case 'employee-add-btn':
                window.parent.mainFrm.location.href = "/employee/add";
                break;
        }
    });
});
