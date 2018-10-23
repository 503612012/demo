package com.skyer.enumerate;

/**
 * 统一返回枚举类
 *
 * @author SKYER
 */
public enum ResultEnum {

    SUCCESS(200, "操作成功！"),

    UNKNOW_ERROR(3001, "未知错误！"),

    SEARCH_ERROR(5001, "查询错误！"),
    INSERT_ERROR(5002, "添加错误！"),
    DELETE_ERROR(5003, "删除错误！"),
    UPDATE_ERROR(5004, "修改错误！"),

    NO_THIS_USER(4001, "该用户不存在！"),
    PASSWORD_WRONG(4002, "密码错误！"),
    USER_DISABLE(4003, "该用户已被禁用，请联系管理人员！"),
    SESSION_TIMEOUT(4004, "未登录或会话超时，请重新登录！"),
    LOSE_LOGIN(4005, "已掉线，请重新登录！"),
    OTHER_LOGINED(4006, "会话失效，该账号已被其他人登录。请检查账号是否丢失或立即修改密码！");

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
