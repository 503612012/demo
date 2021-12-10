package com.oven.demo.core.finance.controller;

import com.oven.demo.common.constant.PermissionCode;
import com.oven.demo.common.enumerate.ResultEnum;
import com.oven.demo.common.util.LayuiPager;
import com.oven.demo.core.base.controller.BaseController;
import com.oven.demo.core.finance.service.FinanceService;
import com.oven.demo.core.finance.vo.Finance;
import com.oven.demo.framework.exception.MyException;
import com.oven.demo.framework.limitation.Limit;
import com.oven.demo.framework.limitation.LimitKey;
import com.oven.demo.framework.limitation.LimitType;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 财务管理控制层
 *
 * @author Oven
 */
@Controller
@RequestMapping("/finance")
public class FinanceController extends BaseController {

    @Resource
    private FinanceService financeService;

    /**
     * 去到财务管理页面
     */
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.FINANCE_MANAGE)
    public String index() {
        return "finance/finance";
    }

    /**
     * 财务登记
     */
    @ResponseBody
    @RequestMapping("/add")
    @RequiresPermissions(PermissionCode.FINANCE_INSERT)
    @Limit(key = LimitKey.FINANCE_INSERT_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.INSERT_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object add(Finance finance) throws MyException {
        try {
            Finance financeInDb = financeService.getByWorksiteId(finance.getWorksiteId());
            if (financeInDb != null) {
                return super.fail(ResultEnum.INSERT_FINANCE_ERROR.getCode(), ResultEnum.INSERT_FINANCE_ERROR.getValue());
            }
            financeService.add(finance);
            return super.success(ResultEnum.INSERT_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.INSERT_ERROR.getCode(), ResultEnum.INSERT_ERROR.getValue(), "财务登记异常", e);
        }
    }

    /**
     * 删除财务登记
     *
     * @param id 财务登记ID
     */
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions(PermissionCode.FINANCE_DELETE)
    @Limit(key = LimitKey.FINANCE_DELETE_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.DELETE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object delete(Integer id) throws MyException {
        try {
            // 判断该财务登记是否已经完结
            Finance finance = financeService.getById(id);
            if (finance == null) {
                return super.fail(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue());
            } else {
                if (finance.getFinishFlag() == 0) {
                    return super.fail(ResultEnum.DELETE_FINANCE_ERROR.getCode(), ResultEnum.DELETE_FINANCE_ERROR.getValue());
                }
            }
            boolean result = financeService.delete(id);
            if (result) {
                return super.success(ResultEnum.DELETE_SUCCESS.getValue());
            } else {
                return super.fail(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue());
            }
        } catch (Exception e) {
            throw new MyException(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue(), "删除财务登记异常", e);
        }
    }

    /**
     * 分页获取财务登记
     *
     * @param page  页码
     * @param limit 每页显示数量
     */
    @ResponseBody
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.FINANCE_MANAGE)
    public Object getByPage(Integer page, Integer limit, Finance finance) throws MyException {
        try {
            LayuiPager<Finance> result = new LayuiPager<>();
            List<Finance> list = financeService.getByPage(page, limit, finance);
            Integer totalNum = financeService.getTotalNum(finance);
            result.setCode(0);
            result.setMsg("");
            result.setData(list);
            result.setCount(totalNum);
            return result;
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_PAGE_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "分页获取财务登记异常", e);
        }
    }

}
