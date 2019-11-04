//@sourceURL=/js/advanceSalary/advanceSalary.js

layui.use(['table', 'laydate'], function() {
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
            }
            , where: {
                employeeName: employeeName.val(),
                advanceTime: advanceTime.val()
            }
        });
    };

    table.render({
        elem: '#advanceSalary-list'
        , url: '/advanceSalary/getByPage/'
        , toolbar: '#advanceSalaryListToolBar'
        , id: 'advanceSalaryReload'
        , even: true
        , cols: [[
            {type: 'numbers'}
            , {field: 'employeeName', title: '员工名称', sort: true}
            , {field: 'advanceTime', title: '预支时间', sort: true}
            , {
                field: 'money', title: '预支金额', templet: function(d) {
                    return '<span class="money" data-value="' + d.money + '" style="cursor: pointer;">***</span>';
                }
            }
            , {field: 'createName', title: '录入人'}
            , {
                field: 'remark', title: '备注', templet: function(d) {
                    if (d.remark == '' || d.remark == null) {
                        return '无';
                    } else {
                        return d.remark;
                    }
                }
            }
            , {
                field: 'hasRepay', title: '是否归还', sort: true, templet: function(d) {
                    if (d.hasRepay == 0) {
                        return '<div><div class="layui-unselect layui-form-checkbox layui-form-checked"><span>是</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    } else if (d.hasRepay == 1) {
                        return '<div><div class="layui-unselect layui-form-checkbox"><span>否</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    }
                }
            }
            , {title: '操作', width: 80, align: 'center', toolbar: '#advanceSalaryListBar'}
        ]]
        , page: true
        , done: function(res) {
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

    var $ = layui.$;

    /**
     * 显示/隐藏总金额
     */
    $("body").on("click", "span.totalAdvanceSalary", function() {
        if (hasPermission(hasShowAdvanceSalaryTotalMoneyPermission)) {
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
     * 显示/隐藏金额
     */
    $("body").on("click", "span.money", function() {
        if (hasPermission(hasShowAdvanceSalaryMoneyPermission)) {
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

    // 监听工具条
    table.on('tool(advanceSalary-list)', function(obj) {
        var data = obj.data;
        if (obj.event == 'del') {
            layer.confirm('真的删除此条记录么？', {anim: 6}, function(index) {
                $.ajax({
                    url: '/advanceSalary/delete',
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
        }
    });

    // 头工具栏事件
    table.on('toolbar(advanceSalary-list)', function(obj) {
        if (obj.event == 'advanceSalary-add-btn') {
            window.parent.mainFrm.location.href = "/advanceSalary/add";
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
