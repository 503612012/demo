//@sourceURL=/left.js
$(function () {

    /**
     * 填充菜单
     */
    var fillMenus = function(data) {
        var html = '';
        for (var i = 0; i < data.length; i++) {
            html += '<li data-name="" data-jump="" class="layui-nav-item ' + (i == 0 ? 'layui-nav-itemed' : '') + '">';
            html += '<a href="javascript:" lay-tips="主页" lay-direction="2">';
            html += '<i class="layui-icon layui-icon-home"></i>';
            html += '<cite>' + data[i].menu.menuName + '</cite>';
            html += '<span class="layui-nav-more"></span>';
            html += '</a>';
            for (var j = 0; j < data[i].children.length; j++) {
                html += '<dl class="layui-nav-child">';
                html += '<dd data-name="" data-jump="/"><a href="javascript:" lay-href="/">' + data[i].children[j].menuName + '</a></dd>';
                html += '</dl>';
            }
            html += '</li>';
        }
        $("#user-menus").html(html);
    };

    /**
     * 获取当前登录用户的菜单
     */
    $.ajax({
        url: "/getMenus",
        type: "GET",
        data: {},
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
            fillMenus(result.data);
        }
    });

});