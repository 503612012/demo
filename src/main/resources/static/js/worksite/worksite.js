//@sourceURL=/js/worksite/worksite.js

layui.use('table', function() {
    var table = layui.table;

    /**
     * 重新加载表格
     */
    var reload = function() {
        var worksiteNameReload = $('#worksiteNameReload');
        // 执行重载
        table.reload('worksiteReload', {
            page: {
                curr: 1 // 重新从第 1 页开始
            }
            , where: {
                name: worksiteNameReload.val()
            }
        });
    };

    table.render({
        elem: '#worksite-list'
        , url: '/worksite/getByPage/'
        , toolbar: '#worksiteListToolBar'
        , id: 'worksiteReload'
        , even: true
        , title: '工地数据表'
        , cols: [[
            {type: 'numbers'}
            , {field: 'name', title: '工地名称', sort: true}
            , {field: 'desc', title: '工地描述'}
            , {field: 'createTime', title: '创建时间', sort: true}
            , {field: 'createName', title: '创建人'}
            , {field: 'lastModifyTime', title: '最后修改时间', sort: true}
            , {field: 'lastModifyName', title: '最后修改人'}
            , {
                field: 'status', title: '状态', templet: function(d) {
                    if (d.status == 1) {
                        return '<div><div class="layui-unselect layui-form-checkbox layui-form-checked worksite-status" data-id="' + d.id + '" data-status="' + d.status + '"><span>锁定</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    } else if (d.status == 0) {
                        return '<div><div class="layui-unselect layui-form-checkbox worksite-status" data-id="' + d.id + '" data-status="' + d.status + '"><span>锁定</span><i class="layui-icon layui-icon-ok"></i></div></div>';
                    }
                }
            }
            , {title: '操作', toolbar: '#worksiteListBar'}
        ]]
        , page: true
    });

    var $ = layui.$;

    /**
     * 查询按钮点击事件绑定
     */
    $('.worksiteTable .worksite-search').on('click', function() {
        reload();
    });

    /**
     * 重置按钮点击事件绑定
     */
    $('.worksiteTable .worksite-reset').on('click', function() {
        $('#worksiteNameReload').val('');
        reload();
    });

    /**
     * 更改工地状态
     */
    var updateUserStatus = function(worksiteId, status) {
        $.ajax({
            url: "/worksite/updateStatus",
            type: "POST",
            data: {
                'worksiteId': worksiteId,
                'status': status
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
     * 绑定工地状态更改点击事件
     */
    $("body").on("click", ".worksite-status", function() {
        if (hasPermission("B1_03_04")) {
            var id = $(this).attr("data-id");
            var status = $(this).attr("data-status");
            if (status == 0) {
                status = 1;
            } else if (status == 1) {
                status = 0;
            }
            if (status == 1) { // 锁定
                layer.confirm('确认锁定该工地吗？', function(index) {
                    updateUserStatus(id, 1);
                    layer.close(index);
                });
            }
            if (status == 0) { // 取消锁定
                layer.confirm('确认取消锁定该工地吗？', function(index) {
                    updateUserStatus(id, 0);
                    layer.close(index);
                });
            }
        }
    });

    // 监听工具条
    table.on('tool(worksite-list)', function(obj) {
        var data = obj.data;
        if (obj.event == 'detail') {
            window.parent.mainFrm.location.href = "/worksite/worksiteMenu?worksiteId=" + data.id;
        } else if (obj.event == 'del') {
            layer.confirm('真的删除此条记录么？', function(index) {
                $.ajax({
                    url: '/worksite/delete',
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
        } else if (obj.event == 'edit') {
            window.parent.mainFrm.location.href = "/worksite/update?id=" + data.id;
        }
    });

    // 头工具栏事件
    table.on('toolbar(worksite-list)', function(obj) {
        if (obj.event == 'worksite-add-btn') {
            window.parent.mainFrm.location.href = "/worksite/add";
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
