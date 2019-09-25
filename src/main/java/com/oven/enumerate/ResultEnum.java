package com.oven.enumerate;

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
    DELETE_EMPLOYEE_ERROR(3003, "该员工存在未发薪资的工时，请核对后再删除！"),
    SYSTEM_ERROR(3004, "系统错误！"),

    NO_THIS_USER(4001, "该用户不存在！"),
    PASSWORD_WRONG(4002, "密码错误！"),
    USER_DISABLE(4003, "该用户已被锁定，请联系管理人员！"),
    SESSION_TIMEOUT(4004, "未登录或会话超时，请重新登录！"),
    LOSE_LOGIN(4005, "已掉线，请重新登录！"),
    OTHER_LOGINED(4006, "会话失效，该账号已被其他人登录。请检查账号是否丢失或立即修改密码！"),
    CAPTCHA_ERROR(4007, "验证码错误，请重新输入！"),
    CAPTCHA_IS_NONE(4008, "请输入验证码！"),

    SEARCH_PAGE_ERROR(5000, "分页查询错误！"),
    SEARCH_ERROR(5001, "查询错误！"),
    INSERT_ERROR(5002, "添加错误！"),
    DELETE_ERROR(5003, "删除错误！"),
    UPDATE_ERROR(5004, "修改错误！"),
    ERROR_PAGE(5005, "去到错误页面！");

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
