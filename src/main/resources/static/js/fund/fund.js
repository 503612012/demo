//@sourceURL=/js/fund/fund.js
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

    var layer = layui.layer;
    var table = layui.table;

    /**
     * 重新加载表格
     */
    var reload = function() {
        var nameReload = $('#nameReload');
        // 执行重载
        table.reload('fundReload', {
            page: {
                curr: 1 // 重新从第 1 页开始
            }
            , where: {
                fundName: nameReload.val()
            }
        });
    };

    var orderColumn = {field: 'order', title: '排序值'};
    if (hasPermission(hasChangeFundOrderPermission)) {
        orderColumn = {field: 'order', title: '排序值', edit: 'text'};
    }

    table.render({
        elem: '#fund-list'
        , url: '/fund/getByPage/'
        , toolbar: '#fundListToolBar'
        , id: 'fundReload'
        , even: true
        , cols: [[
            {type: 'numbers'}
            , {field: 'fundName', title: '基金名称', sort: true}
            , {field: 'createTime', title: '创建时间'}
            , {field: 'createName', title: '创建人'}
            , orderColumn
            , {
                field: 'status', title: '状态', templet: function(d) {
                    if (d.status == 1) {
                        return '<div><div class="layui-unselect layui-form-checkbox layui-form-checked fund-status" data-id="' + d.id + '" data-status="' + d.status + '"><span>锁定</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    } else if (d.status == 0) {
                        return '<div><div class="layui-unselect layui-form-checkbox fund-status" data-id="' + d.id + '" data-status="' + d.status + '"><span>锁定</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    }
                }
            }
            , {title: '操作', width: 120, align: 'center', toolbar: '#fundListBar'}
        ]]
        , page: true
    });

    /**
     * 查询按钮点击事件绑定
     */
    $('.fundTable .fund-search').on('click', function() {
        reload();
    });

    /**
     * 重置按钮点击事件绑定
     */
    $('.fundTable .fund-reset').on('click', function() {
        $('#nameReload').val('');
        reload();
    });

    /**
     * 更改基金状态
     */
    var updateFundStatus = function(fundId, status) {
        var params = {
            "fundId": fundId,
            "status": status
        };
        http.post('/fund/updateStatus', params, function() {
            reload();
        });
    };

    /**
     * 绑定基金状态更改点击事件
     */
    $("body").on("click", ".fund-status", function() {
        if (hasPermission(hasChangeFundStatusPermission)) {
            var id = $(this).attr("data-id");
            var status = $(this).attr("data-status");
            if (status == 0) {
                status = 1;
            } else if (status == 1) {
                status = 0;
            }
            if (status == 1) { // 锁定
                layer.confirm('确认锁定该基金吗？', {anim: 6}, function(index) {
                    updateFundStatus(id, 1);
                    layer.close(index);
                });
            }
            if (status == 0) { // 取消锁定
                layer.confirm('确认取消锁定该基金吗？', {anim: 6}, function(index) {
                    updateFundStatus(id, 0);
                    layer.close(index);
                });
            }
        }
    });

    //监听单元格编辑
    table.on('edit(fund-list)', function(obj) {
        var params = {
            order: obj.value,
            fundId: obj.data.id
        };
        http.post('/fund/updateOrder', params, function() {
            reload();
        });
    });

    // 监听工具条
    table.on('tool(fund-list)', function(obj) {
        var data = obj.data;
        if (obj.event == 'del') {
            layer.confirm('真的删除此条记录么？', {anim: 6}, function(index) {
                http.post('/fund/delete', {id: data.id}, function() {
                    layer.close(index);
                    reload();
                });
            });
        } else if (obj.event == 'edit') {
            window.parent.mainFrm.location.href = "/fund/update?id=" + data.id;
        }
    });

    // 头工具栏事件
    table.on('toolbar(fund-list)', function(obj) {
        if (obj.event == 'fund-add-btn') {
            window.parent.mainFrm.location.href = "/fund/add";
        }
    });

    common.cacheMousedown();

});
