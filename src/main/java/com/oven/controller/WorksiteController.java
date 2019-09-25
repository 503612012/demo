package com.oven.controller;

import com.alibaba.fastjson.JSONObject;
import com.oven.constant.AppConst;
import com.oven.constant.PermissionCode;
import com.oven.enumerate.ResultEnum;
import com.oven.exception.MyException;
import com.oven.limitation.Limit;
import com.oven.limitation.LimitType;
import com.oven.service.UserService;
import com.oven.service.WorksiteService;
import com.oven.vo.Worksite;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 工地控制层
 *
 * @author Oven
 */
@Controller
@RequestMapping("/worksite")
public class WorksiteController extends BaseController {

    @Resource
    private UserService userService;
    @Resource
    private WorksiteService worksiteService;

    /**
     * 去到工地管理页面
     */
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.WORKSITE_MANAGER)
    public String index() {
        return "worksite/worksite";
    }

    /**
     * 分页获取工地
     *
     * @param page  页码
     * @param limit 每页显示数量
     */
    @ResponseBody
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.WORKSITE_MANAGER)
    public Object getByPage(Integer page, Integer limit, Worksite worksite) throws MyException {
        JSONObject result = new JSONObject();
        try {
            List<Worksite> list = worksiteService.getByPage(page, limit, worksite);
            for (Worksite item : list) {
                item.setCreateName(userService.getById(item.getCreateId()).getNickName());
                item.setLastModifyName(userService.getById(item.getLastModifyId()).getNickName());
            }
            Integer totalNum = worksiteService.getTotalNum(worksite);
            result.put("code", 0);
            result.put("count", totalNum);
            result.put("msg", "");
            result.put("data", list);
            return result;
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_PAGE_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
        }
    }

    /**
     * 去到添加工地页面
     */
    @RequestMapping("/add")
    @RequiresPermissions(PermissionCode.WORKSITE_INSERT)
    public String add() {
        return "worksite/add";
    }

    /**
     * 添加工地
     */
    @ResponseBody
    @RequestMapping("/doAdd")
    @RequiresPermissions(PermissionCode.WORKSITE_INSERT)
    @Limit(key = "limit", period = 10, count = 1, errMsg = AppConst.INSERT_LIMIT, limitType = LimitType.IP)
    public Object doAdd(Worksite worksite) throws MyException {
        try {
            worksiteService.add(worksite);
            return super.success(ResultEnum.INSERT_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.INSERT_ERROR.getCode(), ResultEnum.INSERT_ERROR.getValue(), e);
        }
    }

    /**
     * 去到工地更新页面
     *
     * @param id 工地ID
     */
    @RequestMapping("/update")
    @RequiresPermissions(PermissionCode.WORKSITE_UPDATE)
    public String update(Integer id, Model model) throws MyException {
        try {
            Worksite worksite = worksiteService.getById(id);
            model.addAttribute("worksite", worksite);
            return "/worksite/update";
        } catch (Exception e) {
            throw new MyException(ResultEnum.ERROR_PAGE.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
        }
    }

    /**
     * 修改工地
     */
    @ResponseBody
    @RequestMapping("/doUpdate")
    @RequiresPermissions(PermissionCode.WORKSITE_UPDATE)
    @Limit(key = "limit", period = 10, count = 1, errMsg = AppConst.UPDATE_LIMIT, limitType = LimitType.IP)
    public Object doUpdate(Worksite worksite) throws MyException {
        try {
            worksiteService.update(worksite);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), e);
        }
    }

    /**
     * 删除工地
     *
     * @param id 工地ID
     */
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions(PermissionCode.WORKSITE_DELETE)
    @Limit(key = "limit", period = 10, count = 1, errMsg = AppConst.DELETE_LIMIT, limitType = LimitType.IP)
    public Object delete(Integer id) throws MyException {
        try {
            worksiteService.delete(id);
            return super.success(ResultEnum.DELETE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue(), e);
        }
    }

    /**
     * 修改工地状态
     *
     * @param worksiteId 工地ID
     * @param status     状态编码
     */
    @ResponseBody
    @RequestMapping("/updateStatus")
    @RequiresPermissions(PermissionCode.WORKSITE_SETSTATUS)
    @Limit(key = "limit", period = 5, count = 1, errMsg = AppConst.UPDATE_LIMIT, limitType = LimitType.IP)
    public Object updateStatus(Integer worksiteId, Integer status) throws MyException {
        try {
            Worksite worksite = worksiteService.getById(worksiteId);
            worksite.setStatus(status);
            worksiteService.update(worksite);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), e);
        }
    }

    /**
     * 获取所有工地
     */
    @ResponseBody
    @RequestMapping("/getAll")
    @RequiresPermissions(PermissionCode.WORKSITE_MANAGER)
    public Object getAll() throws MyException {
        try {
            return super.success(worksiteService.getAll());
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
        }
    }

}
