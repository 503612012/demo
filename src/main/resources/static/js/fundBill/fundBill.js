//@sourceURL=/js/fundBill/fundBill.js
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

    var laydate = layui.laydate;
    var layer = layui.layer;
    var table = layui.table;
    var form = layui.form;

    // 初始化日期选择框
    laydate.render({
        elem: '#dataDateReload',
        ready: function(value) {
            common.disabledDate(value, '7,1')
        },
        change: function(value) {
            common.disabledDate(value, '7,1')
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
        , limit: 9
        , id: 'fundBillReload'
        , even: true
        , cols: [[
            {type: 'numbers'}
            , {field: 'fundName', title: '基金名称', sort: true}
            , {field: 'dataDate', title: '收益时间'}
            , {
                field: 'money', title: '收益金额', templet: function(d) {
                    if (d.money > 0) {
                        return '<span style="color: red;">' + d.money + '</span>';
                    } else {
                        return '<span style="color: green;">' + d.money + '</span>';
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
        http.get('/fund/getAll', {}, function(data) {
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
                    common.disabledDate(value, '7,1')
                },
                change: function(value) {
                    common.disabledDate(value, '7,1')
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
        });
    }

    // 监听工具条
    table.on('tool(fundBill-list)', function(obj) {
        var data = obj.data;
        if (obj.event == 'del') {
            layer.confirm('真的删除此条记录么？', {anim: 6}, function(index) {
                http.post('/fundBill/delete', {id: data.id}, function() {
                    layer.close(index);
                    reload();
                });
            });
        } else if (obj.event == 'edit') {
            http.get('/fundBill/getById', {id: data.id}, function(data) {
                openUpdateTips(data);
            });
        }
    });

    // 头工具栏事件
    table.on('toolbar(fundBill-list)', function(obj) {
        if (obj.event == 'fundBill-input-profit') {
            // 初始化基金下拉框
            http.get('/fund/getAll', {}, function(data) {
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
                        common.disabledDate(value, '7,1')
                    },
                    change: function(value) {
                        common.disabledDate(value, '7,1')
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
            });
        }
    });

    // 监听提交
    form.on('submit(updateProfit-submit)', function(data) {
        var that = $(this);
        if (!that.hasClass('layui-btn-disabled')) {
            that.addClass('layui-btn-disabled'); // 禁用提交按钮
            http.post('/fundBill/doUpdate', data.field, function() {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
                window.parent.mainFrm.location.href = "/fundBill/index";
            }, function() {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
            });
        }
        return false;
    });

    // 监听提交
    form.on('submit(inputProfit-submit)', function(data) {
        var that = $(this);
        if (!that.hasClass('layui-btn-disabled')) {
            that.addClass('layui-btn-disabled'); // 禁用提交按钮
            http.post('/fundBill/doAdd', data.field, function() {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
                window.parent.mainFrm.location.href = "/fundBill/index";
            }, function() {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
            });
        }
        return false;
    });

    common.cacheMousedown();

});
