//@sourceURL=/js/workhour/workhour.js

layui.use(['table', 'laydate'], function() {
    var table = layui.table;
    var laydate = layui.laydate;

    // 初始化日期选择框
    laydate.render({
        elem: '#workDate',
        format: 'yyyy-MM-dd'
    });

    /**
     * 重新加载表格
     */
    var reload = function() {
        var employeeName = $('#employeeName');
        var worksiteName = $('#worksiteName');
        var workDate = $('#workDate');
        // 执行重载
        table.reload('workhourReload', {
            page: {
                curr: 1 // 重新从第 1 页开始
            }
            , where: {
                employeeName: employeeName.val(),
                worksiteName: worksiteName.val(),
                workDate: workDate.val()
            }
        });
    };

    table.render({
        elem: '#workhour-list'
        , url: '/workhour/getByPage/'
        , toolbar: '#workhourListToolBar'
        , id: 'workhourReload'
        , even: true
        , cols: [[
            {type: 'numbers'}
            , {field: 'employeeName', title: '员工名称', sort: true}
            , {field: 'worksiteName', title: '工地名称'}
            , {field: 'workDate', title: '工时日期', sort: true}
            , {field: 'workhour', title: '录入工时'}
            , {
                field: 'hourSalary', title: '当日时薪', templet: function(d) {
                    return '<span class="hourSalary" data-value="' + d.hourSalary + '" style="cursor: pointer;">***</span>';
                }
            }
            , {field: 'createName', title: '录入人'}
            , {field: 'createTime', title: '录入时间', sort: true}
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
                field: 'hasPay', title: '是否发放', sort: true, templet: function(d) {
                    if (d.hasPay == 1) {
                        return '<div><div class="layui-unselect layui-form-checkbox layui-form-checked"><span>是</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    } else if (d.hasPay == 0) {
                        return '<div><div class="layui-unselect layui-form-checkbox"><span>否</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    }
                }
            }
            , {title: '操作', toolbar: '#workhourListBar'}
        ]]
        , page: true
        , done: function(res) {
            if (hasPermission(hasShowWorkhourMoneyPermission)) {
                $('#layui-table-page1').css("display", "flex");
                var totalWorkhour = 0;
                for (var i = 0; i < res.data.length; i++) {
                    totalWorkhour += res.data[i].workhour;
                }
                var html = "<div style='margin-left: 20px; font-weight: bold; color: red;'>本页录入总工时为：<span style='cursor: pointer;' data-value='" + totalWorkhour + "' class='totalWorkhour'>***</span></div>";
                $('#layui-table-page1').append(html);
            }
        }
    });

    var $ = layui.$;

    /**
     * 显示/隐藏金额
     */
    $("body").on("click", "span.hourSalary", function() {
        if (hasPermission(hasShowWorkhourMoneyPermission)) {
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
     * 显示/隐藏总工时
     */
    $("body").on("click", "span.totalWorkhour", function() {
        if (hasPermission(hasShowWorkhourTotalWorkhourPermission)) {
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
    $('.workhourTable .workhour-search').on('click', function() {
        reload();
    });

    /**
     * 重置按钮点击事件绑定
     */
    $('.workhourTable .workhour-reset').on('click', function() {
        $('#employeeName').val('');
        $('#worksiteName').val('');
        $('#workDate').val('');
        reload();
    });

    // 监听工具条
    table.on('tool(workhour-list)', function(obj) {
        var data = obj.data;
        if (obj.event == 'del') {
            layer.confirm('真的删除此条记录么？', {anim: 6}, function(index) {
                $.ajax({
                    url: '/workhour/delete',
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
        }
    });

    // 头工具栏事件
    table.on('toolbar(workhour-list)', function(obj) {
        if (obj.event == 'workhour-add-btn') {
            window.parent.mainFrm.location.href = "/workhour/add";
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
