package com.oven.core.workhour.controller;

import com.oven.common.constant.PermissionCode;
import com.oven.common.enumerate.ResultEnum;
import com.oven.core.base.controller.BaseController;
import com.oven.core.workhour.service.WorkhourService;
import com.oven.framework.exception.MyException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 薪资统计控制层
 *
 * @author Oven
 */
@Controller
@RequestMapping("/workhourReport")
public class WorkhourReportController extends BaseController {

    @Resource
    private WorkhourService workhourService;

    /**
     * 去到薪资统计页面
     */
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.WORKHOUR_REPORT_MANAGERT)
    public String index() {
        return "workhourReport/workhourReport";
    }

    /**
     * 获取工时报表数据
     */
    @ResponseBody
    @RequestMapping("/getWorkhourReportData")
    @RequiresPermissions(PermissionCode.WORKHOUR_REPORT_MANAGERT)
    public Object getSalaryData(String date, Integer dateType, Integer employeeId) throws MyException {
        try {
            @SuppressWarnings("rawtypes")
            Map<String, List> result = new HashMap<>();
            List<String> categories = workhourService.getCategoriesForWorkhour(date, dateType, employeeId); // x轴信息
            List<Double> workhours = new ArrayList<>();
            List<Double> preWorkhours = new ArrayList<>();
            List<String> preCategories = new ArrayList<>();
            if (!CollectionUtils.isEmpty(categories)) {
                for (String category : categories) {
                    Double workhour = workhourService.getWorkhourByDateAndDateType(category, dateType, employeeId);
                    workhours.add(workhour == null ? 0d : workhour);
                    if (dateType == 1) { // 年月
                        preCategories.add(DateTime.parse(category).plusYears(-1).toString("yyyy-MM"));
                    } else if (dateType == 2) { // 年月日
                        preCategories.add(DateTime.parse(category).plusMonths(-1).toString("yyyy-MM-dd"));
                    }
                }
            }
            if (!CollectionUtils.isEmpty(preCategories)) {
                for (String preCategory : preCategories) {
                    Double workhour = workhourService.getWorkhourByDateAndDateType(preCategory, dateType, employeeId);
                    preWorkhours.add(workhour == null ? 0d : workhour);
                }
            }
            result.put("categories", categories);
            result.put("workhours", workhours);
            result.put("preWorkhours", preWorkhours);
            return result;
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "获取工时报表数据异常", e);
        }
    }

}
