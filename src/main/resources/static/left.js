//@sourceURL=/left.js
$(function () {

    /**
     * 填充菜单
     */
    var fillMenus = function(data) {
        var html = '';
        for (var i = 0; i < data.length; i++) {
            html += '<li data-name="" data-jump="" class="layui-nav-item ' + (i == 0 ? 'layui-nav-itemed' : '') + '">';
            html += '<a href="javascript:" lay-tips="主页" lay-direction="2" class="level-one-menu">';
            html += '<i class="layui-icon ' + data[i].menu.iconCls + '"></i>';
            html += '<cite>&emsp;' + data[i].menu.menuName + '</cite>';
            if (data[i].children.length > 0) {
                html += '<span class="layui-nav-more"></span>';
            }
            html += '</a>';
            for (var j = 0; j < data[i].children.length; j++) {
                html += '<dl class="layui-nav-child">';
                html += '<dd data-name="" data-jump="/" class="level-two-menu ' + ((i == 0 && j == 0) == true ? 'layui-this' : '') + '"><a href="' + data[i].children[j].url + '" target="mainFrm">' + data[i].children[j].menuName + '</a></dd>';
                html += '</dl>';
            }
            html += '</li>';
        }
        $("#user-menus").html(html).find(".level-one-menu").on("click", function() {
            $(this).parent().toggleClass("layui-nav-itemed");
            $(".menu-border-left").removeClass("menu-border-left");
            $(this).addClass("menu-border-left");
        });
        $("#user-menus").find(".level-two-menu").on("click", function() {
            $(".layui-this").removeClass("layui-this");
            $(this).addClass("layui-this");
            $(".menu-border-left").removeClass("menu-border-left");
            $(this).parent().parent().find(".level-one-menu").addClass("menu-border-left");
        });
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