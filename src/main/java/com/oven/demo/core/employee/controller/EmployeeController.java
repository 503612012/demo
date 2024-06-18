package com.oven.demo.core.employee.controller;

import com.oven.basic.common.util.LayuiPager;
import com.oven.basic.common.util.Result;
import com.oven.demo.common.constant.AppConst;
import com.oven.demo.common.constant.PermissionCode;
import com.oven.demo.common.enumerate.ResultCode;
import com.oven.demo.core.employee.entity.Employee;
import com.oven.demo.core.employee.service.EmployeeService;
import com.oven.demo.core.user.service.UserService;
import com.oven.demo.framework.annotation.AspectLog;
import com.oven.demo.framework.exception.MyException;
import com.oven.demo.framework.limitation.Limit;
import com.oven.demo.framework.limitation.LimitKey;
import com.oven.demo.framework.limitation.LimitType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

/**
 * 员工控制层
 *
 * @author Oven
 */
@Controller
@Api(tags = "员工控制器")
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private UserService userService;
    @Resource
    private EmployeeService employeeService;

    /**
     * 去到员工管理页面
     */
    @ApiIgnore
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.EMPLOYEE_MANAGER)
    public String index() {
        return "employee/employee";
    }

    /**
     * 通过id获取员工
     *
     * @param id 员工id
     */
    @ResponseBody
    @RequestMapping("/getById")
    @RequiresPermissions(PermissionCode.EMPLOYEE_MANAGER)
    @ApiImplicitParam(name = "id", value = "员工主键", required = true)
    @ApiOperation(value = "通过id获取员工", notes = "通过id获取员工接口", httpMethod = AppConst.GET)
    public Result<Employee> getById(Integer id) throws MyException {
        try {
            return Result.success(employeeService.getById(id));
        } catch (Exception e) {
            throw MyException.build(ResultCode.SEARCH_ERROR, "通过id获取员工异常", e);
        }
    }

    /**
     * 分页获取员工
     */
    @ResponseBody
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.EMPLOYEE_MANAGER)
    @ApiOperation(value = "分页获取员工", notes = "分页获取员工接口", httpMethod = AppConst.GET)
    public LayuiPager<Employee> getByPage(@RequestBody Employee employee) throws MyException {
        try {
            List<Employee> list = employeeService.getByPage(employee);
            for (Employee item : list) {
                item.setCreateName(userService.getById(item.getCreateId()).getNickName());
                item.setLastModifyName(userService.getById(item.getLastModifyId()).getNickName());
            }
            Integer totalNum = employeeService.getTotalNum(employee);
            return LayuiPager.build(list, totalNum);
        } catch (Exception e) {
            throw MyException.build(ResultCode.SEARCH_PAGE_ERROR, "分页获取员工异常", e);
        }
    }

    /**
     * 添加员工
     */
    @ResponseBody
    @RequestMapping("/save")
    @AspectLog(title = "添加员工")
    @RequiresPermissions(PermissionCode.EMPLOYEE_INSERT)
    @ApiOperation(value = "添加员工", notes = "添加员工接口", httpMethod = AppConst.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "address", value = "住址", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "contact", value = "联系方式", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "age", value = "年龄", dataType = "java.lang.Integer", paramType = "query", required = true),
            @ApiImplicitParam(name = "hourSalary", value = "时薪", dataType = "java.lang.Double", paramType = "query", required = true),
            @ApiImplicitParam(name = "gender", value = "性别：0-女、1-男", dataType = "java.lang.Integer", paramType = "query", required = true),
    })
    @Limit(key = LimitKey.EMPLOYEE_INSERT_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.INSERT_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Result<Object> save(@ApiIgnore Employee employee) throws MyException {
        try {
            employeeService.save(employee);
            return Result.success(ResultCode.SUCCESS);
        } catch (Exception e) {
            throw MyException.build(ResultCode.INSERT_ERROR, "添加员工异常", e);
        }
    }

    /**
     * 修改员工
     */
    @ResponseBody
    @AspectLog(title = "修改员工")
    @RequestMapping("/update")
    @RequiresPermissions(PermissionCode.EMPLOYEE_UPDATE)
    @ApiOperation(value = "修改员工", notes = "修改员工接口", httpMethod = AppConst.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "address", value = "住址", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "contact", value = "联系方式", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "id", value = "主键", dataType = "java.lang.Integer", paramType = "query", required = true),
            @ApiImplicitParam(name = "age", value = "年龄", dataType = "java.lang.Integer", paramType = "query", required = true),
            @ApiImplicitParam(name = "hourSalary", value = "时薪", dataType = "java.lang.Double", paramType = "query", required = true),
            @ApiImplicitParam(name = "gender", value = "性别：0-女、1-男", dataType = "java.lang.Integer", paramType = "query", required = true),
    })
    @Limit(key = LimitKey.EMPLOYEE_UPDATE_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.UPDATE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Result<Object> update(@ApiIgnore Employee employee) throws MyException {
        try {
            employeeService.update(employee);
            return Result.success(ResultCode.SUCCESS);
        } catch (Exception e) {
            throw MyException.build(ResultCode.UPDATE_ERROR, "修改员工异常", e);
        }
    }

    /**
     * 删除员工
     *
     * @param id 员工id
     */
    @ResponseBody
    @AspectLog(title = "删除员工")
    @RequestMapping("/delete")
    @RequiresPermissions(PermissionCode.EMPLOYEE_DELETE)
    @ApiOperation(value = "删除员工", notes = "删除员工接口", httpMethod = AppConst.POST)
    @ApiImplicitParam(name = "id", value = "员工主键", dataType = "int", required = true)
    @Limit(key = LimitKey.EMPLOYEE_DELETE_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.DELETE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Result<Object> delete(Integer id) throws MyException {
        try {
            boolean result = employeeService.delete(id);
            if (result) {
                return Result.success(ResultCode.SUCCESS);
            } else {
                return Result.fail(ResultCode.DELETE_ERROR);
            }
        } catch (Exception e) {
            throw MyException.build(ResultCode.DELETE_ERROR, "删除员工异常", e);
        }
    }

    /**
     * 修改员工状态
     *
     * @param employeeId 员工id
     * @param status     状态编码
     */
    @ResponseBody
    @AspectLog(title = "修改员工状态")
    @RequestMapping("/updateStatus")
    @RequiresPermissions(PermissionCode.EMPLOYEE_SETSTATUS)
    @ApiOperation(value = "修改员工状态", notes = "修改员工状态接口", httpMethod = AppConst.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "员工状态", dataType = "int", required = true),
            @ApiImplicitParam(name = "employeeId", value = "员工主键", dataType = "int", required = true)
    })
    @Limit(key = LimitKey.EMPLOYEE_UPDATE_STATUS_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.UPDATE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Result<Object> updateStatus(Integer employeeId, Integer status) throws MyException {
        try {
            Employee employee = employeeService.getById(employeeId);
            employee.setStatus(status);
            employeeService.update(employee);
            return Result.success(ResultCode.SUCCESS);
        } catch (Exception e) {
            throw MyException.build(ResultCode.UPDATE_ERROR, "修改员工状态异常", e);
        }
    }

    /**
     * 获取所有员工
     */
    @ResponseBody
    @RequestMapping("/getAll")
    @RequiresPermissions(PermissionCode.EMPLOYEE_MANAGER)
    @ApiOperation(value = "获取所有员工", notes = "获取所有员工接口", httpMethod = AppConst.GET)
    public Result<Object> getAll() throws MyException {
        try {
            return Result.success(employeeService.getAll());
        } catch (Exception e) {
            throw MyException.build(ResultCode.SEARCH_ERROR, "获取所有员工异常", e);
        }
    }

    /**
     * 获取一个员工的时薪
     */
    @ResponseBody
    @RequestMapping("/getHourSalaryByEmployeeId")
    @RequiresPermissions(PermissionCode.EMPLOYEE_SHOW_MONEY)
    @ApiImplicitParam(name = "employeeId", value = "员工主键", required = true)
    @ApiOperation(value = "获取一个员工的时薪", notes = "获取一个员工的时薪接口", httpMethod = AppConst.GET)
    public Result<Object> getHourSalaryByEmployeeId(Integer employeeId) throws MyException {
        try {
            return Result.success(employeeService.getHourSalaryByEmployeeId(employeeId));
        } catch (Exception e) {
            throw MyException.build(ResultCode.SEARCH_ERROR, "获取一个员工的时薪异常", e);
        }
    }

}
