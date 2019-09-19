//@sourceURL=/main.js
$(function() {

    $(".layui-carousel-ind ul li").hover(function() {
        var that = $(this);
        if (!that.hasClass("layui-this")) {
            that.parent().find("li").removeClass("layui-this");
            that.addClass("layui-this");

            that.parent().parent().parent().find("ul").toggleClass("layui-this");
        }
    });

    /**
     * 加载首页数据
     */
    function loadData() {
        $.ajax({
            url: '/getMainPageData',
            type: 'GET',
            data: {},
            dataType: 'json',
            success: function(result) {
                if (result.code != 200) {
                    $("input[name=vercode]").val("");
                    layer.open({
                        title: '系统提示',
                        content: result.data,
                        btnAlign: 'c'
                    });
                    return;
                }
                var data = result.data;
                $("#totalLog").html(data.totalLog);
                $("#totalEmployee").html(data.totalEmployee);
                $("#totalWorkhour").html(data.totalWorkhour);
                $("#totalWorksite").html(data.totalWorksite);
            }
        });
    }

    loadData(); // 加载首页数据

});