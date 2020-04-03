package com.oven.core.fundBill.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oven.constant.PermissionCode;
import com.oven.core.base.controller.BaseController;
import com.oven.enumerate.ResultEnum;
import com.oven.exception.MyException;
import com.oven.core.fundBill.service.FundBillService;
import com.oven.core.fund.service.FundService;
import com.oven.core.fund.vo.Fund;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 收益报表控制层
 *
 * @author Oven
 */
@Controller
@RequestMapping("/fundBillCharts")
public class FundBillChartsController extends BaseController {

    @Resource
    private FundService fundService;
    @Resource
    private FundBillService fundBillService;

    /**
     * 去到收益报表页面
     */
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.FUNDBILLCHARTS_MANAGER)
    public String index() {
        return "fundBillCharts/fundBillCharts";
    }

    /**
     * 获取累计收益
     */
    @ResponseBody
    @RequestMapping("/getTotal")
    @RequiresPermissions(PermissionCode.FUNDBILLCHARTS_MANAGER)
    public Object getTotal() throws MyException {
        try {
            return super.success(fundBillService.getTotal());
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
        }
    }

    /**
     * 获取收益报表数据
     *
     * @param dateType 日期类型：1按月、2按天
     */
    @ResponseBody
    @RequestMapping("/getChartsData")
    @RequiresPermissions(PermissionCode.FUNDBILLCHARTS_MANAGER)
    public Object getChartsData(String date, Integer dateType) throws MyException {
        try {
            JSONObject result = new JSONObject();
            JSONArray source = new JSONArray();
            JSONArray series = new JSONArray();
            int categoryNum;
            if (dateType == 1) { // 按月查看，获取当年12个月份的数据
                categoryNum = 12;
            } else if (dateType == 2) { // 按日查看，获取当月每天的数据
                DateTime now = new DateTime(date);
                categoryNum = now.plusMonths(1).plusDays(-now.plusMonths(1).dayOfMonth().get()).getDayOfMonth();
            } else {
                return super.fail(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getValue());
            }
            List<Fund> fundsList = fundService.getAll();
            if (CollectionUtils.isEmpty(fundsList)) {
                return super.fail(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue());
            }
            for (int i = 0; i < fundsList.size() + 1; i++) {
                JSONObject seriesItem = new JSONObject();
                seriesItem.put("type", "line");
                seriesItem.put("smooth", true);
                if (i == fundsList.size() + 1) {
                    seriesItem.put("color", "green");
                }
                series.add(seriesItem);
            }
            JSONArray firstLine = new JSONArray();
            firstLine.add("funds");
            for (Fund fund : fundsList) {
                firstLine.add(fund.getFundName());
            }
            if (categoryNum > 12) {
                firstLine.add("当日累计");
            } else {
                firstLine.add("当月累计");
            }
//            firstLine.add("平均收益累计");
            source.add(firstLine);
            for (int i = 1; i <= categoryNum; i++) {
                JSONArray lineItem = new JSONArray();
                String category;
                if (i < 10) {
                    category = date + "-0" + i;
                } else {
                    category = date + "-" + i;
                }
                if (categoryNum > 12 && isMondayOrSunday(category)) { // 如果看的是日视图，剔除掉周一和周日的数据
                    continue;
                }
                lineItem.add(category);
                for (Fund fund : fundsList) {
                    Map<String, Object> fundData = fundBillService.getChartsData(category, dateType, fund.getId());
                    lineItem.add(fundData == null ? 0 : (fundData.get("money") == null ? 0 : fundData.get("money")));
                }
                Double currentDayTotal = fundBillService.getCurrentDayTotalByDate(category, dateType); // 当日累计
//                Double total = fundBillService.getTotalByDate(category, dateType); // 持有累计
                lineItem.add(currentDayTotal == null ? 0 : currentDayTotal);
//                lineItem.add(total == null ? 0 : (total / fundsList.size()));
                source.add(lineItem);
            }
            result.put("source", source);
            result.put("series", series);
            return super.success(result);
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
        }
    }

    /**
     * 获取微信累计基金报表数据
     */
    @ResponseBody
    @RequestMapping("/getTotalChartsData")
    @RequiresPermissions(PermissionCode.FUNDBILLCHARTS_MANAGER)
    public Object getTotalChartsData(String date, Integer dateType) throws MyException {
        try {
            JSONObject result = new JSONObject();
            List<String> categories = new ArrayList<>();
            JSONArray data = new JSONArray();
            int categoryNum;
            if (dateType == 1) { // 按月查看，获取当年12个月份的数据
                categoryNum = 12;
            } else if (dateType == 2) { // 按日查看，获取当月每天的数据
                DateTime now = new DateTime(date);
                categoryNum = now.plusMonths(1).plusDays(-now.plusMonths(1).dayOfMonth().get()).getDayOfMonth();
            } else {
                return super.fail(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getValue());
            }
            for (int i = 1; i <= categoryNum; i++) {
                String category;
                if (i < 10) {
                    category = date + "-0" + i;
                } else {
                    category = date + "-" + i;
                }
                if (dateType == 2 && isMondayOrSunday(category)) { // 剔除掉周一和周日的数据
                    continue;
                }
                categories.add(category);
                Double money = fundBillService.getTotalByDate(category, dateType);
                data.add(money == null ? 0 : money);
            }
            result.put("categories", categories);
            result.put("data", data);
            return super.success(result);
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
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
