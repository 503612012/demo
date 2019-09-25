//@sourceURL=/js/pay/add.js

layui.use(['table', 'form', 'layedit'], function() {
    var table = layui.table;
    var form = layui.form;
    var layer = layui.layer;

    /**
     * 查询按钮点击事件绑定
     */
    $('.payTable .pay-search').on('click', function() {
        $('.pay-btn').addClass('hide');
        var employeeId = $('#employeeSelect').val();
        var worksiteId = $('#worksiteSelect').val();
        table.render({
            elem: '#pay-list'
            , url: '/pay/getWorkhourData/'
            , id: 'payReload'
            , even: true
            , title: '工时数据'
            , cols: [[
                {type: 'checkbox'}
                , {field: 'employeeName', title: '员工名称'}
                , {field: 'worksiteName', title: '工地名称'}
                , {field: 'workDate', title: '工时日期'}
                , {field: 'workhour', title: '录入工时'}
                , {field: 'hourSalary', title: '当日时薪'}
                , {field: 'createName', title: '录入人'}
            ]]
            , page: false
            , where: {
                employeeId: employeeId,
                worksiteId: worksiteId
            }
        });
    });

    /**
     * 监听复选框选择事件
     */
    table.on('checkbox(pay-list)', function() {
        var selectedNum = $(".layui-form-checked").length;
        if (selectedNum <= 0) {
            $('.pay-btn').addClass('hide');
        } else {
            $('.pay-btn').removeClass('hide');
        }
    });

    /**
     * 重置按钮点击事件绑定
     */
    $('.payTable .pay-reset').on('click', function() {
        $('.pay-btn').addClass('hide');
        $('#employeeSelect').val('');
        $('#worksiteSelect').val('');
        loadSelectBox();
        reload();
    });

    /**
     * 重新加载表格
     */
    var reload = function() {
        var employeeId = $('#employeeSelect');
        var worksiteId = $('#worksiteSelect');
        // 执行重载
        table.reload('payReload', {
            where: {
                employeeId: employeeId.val(),
                worksiteId: worksiteId.val()
            }
        });
    };

    /**
     * 薪资发放按钮点击事件绑定
     */
    $('.payTable .pay-btn').on('click', function() {
        var list = table.cache.payReload;
        var workhourIds = [];
        var totalWorkhour = 0;
        var totalMoney = 0;
        for (var i = 0; i < list.length; i++) {
            if (list[i].LAY_CHECKED) {
                workhourIds.push(list[i].id);
                var workhour = list[i].workhour;
                var hourSalary = list[i].hourSalary;
                totalWorkhour += workhour;
                totalMoney += (workhour * hourSalary);
            }
        }
        var notice = "本次给【<span style='color: red;'>" + list[0].employeeName + "</span>】发放薪资共计【<span style='color: red;'>" + totalWorkhour + "</span>】工时，合计【<span style='color: red;'>" + totalMoney + "</span>】元，核对无误后点击确定！";
        $("#payNoticeText").html(notice);
        layer.open({
            title: '系统提示！',
            type: 1,
            content: $('#payRemarkBox'),
            btn: ['确定', '取消'],
            area: ['500px', '300px'],
            resize: false,
            yes: function(index) {
                var remark = $('#payRemark').val();
                $.ajax({
                    url: '/pay/doPay',
                    type: 'POST',
                    data: {
                        "workhourIds": workhourIds.join(','),
                        "employeeId": list[0].employeeId,
                        "totalMoney": totalMoney,
                        "totalHour": totalWorkhour,
                        "remark": remark
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
                        layer.close(index);
                        $('.pay-btn').addClass('hide');
                        $("#payNoticeText").html('');
                        $("#payRemarkBox").addClass("hide");
                        $("#payRemarkBox").css("display", "none");
                        reload();
                    }
                });
            },
            btn2: function() {
                $("#payNoticeText").html('');
                $("#payRemarkBox").addClass("hide");
                $("#payRemarkBox").css("display", "none");
            },
            cancel: function() {
                $("#payNoticeText").html('');
                $("#payRemarkBox").addClass("hide");
                $("#payRemarkBox").css("display", "none");
            }
        });
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