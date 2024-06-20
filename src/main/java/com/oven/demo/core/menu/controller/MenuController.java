package com.oven.demo.core.menu.controller;

import com.oven.basic.common.util.Result;
import com.oven.demo.common.constant.PermissionCode;
import com.oven.demo.common.enumerate.ResultCode;
import com.oven.demo.core.menu.entity.Menu;
import com.oven.demo.core.menu.service.MenuService;
import com.oven.demo.framework.annotation.AspectLog;
import com.oven.demo.framework.exception.MyException;
import com.oven.demo.framework.limitation.Limit;
import com.oven.demo.framework.limitation.LimitKey;
import com.oven.demo.framework.limitation.LimitType;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * 菜单控制层
 *
 * @author Oven
 */
@ApiIgnore
@Controller
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    /**
     * 去到菜单管理页面
     */
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.MENU_MANAGER)
    public String index() {
        return "menu/menu";
    }

    /**
     * 分页获取菜单树形表格内容
     */
    @ResponseBody
    @RequestMapping("/getMenuTreeTableData")
    @RequiresPermissions(PermissionCode.MENU_MANAGER)
    public Result<Object> getMenuTreeTableData() throws MyException {
        try {
            return Result.success(menuService.getMenuTreeTableData());
        } catch (Exception e) {
            throw MyException.build(ResultCode.SEARCH_ERROR, "分页获取菜单树形表格内容异常", e);
        }
    }

    /**
     * 修改菜单
     */
    @ResponseBody
    @AspectLog(title = "修改菜单")
    @RequestMapping("/doUpdate")
    @RequiresPermissions(PermissionCode.MENU_UPDATE)
    @Limit(key = LimitKey.MENU_UPDATE_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.UPDATE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Result<Object> doUpdate(Menu menu) throws MyException {
        try {
            // menuService.update(menu);
            return Result.success(ResultCode.NOT_YET_ACTIVATED_ERROR);
        } catch (Exception e) {
            throw MyException.build(ResultCode.UPDATE_ERROR, "修改菜单异常", e);
        }
    }

    /**
     * 修改菜单状态
     *
     * @param menuId 菜单id
     * @param status 状态编码
     */
    @ResponseBody
    @AspectLog(title = "修改菜单状态")
    @RequestMapping("/updateStatus")
    @RequiresPermissions(PermissionCode.MENU_SETSTATUS)
    @Limit(key = LimitKey.MENU_UPDATE_STATUS_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.UPDATE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Result<Object> updateStatus(Integer menuId, Integer status) throws MyException {
        try {
            Menu menu = menuService.getById(menuId);
            menu.setStatus(status);
            menuService.update(menu);
            return Result.success();
        } catch (Exception e) {
            throw MyException.build(ResultCode.UPDATE_ERROR, "修改菜单状态异常", e);
        }
    }

}
