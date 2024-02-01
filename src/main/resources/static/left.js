//@sourceURL=/left.js
// noinspection DuplicatedCode

define(['jquery'], function($) {

    var isShow = true;  // 定义一个标志位

    /**
     * 手机端缩回菜单栏
     */
    function mobileDownMenu() {
        // 操作logo部分
        $("#logoBox i,.mobile-shadow").hide();
        $("#logoutBtn,#currentUserSpan").show();
        $("#logoWord").addClass("hide");

        $('.layui-side.layui-bg-black,#logoBox').animate({'width': '0'}); // 设置宽度
        $('.layui-body,.layui-footer,#searchBox,.mobile-shadow').animate({'left': '0'});
        // 将二级导航栏隐藏
        $('dd span').each(function() {
            $(this).hide();
        });
        $('body').find(".layui-icon-shrink-right").removeClass("layui-icon-shrink-right").addClass("layui-icon-spread-left");
        $('body').find("li.layui-nav-itemed a.level-one-menu").click();
        $('body').find("a.level-one-menu cite").addClass("hide");
        isShow = false;
    }

    /**
     * 填充菜单
     */
    var fillMenus = function(data) {
        var mainPage = null;
        var html = '';
        for (var i = 0; i < data.length; i++) {
            html += '<li data-name="" data-jump="" class="layui-nav-item ' + (i == 0 ? 'layui-nav-itemed' : '') + '">';
            if (data[i].children.length > 0) {
                html += '<a href="javascript:" lay-tips="' + data[i].menu.menuName + '" lay-direction="2" class="level-one-menu">';
            } else {
                html += '<a data-icon="' + data[i].menu.iconCls + '" href="' + data[i].menu.url + '" target="mainFrm" lay-tips="' + data[i].menu.menuName + '" lay-direction="2" class="level-one-menu menu-border-left path-menu-item">';
            }
            html += '<i class="layui-icon ' + data[i].menu.iconCls + '"></i>';
            html += '<cite>&emsp;' + data[i].menu.menuName + '</cite>';
            if (data[i].children.length > 0) {
                html += '<span class="layui-nav-more layui-icon layui-icon-down"></span>';
            }
            html += '</a>';
            for (var j = 0; j < data[i].children.length; j++) {
                if (data[i].children[j].open == true) {
                    mainPage = data[i].children[j].url;
                }
                html += '<dl class="layui-nav-child" style="padding: 0;">';
                html += '<dd data-name="" data-jump="/" class="level-two-menu ' + ((i == 0 && j == 0) == true ? 'layui-this' : '') + '">';
                html += '<a class="path-menu-item" data-parent-icon="' + data[i].menu.iconCls + '" data-parent-name="' + data[i].menu.menuName + '" href="' + data[i].children[j].url + '" target="mainFrm">' + data[i].children[j].menuName + '</a>'
                html += '</dd>';
                html += '</dl>';
            }
            html += '</li>';
        }
        $("#user-menus").html(html).find(".level-one-menu").on("click", function() {
            $(this).parent().toggleClass("layui-nav-itemed");
            $(".menu-border-left").removeClass("menu-border-left");
            $(this).addClass("menu-border-left");
            $(".layui-this").removeClass("layui-this");
        });
        $("#user-menus").find(".level-two-menu").on("click", function() {
            if ($(window).width() < 750) {
                mobileDownMenu();
            }
            $(".layui-this").removeClass("layui-this");
            $(this).addClass("layui-this");
            $(".menu-border-left").removeClass("menu-border-left");
            $(this).parent().parent().find(".level-one-menu").addClass("menu-border-left");
        });
        return mainPage;
    };

    $('body').on('click', '.path-menu-item', function() {
        var parentName = $(this).attr('data-parent-name');
        var parentIcon = $(this).attr('data-parent-icon');
        var name;
        if (parentName == null || parentName == '' || parentName == undefined) {
            var icon = $(this).attr('data-icon');
            name = $(this).children('cite').html();
            $('#menuPath').html('<span style="cursor: default;"><i style="margin: 0 5px;" class="layui-icon ' + icon + '"></i>' + name.trim() + '</span>');
        } else {
            name = $(this).html();
            $('#menuPath').html('<span style="cursor: default;"><i style="margin: 0 5px;" class="layui-icon ' + parentIcon + '"></i>' + parentName + '  /  ' + name + '</span>');
        }
    });

    function initMenu(data) {
        if ($(window).width() < 750) {
            mobileDownMenu();
            $("body").on("click", ".mobile-shadow", function() {
                mobileDownMenu();
            });
        }

        fillMenus(data);

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
                if ($(window).width() < 750) {
                    mobileDownMenu();
                } else {
                    $("#logoWord").addClass("hide");
                    $('.layui-side.layui-bg-black,#logoBox').animate({'width': '55px'}); // 设置宽度
                    $('.layui-footer,.layui-body,#searchBox').animate({'left': '55px'});
                    // 将二级导航栏隐藏
                    $('dd span').each(function() {
                        $(this).hide();
                    });
                    $('body').find(".layui-icon-shrink-right").removeClass("layui-icon-shrink-right").addClass("layui-icon-spread-left");
                    $('body').find("li.layui-nav-itemed a.level-one-menu").click();
                    $('body').find("a.level-one-menu cite").addClass("hide");
                    isShow = false;
                }
            } else { // 展开
                if ($(window).width() < 750) {
                    $("#logoutBtn,#currentUserSpan").hide();
                    $(".layui-side-scroll .layui-nav-more,.mobile-shadow").show();
                }
                $("#logoBox i").show();
                $("#logoWord").removeClass("hide");

                $('.layui-side.layui-bg-black,#logoBox').animate({'width': '200px'});
                $('.layui-body,.mobile-shadow,#searchBox,.layui-footer').animate({'left': '200px'});
                $('dd span').each(function() {
                    $(this).show();
                });
                $('body').find(".layui-icon-spread-left").removeClass("layui-icon-spread-left").addClass("layui-icon-shrink-right");
                $('body').find("a.level-one-menu cite").removeClass("hide");
                isShow = true;
            }
        });
    }

    return {
        initMenu: initMenu
    }

});