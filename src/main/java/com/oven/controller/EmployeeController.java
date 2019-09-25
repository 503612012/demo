package com.oven.controller;

import com.alibaba.fastjson.JSONObject;
import com.oven.constant.AppConst;
import com.oven.constant.PermissionCode;
import com.oven.enumerate.ResultEnum;
import com.oven.exception.MyException;
import com.oven.limitation.Limit;
import com.oven.limitation.LimitType;
import com.oven.service.EmployeeService;
import com.oven.service.UserService;
import com.oven.service.WorkhourService;
import com.oven.vo.Employee;
import com.oven.vo.Workhour;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
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
            JSONObject result = new JSONObject();
            List<Employee> list = employeeService.getByPage(page, limit, employee);
            for (Employee item : list) {
                item.setCreateName(userService.getById(item.getCreateId()).getNickName());
                item.setLastModifyName(userService.getById(item.getLastModifyId()).getNickName());
            }
            Integer totalNum = employeeService.getTotalNum(employee);
            result.put("code", 0);
            result.put("msg", "");
            result.put("count", totalNum);
            result.put("data", list);
            return result;
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_PAGE_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
        }
    }

    /**
     * 去到添加员工页面
     */
    @RequestMapping("/add")
    @RequiresPermissions(PermissionCode.EMPLOYEE_INSERT)
    public String add() {
        return "employee/add";
    }

    /**
     * 添加员工
     */
    @ResponseBody
    @RequestMapping("/doAdd")
    @RequiresPermissions(PermissionCode.EMPLOYEE_INSERT)
    @Limit(key = AppConst.EMPLOYEE_INSERT_LIMIT_KEY, period = 10, count = 1, errMsg = AppConst.INSERT_LIMIT, limitType = LimitType.CUSTOMER)
    public Object doAdd(Employee employee) throws MyException {
        try {
            employeeService.add(employee);
            return super.success(ResultEnum.INSERT_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.INSERT_ERROR.getCode(), ResultEnum.INSERT_ERROR.getValue(), e);
        }
    }

    /**
     * 去到员工更新页面
     *
     * @param id 员工ID
     */
    @RequestMapping("/update")
    @RequiresPermissions(PermissionCode.EMPLOYEE_UPDATE)
    public String update(Integer id, Model model) throws MyException {
        try {
            Employee employee = employeeService.getById(id);
            model.addAttribute("employee", employee);
            return "/employee/update";
        } catch (Exception e) {
            throw new MyException(ResultEnum.ERROR_PAGE.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
        }
    }

    /**
     * 修改员工
     */
    @ResponseBody
    @RequestMapping("/doUpdate")
    @RequiresPermissions(PermissionCode.EMPLOYEE_UPDATE)
    @Limit(key = AppConst.EMPLOYEE_UPDATE_LIMIT_KEY, period = 10, count = 1, errMsg = AppConst.UPDATE_LIMIT, limitType = LimitType.CUSTOMER)
    public Object doUpdate(Employee employee) throws MyException {
        try {
            employeeService.update(employee);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), e);
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
    @Limit(key = AppConst.EMPLOYEE_DELETE_LIMIT_KEY, period = 10, count = 1, errMsg = AppConst.DELETE_LIMIT, limitType = LimitType.CUSTOMER)
    public Object delete(Integer id) throws MyException {
        try {
            // 判断该员工有没有未发的薪资
            List<Workhour> workhours = workhourService.getUnPayByEmployeeId(id);
            if (workhours != null && workhours.size() > 0) {
                return super.fail(ResultEnum.DELETE_EMPLOYEE_ERROR.getCode(), ResultEnum.DELETE_EMPLOYEE_ERROR.getValue());
            }
            boolean result = employeeService.delete(id);
            if (result) {
                return super.success(ResultEnum.DELETE_SUCCESS.getValue());
            } else {
                return super.fail(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue());
            }
        } catch (Exception e) {
            throw new MyException(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue(), e);
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
    @Limit(key = AppConst.EMPLOYEE_UPDATE_STATUS_LIMIT_KEY, period = 5, count = 1, errMsg = AppConst.UPDATE_LIMIT, limitType = LimitType.CUSTOMER)
    public Object updateStatus(Integer employeeId, Integer status) throws MyException {
        try {
            Employee employee = employeeService.getById(employeeId);
            employee.setStatus(status);
            employeeService.update(employee);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), e);
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
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
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
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
        }
    }

}
