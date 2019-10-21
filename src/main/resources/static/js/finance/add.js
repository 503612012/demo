//@sourceURL=/js/finance/add.js

var worksiteName = '';
layui.use(['form', 'layedit'], function() {
    var form = layui.form;
    var layer = layui.layer;

    // 监听提交
    form.on('submit(finance-add-submit)', function(data) {
        var that = $(this);
        that.addClass('layui-btn-disabled'); // 禁用提交按钮
        $.ajax({
            url: '/finance/doAdd',
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
                window.parent.mainFrm.location.href = "/finance/index";
            }
        });
        return false;
    });

    /**
     * 初始化工地下拉框
     */
    function loadSelectBox() {
        $.ajax({
            url: '/worksite/getAll',
            type: 'GET',
            data: {},
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
                var data = result.data;
                var html = '<option value="">请选择员工</option>';
                for (var i = 0; i < data.length; i++) {
                    html += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
                }
                $("#worksiteSelect").html(html);
                form.render("select");
            }
        });
    }

    loadSelectBox();

});