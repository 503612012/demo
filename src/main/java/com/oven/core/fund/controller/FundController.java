package com.oven.core.fund.controller;

import com.oven.common.constant.PermissionCode;
import com.oven.common.enumerate.ResultEnum;
import com.oven.common.util.LayuiPager;
import com.oven.core.base.controller.BaseController;
import com.oven.core.fund.service.FundService;
import com.oven.core.fund.vo.Fund;
import com.oven.core.user.vo.User;
import com.oven.framework.annotation.CurrentUser;
import com.oven.framework.cache.CacheService;
import com.oven.framework.exception.MyException;
import com.oven.framework.limitation.Limit;
import com.oven.framework.limitation.LimitKey;
import com.oven.framework.limitation.LimitType;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 基金控制层
 *
 * @author Oven
 */
@Controller
@RequestMapping("/fund")
public class FundController extends BaseController {

    @Resource
    private FundService fundService;
    @Resource
    private CacheService cacheService;
    @Resource
    private ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 去到基金管理页面
     */
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.FUND_MANAGER)
    public String index() {
        return "fund/fund";
    }

    /**
     * 通过ID获取基金
     *
     * @param id 基金ID
     */
    @ResponseBody
    @RequestMapping("/getById")
    @RequiresPermissions(PermissionCode.FUND_MANAGER)
    public Object getById(Integer id) throws MyException {
        try {
            return super.success(fundService.getById(id));
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "通过ID获取基金异常", e);
        }
    }

    /**
     * 分页获取基金
     *
     * @param page  页码
     * @param limit 每页显示数量
     */
    @ResponseBody
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.FUND_MANAGER)
    public Object getByPage(Integer page, Integer limit, String fundName) throws MyException {
        try {
            LayuiPager<Fund> result = new LayuiPager<>();
            List<Fund> list = fundService.getByPage(page, limit, fundName);
            Integer totalNum = fundService.getTotalNum(fundName);
            result.setCode(0);
            result.setMsg("");
            result.setData(list);
            result.setCount(totalNum);
            return result;
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_PAGE_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "分页获取基金异常", e);
        }
    }

    /**
     * 添加基金
     */
    @ResponseBody
    @RequestMapping("/add")
    @RequiresPermissions(PermissionCode.FUND_INSERT)
    @Limit(key = LimitKey.FUND_INSERT_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.INSERT_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object add(Fund fund) throws MyException {
        try {
            fundService.add(fund);
            return super.success(ResultEnum.INSERT_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.INSERT_ERROR.getCode(), ResultEnum.INSERT_ERROR.getValue(), "添加基金异常", e);
        }
    }

    /**
     * 去到基金更新页面-测试页面缓存
     *
     * @param id 基金ID
     */
    @ResponseBody
    @RequestMapping(value = "/updateWithCache", produces = "text/html")
    public String updateWithCache(Integer id, Model model, HttpServletRequest req, HttpServletResponse resp, @CurrentUser User user) throws MyException {
        try {
            System.out.println("自定义参数解析器注入的当前用户：" + user.toString());
            // 查询缓存
            String html = cacheService.get("fund_update");
            if (!StringUtils.isEmpty(html)) {
                return html;
            }
            // 手动渲染
            Fund fund = fundService.getById(id);
            model.addAttribute("fund", fund);
            WebContext ctx = new WebContext(req, resp, req.getServletContext(), req.getLocale(), model.asMap());
            html = thymeleafViewResolver.getTemplateEngine().process("/fund/update", ctx);
            if (!StringUtils.isEmpty(html)) {
                cacheService.set("fund_update", html, 1800000L);
            }
            return html;
        } catch (Exception e) {
            throw new MyException(ResultEnum.ERROR_PAGE.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "去到基金更新页面-测试页面缓存异常", e);
        }
    }

    /**
     * 修改基金
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions(PermissionCode.FUND_UPDATE)
    @Limit(key = LimitKey.FUND_UPDATE_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.UPDATE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object update(Fund fund) throws MyException {
        try {
            fundService.update(fund);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), "修改基金异常", e);
        }
    }

    /**
     * 修改基金排序
     */
    @ResponseBody
    @RequestMapping("/updateOrder")
    @RequiresPermissions(PermissionCode.FUND_UPDATE_ORDER)
    public Object updateOrder(Integer fundId, Integer order) throws MyException {
        try {
            fundService.updateOrder(fundId, order);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), "修改基金排序异常", e);
        }
    }

    /**
     * 删除基金
     *
     * @param id 基金ID
     */
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions(PermissionCode.FUND_DELETE)
    @Limit(key = LimitKey.FUND_DELETE_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.DELETE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object delete(Integer id) throws MyException {
        try {
            boolean result = fundService.delete(id);
            if (result) {
                return super.success(ResultEnum.DELETE_SUCCESS.getValue());
            } else {
                return super.fail(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue());
            }
        } catch (Exception e) {
            throw new MyException(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue(), "删除基金异常", e);
        }
    }

    /**
     * 修改基金状态
     *
     * @param fundId 基金ID
     * @param status 状态编码
     */
    @ResponseBody
    @RequestMapping("/updateStatus")
    @RequiresPermissions(PermissionCode.FUND_SETSTATUS)
    @Limit(key = LimitKey.FUND_UPDATE_STATUS_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.UPDATE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object updateStatus(Integer fundId, Integer status) throws MyException {
        try {
            Fund fund = fundService.getById(fundId);
            fund.setStatus(status);
            fundService.update(fund);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), "修改基金状态异常", e);
        }
    }

    /**
     * 获取所有基金
     */
    @ResponseBody
    @RequestMapping("/getAll")
    @RequiresPermissions(PermissionCode.FUND_MANAGER)
    public Object getAll() throws MyException {
        try {
            return super.success(fundService.getAll());
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "获取所有基金异常", e);
        }
    }

}
