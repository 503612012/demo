package com.oven.demo.core.log.controller;

import com.oven.basic.common.util.LayuiPager;
import com.oven.basic.common.util.ResultInfo;
import com.oven.demo.common.constant.PermissionCode;
import com.oven.demo.common.enumerate.ResultEnum;
import com.oven.demo.core.log.entity.Log;
import com.oven.demo.core.log.service.LogService;
import com.oven.demo.framework.exception.MyException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
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
    public ResultInfo<Object> getById(Integer id) throws MyException {
        try {
            return ResultInfo.success(logService.getById(id));
        } catch (Exception e) {
            throw MyException.build(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "通过id获取日志异常", e);
        }
    }

    /**
     * 分页获取日志
     *
     * @param page  页码
     * @param limit 每页显示数量
     */
    @ResponseBody
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.LOG_MANAGER)
    public Object getByPage(Integer page, Integer limit, Log logVo) throws MyException {
        try {
            LayuiPager<Log> result = new LayuiPager<>();
            List<Log> list = logService.getByPage(page, limit, logVo);
            Integer totalNum = logService.getTotalNum(logVo);
            result.setCode(0);
            result.setMsg("");
            result.setData(list);
            result.setCount(totalNum);
            return result;
        } catch (Exception e) {
            throw MyException.build(ResultEnum.SEARCH_PAGE_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "分页获取日志异常", e);
        }
    }

}
