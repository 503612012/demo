//@sourceURL=/monitor/index.js
requirejs.config({
    baseUrl: '/',
    paths: {
        jquery: 'js/lib/jquery.min',
        layui: 'layui/layui.all',
        http: 'js/common/http'
    },
    shim: {
        layui: {exports: "layui"}
    }
});

requirejs(['jquery', 'layui', 'http', 'common'], function($, layui, http) {

    var loadData = function() {
        http.get('/monitor/data', {}, function(result) {
            $('#osName').html(result.sysOsInfo.osName);
            $('#osArch').html(result.sysOsInfo.osArch);
            $('#osVersion').html(result.sysOsInfo.osVersion);
            $('#osHostName').html(result.sysOsInfo.osHostName);
            $('#osHostAddress').html(result.sysOsInfo.osHostAddress);

            $('#jvmName').html(result.sysJavaInfo.jvmName);
            $('#jvmVersion').html(result.sysJavaInfo.jvmVersion);
            $('#jvmVendor').html(result.sysJavaInfo.jvmVendor);
            $('#javaName').html(result.sysJavaInfo.javaName);
            $('#javaVersion').html(result.sysJavaInfo.javaVersion);

            $('#jvmMaxMemory').html(result.sysJvmMemInfo.jvmMaxMemory);
            $('#jvmUsableMemory').html(result.sysJvmMemInfo.jvmUsableMemory);
            $('#jvmTotalMemory').html(result.sysJvmMemInfo.jvmTotalMemory);
            $('#jvmUsedMemory').html(result.sysJvmMemInfo.jvmUsedMemory);
            $('#jvmFreeMemory').html(result.sysJvmMemInfo.jvmFreeMemory);
            $('#jvmMemoryUsedRate').html(result.sysJvmMemInfo.jvmMemoryUsedRate);
        });
    }

    loadData();

});