//@sourceURL=/js/role/update.js

layui.use(['form', 'layedit', 'laydate'], function() {
    var form = layui.form;
    var layer = layui.layer;

    // 监听提交
    // $(".role-update-btn").on();
    form.on('submit(role-update-submit)', function(data) {
        var that = $(this);
        that.addClass('layui-btn-disabled'); // 禁用提交按钮
        $.ajax({
            url: '/role/doUpdate',
            type: 'POST',
            data: data.field,
            dataType: 'json',
            async: false,
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
                window.location.href = "/role/index";
            }
        });
        return false;
    });
});
