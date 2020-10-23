//@sourceURL=/js/finance/finance.js
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

    /**
     * 重新加载表格
     */
    var reload = function() {
        var worksiteName = $('#worksiteName');
        // 执行重载
        table.reload('financeReload', {
            page: {
                curr: 1 // 重新从第 1 页开始
            },
            where: {
                worksiteName: worksiteName.val()
            }
        });
    };

    table.render({
        elem: '#finance-list',
        url: '/finance/getByPage/',
        toolbar: '#financeListToolBar',
        id: 'financeReload',
        even: true,
        cols: [[
            {type: 'numbers'},
            {field: 'worksiteName', title: '工地名称', sort: true},
            {
                field: 'money', title: '登记金额', templet: function(d) {
                    return '<span class="money" data-value="' + d.money + '" style="cursor: pointer;">***</span>';
                }
            },
            {
                field: 'outMoney', title: '已支出金额', templet: function(d) {
                    return '<span class="outMoney" data-value="' + d.outMoney + '" style="cursor: pointer;">***</span>';
                }
            },
            {field: 'createName', title: '登记人'},
            {field: 'createTime', title: '登记日期'},
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
                field: 'finishFlag', title: '是否完结', sort: true, templet: function(d) {
                    if (d.finishFlag == 0) {
                        return '<div><div class="layui-unselect layui-form-checkbox layui-form-checked"><span>是</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    } else if (d.finishFlag == 1) {
                        return '<div><div class="layui-unselect layui-form-checkbox"><span>否</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    }
                }
            },
            {title: '操作', width: 80, align: 'center', toolbar: '#financeListBar'}
        ]],
        page: true,
        done: function(res) {
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

    /**
     * 显示/隐藏总登记金额
     */
    $("body").on("click", "span.in_finance", function() {
        common.showOrHide($(this), hasShowFinanceTotalInMoneyPermission);
    });

    /**
     * 显示/隐藏总登记金额
     */
    $("body").on("click", "span.out_finance", function() {
        common.showOrHide($(this), hasShowFinanceTotalOutMoneyPermission);
    });

    /**
     * 显示/隐藏金额
     */
    $("body").on("click", "span.money", function() {
        common.showOrHide($(this), hasShowFinanceMoneyPermission);
    });

    /**
     * 显示/隐藏金额
     */
    $("body").on("click", "span.outMoney", function() {
        common.showOrHide($(this), hasShowFinanceMoneyPermission);
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

    var openDialog = function(title) {
        layer.open({
            title: title,
            id: "financeDialog",
            type: 1,
            offset: '20px',
            content: $('#financeTips'),
            area: [$(window).width() <= 750 ? '60%' : '500px', '400px'],
            resize: false,
            end: function() {
                $("#financeTips").css("display", 'none');
            }
        });
    };

    form.on('submit(finance-submit)', function(data) {
        common.commitForm($(this), layer, '/finance/add', data.field, reload);
    });

    // 监听工具条
    table.on('tool(finance-list)', function(obj) {
        var data = obj.data;
        if (obj.event == 'del') {
            layer.confirm('真的删除此条记录么？', {anim: 6}, function(index) {
                http.post('/finance/delete', {id: data.id}, function() {
                    layer.close(index);
                    reload();
                });
            });
        }
    });

    // 头工具栏事件
    table.on('toolbar(finance-list)', function(obj) {
        if (obj.event == 'finance-add-btn') {
            http.get('/worksite/getAll', {}, function(data) {
                common.initWorksiteSelectBox(data, $("#worksiteSelect"), form);
            });
            $('#money').val('');
            $('#remark').val('');
            openDialog('财务登记');
        }
    });

    common.cacheMousedown();

});
