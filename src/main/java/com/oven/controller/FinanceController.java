package com.oven.controller;

import com.alibaba.fastjson.JSONObject;
import com.oven.constant.AppConst;
import com.oven.constant.PermissionCode;
import com.oven.enumerate.ResultEnum;
import com.oven.exception.MyException;
import com.oven.limitation.Limit;
import com.oven.limitation.LimitType;
import com.oven.service.FinanceService;
import com.oven.vo.Finance;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 财务管理控制层
 *
 * @author Oven
 */
@Controller
@RequestMapping("/finance")
public class FinanceController extends BaseController {

    @Resource
    private FinanceService financeService;

    /**
     * 去到财务管理页面
     */
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.FINANCE_MANAGE)
    public String index() {
        return "finance/finance";
    }

    /**
     * 去到财务登记页面
     */
    @RequestMapping("/add")
    @RequiresPermissions(PermissionCode.FINANCE_INSERT)
    public String add() {
        return "finance/add";
    }

    /**
     * 财务登记
     */
    @ResponseBody
    @RequestMapping("/doAdd")
    @RequiresPermissions(PermissionCode.FINANCE_INSERT)
    @Limit(key = AppConst.FINANCE_INSERT_LIMIT_KEY, period = 10, count = 1, errMsg = AppConst.INSERT_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object doAdd(Finance finance) throws MyException {
        try {
            Finance financeInDb = financeService.getByWorksiteId(finance.getWorksiteId());
            if (financeInDb != null) {
                return super.fail(ResultEnum.INSERT_FINANCE_ERROR.getCode(), ResultEnum.INSERT_FINANCE_ERROR.getValue());
            }
            financeService.add(finance);
            return super.success(ResultEnum.INSERT_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.INSERT_ERROR.getCode(), ResultEnum.INSERT_ERROR.getValue(), e);
        }
    }

    /**
     * 删除财务登记
     *
     * @param id 财务登记ID
     */
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions(PermissionCode.FINANCE_DELETE)
    @Limit(key = AppConst.FINANCE_DELETE_LIMIT_KEY, period = 10, count = 1, errMsg = AppConst.DELETE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object delete(Integer id) throws MyException {
        try {
            // 判断该财务登记是否已经完结
            Finance finance = financeService.getById(id);
            if (finance == null) {
                return super.fail(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue());
            } else {
                if (finance.getFinishFlag() == 0) {
                    return super.fail(ResultEnum.DELETE_FINANCE_ERROR.getCode(), ResultEnum.DELETE_FINANCE_ERROR.getValue());
                }
            }
            boolean result = financeService.delete(id);
            if (result) {
                return super.success(ResultEnum.DELETE_SUCCESS.getValue());
            } else {
                return super.fail(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue());
            }
        } catch (Exception e) {
            throw new MyException(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue(), e);
        }
    }

    /**
     * 分页获取财务登记
     *
     * @param page  页码
     * @param limit 每页显示数量
     */
    @ResponseBody
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.FINANCE_MANAGE)
    public Object getByPage(Integer page, Integer limit, Finance finance) throws MyException {
        try {
            JSONObject result = new JSONObject();
            List<Finance> list = financeService.getByPage(page, limit, finance);
            Integer totalNum = financeService.getTotalNum(finance);
            result.put("code", 0);
            result.put("msg", "");
            result.put("count", totalNum);
            result.put("data", list);
            return result;
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_PAGE_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
        }
    }

}
