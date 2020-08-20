package com.oven.core.wechatFund.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oven.common.constant.PermissionCode;
import com.oven.common.enumerate.ResultEnum;
import com.oven.core.base.controller.BaseController;
import com.oven.core.wechatFund.service.WechatFundService;
import com.oven.core.wechatFund.vo.WechatFund;
import com.oven.framework.exception.MyException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 微信基金控制层
 *
 * @author Oven
 */
@Controller
@RequestMapping("/wechatFund")
public class WechatFundController extends BaseController {

    @Resource
    private WechatFundService wechatFundService;

    /**
     * 去到微信基金页面
     */
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.WECHAT_FUND_MANAGER)
    public String index() {
        return "wechatFund/wechatFund";
    }

    /**
     * 录入收益
     */
    @ResponseBody
    @RequestMapping("/doAdd")
    @RequiresPermissions(PermissionCode.WECHAT_FUND_INSERT)
    public Object doAdd(WechatFund wechatFund) throws MyException {
        try {
            wechatFundService.add(wechatFund);
            return super.success(ResultEnum.INSERT_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.INSERT_ERROR.getCode(), ResultEnum.INSERT_ERROR.getValue(), "录入收益异常", e);
        }
    }

    /**
     * 获取微信基金报表数据
     */
    @ResponseBody
    @RequestMapping("/getChartsData")
    @RequiresPermissions(PermissionCode.WECHAT_FUND_MANAGER)
    public Object getChartsData(String date) throws MyException {
        try {
            JSONObject result = new JSONObject();
            List<String> categories = new ArrayList<>();
            JSONArray data = new JSONArray();
            DateTime now = new DateTime(date);
            int categoryNum = now.plusMonths(1).plusDays(-now.plusMonths(1).dayOfMonth().get()).getDayOfMonth();
            for (int i = 1; i <= categoryNum; i++) {
                String category;
                if (i < 10) {
                    category = date + "-0" + i;
                } else {
                    category = date + "-" + i;
                }
                if (isMondayOrSunday(category)) { // 剔除掉周一和周日的数据
                    continue;
                }
                categories.add(category);
                Map<String, Object> fundData = wechatFundService.getChartsData(category);
                data.add(fundData == null ? 0 : (fundData.get("money") == null ? 0 : fundData.get("money")));
            }
            result.put("categories", categories);
            result.put("data", data);
            return super.success(result);
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "获取微信基金报表数据异常", e);
        }
    }

    /**
     * 获取微信累计基金报表数据
     */
    @ResponseBody
    @RequestMapping("/getTotalChartsData")
    @RequiresPermissions(PermissionCode.WECHAT_FUND_MANAGER)
    public Object getTotalChartsData(String date) throws MyException {
        try {
            JSONObject result = new JSONObject();
            List<String> categories = new ArrayList<>();
            JSONArray data = new JSONArray();
            DateTime now = new DateTime(date);
            int categoryNum = now.plusMonths(1).plusDays(-now.plusMonths(1).dayOfMonth().get()).getDayOfMonth();
            for (int i = 1; i <= categoryNum; i++) {
                String category;
                if (i < 10) {
                    category = date + "-0" + i;
                } else {
                    category = date + "-" + i;
                }
                if (isMondayOrSunday(category)) { // 剔除掉周一和周日的数据
                    continue;
                }
                categories.add(category);
                Map<String, Object> fundData = wechatFundService.getTotalChartsData(category);
                data.add(fundData == null ? 0 : (fundData.get("money") == null ? 0 : fundData.get("money")));
            }
            result.put("categories", categories);
            result.put("data", data);
            return super.success(result);
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "获取微信累计基金报表数据异常", e);
        }
    }

    /**
     * 判断指定日期是不是周一或者周日
     */
    private boolean isMondayOrSunday(String date) {
        int dayOfWeek = new DateTime(date).getDayOfWeek();
        return dayOfWeek == 1 || dayOfWeek == 7;
    }

}
