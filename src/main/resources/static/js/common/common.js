define(['jquery'], function($) {

    /**
     * 获取当前日期
     */
    function getCurrentDate(type) {
        var date = new Date();
        if (type == 1) {
            return date.getFullYear();
        } else if (type == 2) {
            var seperator = "-";
            var month = date.getMonth() + 1;
            if (month >= 1 && month <= 9) {
                month = "0" + month;
            }
            return date.getFullYear() + seperator + month;
        }
    }

    /**
     * 格式化日期
     */
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

    /**
     * 缓存当前操作的是哪个表格的哪个tr的哪个td
     */
    function cacheMousedown() {
        $(document).off('mousedown', '.layui-table-grid-down').on('mousedown', '.layui-table-grid-down', function() {
            table._tableTrCurr = $(this).closest('td');
        });

        $(document).off('click', '.layui-table-tips-main [lay-event]').on('click', '.layui-table-tips-main [lay-event]', function() {
            var elem = $(this);
            var tableTrCurr = table._tableTrCurr;
            if (!tableTrCurr) {
                return;
            }
            var layerIndex = elem.closest('.layui-table-tips').attr('times');
            // 关闭当前这个显示更多的tip
            layer.close(layerIndex);
            table._tableTrCurr.find('[lay-event="' + elem.attr('lay-event') + '"]')[0].click();
        });
    }

    /**
     * 显示或者隐藏
     */
    function showOrHide(element, permission) {
        if (hasPermission(permission)) {
            if (element.hasClass("red")) { // 隐藏
                element.removeClass("red");
                element.html("***");
            } else {
                element.addClass("red");
                element.html(element.attr("data-value"));
            }
        }
    }

    /**
     * 参数说明：
     * number：要格式化的数字
     * decimals：保留几位小数
     * dec_point：小数点符号
     * thousands_sep：千分位符号
     */
    function numberFormat(number, decimals, dec_point, thousands_sep) {
        number = (number + '').replace(/[^0-9+-Ee.]/g, '');
        var n = !isFinite(+number) ? 0 : +number;
        var prec = !isFinite(+decimals) ? 0 : Math.abs(decimals);
        var sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep;
        var dec = (typeof dec_point === 'undefined') ? '.' : dec_point;
        var s;
        var toFixedFix = function(n, prec) {
            var k = Math.pow(10, prec);
            return '' + Math.ceil(n * k) / k;
        };

        s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
        var re = /(-?\d+)(\d{3})/;
        while (re.test(s[0])) {
            s[0] = s[0].replace(re, "$1" + sep + "$2");
        }

        if ((s[1] || '').length < prec) {
            s[1] = s[1] || '';
            s[1] += new Array(prec - s[1].length + 1).join('0');
        }
        return s.join(dec);
    }

    /**
     * 初始化员工下拉框
     */
    function initEmployeeSelectBox(data, dom, form) {
        var html = '<option value="">请选择员工</option>';
        for (var i = 0; i < data.length; i++) {
            html += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
        }
        dom.html(html);
        form.render("select");
    }

    /**
     * 初始化工地下拉框
     */
    function initWorksiteSelectBox(data, dom, form) {
        var html = '<option value="">请选择工地</option>';
        for (var i = 0; i < data.length; i++) {
            html += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
        }
        dom.html(html);
        form.render("select");
    }

    /**
     *设置不可选择的星期
     *value:选中的值
     *appointmentDate星期：如1,2,3,4,5,6,7
     */
    function disabledDate(value, appointmentDate) {
        var mm = value.year + '-' + value.month + '-' + value.date;
        $('.laydate-theme-grid table tbody').find('[lay-ymd="' + mm + '"]').removeClass('layui-this');

        if (appointmentDate != null && appointmentDate != '') {
            var dates = appointmentDate.split(",");
            for (var i = 0; i < dates.length; i++) {
                if (dates[i] == "7") {
                    dates[i] = 0;
                }
                $("table>tbody>tr").find("td:eq(" + dates[i] + ")").addClass('ng-laydate-disabled').addClass('laydate-disabled');
            }
        }
    }

    return {
        getCurrentDate: getCurrentDate,
        getNowFormatDate: getNowFormatDate,
        cacheMousedown: cacheMousedown,
        showOrHide: showOrHide,
        disabledDate: disabledDate,
        numberFormat: numberFormat,
        initEmployeeSelectBox: initEmployeeSelectBox,
        initWorksiteSelectBox: initWorksiteSelectBox
    };

});