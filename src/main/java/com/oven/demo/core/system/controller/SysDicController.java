package com.oven.demo.core.system.controller;

import com.oven.basic.common.util.LayuiPager;
import com.oven.basic.common.util.ResultInfo;
import com.oven.demo.common.constant.PermissionCode;
import com.oven.demo.common.enumerate.ResultEnum;
import com.oven.demo.core.system.entity.SysDicEntity;
import com.oven.demo.core.system.service.SysDicService;
import com.oven.demo.framework.annotation.AspectLog;
import com.oven.demo.framework.config.InitSysDic;
import com.oven.demo.framework.exception.MyException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
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
    public ResultInfo<Object> getById(Integer id) throws MyException {
        try {
            return ResultInfo.success(sysDicService.getById(id));
        } catch (Exception e) {
            throw MyException.build(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "通过id获取数据字典异常", e);
        }
    }

    /**
     * 分页获取数据字典
     *
     * @param page  页码
     * @param limit 每页显示数量
     */
    @ResponseBody
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.SYSDIC_MANAGER)
    public Object getByPage(Integer page, Integer limit, SysDicEntity sysdic) throws MyException {
        try {
            LayuiPager<SysDicEntity> result = new LayuiPager<>();
            List<SysDicEntity> list = sysDicService.getByPage(page, limit, sysdic);
            Integer totalNum = sysDicService.getTotalNum(sysdic);
            result.setCode(0);
            result.setMsg("");
            result.setData(list);
            result.setCount(totalNum);
            return result;
        } catch (Exception e) {
            throw MyException.build(ResultEnum.SEARCH_PAGE_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "分页获取数据字典异常", e);
        }
    }

    /**
     * 添加数据字典
     */
    @ResponseBody
    @RequestMapping("/save")
    @AspectLog(title = "添加数据字典")
    @RequiresPermissions(PermissionCode.SYSDIC_INSERT)
    public ResultInfo<Object> save(SysDicEntity sysdic) throws MyException {
        try {
            sysDicService.save(sysdic);
            return ResultInfo.success(ResultEnum.SUCCESS.getValue());
        } catch (Exception e) {
            throw MyException.build(ResultEnum.INSERT_ERROR.getCode(), ResultEnum.INSERT_ERROR.getValue(), "添加数据字典异常", e);
        }
    }

    /**
     * 修改数据字典
     */
    @ResponseBody
    @RequestMapping("/update")
    @AspectLog(title = "修改数据字典")
    @RequiresPermissions(PermissionCode.SYSDIC_UPDATE)
    public ResultInfo<Object> update(SysDicEntity sysdic) throws MyException {
        try {
            sysDicService.update(sysdic);
            return ResultInfo.success(ResultEnum.SUCCESS.getValue());
        } catch (Exception e) {
            throw MyException.build(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), "修改数据字典异常", e);
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
    public ResultInfo<Object> delete(Integer id) throws MyException {
        try {
            boolean result = sysDicService.delete(id);
            if (result) {
                return ResultInfo.success(ResultEnum.SUCCESS.getValue());
            } else {
                return ResultInfo.fail(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue());
            }
        } catch (Exception e) {
            throw MyException.build(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue(), "删除数据字典异常", e);
        }
    }

    /**
     * 重载数据字典
     */
    @ResponseBody
    @RequestMapping("/reload")
    @RequiresPermissions(PermissionCode.SYSDIC_RELOAD)
    public ResultInfo<Object> reload() throws MyException {
        try {
            initSysDic.initSysDic();
            return ResultInfo.success(ResultEnum.SUCCESS.getValue());
        } catch (Exception e) {
            throw MyException.build(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getValue(), "重载数据字典异常", e);
        }
    }

    /**
     * 修改状态
     */
    @ResponseBody
    @RequestMapping("/updateStatus")
    @AspectLog(title = "修改数据字典状态")
    @RequiresPermissions(PermissionCode.SYSDIC_STATUS)
    public ResultInfo<Object> updateStatus(Integer id, Integer status) throws MyException {
        try {
            sysDicService.updateStatus(id, status);
            return ResultInfo.success(ResultEnum.SUCCESS.getValue());
        } catch (Exception e) {
            throw MyException.build(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), "修改数据字典状态异常", e);
        }
    }

}
