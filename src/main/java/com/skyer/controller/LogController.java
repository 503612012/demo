package com.skyer.controller;

import com.alibaba.fastjson.JSONObject;
import com.skyer.contants.AppConst;
import com.skyer.contants.PermissionCode;
import com.skyer.enumerate.ResultEnum;
import com.skyer.service.LogService;
import com.skyer.service.UserService;
import com.skyer.vo.Log;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日志控制层
 *
 * @author SKYER
 */
@Controller
@RequestMapping("/log")
public class LogController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(LogController.class);

    @Resource
    private LogService logService;
    @Resource
    private UserService userService;

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
    @RequestMapping("/getById")
    @RequiresPermissions(PermissionCode.LOG_MANAGER)
    @ResponseBody
    public Object getById(Integer id) {
        try {
            return super.success(logService.getById(id));
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[id: {}]", id);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "通过ID查询日志出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue());
    }

    /**
     * 分页获取日志
     *
     * @param page  页码
     * @param limit 每页显示数量
     */
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.LOG_MANAGER)
    @ResponseBody
    public Object getByPage(Integer page, Integer limit, Log log) {
        JSONObject result = new JSONObject();
        try {
            List<Log> list = logService.getByPage(page, limit, log);
            for (Log item : list) {
                item.setOperatorName(userService.getById(item.getOperatorId()).getNickName());
            }
            Long totalNum = logService.getTotalNum(log);
            result.put("code", 0);
            result.put("msg", "");
            result.put("count", totalNum);
            result.put("data", list);
        } catch (Exception e) {
            result.put("code", ResultEnum.SEARCH_ERROR.getCode());
            result.put("msg", ResultEnum.SEARCH_ERROR.getValue());
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[page: {}, limit: {}]", page, limit);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "分页查询日志出错，错误信息：", e);
            e.printStackTrace();
        }
        return result;
    }

}
