package com.skyer.controller;

import com.alibaba.fastjson.JSONObject;
import com.skyer.contants.AppConst;
import com.skyer.enumerate.ResultEnum;
import com.skyer.service.UserService;
import com.skyer.vo.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

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
     * 去到用户管理页面
     */
    @RequestMapping("/index")
    public String index() {
        return "user/user";
    }

    /**
     * 通过ID获取用户
     *
     * @param id 用户ID
     */
    @RequestMapping("/getById")
    @RequiresPermissions("A1_01")
    @ResponseBody
    public Object getById(Integer id) {
        try {
            return super.success(userService.getById(id));
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[id: {}]", id);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "通过ID查询用户出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue());
    }

    /**
     * 分页获取用户
     *
     * @param page     页码
     * @param limit    每页显示数量
     */
    @RequestMapping("/getByPage")
    @RequiresPermissions("A1_01")
    @ResponseBody
    public Object getByPage(Integer page, Integer limit, User user) {
        JSONObject result = new JSONObject();
        try {
            List<User> list = userService.getByPage(page, limit, user);
            for (User item : list) {
                item.setCreateName(userService.getById(item.getCreateId()).getNickName());
                item.setLastModifyName(userService.getById(item.getLastModifyId()).getNickName());
            }
            Long totalNum = userService.getTotalNum(user);
            result.put("code", 0);
            result.put("msg", "");
            result.put("count", totalNum);
            result.put("data", list);
        } catch (Exception e) {
            result.put("code", ResultEnum.SEARCH_ERROR.getCode());
            result.put("msg", ResultEnum.SEARCH_ERROR.getValue());
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[page: {}, limit: {}]", page, limit);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "分页查询用户出错，错误信息：", e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 添加用户
     */
    @RequestMapping("/add")
    @RequiresPermissions("A1_01_01")
    @ResponseBody
    public Object add(User user) {
        try {
            user.setCreateId(super.getCurrentUser().getId());
            user.setCreateTime(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
            user.setLastModifyId(super.getCurrentUser().getId());
            user.setLastModifyTime(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
            Md5Hash md5 = new Md5Hash(user.getPassword(), AppConst.MD5_SALT, 2);
            user.setPassword(md5.toString());
            userService.add(user);
            return super.success("添加成功！");
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参: {}", user.toString());
            LOG.error(AppConst.ERROR_LOG_PREFIX + "添加用户出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.INSERT_ERROR.getCode(), ResultEnum.INSERT_ERROR.getValue());
    }

}
