package com.oven.core.advanceSalary.controller;

import com.oven.common.constant.AppConst;
import com.oven.common.constant.PermissionCode;
import com.oven.common.enumerate.ResultEnum;
import com.oven.common.util.LayuiPager;
import com.oven.core.advanceSalary.service.AdvanceSalaryService;
import com.oven.core.advanceSalary.vo.AdvanceSalary;
import com.oven.core.base.controller.BaseController;
import com.oven.framework.exception.MyException;
import com.oven.framework.limitation.Limit;
import com.oven.framework.limitation.LimitType;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 预支薪资控制层
 *
 * @author Oven
 */
@Controller
@RequestMapping("/advanceSalary")
public class AdvanceSalaryController extends BaseController {

    @Resource
    private AdvanceSalaryService advanceSalaryService;

    /**
     * 去到预支薪资管理页面
     */
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.ADVANCE_SALARY)
    public String index() {
        return "advanceSalary/advanceSalary";
    }

    /**
     * 分页获取预支薪资
     *
     * @param page  页码
     * @param limit 每页显示数量
     */
    @ResponseBody
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.ADVANCE_SALARY)
    public Object getByPage(Integer page, Integer limit, AdvanceSalary advanceSalary) throws MyException {
        try {
            LayuiPager<AdvanceSalary> result = new LayuiPager<>();
            List<AdvanceSalary> list = advanceSalaryService.getByPage(page, limit, advanceSalary);
            Integer totalNum = advanceSalaryService.getTotalNum(advanceSalary);
            result.setCode(0);
            result.setMsg("");
            result.setData(list);
            result.setCount(totalNum);
            return result;
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_PAGE_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "分页获取预支薪资异常", e);
        }
    }

    /**
     * 去到添加预支薪资页面
     */
    @RequestMapping("/add")
    @RequiresPermissions(PermissionCode.ADVANCE_SALARY_INSERT)
    public String add() {
        return "advanceSalary/add";
    }

    /**
     * 添加预支薪资
     */
    @ResponseBody
    @RequestMapping("/doAdd")
    @RequiresPermissions(PermissionCode.ADVANCE_SALARY_INSERT)
    @Limit(key = AppConst.ADVANCESALARY_INSERT_LIMIT_KEY, period = AppConst.LIMIT_TIME, count = 1, errMsg = AppConst.INSERT_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object doAdd(AdvanceSalary advanceSalary) throws MyException {
        try {
            advanceSalaryService.add(advanceSalary);
            return super.success(ResultEnum.INSERT_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.INSERT_ERROR.getCode(), ResultEnum.INSERT_ERROR.getValue(), "添加预支薪资异常", e);
        }
    }

    /**
     * 删除预支薪资
     *
     * @param id 预支薪资ID
     */
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions(PermissionCode.ADVANCE_SALARY_DELETE)
    @Limit(key = AppConst.ADVANCESALARY_DELETE_LIMIT_KEY, period = AppConst.LIMIT_TIME, count = 1, errMsg = AppConst.DELETE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object delete(Integer id) throws MyException {
        try {
            // 判断该预支薪资是否已经归还
            AdvanceSalary advanceSalary = advanceSalaryService.getById(id);
            if (advanceSalary == null) {
                return super.fail(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue());
            } else {
                if (advanceSalary.getHasRepay() == 0) {
                    return super.fail(ResultEnum.DELETE_ADVANCE_SALARY_ERROR.getCode(), ResultEnum.DELETE_ADVANCE_SALARY_ERROR.getValue());
                }
            }
            boolean result = advanceSalaryService.delete(id);
            if (result) {
                return super.success(ResultEnum.DELETE_SUCCESS.getValue());
            } else {
                return super.fail(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue());
            }
        } catch (Exception e) {
            throw new MyException(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue(), "删除预支薪资异常", e);
        }
    }

    /**
     * 获取员工未归还薪资总额
     */
    @ResponseBody
    @RequiresPermissions(PermissionCode.ADVANCE_SALARY)
    @RequestMapping("/getTotalAdvanceSalaryByEmployeeId")
    public Object getTotalAdvanceSalaryByEmployeeId(Integer employeeId) throws MyException {
        try {
            Double totalAdvanceSalary = advanceSalaryService.getTotalAdvanceSalaryByEmployeeId(employeeId);
            if (totalAdvanceSalary == null || totalAdvanceSalary == 0) {
                return super.success(false);
            } else {
                return super.success(totalAdvanceSalary);
            }
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "获取员工未归还薪资总额异常", e);
        }
    }

}
