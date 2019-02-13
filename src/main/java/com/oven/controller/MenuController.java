package com.oven.controller;

import com.oven.contants.AppConst;
import com.oven.contants.PermissionCode;
import com.oven.enumerate.ResultEnum;
import com.oven.exception.MyException;
import com.oven.service.MenuService;
import com.oven.vo.Menu;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 菜单控制层
 *
 * @author Oven
 */
@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController {

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
    @RequestMapping("/getMenuTreeTableData")
    @RequiresPermissions(PermissionCode.MENU_MANAGER)
    @ResponseBody
    public Object getMenuTreeTableData() throws MyException {
        try {
            return super.success(menuService.getMenuTreeTableData());
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), AppConst.SYSTEM_ERROR, e);
        }
    }

    /**
     * 修改菜单
     */
    @RequestMapping("/doUpdate")
    @RequiresPermissions(PermissionCode.MENU_UPDATE)
    @ResponseBody
    public Object doUpdate(Menu menu) throws MyException {
        try {
            menuService.update(menu);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), "修改菜单出错，请联系网站管理人员。", e);
        }
    }

    /**
     * 修改菜单状态
     *
     * @param menuId 菜单ID
     * @param status 状态编码
     */
    @RequestMapping("/updateStatus")
    @RequiresPermissions(PermissionCode.MENU_SETSTATUS)
    @ResponseBody
    public Object updateStatus(Integer menuId, Integer status) throws MyException {
        try {
            Menu menu = menuService.getById(menuId);
            menu.setStatus(status);
            menuService.update(menu);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), "修改菜单状态出错，请联系网站管理人员。", e);
        }
    }

}
