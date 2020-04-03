package com.oven.core.payRecord.controller;

import com.oven.constant.PermissionCode;
import com.oven.core.base.controller.BaseController;
import com.oven.enumerate.ResultEnum;
import com.oven.exception.MyException;
import com.oven.core.payRecord.service.PayRecordService;
import com.oven.util.LayuiPager;
import com.oven.core.payRecord.vo.PayRecord;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 发薪记录控制层
 *
 * @author Oven
 */
@Controller
@RequestMapping("/payRecord")
public class PayRecordController extends BaseController {

    @Resource
    private PayRecordService payRecordService;

    /**
     * 去到发薪记录管理页面
     */
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.SALARY_PAY_RECORD)
    public String index() {
        return "payRecord/payRecord";
    }

    /**
     * 分页获取发薪记录
     *
     * @param page  页码
     * @param limit 每页显示数量
     */
    @ResponseBody
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.SALARY_PAY_RECORD)
    public Object getByPage(Integer page, Integer limit, String employeeName) throws MyException {
        try {
            LayuiPager<PayRecord> result = new LayuiPager<>();
            List<PayRecord> list = payRecordService.getByPage(page, limit, employeeName);
            Integer totalNum = payRecordService.getTotalNum(employeeName);
            result.setCode(0);
            result.setMsg("");
            result.setData(list);
            result.setCount(totalNum);
            return result;
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_PAGE_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
        }
    }

}
