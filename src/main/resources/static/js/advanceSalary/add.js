//@sourceURL=/js/advanceSalary/add.js

var worksiteName = '';
layui.use(['form', 'layedit', 'laydate'], function() {
    var form = layui.form;
    var layer = layui.layer;
    var laydate = layui.laydate;

    // 监听提交
    form.on('submit(advanceSalary-add-submit)', function(data) {
        $.ajax({
            url: '/advanceSalary/doAdd',
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
                window.parent.mainFrm.location.href = "/advanceSalary/index";
            }
        });
        return false;
    });

    // 初始化日期选择框
    laydate.render({
        elem: '#advanceTime',
        format: 'yyyy-MM-dd',
        max: getNowFormatDate()
    });

    function getNowFormatDate() {
        var date = new Date();
        var seperator1 = "-";
        var seperator2 = ":";
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        return date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
    }

    function loadSelectBox() {
        // 初始化员工下拉框
        $.ajax({
            url: '/employee/getAll',
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
                $("#employeeSelect").html(html);
                form.render("select");
            }
        });
    }

    loadSelectBox();
});