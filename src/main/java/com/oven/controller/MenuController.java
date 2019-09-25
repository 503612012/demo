package com.oven.controller;

import com.oven.constant.AppConst;
import com.oven.constant.PermissionCode;
import com.oven.enumerate.ResultEnum;
import com.oven.exception.MyException;
import com.oven.limitation.Limit;
import com.oven.limitation.LimitType;
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
    @ResponseBody
    @RequestMapping("/getMenuTreeTableData")
    @RequiresPermissions(PermissionCode.MENU_MANAGER)
    public Object getMenuTreeTableData() throws MyException {
        try {
            return super.success(menuService.getMenuTreeTableData());
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
        }
    }

    /**
     * 修改菜单
     */
    @ResponseBody
    @RequestMapping("/doUpdate")
    @RequiresPermissions(PermissionCode.MENU_UPDATE)
    @Limit(key = "limit", period = 10, count = 1, errMsg = AppConst.UPDATE_LIMIT, limitType = LimitType.IP)
    public Object doUpdate(Menu menu) throws MyException {
        try {
            menuService.update(menu);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), e);
        }
    }

    /**
     * 修改菜单状态
     *
     * @param menuId 菜单ID
     * @param status 状态编码
     */
    @ResponseBody
    @RequestMapping("/updateStatus")
    @RequiresPermissions(PermissionCode.MENU_SETSTATUS)
    @Limit(key = "limit", period = 5, count = 1, errMsg = AppConst.UPDATE_LIMIT, limitType = LimitType.IP)
    public Object updateStatus(Integer menuId, Integer status) throws MyException {
        try {
            Menu menu = menuService.getById(menuId);
            menu.setStatus(status);
            menuService.update(menu);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), e);
        }
    }

}
