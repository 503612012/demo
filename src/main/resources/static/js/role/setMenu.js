//@sourceURL=/js/role/setMenu.js
requirejs.config({
    baseUrl: '/',
    paths: {
        jquery: 'js/lib/jquery.min',
        layui: 'layui/layui.all',
        http: 'js/common/http',
        eleTree: 'js/lib/eleTree'
    },
    shim: {
        layui: {exports: "layui"},
        eleTree: {
            deps: ['layui'],
            exports: "eleTree"
        }
    }
});

requirejs(['jquery', 'layui', 'http', 'eleTree'], function($, layui, http, eleTree) {

    var tree;

    var roleId = $("#roleId").val();
    http.get('/role/getRoleMenuTree', {roleId: roleId}, function(data) {
        tree = eleTree.render({
            elem: '.permission_tree',
            highlightCurrent: true,
            checkStrictly: true,
            showLine: true,
            data: data,
            showCheckbox: true,
        });
    });

    var pushMenuId = function(data, obj) {
        data.push(obj.id);
        if (obj.children != null && obj.children.length >= 0) {
            for (var i = 0; i < obj.children.length; i++) {
                pushMenuId(data, obj.children[i]);
            }
        }
    };

    /**
     * 确认事件
     */
    $(".layui-btn").on("click", function() {
        var that = $(this);
        var list = tree.getChecked(false, true);
        var data = [];
        for (var i = 0; i < list.length; i++) {
            data.push(list[i].id);
        }
        if (!that.hasClass('layui-btn-disabled')) {
            that.addClass('layui-btn-disabled'); // 禁用提交按钮
            var params = {
                "menuIds": data.toString(),
                "roleId": roleId
            };
            http.post('/role/setRoleMenu', params, function() {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
                window.parent.mainFrm.location.href = "/role/index";
            }, function() {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
            });
        }
    });

});