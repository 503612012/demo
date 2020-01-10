//@sourceURL=/header.js
requirejs.config({
    baseUrl: '/',
    paths: {
        jquery: 'easyui/jquery.min',
        layui: 'layui/layui.all',
        crypto: 'js/lib/crypto-js',
        http: 'js/common/http',
        left: 'left'
    },
    shim: {
        "layui": {exports: "layui"},
        "crypto": {exports: "crypto"}
    }
});

requirejs(['jquery', 'crypto', 'layui', 'http', 'left'], function($, crypto, layui, http, left) {

    var layer = layui.layer;
    var form = layui.form;

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
                area: [$(window).width() <= 750 ? '80%' : '500px', 'auto'],
                content: $('#baseInffoBtnFrm')
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
            layer.open({
                title: '系统提示',
                anim: 6,
                content: '请输入原始密码！',
                btnAlign: 'c'
            });
            return;
        }
        var newPwd = $("input[name=newPwd]").val();
        if (newPwd == null || newPwd == '' || newPwd == undefined) {
            layer.open({
                title: '系统提示',
                anim: 6,
                content: '请输入新密码！',
                btnAlign: 'c'
            });
            return;
        }
        var confirmPwd = $("input[name=confirmPwd]").val();
        if (confirmPwd == null || confirmPwd == '' || confirmPwd == undefined) {
            layer.open({
                title: '系统提示',
                anim: 6,
                content: '请输入确认密码！',
                btnAlign: 'c'
            });
            return;
        }
        if (newPwd != confirmPwd) {
            layer.open({
                title: '系统提示',
                anim: 6,
                content: '两次输入的密码不一致！',
                btnAlign: 'c'
            });
            return;
        }
        var that = $("#changePwd-submit");
        that.addClass('layui-btn-disabled'); // 禁用提交按钮
        var key = $("input[name=key]").val();
        key = crypto.enc.Utf8.parse(key);

        var srcs = crypto.enc.Utf8.parse(oldPwd);
        var encrypted = crypto.AES.encrypt(srcs, key, {mode: crypto.mode.ECB, padding: crypto.pad.Pkcs7});
        oldPwd = encrypted.toString();

        srcs = crypto.enc.Utf8.parse(newPwd);
        encrypted = crypto.AES.encrypt(srcs, key, {mode: crypto.mode.ECB, padding: crypto.pad.Pkcs7});
        newPwd = encrypted.toString();

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
        });
        return false;
    });

    $('body').on('click', '#baseInffoBtnFrmSubmitBtn', function() {
        var that = $(this);
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
        });
    });

    http.get('/getMenus', {}, function(data) {
        left.initMenu(data);
    });

});