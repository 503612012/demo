//@sourceURL=/js/fundBill/fundBill.js
layui.use(['table', 'form', 'laydate'], function() {

    var laydate = layui.laydate;
    var layer = layui.layer;
    var table = layui.table;
    var form = layui.form;
    var $ = layui.$;

    // 初始化日期选择框
    laydate.render({
        elem: '#dataDateReload',
        ready: function(value) {
            disabled_date(value, '7,1')
        },
        change: function(value, date, endDate) {
            disabled_date(value, '7,1')
        },
        trigger: 'click',
        format: 'yyyy-MM-dd'
    });

    /**
     * 重新加载表格
     */
    var reload = function() {
        var nameReload = $('#nameReload');
        var dataDateReload = $('#dataDateReload');
        // 执行重载
        table.reload('fundBillReload', {
            page: {
                curr: 1 // 重新从第 1 页开始
            }
            , where: {
                fundName: nameReload.val(),
                date: dataDateReload.val()
            }
        });
    };

    table.render({
        elem: '#fundBill-list'
        , url: '/fundBill/getByPage/'
        , toolbar: '#fundBillListToolBar'
        , id: 'fundBillReload'
        , even: true
        , cols: [[
            {type: 'numbers'}
            , {field: 'fundName', title: '基金名称', sort: true}
            , {field: 'dataDate', title: '收益时间'}
            , {
                field: 'money', title: '收益金额', templet: function(d) {
                    if (d.money > 0) {
                        return '<span style="color: green;">' + d.money + '</span>';
                    } else {
                        return '<span style="color: red;">' + d.money + '</span>';
                    }
                }
            }
            , {title: '操作', width: 120, align: 'center', toolbar: '#fundBillListBar'}
        ]]
        , page: true
    });

    /**
     * 查询按钮点击事件绑定
     */
    $('.fundBillTable .fundBill-search').on('click', function() {
        reload();
    });

    /**
     * 重置按钮点击事件绑定
     */
    $('.fundBillTable .fundBill-reset').on('click', function() {
        $('#nameReload').val('');
        $('#dataDateReload').val('');
        reload();
    });

    /**
     * 修改收益
     */
    function openUpdateTips(fundBill) {
        // 初始化基金下拉框
        $.ajax({
            url: '/fund/getAll',
            type: 'GET',
            data: {},
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
                var html = '<option value="">请选择基金</option>';
                for (var i = 0; i < data.length; i++) {
                    html += '<option value="' + data[i].id + '">' + data[i].fundName + '</option>';
                }
                $("#updateFundSelect").html(html);
                form.val('updateProfitForm', {
                    'id': fundBill.id,
                    'fundId': fundBill.fundId,
                    'money': fundBill.money
                });
                form.render("select");

                // 初始化日期选择框
                laydate.render({
                    elem: '#updateDataDate',
                    value: fundBill.dataDate,
                    ready: function(value) {
                        disabled_date(value, '7,1')
                    },
                    change: function(value, date, endDate) {
                        disabled_date(value, '7,1')
                    },
                    trigger: 'click',
                    format: 'yyyy-MM-dd'
                });

                layer.open({
                    title: '修改收益',
                    id: 'updateProfit',
                    type: 1,
                    content: $('#update-profit-box'),
                    area: [$(window).width() <= 750 ? '80%' : '400px', '280px'],
                    resize: false,
                    cancel: function() {
                        $(document).off("click");
                    }
                });
            }
        });
    }

    // 监听工具条
    table.on('tool(fundBill-list)', function(obj) {
        var data = obj.data;
        if (obj.event == 'del') {
            layer.confirm('真的删除此条记录么？', {anim: 6}, function(index) {
                $.ajax({
                    url: '/fundBill/delete',
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
            $.ajax({
                url: '/fundBill/getById',
                type: 'GET',
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
                    openUpdateTips(result.data);
                }
            });
        }
    });

    // 头工具栏事件
    table.on('toolbar(fundBill-list)', function(obj) {
        if (obj.event == 'fundBill-input-profit') {
            // 初始化基金下拉框
            $.ajax({
                url: '/fund/getAll',
                type: 'GET',
                data: {},
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
                    var html = '<option value="">请选择基金</option>';
                    for (var i = 0; i < data.length; i++) {
                        html += '<option value="' + data[i].id + '">' + data[i].fundName + '</option>';
                    }
                    $("#fundSelect").html(html);
                    form.render("select");

                    // 初始化日期选择框
                    laydate.render({
                        elem: '#dataDate',
                        value: new Date(),
                        ready: function(value) {
                            disabled_date(value, '7,1')
                        },
                        change: function(value, date, endDate) {
                            disabled_date(value, '7,1')
                        },
                        trigger: 'click',
                        format: 'yyyy-MM-dd'
                    });

                    layer.open({
                        title: '录入收益',
                        id: 'inputProfit',
                        type: 1,
                        content: $('#input-profit-box'),
                        area: [$(window).width() <= 750 ? '80%' : '400px', '280px'],
                        resize: false,
                        cancel: function() {
                            $(document).off("click");
                        }
                    });
                }
            });
        }
    });

    // 监听提交
    form.on('submit(updateProfit-submit)', function(data) {
        var that = $(this);
        that.addClass('layui-btn-disabled'); // 禁用提交按钮
        $.ajax({
            url: '/fundBill/doUpdate',
            type: 'POST',
            data: data.field,
            dataType: 'json',
            success: function(result) {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
                if (result.code != 200) {
                    layer.open({
                        title: '系统提示',
                        anim: 6,
                        content: result.data,
                        btnAlign: 'c'
                    });
                    return;
                }
                window.parent.mainFrm.location.href = "/fundBill/index";
            }
        });
        return false;
    });

    // 监听提交
    form.on('submit(inputProfit-submit)', function(data) {
        var that = $(this);
        that.addClass('layui-btn-disabled'); // 禁用提交按钮
        $.ajax({
            url: '/fundBill/doAdd',
            type: 'POST',
            data: data.field,
            dataType: 'json',
            success: function(result) {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
                if (result.code != 200) {
                    layer.open({
                        title: '系统提示',
                        anim: 6,
                        content: result.data,
                        btnAlign: 'c'
                    });
                    return;
                }
                window.parent.mainFrm.location.href = "/fundBill/index";
            }
        });
        return false;
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

    /**
     *设置不可选择的星期
     *value:选中的值
     *appointmentDate星期：如1,2,3,4,5,6,7
     */
    function disabled_date(value, appointmentDate) {
        var mm = value.year + '-' + value.month + '-' + value.date;
        $('.laydate-theme-grid table tbody').find('[lay-ymd="' + mm + '"]').removeClass('layui-this');

        if (appointmentDate != null && appointmentDate != '') {
            var dates = appointmentDate.split(",");
            for (var i = 0; i < dates.length; i++) {
                if (dates[i] == "7") {
                    dates[i] = 0;
                }
                $("table>tbody>tr").find("td:eq(" + dates[i] + ")").addClass('ng-laydate-disabled').addClass('laydate-disabled');
            }
        }
    }

});
