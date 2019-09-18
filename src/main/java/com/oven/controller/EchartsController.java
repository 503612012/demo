package com.oven.controller;

import com.oven.constant.PermissionCode;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Echarts控制层
 *
 * @author Oven
 */
@Controller
@RequestMapping("/tools/echarts")
public class EchartsController extends BaseController {

    /**
     * 去到Echarts页面
     */
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.ECHARTS_MANAGER)
    public String index() {
        return "echarts/echarts";
    }

    @ResponseBody
    @RequestMapping("/getData")
    @RequiresPermissions(PermissionCode.ECHARTS_MANAGER)
    public Object getData() {
        Map<String, List> obj = new HashMap<>();
        List<String> categories = new ArrayList<>();
        DateTime dateTime = new DateTime();
        categories.add(dateTime.plusSeconds(-6).toString("HH:mm:ss"));
        categories.add(dateTime.plusSeconds(-5).toString("HH:mm:ss"));
        categories.add(dateTime.plusSeconds(-4).toString("HH:mm:ss"));
        categories.add(dateTime.plusSeconds(-3).toString("HH:mm:ss"));
        categories.add(dateTime.plusSeconds(-2).toString("HH:mm:ss"));
        categories.add(dateTime.plusSeconds(-1).toString("HH:mm:ss"));
        obj.put("categories", categories);
        List<String> data = new ArrayList<>();
        Random random = new Random();
        data.add(String.valueOf(random.nextInt(10)));
        data.add(String.valueOf(random.nextInt(10)));
        data.add(String.valueOf(random.nextInt(10)));
        data.add(String.valueOf(random.nextInt(10)));
        data.add(String.valueOf(random.nextInt(10)));
        data.add(String.valueOf(random.nextInt(10)));
        obj.put("data", data);
        return obj;
    }

}
