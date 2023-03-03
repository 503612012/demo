package com.oven.demo.core.system.controller;

import com.oven.basic.base.controller.BaseController;
import com.oven.basic.common.util.LayuiPager;
import com.oven.demo.common.constant.PermissionCode;
import com.oven.demo.common.enumerate.ResultEnum;
import com.oven.demo.core.system.entity.SysDicEntity;
import com.oven.demo.core.system.service.SysDicService;
import com.oven.demo.framework.annotation.AspectLog;
import com.oven.demo.framework.config.InitSysDic;
import com.oven.demo.framework.config.SysDic;
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
public class SysDicController extends BaseController<SysDic> {

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
    public Object getById(Integer id) throws MyException {
        try {
            return super.success(sysDicService.getById(id));
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "通过id获取数据字典异常", e);
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
            throw new MyException(ResultEnum.SEARCH_PAGE_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "分页获取数据字典异常", e);
        }
    }

    /**
     * 添加数据字典
     */
    @ResponseBody
    @RequestMapping("/save")
    @AspectLog(title = "添加数据字典")
    @RequiresPermissions(PermissionCode.SYSDIC_INSERT)
    public Object save(SysDicEntity sysdic) throws MyException {
        try {
            sysDicService.save(sysdic);
            return super.success(ResultEnum.SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.INSERT_ERROR.getCode(), ResultEnum.INSERT_ERROR.getValue(), "添加数据字典异常", e);
        }
    }

    /**
     * 修改数据字典
     */
    @ResponseBody
    @RequestMapping("/update")
    @AspectLog(title = "修改数据字典")
    @RequiresPermissions(PermissionCode.SYSDIC_UPDATE)
    public Object update(SysDicEntity sysdic) throws MyException {
        try {
            sysDicService.update(sysdic);
            return super.success(ResultEnum.SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), "修改数据字典异常", e);
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
    public Object delete(Integer id) throws MyException {
        try {
            boolean result = sysDicService.delete(id);
            if (result) {
                return super.success(ResultEnum.SUCCESS.getValue());
            } else {
                return super.fail(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue());
            }
        } catch (Exception e) {
            throw new MyException(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue(), "删除数据字典异常", e);
        }
    }

    /**
     * 重载数据字典
     */
    @ResponseBody
    @RequestMapping("/reload")
    @RequiresPermissions(PermissionCode.SYSDIC_RELOAD)
    public Object reload() throws MyException {
        try {
            initSysDic.initSysDic();
            return super.success(ResultEnum.SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getValue(), "重载数据字典异常", e);
        }
    }

    /**
     * 修改状态
     */
    @ResponseBody
    @RequestMapping("/updateStatus")
    @AspectLog(title = "修改数据字典状态")
    @RequiresPermissions(PermissionCode.SYSDIC_STATUS)
    public Object updateStatus(Integer id, Integer status) throws MyException {
        try {
            sysDicService.updateStatus(id, status);
            return super.success(ResultEnum.SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), "修改数据字典状态异常", e);
        }
    }

}
