package com.oven.core.log.controller;

import com.oven.common.constant.PermissionCode;
import com.oven.common.enumerate.ResultEnum;
import com.oven.common.util.LayuiPager;
import com.oven.core.base.controller.BaseController;
import com.oven.core.log.service.LogService;
import com.oven.core.log.vo.Log;
import com.oven.framework.exception.MyException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日志控制层
 *
 * @author Oven
 */
@Controller
@RequestMapping("/log")
public class LogController extends BaseController {

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
     * 通过ID获取日志
     *
     * @param id 日志ID
     */
    @ResponseBody
    @RequestMapping("/getById")
    @RequiresPermissions(PermissionCode.LOG_MANAGER)
    public Object getById(Integer id) throws MyException {
        try {
            return super.success(logService.getById(id));
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "通过ID获取日志异常", e);
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
            throw new MyException(ResultEnum.SEARCH_PAGE_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "分页获取日志异常", e);
        }
    }

}
