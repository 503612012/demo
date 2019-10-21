//@sourceURL=/js/role/add.js

layui.use(['form', 'layedit', 'laydate'], function() {
    var form = layui.form;
    var layer = layui.layer;

    // 监听提交
    form.on('submit(role-add-submit)', function(data) {
        var that = $(this);
        that.addClass('layui-btn-disabled'); // 禁用提交按钮
        $.ajax({
            url: '/role/doAdd',
            type: 'POST',
            data: data.field,
            dataType: 'json',
            success: function(result) {
                that.removeClass('layui-btn-disabled'); // 释放提交按钮
                if (result.code != 200) {
                    layer.open({
                        title: '系统提示',
                        content: result.data,
                        btnAlign: 'c'
                    });
                    return;
                }
                window.parent.mainFrm.location.href = "/role/index";
            }
        });
        return false;
    });
});
