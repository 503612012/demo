package com.oven.controller;

import com.alibaba.fastjson.JSONObject;
import com.oven.constant.AppConst;
import com.oven.constant.PermissionCode;
import com.oven.enumerate.ResultEnum;
import com.oven.exception.MyException;
import com.oven.limitation.Limit;
import com.oven.limitation.LimitType;
import com.oven.service.WorkhourService;
import com.oven.vo.Workhour;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 工时控制层
 *
 * @author Oven
 */
@Controller
@RequestMapping("/workhour")
public class WorkhourController extends BaseController {

    @Resource
    private WorkhourService workhourService;

    /**
     * 去到工时管理页面
     */
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.WORKHOUR_MANAGER)
    public String index() {
        return "workhour/workhour";
    }

    /**
     * 分页获取工时
     *
     * @param page  页码
     * @param limit 每页显示数量
     */
    @ResponseBody
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.WORKHOUR_MANAGER)
    public Object getByPage(Integer page, Integer limit, Workhour workhour) throws MyException {
        JSONObject result = new JSONObject();
        try {
            List<Workhour> list = workhourService.getByPage(page, limit, workhour);
            Integer totalNum = workhourService.getTotalNum(workhour);
            result.put("code", 0);
            result.put("count", totalNum);
            result.put("msg", "");
            result.put("data", list);
            return result;
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_PAGE_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
        }
    }

    /**
     * 去到添加工时页面
     */
    @RequestMapping("/add")
    @RequiresPermissions(PermissionCode.WORKHOUR_INSERT)
    public String add() {
        return "workhour/add";
    }

    /**
     * 添加工时
     */
    @ResponseBody
    @RequestMapping("/doAdd")
    @RequiresPermissions(PermissionCode.WORKHOUR_INSERT)
    @Limit(key = AppConst.WORKHOUR_INSERT_LIMIT_KEY, period = 10, count = 1, errMsg = AppConst.INSERT_LIMIT, limitType = LimitType.CUSTOMER)
    public Object doAdd(Workhour workhour) throws MyException {
        try {
            Workhour workhourInDeb = workhourService.isInputed(workhour.getEmployeeId(), workhour.getWorkDate(), workhour.getWorksiteId());
            if (workhourInDeb != null) {
                return super.success(true);
            }
            workhourService.add(workhour);
            return super.success(ResultEnum.INSERT_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.INSERT_ERROR.getCode(), ResultEnum.INSERT_ERROR.getValue(), e);
        }
    }

    /**
     * 删除工时
     *
     * @param id 工时ID
     */
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions(PermissionCode.WORKHOUR_DELETE)
    @Limit(key = AppConst.WORKHOUR_DELETE_LIMIT_KEY, period = 10, count = 1, errMsg = AppConst.DELETE_LIMIT, limitType = LimitType.CUSTOMER)
    public Object delete(Integer id) throws MyException {
        try {
            workhourService.delete(id);
            return super.success(ResultEnum.DELETE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue(), e);
        }
    }

    /**
     * 判断该员工该日期是否有录入过
     */
    @ResponseBody
    @RequestMapping("/isInputed")
    @RequiresPermissions(PermissionCode.WORKHOUR_INSERT)
    public Object isInputed(Integer employeeId, String workDate, Integer worksiteId) throws MyException {
        try {
            Workhour workhour = workhourService.isInputed(employeeId, workDate, worksiteId);
            if (workhour == null) {
                return super.success(false);
            } else {
                return super.success(true);
            }
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
        }
    }

}
