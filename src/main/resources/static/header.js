//@sourceURL=/header.js
layui.use(['form'], function() {

    var layer = layui.layer;
    var form = layui.form;
    var $ = layui.jquery;

    /**
     * 基本资料
     */
    $("#baseInfoBtn").on("click", function() {
        $.ajax({
            url: '/user/getCurrentUserInfo',
            type: 'POST',
            dataType: 'json',
            success: function(result) {
                if (result.code != 200) {
                    layer.open({
                        title: '系统提示',
                        anim: 6,
                        content: result.data,
                        btnAlign: 'c'
                    });
                    return;
                }
                var data = result.data;
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
            }
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
        key = CryptoJS.enc.Utf8.parse(key);

        var srcs = CryptoJS.enc.Utf8.parse(oldPwd);
        var encrypted = CryptoJS.AES.encrypt(srcs, key, {mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7});
        oldPwd = encrypted.toString();

        srcs = CryptoJS.enc.Utf8.parse(newPwd);
        encrypted = CryptoJS.AES.encrypt(srcs, key, {mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7});
        newPwd = encrypted.toString();

        $.ajax({
            url: '/user/changePwd',
            type: 'POST',
            data: {
                'oldPwd': oldPwd,
                'newPwd': newPwd
            },
            dataType: 'json',
            success: function(result) {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
                if (result.code != 200) {
                    layer.open({
                        title: '系统提示',
                        anim: 6,
                        content: result.data,
                        btnAlign: 'c'
                    });
                    return;
                }
                layer.open({
                    title: '系统提示',
                    content: '修改成功，请重新登录！',
                    yes: function() {
                        layer.closeAll();
                        window.parent.mainFrm.location.href = "/logout";
                    }
                });
            }
        });
        return false;
    });

    $('body').on('click', '#baseInffoBtnFrmSubmitBtn', function() {
        var that = $(this);
        that.addClass('layui-btn-disabled'); // 禁用提交按钮
        var data = form.val("updateBaseInfoForm");
        $.ajax({
            url: '/user/doUpdate',
            type: 'POST',
            data: data,
            dataType: 'json',
            success: function(result) {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
                if (result.code != 200) {
                    layer.open({
                        title: '系统提示',
                        anim: 6,
                        content: result.data,
                        btnAlign: 'c'
                    });
                    return;
                }
                layer.open({
                    title: '系统提示',
                    content: '保存成功！',
                    yes: function() {
                        layer.closeAll();
                    }
                });
            }
        });
    });

});