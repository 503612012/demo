define(['jquery'], function($) {

    function get(url, params, callback, failCallback) {
        $.ajax({
            url: url,
            type: "GET",
            data: params,
            dataType: "json",
            success: function(result) {
                if (result.code != 200) {
                    layer.open({
                        title: '系统提示',
                        anim: 6,
                        content: result.data,
                        btnAlign: 'c'
                    });
                    if (failCallback != undefined) {
                        failCallback();
                    }
                    return;
                }
                callback(result.data);
            },
            error: function() {
                layer.open({
                    title: '系统提示',
                    anim: 6,
                    content: '网络请求异常！',
                    btnAlign: 'c'
                });
            }
        });
    }

    function post(url, params, callback, failCallback) {
        $.ajax({
            url: url,
            type: "POST",
            data: params,
            dataType: "json",
            success: function(result) {
                if (result.code != 200) {
                    layer.open({
                        title: '系统提示',
                        anim: 6,
                        content: result.data,
                        btnAlign: 'c'
                    });
                    if (failCallback != undefined) {
                        failCallback();
                    }
                    return;
                }
                callback(result.data);
            },
            error: function() {
                layer.open({
                    title: '系统提示',
                    anim: 6,
                    content: '网络请求异常！',
                    btnAlign: 'c'
                });
            }
        });
    }

    return {
        get: get,
        post: post
    };

});