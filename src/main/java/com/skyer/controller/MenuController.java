package com.skyer.controller;

import com.alibaba.fastjson.JSONObject;
import com.skyer.contants.AppConst;
import com.skyer.contants.PermissionCode;
import com.skyer.enumerate.ResultEnum;
import com.skyer.service.MenuService;
import com.skyer.vo.Menu;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 菜单控制层
 *
 * @author SKYER
 */
@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(MenuController.class);

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
     * 分页菜单树形表格内容
     */
    @RequestMapping("/getMenuTreeTableData")
    @RequiresPermissions(PermissionCode.MENU_MANAGER)
    @ResponseBody
    public Object getMenuTreeTableData() {
        JSONObject result = new JSONObject();
        try {
            return super.success(menuService.getMenuTreeTableData());
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "分页查询菜单出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue());
    }

    /**
     * 修改菜单
     */
    @RequestMapping("/doUpdate")
    @RequiresPermissions(PermissionCode.MENU_UPDATE)
    @ResponseBody
    public Object doUpdate(Menu menu) {
        try {
            menuService.update(menu);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[menu: {}]", menu.toString());
            LOG.error(AppConst.ERROR_LOG_PREFIX + "修改菜单出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue());
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
    public Object updateStatus(Integer menuId, Integer status) {
        try {
            Menu menu = menuService.getById(menuId);
            menu.setStatus(status);
            menuService.update(menu);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[menuId: {}, status: {}]", menuId, status);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "修改菜单状态出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue());
    }

}
