package com.skyer.controller;

import com.skyer.contants.AppConst;
import com.skyer.enumerate.ResultEnum;
import com.skyer.service.UserService;
import com.skyer.vo.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制层
 *
 * @author SKYER
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    /**
     * 通过ID获取用户
     *
     * @param id 用户ID
     */
    @RequestMapping("/getById")
    public String getById(Integer id, Model model, HttpServletRequest req) {
        try {
            User user = userService.getById(id);
            model.addAttribute("user", user);
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[id: {}]", id);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "通过ID查询用户出错，错误信息：", e);
            e.printStackTrace();
        }
        return "detail";
    }

    /**
     * 分页获取用户
     *
     * @param pageNum 页码
     */
    @RequestMapping("/getByPage")
    @ResponseBody
    @RequiresPermissions("A1_01")
    public Object getByPage(Integer pageNum, HttpServletRequest req) {
        try {
            Map<String, Object> result = new HashMap<>();
            List<User> list = userService.getByPage(pageNum, AppConst.PAGE_SIZE);
            Long totalNum = userService.getTotalNum();
            Long totalPage = totalNum % AppConst.PAGE_SIZE == 0 ? (totalNum / AppConst.PAGE_SIZE) : (totalNum / AppConst.PAGE_SIZE + 1);
            result.put("list", list);
            result.put("pageNum", pageNum);
            result.put("totalPage", totalPage);
            return super.success(result);
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[pageNum: {}]", pageNum);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "分页查询用户出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue());
    }

}
