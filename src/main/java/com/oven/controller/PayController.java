package com.oven.controller;

import com.alibaba.fastjson.JSONObject;
import com.oven.constant.PermissionCode;
import com.oven.enumerate.ResultEnum;
import com.oven.exception.MyException;
import com.oven.service.PayRecordService;
import com.oven.service.PayService;
import com.oven.vo.PayRecord;
import com.oven.vo.Workhour;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
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
    @Resource
    private PayRecordService payRecordService;

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
    @RequestMapping("/getWorkhourData")
    @RequiresPermissions(PermissionCode.SALARY_PAY)
    @ResponseBody
    public Object getWorkhourData(String employeeId) throws MyException {
        JSONObject result = new JSONObject();
        try {
            List<Workhour> list = payService.getWorkhourData(employeeId);
            result.put("code", 0);
            result.put("msg", "");
            result.put("data", list);
            return result;
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
        }
    }

    /**
     * 下发薪资
     */
    @RequestMapping("/doPay")
    @RequiresPermissions(PermissionCode.SALARY_PAY)
    @ResponseBody
    public Object doPay(String workhourIds, Integer employeeId, Integer totalHour, Double totalMoney) throws MyException {
        try {
            payService.doPay(workhourIds);
            // 保存发薪记录
            PayRecord payRecord = new PayRecord();
            payRecord.setPayerId(super.getCurrentUser().getId());
            payRecord.setEmployeeId(employeeId);
            payRecord.setPayDate(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
            payRecord.setTotalHour(totalHour);
            payRecord.setTotalMoney(totalMoney);
            payRecord.setWorkhourIds(workhourIds);
            payRecordService.add(payRecord);
            return super.success("发薪成功！");
        } catch (Exception e) {
            throw new MyException(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
        }
    }

}
