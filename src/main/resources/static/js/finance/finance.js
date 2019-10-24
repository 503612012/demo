//@sourceURL=/js/finance/finance.js

layui.use(['table', 'laydate'], function() {
    var table = layui.table;

    /**
     * 重新加载表格
     */
    var reload = function() {
        var worksiteName = $('#worksiteName');
        // 执行重载
        table.reload('financeReload', {
            page: {
                curr: 1 // 重新从第 1 页开始
            }
            , where: {
                worksiteName: worksiteName.val()
            }
        });
    };

    table.render({
        elem: '#finance-list'
        , url: '/finance/getByPage/'
        , toolbar: '#financeListToolBar'
        , id: 'financeReload'
        , even: true
        , cols: [[
            {type: 'numbers'}
            , {field: 'worksiteName', title: '工地名称', sort: true}
            , {
                field: 'money', title: '登记金额', templet: function(d) {
                    return '<span class="money" data-value="' + d.money + '" style="cursor: pointer;">***</span>';
                }
            }
            , {
                field: 'outMoney', title: '已支出金额', templet: function(d) {
                    return '<span class="outMoney" data-value="' + d.outMoney + '" style="cursor: pointer;">***</span>';
                }
            }
            , {field: 'createName', title: '登记人'}
            , {field: 'createTime', title: '登记日期'}
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
                field: 'finishFlag', title: '是否完结', sort: true, templet: function(d) {
                    if (d.finishFlag == 0) {
                        return '<div><div class="layui-unselect layui-form-checkbox layui-form-checked"><span>是</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    } else if (d.finishFlag == 1) {
                        return '<div><div class="layui-unselect layui-form-checkbox"><span>否</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    }
                }
            }
            , {title: '操作', toolbar: '#financeListBar'}
        ]]
        , page: true
        , done: function(res) {
            var html = "<div style='margin-left: 20px; font-weight: bold; color: red;'>本页";
            if (hasPermission(hasShowFinanceTotalInMoneyPermission)) {
                $('#layui-table-page1').css("display", "flex");
                var totalInFinance = 0;
                for (var i = 0; i < res.data.length; i++) {
                    totalInFinance += res.data[i].money;
                }
                html += "总登记金额为：<span style='cursor: pointer;' data-value='" + totalInFinance + "元' class='in_finance'>***</span>";
            }
            if (hasPermission(hasShowFinanceTotalOutMoneyPermission)) {
                $('#layui-table-page1').css("display", "flex");
                var totalOutFinance = 0;
                for (var n = 0; n < res.data.length; n++) {
                    totalOutFinance += res.data[n].outMoney;
                }
                if (hasPermission(hasShowFinanceTotalInMoneyPermission)) {
                    html += "<span style='margin-left: 20px;'>总支出金额为：<span style='cursor: pointer;' data-value='" + totalOutFinance + "元' class='out_finance'>***</span></span>";
                } else {
                    html += "总支出薪资为：<span style='cursor: pointer;' data-value='" + totalOutFinance + "元' class='out_finance'>***</span>";
                }
            }
            html += '</div>';
            $('#layui-table-page1').append(html);
        }
    });

    var $ = layui.$;

    /**
     * 显示/隐藏总登记金额
     */
    $("body").on("click", "span.in_finance", function() {
        if (hasPermission(hasShowFinanceTotalInMoneyPermission)) {
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
     * 显示/隐藏总登记金额
     */
    $("body").on("click", "span.out_finance", function() {
        if (hasPermission(hasShowFinanceTotalOutMoneyPermission)) {
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
        if (hasPermission(hasShowFinanceMoneyPermission)) {
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
    $("body").on("click", "span.outMoney", function() {
        if (hasPermission(hasShowFinanceMoneyPermission)) {
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
    $('.financeTable .finance-search').on('click', function() {
        reload();
    });

    /**
     * 重置按钮点击事件绑定
     */
    $('.financeTable .finance-reset').on('click', function() {
        $('#worksiteName').val('');
        reload();
    });

    // 监听工具条
    table.on('tool(finance-list)', function(obj) {
        var data = obj.data;
        if (obj.event == 'del') {
            layer.confirm('真的删除此条记录么？', {anim: 6}, function(index) {
                $.ajax({
                    url: '/finance/delete',
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
    table.on('toolbar(finance-list)', function(obj) {
        if (obj.event == 'finance-add-btn') {
            window.parent.mainFrm.location.href = "/finance/add";
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
