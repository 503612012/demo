package com.oven.common.enumerate;

/**
 * 统一返回枚举类
 *
 * @author Oven
 */
public enum ResultEnum {

    SUCCESS(200, "操作成功！"),
    INSERT_SUCCESS(200, "添加成功！"),
    DELETE_SUCCESS(200, "删除成功！"),
    UPDATE_SUCCESS(200, "修改成功！"),

    UNKNOW_ERROR(3001, "未知错误！"),
    OVER_LIMIT_ERROR(3002, "OVER LIMIT!"),
    DELETE_EMPLOYEE_ERROR_UNPAY_SALARY(3003, "该员工存在未发薪资的工时，请核对后再删除！"),
    DELETE_EMPLOYEE_ERROR_UNBACK_ADVANCE_SALARY(3004, "该员工存在未归还的预支薪资，请核对后再删除！"),
    DELETE_FINANCE_ERROR(3005, "该笔财务已经完结，禁止删除！"),
    INSERT_FINANCE_ERROR(3006, "该工地财务已经录入，禁止重复登记！"),
    DELETE_ADVANCE_SALARY_ERROR(3007, "该员工预支薪资已经归还，禁止删除！"),
    DELETE_WORKHOUR_ERROR_HAS_UNPAY_WORKHOUR(3008, "该工地下存在未发放薪资的工时，禁止删除！"),
    DELETE_WORKHOUR_ERROR_HAS_FINANCE(3009, "该工地有录入的财务信息，禁止删除！"),
    SYSTEM_ERROR(3010, "系统错误！"),

    NO_THIS_USER(4001, "用户名或密码错误！"),
    PASSWORD_WRONG(4002, "用户名或密码错误！"),
    USER_DISABLE(4003, "该用户已被锁定，请联系管理人员！"),
    SESSION_TIMEOUT(4004, "未登录或会话超时，请重新登录！"),
    LOSE_LOGIN(4005, "已掉线，请重新登录！"),
    OTHER_LOGINED(4006, "会话失效，该账号已被其他人登录。请检查账号是否丢失或立即修改密码！"),
    CAPTCHA_ERROR(4007, "验证码错误，请重新输入！"),
    CAPTCHA_IS_NONE(4008, "请输入验证码！"),
    OLD_PASSWORD_WRONG(4009, "原始密码错误！"),
    USER_ALREADY_EXIST(4010, "该用户名已存在！"),

    DOPAY_ADVANCE_SALARY_OVER_PAY_SALARY(5001, "预支总金额大于所发薪资，本次发薪无效！"),
    DOPAY_NO_WORKSITE_SALARY(5002, "该工地还未录入薪资，请录入后再进行发放操作！"),
    DOPAY_TOTAL_SALARY_OVER_PAY_SALARY(5003, "该工地仅剩余{0}元，不够本次薪资发放，本次薪资发放无效！"),

    SEARCH_PAGE_ERROR(6000, "分页查询错误！"),
    SEARCH_ERROR(6001, "查询错误！"),
    INSERT_ERROR(6002, "添加错误！"),
    DELETE_ERROR(6003, "删除错误！"),
    UPDATE_ERROR(6004, "修改错误！"),
    ERROR_PAGE(6005, "去到错误页面！");

    private Integer code;
    private String value;

    ResultEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
