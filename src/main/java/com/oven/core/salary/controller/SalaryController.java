package com.oven.core.salary.controller;

import com.oven.common.constant.PermissionCode;
import com.oven.common.enumerate.ResultEnum;
import com.oven.core.advanceSalary.service.AdvanceSalaryService;
import com.oven.core.base.controller.BaseController;
import com.oven.core.workhour.service.WorkhourService;
import com.oven.framework.exception.MyException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@RequestMapping("/salary")
public class SalaryController extends BaseController {

    @Resource
    private WorkhourService workhourService;
    @Resource
    private AdvanceSalaryService advanceSalaryService;

    /**
     * 去到薪资统计页面
     */
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.SALARY_COUNT)
    public String index() {
        return "salary/salary";
    }

    /**
     * 获取薪资统计图表数据
     */
    @ResponseBody
    @RequestMapping("/getSalaryData")
    @RequiresPermissions(PermissionCode.SALARY_COUNT)
    public Object getSalaryData(String date, Integer dateType) throws MyException {
        try {
            // 获取X轴信息
            List<String> categories = workhourService.getCategories(date, dateType);
            @SuppressWarnings("rawtypes")
            Map<String, List> result = new HashMap<>();
            List<Double> salaryIn = new ArrayList<>();
            List<Double> salaryOut = new ArrayList<>();
            List<Double> advanceSalarys = new ArrayList<>();
            if (dateType == 1) {
                dateType = 2;
            } else if (dateType == 2) {
                dateType = 3;
            }
            if (!CollectionUtils.isEmpty(categories)) {
                for (String category : categories) {
                    Double salaryInData = workhourService.getSalaryByDateAndDateType(category, dateType, "in");
                    salaryIn.add(salaryInData == null ? 0d : salaryInData);
                    Double salaryOutMoney = workhourService.getSalaryByDateAndDateType(category, dateType, "out");
                    salaryOut.add(salaryOutMoney == null ? 0d : salaryOutMoney);
                    Double advanceSalary = advanceSalaryService.getAdvanceSalaryByDateAndDateType(category, dateType);
                    advanceSalarys.add(advanceSalary == null ? 0d : advanceSalary);
                }
            }
            result.put("categories", categories);
            result.put("salaryIn", salaryIn);
            result.put("salaryOut", salaryOut);
            result.put("advanceSalary", advanceSalarys);
            return super.success(result);
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "获取薪资统计图表数据异常", e);
        }
    }

    /**
     * 加载同比增长数据
     */
    @ResponseBody
    @RequestMapping("/getUpData")
    @RequiresPermissions(PermissionCode.SALARY_COUNT)
    public Object getUpData(String date, Integer dateType) throws MyException {
        try {
            Map<String, Double> result = new HashMap<>();
            Double inSalary = workhourService.getSalaryCompareProportion(date, dateType, "in");
            Double outSalary = workhourService.getSalaryCompareProportion(date, dateType, "out");
            Double advanceSalary = advanceSalaryService.getAdvanceSalaryCompareProportion(date, dateType);
            result.put("inSalary", inSalary);
            result.put("outSalary", outSalary);
            result.put("advanceSalary", advanceSalary);
            return super.success(result);
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "加载同比增长数据异常", e);
        }
    }

}
