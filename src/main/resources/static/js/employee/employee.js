//@sourceURL=/js/employee/employee.js
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
        var nameReload = $('#nameReload');
        var contactReload = $('#contactReload');
        // 执行重载
        table.reload('employeeReload', {
            page: {
                curr: 1 // 重新从第 1 页开始
            },
            where: {
                name: nameReload.val(),
                contact: contactReload.val()
            }
        });
    };

    table.render({
        elem: '#employee-list',
        url: '/employee/getByPage/',
        toolbar: '#employeeListToolBar',
        id: 'employeeReload',
        even: true,
        cols: [[
            {field: 'name', title: '员工姓名', sort: true},
            {field: 'age', title: '年龄', sort: true},
            {field: 'contact', title: '手机号'},
            {
                field: 'gender', title: '性别', templet: function(d) {
                    return d.gender == 1 ? '男' : '<span style="color: #F581B1;">女</span>';
                }
            },
            {
                field: 'hourSalary', title: '时薪', templet: function(d) {
                    return '<span class="hourSalary" data-value="' + d.hourSalary + '" style="cursor: pointer;">***</span>';
                }
            },
            {field: 'address', title: '住址'},
            {
                field: 'status', title: '状态', templet: function(d) {
                    if (d.status == 1) {
                        return '<div><div class="layui-unselect layui-form-checkbox layui-form-checked employee-status" data-id="' + d.id + '" data-status="' + d.status + '"><span>锁定</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    } else if (d.status == 0) {
                        return '<div><div class="layui-unselect layui-form-checkbox employee-status" data-id="' + d.id + '" data-status="' + d.status + '"><span>正常</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    }
                }
            },
            {title: '操作', width: 120, align: 'center', toolbar: '#employeeListBar'}
        ]],
        page: true
    });

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
    var updateEmployeeStatus = function(employeeId, status) {
        var param = {
            "employeeId": employeeId,
            "status": status
        };
        http.post('/employee/updateStatus', param, function() {
            reload();
        });
    };

    /**
     * 绑定员工状态更改点击事件
     */
    $("body").on("click", ".employee-status", function() {
        if (hasPermission(hasChangeEmployeeStatusPermission)) {
            var id = $(this).attr("data-id");
            var status = $(this).attr("data-status");
            if (status == 0) {
                status = 1;
            } else if (status == 1) {
                status = 0;
            }
            if (status == 1) { // 锁定
                layer.confirm('确认锁定该员工吗？', {anim: 6}, function(index) {
                    updateEmployeeStatus(id, 1);
                    layer.close(index);
                });
            }
            if (status == 0) { // 取消锁定
                layer.confirm('确认取消锁定该员工吗？', {anim: 6}, function(index) {
                    updateEmployeeStatus(id, 0);
                    layer.close(index);
                });
            }
        }
    });

    /**
     * 显示/隐藏金额
     */
    $("body").on("click", "span.hourSalary", function() {
        common.showOrHide($(this), hasShowEmployeeMoneyStatusPermission);
    });

    var openDialog = function(title) {
        layer.open({
            title: title,
            id: "employeeDialog",
            type: 1,
            offset: '20px',
            content: $('#employeeTips'),
            area: [$(window).width() <= 750 ? '60%' : '500px', '500px'],
            resize: false,
            end: function() {
                $("#employeeTips").css("display", 'none');
            }
        });
    };

    form.on('submit(employee-submit)', function(data) {
        var url;
        if (data.field.id != null && data.field.id != '' && data.field.id != undefined) { // 修改
            url = '/employee/update';
        } else { // 添加
            url = '/employee/save';
        }
        common.commitForm($(this), layer, url, data.field, reload);
    });

    // 监听工具条
    table.on('tool(employee-list)', function(obj) {
        var data = obj.data;
        if (obj.event == 'del') {
            layer.confirm('真的删除此条记录么？', {anim: 6}, function(index) {
                http.post('/employee/delete', {id: data.id}, function() {
                    layer.close(index);
                    reload();
                });
            });
        } else if (obj.event == 'edit') {
            http.get('/employee/getById', {id: data.id}, function(result) {
                form.val("employee-from", {
                    "id": result.id,
                    "name": result.name,
                    "age": result.age,
                    "gender": result.gender,
                    "address": result.address,
                    "hourSalary": result.hourSalary,
                    "contact": result.contact
                });
                openDialog('修改员工');
            });
        }
    });

    // 头工具栏事件
    table.on('toolbar(employee-list)', function(obj) {
        if (obj.event == 'employee-add-btn') {
            if (obj.event == 'employee-add-btn') {
                $('#employeeForm')[0].reset();
                layui.form.render();
                openDialog('添加员工');
            }
        }
    });

    common.cacheMousedown();

});
