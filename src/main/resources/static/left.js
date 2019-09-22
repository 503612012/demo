//@sourceURL=/left.js
$(function() {

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
            if (result.code !== 200) {
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

    var isShow = true;  // 定义一个标志位
    // 点击一级标题的时候打开缩回去的边栏
    $("body").on("click", ".level-one-menu", function() {
        if (!isShow) {
            $('.kit-side-fold').click();
        }
    });

    $("body").on("click", "#logoBox", function() {
        window.location.href = "/";
    });

    $('.kit-side-fold').click(function() {
        // 选择出所有的span，并判断是不是hidden
        $('.layui-nav-item span').each(function() {
            if ($(this).is(':hidden')) {
                $(this).show();
            } else {
                $(this).hide();
            }
            $("ul.layui-layout-right .layui-nav-item span").show();
        });
        // 判断isshow的状态
        if (isShow) { // 缩回去
            // 操作logo部分
            $("#logoBox").animate({"width": "55px"});
            $("#searchBox").animate({"left": "55px"});
            $("#logoWord").addClass("hide");

            $('.layui-side.layui-bg-black').animate({'width': '55px'}); // 设置宽度
            // $('.kit-side-fold i').animate({'margin-right': '70%'});  // 修改图标的位置
            // 将footer和body的宽度修改
            $('.layui-body').animate({'left': '55px'});
            $('.layui-footer').animate({'left': '55px'});
            // 将二级导航栏隐藏
            $('dd span').each(function() {
                $(this).hide();
            });
            // 修改标志位
            // $('body').find(".layui-nav-itemed").removeClass("layui-nav-iteme");
            $('body').find(".layui-icon-shrink-right").removeClass("layui-icon-shrink-right").addClass("layui-icon-spread-left");
            $('body').find("li.layui-nav-itemed a.level-one-menu").click();
            $('body').find("a.level-one-menu cite").addClass("hide");
            isShow = false;
        } else { // 展开
            // 操作logo部分
            $("#logoBox").animate({"width": "200px"});
            $("#searchBox").animate({"left": "200px"});
            $("#logoWord").removeClass("hide");

            $('.layui-side.layui-bg-black').animate({'width': '200px'});
            // $('.kit-side-fold i').animate({'margin-right': '10%'});
            $('.layui-body').animate({'left': '200px'});
            $('.layui-footer').animate({'left': '200px'});
            $('dd span').each(function() {
                $(this).show();
            });
            $('body').find(".layui-icon-spread-left").removeClass("layui-icon-spread-left").addClass("layui-icon-shrink-right");
            $('body').find("a.level-one-menu cite").removeClass("hide");
            isShow = true;
        }
    });

});
