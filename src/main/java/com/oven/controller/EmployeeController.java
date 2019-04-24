package com.oven.controller;

import com.alibaba.fastjson.JSONObject;
import com.oven.constant.AppConst;
import com.oven.constant.PermissionCode;
import com.oven.enumerate.ResultEnum;
import com.oven.exception.MyException;
import com.oven.service.EmployeeService;
import com.oven.service.UserService;
import com.oven.vo.Employee;
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
    @RequestMapping("/getById")
    @RequiresPermissions(PermissionCode.EMPLOYEE_MANAGER)
    @ResponseBody
    public Object getById(Integer id) throws MyException {
        try {
            return super.success(employeeService.getById(id));
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), AppConst.SYSTEM_ERROR, e);
        }
    }

    /**
     * 分页获取员工
     *
     * @param page  页码
     * @param limit 每页显示数量
     */
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.EMPLOYEE_MANAGER)
    @ResponseBody
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
            throw new MyException(ResultEnum.SEARCH_PAGE_ERROR.getCode(), AppConst.SYSTEM_ERROR, e);
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
    @RequestMapping("/doAdd")
    @RequiresPermissions(PermissionCode.EMPLOYEE_INSERT)
    @ResponseBody
    public Object doAdd(Employee employee) throws MyException {
        try {
            employeeService.add(employee);
            return super.success(ResultEnum.INSERT_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.INSERT_ERROR.getCode(), "添加员工出错，请联系网站管理人员。", e);
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
            throw new MyException(ResultEnum.ERROR_PAGE.getCode(), AppConst.SYSTEM_ERROR, e);
        }
    }

    /**
     * 修改员工
     */
    @RequestMapping("/doUpdate")
    @RequiresPermissions(PermissionCode.EMPLOYEE_UPDATE)
    @ResponseBody
    public Object doUpdate(Employee employee) throws MyException {
        try {
            employeeService.update(employee);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), "修改员工出错，请联系网站管理人员。", e);
        }
    }

    /**
     * 删除员工
     *
     * @param id 员工ID
     */
    @RequestMapping("/delete")
    @RequiresPermissions(PermissionCode.EMPLOYEE_DELETE)
    @ResponseBody
    public Object delete(Integer id) throws MyException {
        try {
            boolean result = employeeService.delete(id);
            if (result) {
                return super.success(ResultEnum.DELETE_SUCCESS.getValue());
            } else {
                return super.fail(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue());
            }
        } catch (Exception e) {
            throw new MyException(ResultEnum.DELETE_ERROR.getCode(), "删除员工出错，请联系网站管理人员。", e);
        }
    }

    /**
     * 修改员工状态
     *
     * @param employeeId 员工ID
     * @param status     状态编码
     */
    @RequestMapping("/updateStatus")
    @RequiresPermissions(PermissionCode.EMPLOYEE_SETSTATUS)
    @ResponseBody
    public Object updateStatus(Integer employeeId, Integer status) throws MyException {
        try {
            Employee employee = employeeService.getById(employeeId);
            employee.setStatus(status);
            employeeService.update(employee);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), "修改员工状态出错，请联系网站管理人员。", e);
        }
    }

}
