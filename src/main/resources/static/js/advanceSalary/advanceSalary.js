//@sourceURL=/js/advanceSalary/advanceSalary.js
requirejs.config({
    baseUrl: '/',
    paths: {
        jquery: 'easyui/jquery.min',
        layui: 'layui/layui.all',
        http: 'js/common/http',
        common: 'js/common/common'
    },
    shim: {
        "layui": {exports: "layui"}
    }
});

requirejs(['jquery', 'layui', 'http', 'common'], function($, layui, http, common) {

    var form = layui.form;
    var table = layui.table;
    var laydate = layui.laydate;

    // 初始化日期选择框
    laydate.render({
        elem: '#advanceTime',
        format: 'yyyy-MM-dd'
    });

    /**
     * 重新加载表格
     */
    var reload = function() {
        var employeeName = $('#employeeName');
        var advanceTime = $('#advanceTime');
        // 执行重载
        table.reload('advanceSalaryReload', {
            page: {
                curr: 1 // 重新从第 1 页开始
            },
            where: {
                employeeName: employeeName.val(),
                advanceTime: advanceTime.val()
            }
        });
    };

    table.render({
        elem: '#advanceSalary-list',
        url: '/advanceSalary/getByPage/',
        toolbar: '#advanceSalaryListToolBar',
        id: 'advanceSalaryReload',
        even: true,
        cols: [[
            {type: 'numbers'},
            {field: 'employeeName', title: '员工名称', sort: true},
            {field: 'advanceTime', title: '预支时间', sort: true},
            {
                field: 'money', title: '预支金额', templet: function(d) {
                    return '<span class="money" data-value="' + d.money + '" style="cursor: pointer;">***</span>';
                }
            },
            {field: 'createName', title: '录入人'},
            {
                field: 'remark', title: '备注', templet: function(d) {
                    if (d.remark == '' || d.remark == null) {
                        return '无';
                    } else {
                        return d.remark;
                    }
                }
            },
            {
                field: 'hasRepay', title: '是否归还', sort: true, templet: function(d) {
                    if (d.hasRepay == 0) {
                        return '<div><div class="layui-unselect layui-form-checkbox layui-form-checked"><span>是</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    } else if (d.hasRepay == 1) {
                        return '<div><div class="layui-unselect layui-form-checkbox"><span>否</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    }
                }
            },
            {title: '操作', width: 80, align: 'center', toolbar: '#advanceSalaryListBar'}
        ]],
        page: true,
        done: function(res) {
            if (hasPermission(hasShowAdvanceSalaryTotalMoneyPermission)) {
                $('#layui-table-page1').css("display", "flex");
                var totalAdvanceSalary = 0;
                for (var i = 0; i < res.data.length; i++) {
                    totalAdvanceSalary += res.data[i].money;
                }
                var html = "<div style='margin-left: 20px; font-weight: bold; color: red;'>本页总预支薪资为：<span style='cursor: pointer;' data-value='" + totalAdvanceSalary + "元' class='totalAdvanceSalary'>***</span></div>";
                $('#layui-table-page1').append(html);
            }
        }
    });

    /**
     * 显示/隐藏总金额
     */
    $("body").on("click", "span.totalAdvanceSalary", function() {
        common.showOrHide($(this), hasShowAdvanceSalaryTotalMoneyPermission);
    });

    /**
     * 显示/隐藏金额
     */
    $("body").on("click", "span.money", function() {
        common.showOrHide($(this), hasShowAdvanceSalaryMoneyPermission);
    });

    /**
     * 查询按钮点击事件绑定
     */
    $('.advanceSalaryTable .advanceSalary-search').on('click', function() {
        reload();
    });

    /**
     * 重置按钮点击事件绑定
     */
    $('.advanceSalaryTable .advanceSalary-reset').on('click', function() {
        $('#employeeName').val('');
        $('#advanceTime').val('');
        reload();
    });

    var openDialog = function(title) {
        layer.open({
            title: title,
            id: "advanceSalaryDialog",
            type: 1,
            offset: '20px',
            content: $('#advanceSalaryTips'),
            area: [$(window).width() <= 750 ? '60%' : '500px', '450px'],
            resize: false,
            end: function() {
                $("#advanceSalaryTips").css("display", 'none');
            }
        });
    };

    form.on('submit(advanceSalary-submit)', function(data) {
        common.commitForm($(this), layer, '/advanceSalary/add', data.field, reload);
    });

    // 监听工具条
    table.on('tool(advanceSalary-list)', function(obj) {
        var data = obj.data;
        if (obj.event == 'del') {
            layer.confirm('真的删除此条记录么？', {anim: 6}, function(index) {
                http.post('/advanceSalary/delete', {id: data.id}, function() {
                    layer.close(index);
                    reload();
                });
            });
        }
    });

    // 头工具栏事件
    table.on('toolbar(advanceSalary-list)', function(obj) {
        if (obj.event == 'advanceSalary-add-btn') {
            // 初始化日期选择框
            laydate.render({
                elem: '#advanceTimeForm',
                format: 'yyyy-MM-dd',
                max: common.getNowFormatDate()
            });

            http.get('/employee/getAll', {}, function(data) {
                common.initEmployeeSelectBox(data, $("#employeeSelect"), form);
            });
            $('#money').val('');
            $('#remark').val('');
            $('#advanceTimeForm').val('');
            openDialog('录入预支薪资');
        }
    });

    common.cacheMousedown();

});
