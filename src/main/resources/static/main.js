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

});