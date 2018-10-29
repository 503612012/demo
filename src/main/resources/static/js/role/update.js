//@sourceURL=/js/role/update.js

layui.use(['form', 'layedit', 'laydate'], function() {
    var form = layui.form
        , layer = layui.layer
        , layedit = layui.layedit
        , laydate = layui.laydate;

    // 监听提交
    // $(".role-update-btn").on();
    form.on('submit(role-update-submit)', function(data) {
        $.ajax({
            url: '/role/doUpdate',
            type: 'POST',
            data: data.field,
            dataType: 'json',
            async: false,
            success: function(result) {
                if (result.code != 200) {
                    layer.open({
                        title: '系统提示',
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
