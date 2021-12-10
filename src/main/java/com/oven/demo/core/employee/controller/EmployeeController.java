package com.oven.demo.core.employee.controller;

import com.oven.demo.common.constant.PermissionCode;
import com.oven.demo.common.enumerate.ResultEnum;
import com.oven.demo.common.util.LayuiPager;
import com.oven.demo.core.advanceSalary.service.AdvanceSalaryService;
import com.oven.demo.core.advanceSalary.vo.AdvanceSalary;
import com.oven.demo.core.base.controller.BaseController;
import com.oven.demo.core.employee.service.EmployeeService;
import com.oven.demo.core.employee.vo.Employee;
import com.oven.demo.core.user.service.UserService;
import com.oven.demo.core.workhour.service.WorkhourService;
import com.oven.demo.core.workhour.vo.Workhour;
import com.oven.demo.framework.exception.MyException;
import com.oven.demo.framework.limitation.Limit;
import com.oven.demo.framework.limitation.LimitKey;
import com.oven.demo.framework.limitation.LimitType;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 员工控制层
 *
 * @author Oven
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController extends BaseController {

    @Resource
    private UserService userService;
    @Resource
    private WorkhourService workhourService;
    @Resource
    private EmployeeService employeeService;
    @Resource
    private AdvanceSalaryService advanceSalaryService;

    /**
     * 去到员工管理页面
     */
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.EMPLOYEE_MANAGER)
    public String index() {
        return "employee/employee";
    }

    /**
     * 通过ID获取员工
     *
     * @param id 员工ID
     */
    @ResponseBody
    @RequestMapping("/getById")
    @RequiresPermissions(PermissionCode.EMPLOYEE_MANAGER)
    public Object getById(Integer id) throws MyException {
        try {
            return super.success(employeeService.getById(id));
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "通过ID获取员工异常", e);
        }
    }

    /**
     * 分页获取员工
     *
     * @param page  页码
     * @param limit 每页显示数量
     */
    @ResponseBody
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.EMPLOYEE_MANAGER)
    public Object getByPage(Integer page, Integer limit, Employee employee) throws MyException {
        try {
            LayuiPager<Employee> result = new LayuiPager<>();
            List<Employee> list = employeeService.getByPage(page, limit, employee);
            for (Employee item : list) {
                item.setCreateName(userService.getById(item.getCreateId()).getNickName());
                item.setLastModifyName(userService.getById(item.getLastModifyId()).getNickName());
            }
            Integer totalNum = employeeService.getTotalNum(employee);
            result.setCode(0);
            result.setMsg("");
            result.setData(list);
            result.setCount(totalNum);
            return result;
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_PAGE_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "分页获取员工异常", e);
        }
    }

    /**
     * 添加员工
     */
    @ResponseBody
    @RequestMapping("/add")
    @RequiresPermissions(PermissionCode.EMPLOYEE_INSERT)
    @Limit(key = LimitKey.EMPLOYEE_INSERT_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.INSERT_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object add(Employee employee) throws MyException {
        try {
            employeeService.add(employee);
            return super.success(ResultEnum.INSERT_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.INSERT_ERROR.getCode(), ResultEnum.INSERT_ERROR.getValue(), "添加员工异常", e);
        }
    }

    /**
     * 修改员工
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions(PermissionCode.EMPLOYEE_UPDATE)
    @Limit(key = LimitKey.EMPLOYEE_UPDATE_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.UPDATE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object update(Employee employee) throws MyException {
        try {
            employeeService.update(employee);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), "修改员工异常", e);
        }
    }

    /**
     * 删除员工
     *
     * @param id 员工ID
     */
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions(PermissionCode.EMPLOYEE_DELETE)
    @Limit(key = LimitKey.EMPLOYEE_DELETE_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.DELETE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object delete(Integer id) throws MyException {
        try {
            // 判断该员工有没有未发的薪资
            List<Workhour> workhours = workhourService.getUnPayByEmployeeId(id);
            if (workhours != null && workhours.size() > 0) {
                return super.fail(ResultEnum.DELETE_EMPLOYEE_ERROR_UNPAY_SALARY.getCode(), ResultEnum.DELETE_EMPLOYEE_ERROR_UNPAY_SALARY.getValue());
            }
            // 判断该员工有没有未归还的预支薪资
            List<AdvanceSalary> advanceSalaries = advanceSalaryService.getByEmployeeId(id, 1);
            if (advanceSalaries != null && advanceSalaries.size() > 0) {
                return super.fail(ResultEnum.DELETE_EMPLOYEE_ERROR_UNBACK_ADVANCE_SALARY.getCode(), ResultEnum.DELETE_EMPLOYEE_ERROR_UNBACK_ADVANCE_SALARY.getValue());
            }
            boolean result = employeeService.delete(id);
            if (result) {
                return super.success(ResultEnum.DELETE_SUCCESS.getValue());
            } else {
                return super.fail(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue());
            }
        } catch (Exception e) {
            throw new MyException(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue(), "删除员工异常", e);
        }
    }

    /**
     * 修改员工状态
     *
     * @param employeeId 员工ID
     * @param status     状态编码
     */
    @ResponseBody
    @RequestMapping("/updateStatus")
    @RequiresPermissions(PermissionCode.EMPLOYEE_SETSTATUS)
    @Limit(key = LimitKey.EMPLOYEE_UPDATE_STATUS_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.UPDATE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object updateStatus(Integer employeeId, Integer status) throws MyException {
        try {
            Employee employee = employeeService.getById(employeeId);
            employee.setStatus(status);
            employeeService.update(employee);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), "修改员工状态异常", e);
        }
    }

    /**
     * 获取所有员工
     */
    @ResponseBody
    @RequestMapping("/getAll")
    @RequiresPermissions(PermissionCode.EMPLOYEE_MANAGER)
    public Object getAll() throws MyException {
        try {
            return super.success(employeeService.getAll());
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "获取所有员工异常", e);
        }
    }

    /**
     * 获取一个员工的时薪
     */
    @ResponseBody
    @RequestMapping("/getHourSalaryByEmployeeId")
    @RequiresPermissions(PermissionCode.EMPLOYEE_SHOWMONEY)
    public Object getHourSalaryByEmployeeId(String employeeId) throws MyException {
        try {
            return super.success(employeeService.getHourSalaryByEmployeeId(employeeId));
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "获取一个员工的时薪异常", e);
        }
    }

}
