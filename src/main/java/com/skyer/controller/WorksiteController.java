package com.skyer.controller;

import com.alibaba.fastjson.JSONObject;
import com.skyer.contants.AppConst;
import com.skyer.contants.PermissionCode;
import com.skyer.enumerate.ResultEnum;
import com.skyer.service.UserService;
import com.skyer.service.WorksiteService;
import com.skyer.vo.Worksite;
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
 * 工地控制层
 *
 * @author SKYER
 */
@Controller
@RequestMapping("/worksite")
public class WorksiteController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(WorksiteController.class);

    @Resource
    private UserService userService;
    @Resource
    private WorksiteService worksiteService;

    /**
     * 去到工地管理页面
     */
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.WORKSITE_MANAGER)
    public String index() {
        return "worksite/worksite";
    }

    /**
     * 分页获取工地
     *
     * @param page  页码
     * @param limit 每页显示数量
     */
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.WORKSITE_MANAGER)
    @ResponseBody
    public Object getByPage(Integer page, Integer limit, Worksite worksite) {
        JSONObject result = new JSONObject();
        try {
            List<Worksite> list = worksiteService.getByPage(page, limit, worksite);
            for (Worksite item : list) {
                item.setCreateName(userService.getById(item.getCreateId()).getNickName());
                item.setLastModifyName(userService.getById(item.getLastModifyId()).getNickName());
            }
            Long totalNum = worksiteService.getTotalNum(worksite);
            result.put("code", 0);
            result.put("msg", "");
            result.put("count", totalNum);
            result.put("data", list);
        } catch (Exception e) {
            result.put("code", ResultEnum.SEARCH_ERROR.getCode());
            result.put("msg", ResultEnum.SEARCH_ERROR.getValue());
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[page: {}, limit: {}, worksite: {}]", page, limit, worksite.toString());
            LOG.error(AppConst.ERROR_LOG_PREFIX + "分页查询工地出错，错误信息：", e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 去到添加工地页面
     */
    @RequestMapping("/add")
    @RequiresPermissions(PermissionCode.WORKSITE_INSERT)
    public String add() {
        return "worksite/add";
    }

    /**
     * 添加工地
     */
    @RequestMapping("/doAdd")
    @RequiresPermissions(PermissionCode.WORKSITE_INSERT)
    @ResponseBody
    public Object doAdd(Worksite worksite) {
        try {
            worksiteService.add(worksite);
            return super.success(ResultEnum.INSERT_SUCCESS.getValue());
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[worksite: {}]", worksite.toString());
            LOG.error(AppConst.ERROR_LOG_PREFIX + "添加工地出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.INSERT_ERROR.getCode(), ResultEnum.INSERT_ERROR.getValue());
    }

    /**
     * 去到工地更新页面
     *
     * @param id 工地ID
     */
    @RequestMapping("/update")
    @RequiresPermissions(PermissionCode.WORKSITE_UPDATE)
    public String update(Integer id, Model model) {
        try {
            Worksite worksite = worksiteService.getById(id);
            model.addAttribute("worksite", worksite);
            return "/worksite/update";
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[id: {}]", id);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "去到工地修改页面出错，错误信息：", e);
            e.printStackTrace();
        }
        return "err";
    }

    /**
     * 修改工地
     */
    @RequestMapping("/doUpdate")
    @RequiresPermissions(PermissionCode.WORKSITE_UPDATE)
    @ResponseBody
    public Object doUpdate(Worksite worksite) {
        try {
            worksiteService.update(worksite);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[worksite: {}]", worksite.toString());
            LOG.error(AppConst.ERROR_LOG_PREFIX + "修改工地出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue());
    }

    /**
     * 删除工地
     *
     * @param id 工地ID
     */
    @RequestMapping("/delete")
    @RequiresPermissions(PermissionCode.WORKSITE_DELETE)
    @ResponseBody
    public Object delete(Integer id) {
        try {
            worksiteService.delete(id);
            return super.success(ResultEnum.DELETE_SUCCESS.getValue());
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[id: {}]", id);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "删除工地出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue());
    }

    /**
     * 修改工地状态
     *
     * @param worksiteId 工地ID
     * @param status     状态编码
     */
    @RequestMapping("/updateStatus")
    @RequiresPermissions(PermissionCode.WORKSITE_SETSTATUS)
    @ResponseBody
    public Object updateStatus(Integer worksiteId, Integer status) {
        try {
            Worksite worksite = worksiteService.getById(worksiteId);
            worksite.setStatus(status);
            worksiteService.update(worksite);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[worksiteId: {}, status: {}]", worksiteId, status);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "修改工地状态出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue());
    }

}
