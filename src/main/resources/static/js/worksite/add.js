//@sourceURL=/js/worksite/add.js

layui.use(['form', 'layedit', 'laydate'], function() {
    var form = layui.form
        , layer = layui.layer
        , layedit = layui.layedit
        , laydate = layui.laydate;

    // 监听提交
    form.on('submit(worksite-add-submit)', function(data) {
        $.ajax({
            url: '/worksite/doAdd',
            type: 'POST',
            data: data.field,
            dataType: 'json',
            success: function(result) {
                if (result.code != 200) {
                    layer.open({
                        title: '系统提示',
                        content: result.data,
                        btnAlign: 'c'
                    });
                    return;
                }
                window.parent.mainFrm.location.href= "/worksite/index";
            }
        });
        return false;
    });
});
