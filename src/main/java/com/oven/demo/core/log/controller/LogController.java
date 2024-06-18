package com.oven.demo.core.log.controller;

import com.oven.basic.common.util.LayuiPager;
import com.oven.basic.common.util.Result;
import com.oven.demo.common.constant.PermissionCode;
import com.oven.demo.common.enumerate.ResultCode;
import com.oven.demo.core.log.entity.Log;
import com.oven.demo.core.log.service.LogService;
import com.oven.demo.framework.exception.MyException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日志控制层
 *
 * @author Oven
 */
@ApiIgnore
@Controller
@RequestMapping("/log")
public class LogController {

    @Resource
    private LogService logService;

    /**
     * 去到日志管理页面
     */
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.LOG_MANAGER)
    public String index() {
        return "log/log";
    }

    /**
     * 通过id获取日志
     *
     * @param id 日志id
     */
    @ResponseBody
    @RequestMapping("/getById")
    @RequiresPermissions(PermissionCode.LOG_MANAGER)
    public Result<Object> getById(Integer id) throws MyException {
        try {
            return Result.success(logService.getById(id));
        } catch (Exception e) {
            throw MyException.build(ResultCode.SEARCH_ERROR, "通过id获取日志异常", e);
        }
    }

    /**
     * 分页获取日志
     */
    @ResponseBody
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.LOG_MANAGER)
    public Object getByPage(@RequestBody Log logVo) throws MyException {
        try {
            List<Log> list = logService.getByPage(logVo);
            Integer totalNum = logService.getTotalNum(logVo);
            return LayuiPager.build(list, totalNum);
        } catch (Exception e) {
            throw MyException.build(ResultCode.SEARCH_PAGE_ERROR, "分页获取日志异常", e);
        }
    }

}
