//@sourceURL=/header.js
requirejs.config({
    baseUrl: '/',
    paths: {
        jquery: 'js/lib/jquery.min',
        layui: 'layui/layui',
        jsencrypt: 'js/lib/jsencrypt.min',
        http: 'js/common/http',
        common: 'js/common/common',
        left: 'left'
    },
    shim: {
        layui: {exports: "layui"},
        jsencrypt: {exports: "JSEncrypt"}
    }
});

requirejs(['jquery', 'jsencrypt', 'layui', 'http', 'common', 'left'], function($, JSEncrypt, layui, http, common, left) {

    var layer = layui.layer;
    var form = layui.form;
    var element = layui.element;
    var upload = layui.upload;

    /**
     * 修改主题
     */
    $('.userTheme').on('click', function() {
        var userTheme = $(this).attr('data');
        http.post('/user/userTheme', {'userTheme': userTheme}, function() {
            window.location.reload();
        });
    });

    /**
     * 修改菜单位置
     */
    $('.menuPosition').on('click', function() {
        var menuPosition = $(this).attr('data');
        http.post('/user/menuPosition', {'menuPosition': menuPosition}, function() {
            window.location.reload();
        });
    });

    var uploadAvatar = upload.render({
        elem: '#uploadAvatar',
        url: '/user/uploadAvatar',
        before: function(obj) {
            obj.preview(function(index, file, result) {
                $('img.userAvatar').attr('src', result);
            });
        },
        done: function(result) {
            if (result.code != 200) {
                return layer.msg('上传失败');
            }
        },
        error: function() {
            var avatarText = $('#avatarText');
            avatarText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs avatar-reload">重试</a>');
            avatarText.find('.avatar-reload').on('click', function() {
                uploadAvatar.upload();
            });
        }
    });

    /**
     * 基本资料
     */
    $("#baseInfoBtn").on("click", function() {
        http.post('/user/getCurrentUserInfo', {}, function(data) {
            $('input[name=id]').val(data.id);
            $('input[name=userName]').val(data.userName);
            $('input[name=nickName]').val(data.nickName);
            $('input[name=age]').val(data.age);
            $('input[name=email]').val(data.email);
            $('input[name=phone]').val(data.phone);

            if (data.gender == 1) { // 男
                $("input[name='gender'][value='1']").prop('checked', true);
            } else if (data.gender == 0) { // 女
                $("input[name='gender'][value='0']").prop('checked', true);
            }
            form.render();
            layer.open({
                id: 'updateBaseInfoFrm',
                type: 1,
                title: '修改基本资料',
                area: [$(window).width() <= 750 ? '80%' : '700px', 'auto'],
                content: $('#baseInfoBtnFrm')
            });
        });
    });

    /**
     * 修改密码
     */
    $("#changePwdBtn").on("click", function() {
        layer.open({
            title: '修改密码',
            id: 'changePwd',
            type: 1,
            content: $('#changePwdTips'),
            area: [$(window).width() <= 750 ? '80%' : '400px', '280px'],
            resize: false,
            cancel: function() {
                $(document).off("click");
            }
        });
    });

    $("body").on("click", "#changePwd-reset", function() {
        $("input[name=oldPwd]").val('');
        $("input[name=newPwd]").val('');
        $("input[name=confirmPwd]").val('');
    });

    // 监听提交
    $("body").on('click', '#changePwd-submit', function() {
        var oldPwd = $("input[name=oldPwd]").val();
        if (oldPwd == null || oldPwd == '' || oldPwd == undefined) {
            common.open('请输入原始密码！');
            return;
        }
        var newPwd = $("input[name=newPwd]").val();
        if (newPwd == null || newPwd == '' || newPwd == undefined) {
            common.open('请输入新密码！');
            return;
        }
        var confirmPwd = $("input[name=confirmPwd]").val();
        if (confirmPwd == null || confirmPwd == '' || confirmPwd == undefined) {
            common.open('请输入确认密码！');
            return;
        }
        if (newPwd != confirmPwd) {
            common.open('两次输入的密码不一致！');
            return;
        }
        var that = $("#changePwd-submit");

        if (!that.hasClass('layui-btn-disabled')) {
            that.addClass('layui-btn-disabled'); // 禁用提交按钮

            var encrypt = new JSEncrypt();
            encrypt.setPublicKey('-----BEGIN PUBLIC KEY-----\n' + $("input[name=key]").val() + '\n-----END PUBLIC KEY-----');
            oldPwd = encrypt.encrypt(oldPwd);
            newPwd = encrypt.encrypt(newPwd);

            var params = {
                'oldPwd': oldPwd,
                'newPwd': newPwd
            };
            http.post('/user/changePwd', params, function() {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
                layer.open({
                    title: '系统提示',
                    content: '修改成功，请重新登录！',
                    yes: function() {
                        layer.closeAll();
                        window.parent.mainFrm.location.href = "/logout";
                    }
                });
            }, function() {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
            });
        }
        return false;
    });

    $('body').on('click', '#baseInfoBtnFrmSubmitBtn', function() {
        var that = $(this);
        if (!that.hasClass('layui-btn-disabled')) {
            that.addClass('layui-btn-disabled'); // 禁用提交按钮
            var data = form.val("updateBaseInfoForm");
            http.post('/user/doUpdate', data, function() {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
                layer.open({
                    title: '系统提示',
                    content: '保存成功！',
                    yes: function() {
                        layer.closeAll();
                    }
                });
            }, function() {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
            });
        }
    });

    var initHeaderMenu = function(result) {
        var html = '<li class="layui-nav-item"><a href="/">首页</a></li>';
        for (var i = 0; i < result.length; i++) {
            if (result[i].children != null && result[i].children.length > 0) { // 有子菜单
                html += '<li  class="layui-nav-item">';
                html += '<a href="javascript:">' + result[i].menu.menuName + '</a>';
                html += '<dl class="layui-nav-child">';
                for (var j = 0; j < result[i].children.length; j++) {
                    html += '<dd><a href="' + result[i].children[j].url + '" target="mainFrm">' + result[i].children[j].menuName + '</a></dd>';
                }
                html += '</dl>';
                html += '</li>';
            } else { // 无子菜单
                html += '<li class="layui-nav-item"><a href="' + result[i].menu.url + '" target="mainFrm">' + result[i].menu.menuName + '</a></li>';
            }
        }
        $('#searchBox').html(html);
        element.render();
    };

    http.get('/getMenus', {}, function(data) {
        if (menuPosition === 'header') {
            initHeaderMenu(data);
        } else {
            left.initMenu(data);
        }
    });

});