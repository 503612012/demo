package com.oven.core.fundBill.controller;

import com.oven.common.constant.AppConst;
import com.oven.common.constant.PermissionCode;
import com.oven.common.enumerate.ResultEnum;
import com.oven.common.util.LayuiPager;
import com.oven.core.base.controller.BaseController;
import com.oven.core.fundBill.service.FundBillService;
import com.oven.core.fundBill.vo.FundBill;
import com.oven.framework.exception.MyException;
import com.oven.framework.limitation.Limit;
import com.oven.framework.limitation.LimitType;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 收益管理控制层
 *
 * @author Oven
 */
@Controller
@RequestMapping("/fundBill")
public class FundBillController extends BaseController {

    @Resource
    private FundBillService fundBillService;

    /**
     * 去到收益管理页面
     */
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.FUNDBILL_MANAGER)
    public String index() {
        return "fundBill/fundBill";
    }

    /**
     * 通过ID获取收益
     *
     * @param id 收益ID
     */
    @ResponseBody
    @RequestMapping("/getById")
    @RequiresPermissions(PermissionCode.FUNDBILL_MANAGER)
    public Object getById(Integer id) throws MyException {
        try {
            return super.success(fundBillService.getById(id));
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "通过ID获取收益异常", e);
        }
    }

    /**
     * 分页获取收益
     *
     * @param page  页码
     * @param limit 每页显示数量
     */
    @ResponseBody
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.FUNDBILL_MANAGER)
    public Object getByPage(Integer page, Integer limit, String fundName, String date) throws MyException {
        try {
            LayuiPager<FundBill> result = new LayuiPager<>();
            List<FundBill> list = fundBillService.getByPage(page, limit, fundName, date);
            Integer totalNum = fundBillService.getTotalNum(fundName, date);
            result.setCode(0);
            result.setMsg("");
            result.setData(list);
            result.setCount(totalNum);
            return result;
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_PAGE_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "分页获取收益异常", e);
        }
    }

    /**
     * 录入收益
     */
    @ResponseBody
    @RequestMapping("/doAdd")
    @RequiresPermissions(PermissionCode.FUNDBILL_INSERT)
    @Limit(key = AppConst.FUND_INPUT_PROFIT_LIMIT_KEY, period = AppConst.LIMIT_TIME, count = 1, errMsg = AppConst.UPDATE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object doAdd(FundBill fundBill) throws MyException {
        try {
            fundBillService.add(fundBill);
            return super.success(ResultEnum.INSERT_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.INSERT_ERROR.getCode(), ResultEnum.INSERT_ERROR.getValue(), "录入收益异常", e);
        }
    }

    /**
     * 修改收益
     */
    @ResponseBody
    @RequestMapping("/doUpdate")
    @RequiresPermissions(PermissionCode.FUNDBILL_UPDATE)
    @Limit(key = AppConst.FUND_INPUT_UPDATE_PROFIT_LIMIT_KEY, period = AppConst.LIMIT_TIME, count = 1, errMsg = AppConst.UPDATE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object doUpdate(FundBill fundBill) throws MyException {
        try {
            fundBillService.update(fundBill);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), "修改收益异常", e);
        }
    }

    /**
     * 删除收益
     *
     * @param id 收益ID
     */
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions(PermissionCode.FUNDBILL_DELETE)
    @Limit(key = AppConst.FUND_INPUT_DELETE_PROFIT_LIMIT_KEY, period = AppConst.LIMIT_TIME, count = 1, errMsg = AppConst.DELETE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object delete(Integer id) throws MyException {
        try {
            boolean result = fundBillService.delete(id);
            if (result) {
                return super.success(ResultEnum.DELETE_SUCCESS.getValue());
            } else {
                return super.fail(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue());
            }
        } catch (Exception e) {
            throw new MyException(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue(), "删除收益异常", e);
        }
    }

}
