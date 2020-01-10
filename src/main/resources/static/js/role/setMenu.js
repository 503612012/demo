//@sourceURL=/js/role/setMenu.js
requirejs.config({
    baseUrl: '/',
    paths: {
        jquery: 'easyui/jquery.min',
        easyui: 'easyui/jquery.easyui.min',
        layui: 'layui/layui.all',
        http: 'js/common/http'
    },
    shim: {
        "layui": {exports: "layui"}
    }
});

requirejs(['jquery', 'easyui', 'layui', 'http'], function($, easyui, layui, http) {

    // 移除tree图标
    $('#permission_tree').find("span.tree-icon").removeClass('tree-icon tree-folder tree-folder-open');
    $('#permission_tree').find("span.tree-hit").removeClass('tree-expanded');
    $('#permission_tree').find("span.tree-file").removeClass('tree-file');
    $("#permission_tree span[class^='tree-icon tree-file']").remove();

    var roleId = $("#roleId").val();
    $('#permission_tree').tree({
        url: "/role/getRoleMenuTree?roleId=" + roleId,
        method: "GET",
        animate: true,
        lines: true,
        onClick: function(node) {
            if (node.state == 'closed' && (!$("#permission_tree").tree('isLeaf', node.target))) { // 状态为关闭而且非叶子节点
                $(this).tree('expand', node.target); // 点击文字展开菜单
            } else {
                if (!($("#permission_tree").tree('isLeaf', node.target))) { // 状态为打开而且为叶子节点
                    $(this).tree('collapse', node.target); // 点击文字关闭菜单
                }
            }
        },
        onCheck: function(node, checked) {
            var childList = $(this).tree('getChildren', node.target);
            if (childList.length > 0) { // 有子菜单，点击的时候级联子菜单
                var checkedTrue = function() {
                    childList.map(function(currentValue) {
                        $("#" + currentValue.domId).parent().find(".tree-checkbox").removeClass("tree-checkbox0").removeClass("tree-checkbox2").addClass("tree-checkbox1");
                    });
                };
                var checkedFalse = function() {
                    $.each(childList, function(index, currentValue) {
                        $("#" + currentValue.domId).parent().find(".tree-checkbox").removeClass("tree-checkbox1").removeClass("tree-checkbox2").addClass("tree-checkbox0");
                    });
                };
                checked == true ? checkedTrue() : checkedFalse();
            } else { // 没有子菜单，即为叶子节点，点击的时候级联父节点
                var parentNode = $('#permission_tree').tree('getParent', node.target); //得到父节点
                var checkBoxList = $("#" + node.domId).parent().parent().find(".tree-checkbox");
                if (checked == true) { // 选中子节点
                    for (var m = 0; m < checkBoxList.length; m++) {
                        if ($(checkBoxList[m]).hasClass("tree-checkbox0")) {
                            $("#" + parentNode.domId).find(".tree-checkbox").removeClass("tree-checkbox0").removeClass("tree-checkbox1").addClass("tree-checkbox2");
                            return;
                        }
                    }
                    $("#" + parentNode.domId).find(".tree-checkbox").removeClass("tree-checkbox0").removeClass("tree-checkbox2").addClass("tree-checkbox1");
                } else { // 取消选中子节点
                    for (var n = 0; n < checkBoxList.length; n++) {
                        if ($(checkBoxList[n]).hasClass("tree-checkbox1")) {
                            $("#" + parentNode.domId).find(".tree-checkbox").removeClass("tree-checkbox0").removeClass("tree-checkbox1").addClass("tree-checkbox2");
                            return;
                        }
                    }
                }
            }
        }
    });

    /**
     * 确认事件
     */
    $(".layui-btn").on("click", function() {
        var list = $('#permission_tree').find(".tree-checkbox"); // 不是获取选中的，而是获取所有节点
        var data = [];
        for (var i = 0; i < list.length; i++) {
            var menuId = $("#permission_tree").tree("getNode", $(list[i]).parent()).id;
            if ($(list[i]).hasClass("tree-checkbox2") || $(list[i]).hasClass("tree-checkbox1")) {
                data.push(menuId);
            }
        }
        var that = $(this);
        that.addClass('layui-btn-disabled'); // 禁用提交按钮
        var params = {
            "menuIds": data.toString(),
            "roleId": roleId
        };
        http.post('/role/setRoleMenu', params, function() {
            that.removeClass('layui-btn-disabled'); // 释放提交按钮
            window.parent.mainFrm.location.href = "/role/index";
        });
        return false;
    })

});