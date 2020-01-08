//@sourceURL=/js/workhour/workhour.js
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
            , {title: '操作', width: 80, align: 'center', toolbar: '#workhourListBar'}
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

    /**
     * 显示/隐藏金额
     */
    $("body").on("click", "span.hourSalary", function() {
        common.showOrHide($(this), hasShowWorkhourMoneyPermission);
    });

    /**
     * 显示/隐藏总工时
     */
    $("body").on("click", "span.totalWorkhour", function() {
        common.showOrHide($(this), hasShowWorkhourTotalWorkhourPermission);
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
                http.post('/workhour/delete', {id: data.id}, function() {
                    layer.close(index);
                    reload();
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

    common.cacheMousedown();

});
