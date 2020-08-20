package com.oven.core.worksite.controller;

import com.oven.common.constant.AppConst;
import com.oven.common.constant.PermissionCode;
import com.oven.common.enumerate.ResultEnum;
import com.oven.common.util.LayuiPager;
import com.oven.core.base.controller.BaseController;
import com.oven.core.finance.service.FinanceService;
import com.oven.core.finance.vo.Finance;
import com.oven.core.user.service.UserService;
import com.oven.core.workhour.service.WorkhourService;
import com.oven.core.workhour.vo.Workhour;
import com.oven.core.worksite.service.WorksiteService;
import com.oven.core.worksite.vo.Worksite;
import com.oven.framework.exception.MyException;
import com.oven.framework.limitation.Limit;
import com.oven.framework.limitation.LimitType;
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
    private FinanceService financeService;
    @Resource
    private WorksiteService worksiteService;
    @Resource
    private WorkhourService workhourService;

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
        try {
            LayuiPager<Worksite> result = new LayuiPager<>();
            List<Worksite> list = worksiteService.getByPage(page, limit, worksite);
            for (Worksite item : list) {
                item.setCreateName(userService.getById(item.getCreateId()).getNickName());
                item.setLastModifyName(userService.getById(item.getLastModifyId()).getNickName());
            }
            Integer totalNum = worksiteService.getTotalNum(worksite);
            result.setCode(0);
            result.setMsg("");
            result.setData(list);
            result.setCount(totalNum);
            return result;
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_PAGE_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "分页获取工地异常", e);
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
    @Limit(key = AppConst.WORKSITE_INSERT_LIMIT_KEY, period = AppConst.LIMIT_TIME, count = 1, errMsg = AppConst.INSERT_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object doAdd(Worksite worksite) throws MyException {
        try {
            worksiteService.add(worksite);
            return super.success(ResultEnum.INSERT_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.INSERT_ERROR.getCode(), ResultEnum.INSERT_ERROR.getValue(), "添加工地异常", e);
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
            throw new MyException(ResultEnum.ERROR_PAGE.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "去到工地更新页面异常", e);
        }
    }

    /**
     * 修改工地
     */
    @ResponseBody
    @RequestMapping("/doUpdate")
    @RequiresPermissions(PermissionCode.WORKSITE_UPDATE)
    @Limit(key = AppConst.WORKSITE_UPDATE_LIMIT_KEY, period = AppConst.LIMIT_TIME, count = 1, errMsg = AppConst.UPDATE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object doUpdate(Worksite worksite) throws MyException {
        try {
            worksiteService.update(worksite);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), "修改工地异常", e);
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
    @Limit(key = AppConst.WORKSITE_DELETE_LIMIT_KEY, period = AppConst.LIMIT_TIME, count = 1, errMsg = AppConst.DELETE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object delete(Integer id) throws MyException {
        try {
            // 判断该工地下有没有未发放薪资的工时
            List<Workhour> worksites = workhourService.getByWorksiteId(id);
            if (worksites != null && worksites.size() > 0) {
                return super.fail(ResultEnum.DELETE_WORKHOUR_ERROR_HAS_UNPAY_WORKHOUR.getCode(), ResultEnum.DELETE_WORKHOUR_ERROR_HAS_UNPAY_WORKHOUR.getValue());
            }
            // 判断该工地下有没有登记的财务
            Finance finance = financeService.getByWorksiteId(id);
            if (finance != null) {
                return super.fail(ResultEnum.DELETE_WORKHOUR_ERROR_HAS_FINANCE.getCode(), ResultEnum.DELETE_WORKHOUR_ERROR_HAS_FINANCE.getValue());
            }
            worksiteService.delete(id);
            return super.success(ResultEnum.DELETE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue(), "删除工地异常", e);
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
    @Limit(key = AppConst.WORKSITE_UPDATE_STATUS_LIMIT_KEY, period = AppConst.LIMIT_TIME, count = 1, errMsg = AppConst.UPDATE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object updateStatus(Integer worksiteId, Integer status) throws MyException {
        try {
            Worksite worksite = worksiteService.getById(worksiteId);
            worksite.setStatus(status);
            worksiteService.update(worksite);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), "修改工地状态异常", e);
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
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "获取所有工地异常", e);
        }
    }

}
