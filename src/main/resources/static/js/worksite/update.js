//@sourceURL=/js/worksite/update.js

layui.use(['form', 'layedit', 'laydate'], function() {
    var form = layui.form
        , layer = layui.layer
        , layedit = layui.layedit
        , laydate = layui.laydate;

    // 监听提交
    // $(".worksite-update-btn").on();
    form.on('submit(worksite-update-submit)', function(data) {
        $.ajax({
            url: '/worksite/doUpdate',
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
                window.location.href = "/worksite/index";
            }
        });
        return false;
    });
});
