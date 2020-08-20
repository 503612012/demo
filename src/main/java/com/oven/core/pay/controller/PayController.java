package com.oven.core.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.oven.common.constant.AppConst;
import com.oven.common.constant.PermissionCode;
import com.oven.common.enumerate.ResultEnum;
import com.oven.core.base.controller.BaseController;
import com.oven.core.pay.service.PayService;
import com.oven.core.workhour.vo.Workhour;
import com.oven.framework.exception.DoPayException;
import com.oven.framework.exception.MyException;
import com.oven.framework.limitation.Limit;
import com.oven.framework.limitation.LimitType;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 薪资发放控制层
 *
 * @author Oven
 */
@Controller
@RequestMapping("/pay")
public class PayController extends BaseController {

    @Resource
    private PayService payService;

    /**
     * 去到薪资发放页面
     */
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.SALARY_PAY)
    public String index() {
        return "pay/pay";
    }

    /**
     * 获取员工未发放的薪资的工时
     */
    @ResponseBody
    @RequestMapping("/getWorkhourData")
    @RequiresPermissions(PermissionCode.SALARY_PAY)
    public Object getWorkhourData(Integer employeeId, Integer worksiteId) throws MyException {
        JSONObject result = new JSONObject();
        try {
            List<Workhour> list = payService.getWorkhourData(employeeId, worksiteId);
            result.put("code", 0);
            result.put("msg", "");
            result.put("data", list);
            return result;
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "获取员工未发放的薪资的工时异常", e);
        }
    }

    /**
     * 下发薪资
     */
    @ResponseBody
    @RequestMapping("/doPay")
    @RequiresPermissions(PermissionCode.SALARY_PAY)
    @Limit(key = AppConst.PAY_DOPAY_LIMIT_KEY, period = AppConst.LIMIT_TIME, count = 1, errMsg = AppConst.SYSTEM_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object doPay(String workhourIds, Integer employeeId, Integer totalHour, Double totalMoney, String remark, Integer worksiteId, Integer hasModifyMoney, Double changeMoney) throws MyException {
        try {
            String result = payService.doPay(workhourIds, employeeId, totalHour, totalMoney, remark, worksiteId, hasModifyMoney, changeMoney);
            if (StringUtils.isEmpty(result)) {
                return super.success("发薪成功！");
            } else {
                return super.fail(ResultEnum.SYSTEM_ERROR.getCode(), result);
            }
        } catch (Exception e) {
            if (e instanceof DoPayException) {
                return super.fail(((DoPayException) e).getCode(), ((DoPayException) e).getMsg());
            }
            throw new MyException(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "下发薪资异常", e);
        }
    }

}
