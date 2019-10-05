//@sourceURL=/js/workhour/add.js

var worksiteName = '';
layui.use(['form', 'layedit', 'laydate'], function() {
    var form = layui.form;
    var layer = layui.layer;
    var laydate = layui.laydate;

    form.on('select(worksiteSelect)', function(data) {
        worksiteName = data.elem[data.elem.selectedIndex].text;
    });

    // 自定义验证规则
    form.verify({
        isInputedEmployee: function() {
            // 判断该员工该日期该工地是否有录入过
            var isInputed = false;
            var employeeId = $("#employeeSelect").val();
            var workDate = $("#workDate").val();
            var worksiteId = $("#worksiteSelect").val();
            if (employeeId == null || employeeId == '' || employeeId == undefined) {
                return '请选择员工！';
            }
            if (workDate == null || workDate == '' || workDate == undefined) {
                return;
            }
            if (worksiteId == null || worksiteId == '' || worksiteId == undefined) {
                return;
            }
            $.ajax({
                url: '/workhour/isInputed',
                type: 'POST',
                async: false,
                data: {
                    "employeeId": employeeId,
                    "workDate": workDate,
                    "worksiteId": worksiteId
                },
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
                    isInputed = result.data;
                }
            });
            if (isInputed) {
                return '该员工在[' + worksiteName + ']工地[' + workDate + ']的工时已经录入过，请删除后重新录入！';
            }
        }
    });

    form.on('select(employeeSelect)', function(data) {
        if (hasPermission(hasShowEmployeeMoneyStatusPermission)) {
            var employeeId = data.value;
            if (employeeId == '') {
                $("#hourSalary").val('');
                return;
            }
            $.ajax({
                url: '/employee/getHourSalaryByEmployeeId',
                type: 'POST',
                data: {
                    "employeeId": employeeId
                },
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
                    $("#hourSalary").val(result.data);
                }
            });
        }
    });

    // 初始化日期选择框
    laydate.render({
        elem: '#workDate',
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

    // 监听提交
    form.on('submit(workhour-add-submit)', function(data) {
        $.ajax({
            url: '/workhour/doAdd',
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
                window.parent.mainFrm.location.href = "/workhour/index";
            }
        });
        return false;
    });

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

        // 初始化下工地拉框
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
                var html = '<option value="">请选择工地</option>';
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

function checkNum() {
    var workhour = $("#workhour").val();
    if (workhour == null || workhour == '' || workhour == undefined) {
        return;
    }
    if (parseInt(workhour) > 24) {
        $("#workhour").val('24');
    }
}