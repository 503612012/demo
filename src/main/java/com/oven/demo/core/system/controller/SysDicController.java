package com.oven.demo.core.system.controller;

import com.oven.basic.common.util.LayuiPager;
import com.oven.basic.common.util.Result;
import com.oven.demo.common.constant.PermissionCode;
import com.oven.demo.common.enumerate.ResultCode;
import com.oven.demo.core.system.entity.SysDicEntity;
import com.oven.demo.core.system.service.SysDicService;
import com.oven.demo.framework.annotation.AspectLog;
import com.oven.demo.framework.config.InitSysDic;
import com.oven.demo.framework.exception.MyException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据字典控制层
 *
 * @author Oven
 */
@ApiIgnore
@Controller
@RequestMapping("/sysdic")
public class SysDicController {

    @Resource
    private InitSysDic initSysDic;
    @Resource
    private SysDicService sysDicService;

    /**
     * 去到数据字典管理页面
     */
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.SYSDIC_MANAGER)
    public String index() {
        return "sysdic/index";
    }

    /**
     * 通过id获取数据字典
     *
     * @param id 数据字典id
     */
    @ResponseBody
    @RequestMapping("/getById")
    @RequiresPermissions(PermissionCode.SYSDIC_MANAGER)
    public Result<Object> getById(Integer id) throws MyException {
        try {
            return Result.success(sysDicService.getById(id));
        } catch (Exception e) {
            throw MyException.build(ResultCode.SEARCH_ERROR, "通过id获取数据字典异常", e);
        }
    }

    /**
     * 分页获取数据字典
     */
    @ResponseBody
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.SYSDIC_MANAGER)
    public Object getByPage(@RequestBody SysDicEntity sysdic) throws MyException {
        try {
            List<SysDicEntity> list = sysDicService.getByPage(sysdic);
            Integer totalNum = sysDicService.getTotalNum(sysdic);
            return LayuiPager.build(list, totalNum);
        } catch (Exception e) {
            throw MyException.build(ResultCode.SEARCH_PAGE_ERROR, "分页获取数据字典异常", e);
        }
    }

    /**
     * 添加数据字典
     */
    @ResponseBody
    @RequestMapping("/save")
    @AspectLog(title = "添加数据字典")
    @RequiresPermissions(PermissionCode.SYSDIC_INSERT)
    public Result<Object> save(SysDicEntity sysdic) throws MyException {
        try {
            sysDicService.save(sysdic);
            return Result.success(ResultCode.SUCCESS);
        } catch (Exception e) {
            throw MyException.build(ResultCode.INSERT_ERROR, "添加数据字典异常", e);
        }
    }

    /**
     * 修改数据字典
     */
    @ResponseBody
    @RequestMapping("/update")
    @AspectLog(title = "修改数据字典")
    @RequiresPermissions(PermissionCode.SYSDIC_UPDATE)
    public Result<Object> update(SysDicEntity sysdic) throws MyException {
        try {
            sysDicService.update(sysdic);
            return Result.success(ResultCode.SUCCESS);
        } catch (Exception e) {
            throw MyException.build(ResultCode.UPDATE_ERROR, "修改数据字典异常", e);
        }
    }

    /**
     * 删除数据字典
     *
     * @param id 数据字典id
     */
    @ResponseBody
    @RequestMapping("/delete")
    @AspectLog(title = "删除数据字典")
    @RequiresPermissions(PermissionCode.SYSDIC_DELETE)
    public Result<Object> delete(Integer id) throws MyException {
        try {
            boolean result = sysDicService.delete(id);
            if (result) {
                return Result.success(ResultCode.SUCCESS);
            } else {
                return Result.fail(ResultCode.DELETE_ERROR);
            }
        } catch (Exception e) {
            throw MyException.build(ResultCode.DELETE_ERROR, "删除数据字典异常", e);
        }
    }

    /**
     * 重载数据字典
     */
    @ResponseBody
    @RequestMapping("/reload")
    @RequiresPermissions(PermissionCode.SYSDIC_RELOAD)
    public Result<Object> reload() throws MyException {
        try {
            initSysDic.initSysDic();
            return Result.success(ResultCode.SUCCESS);
        } catch (Exception e) {
            throw MyException.build(ResultCode.SYSTEM_ERROR, "重载数据字典异常", e);
        }
    }

    /**
     * 修改状态
     */
    @ResponseBody
    @RequestMapping("/updateStatus")
    @AspectLog(title = "修改数据字典状态")
    @RequiresPermissions(PermissionCode.SYSDIC_STATUS)
    public Result<Object> updateStatus(Integer id, Integer status) throws MyException {
        try {
            sysDicService.updateStatus(id, status);
            return Result.success(ResultCode.SUCCESS);
        } catch (Exception e) {
            throw MyException.build(ResultCode.UPDATE_ERROR, "修改数据字典状态异常", e);
        }
    }

}
