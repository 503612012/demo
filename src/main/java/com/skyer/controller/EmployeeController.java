package com.skyer.controller;

import com.alibaba.fastjson.JSONObject;
import com.skyer.contants.AppConst;
import com.skyer.contants.PermissionCode;
import com.skyer.enumerate.ResultEnum;
import com.skyer.service.EmployeeService;
import com.skyer.service.UserService;
import com.skyer.vo.Employee;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 员工控制层
 *
 * @author SKYER
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

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
    public Object getById(Integer id) {
        try {
            return super.success(employeeService.getById(id));
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[id: {}]", id);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "通过ID查询员工出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue());
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
    public Object getByPage(Integer page, Integer limit, Employee employee) {
        JSONObject result = new JSONObject();
        try {
            List<Employee> list = employeeService.getByPage(page, limit, employee);
            for (Employee item : list) {
                item.setCreateName(userService.getById(item.getCreateId()).getNickName());
                item.setLastModifyName(userService.getById(item.getLastModifyId()).getNickName());
            }
            Long totalNum = employeeService.getTotalNum(employee);
            result.put("code", 0);
            result.put("msg", "");
            result.put("count", totalNum);
            result.put("data", list);
        } catch (Exception e) {
            result.put("code", ResultEnum.SEARCH_ERROR.getCode());
            result.put("msg", ResultEnum.SEARCH_ERROR.getValue());
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[page: {}, limit: {}, employee: {}]", page, limit, employee.toString());
            LOG.error(AppConst.ERROR_LOG_PREFIX + "分页查询员工出错，错误信息：", e);
            e.printStackTrace();
        }
        return result;
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
    public Object doAdd(Employee employee) {
        try {
            employeeService.add(employee);
            return super.success(ResultEnum.INSERT_SUCCESS.getValue());
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[employee: {}]", employee.toString());
            LOG.error(AppConst.ERROR_LOG_PREFIX + "添加员工出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.INSERT_ERROR.getCode(), ResultEnum.INSERT_ERROR.getValue());
    }

    /**
     * 去到员工更新页面
     *
     * @param id 员工ID
     */
    @RequestMapping("/update")
    @RequiresPermissions(PermissionCode.EMPLOYEE_UPDATE)
    public String update(Integer id, Model model) {
        try {
            Employee employee = employeeService.getById(id);
            model.addAttribute("employee", employee);
            return "/employee/update";
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[id: {}]", id);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "去到员工修改页面出错，错误信息：", e);
            e.printStackTrace();
        }
        return "err";
    }

    /**
     * 修改员工
     */
    @RequestMapping("/doUpdate")
    @RequiresPermissions(PermissionCode.EMPLOYEE_UPDATE)
    @ResponseBody
    public Object doUpdate(Employee employee) {
        try {
            employeeService.update(employee);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[employee: {}]", employee.toString());
            LOG.error(AppConst.ERROR_LOG_PREFIX + "修改员工出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue());
    }

    /**
     * 删除员工
     *
     * @param id 员工ID
     */
    @RequestMapping("/delete")
    @RequiresPermissions(PermissionCode.EMPLOYEE_DELETE)
    @ResponseBody
    public Object delete(Integer id) {
        try {
            employeeService.delete(id);
            return super.success(ResultEnum.DELETE_SUCCESS.getValue());
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[id: {}]", id);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "删除员工出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue());
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
    public Object updateStatus(Integer employeeId, Integer status) {
        try {
            Employee employee = employeeService.getById(employeeId);
            employee.setStatus(status);
            employeeService.update(employee);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[employeeId: {}, status: {}]", employeeId, status);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "修改员工状态出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue());
    }

}
