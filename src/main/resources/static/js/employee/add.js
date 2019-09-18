//@sourceURL=/js/employee/add.js

layui.use(['form', 'layedit', 'laydate'], function() {
    var form = layui.form;
    var layer = layui.layer;

    // 监听提交
    form.on('submit(employee-add-submit)', function(data) {
        $.ajax({
            url: '/employee/doAdd',
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
                window.parent.mainFrm.location.href = "/employee/index";
            }
        });
        return false;
    });
});
