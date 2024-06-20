package com.oven.demo.common.enumerate;

import com.oven.basic.common.util.IResultCode;

/**
 * 统一返回枚举类
 *
 * @author Oven
 */
public enum ResultCode implements IResultCode {

    SUCCESS(200, "操作成功！"),

    SECKILL_ERROR(3000, "秒杀接口异常！"),
    UNKNOW_ERROR(3001, "未知错误！"),
    OVER_LIMIT_ERROR(3002, "OVER LIMIT!"),
    SYSTEM_ERROR(3003, "系统错误！"),
    UPLOAD_ERROR(3004, "上传文件错误！"),
    GET_CAPTCHA_ERROR(3005, "获取验证码异常！"),
    FILE_NAME_EMPTY_ERROR(3006, "文件名称为空，请重新上传！"),
    NOT_YET_ACTIVATED_ERROR(3007, "暂未开通修改功能！"),
    DELETE_ROLE_ERROR(3008, "该角色被其他用户引用，禁止删除！"),
    LOGIN_ERROR(3009, "登录操作出错，请联系网站管理人员。"),
    GET_CURRENT_USER_ERROR(3010, "获取当前登录用户出错，请联系网站管理人员。"),

    NO_THIS_USER(4001, "用户名或密码错误！"),
    PASSWORD_WRONG(4002, "用户名或密码错误！"),
    USER_DISABLE(4003, "该用户已被锁定，请联系管理人员！"),
    OVER_WRONG_NUM(4004, "密码错误超过5次，请联系管理人员解锁！"),
    SESSION_TIMEOUT(4005, "未登录或会话超时，请重新登录！"),
    LOSE_LOGIN(4006, "已掉线，请重新登录！"),
    OTHER_LOGINED(4007, "会话失效，该账号已被其他人登录。请检查账号是否丢失或立即修改密码！"),
    CAPTCHA_ERROR(4008, "验证码错误，请重新输入！"),
    CAPTCHA_IS_NONE(4009, "请输入验证码！"),
    OLD_PASSWORD_WRONG(4010, "原始密码错误！"),
    USER_ALREADY_EXIST(4011, "该用户名已存在！"),
    FORCE_LOGOUT_ERROR(4012, "强制退出异常！"),
    FORCE_LOGOUT(4013, "您已被管理员强制退出！"),

    SEARCH_PAGE_ERROR(6000, "查询错误！"),
    SEARCH_ERROR(6001, "查询错误！"),
    INSERT_ERROR(6002, "添加错误！"),
    DELETE_ERROR(6003, "删除错误！"),
    UPDATE_ERROR(6004, "修改错误！"),
    ERROR_PAGE(6005, "去到错误页面！"),

    CAN_NOT_UPDATE_USER(7001, "该用户不可以修改"),
    CAN_NOT_DELETE_USER(7002, "该用户不可以删除"),
    CAN_NOT_SET_ROLE(7003, "该用户禁止设置角色"),
    CAN_NOT_SET_PWD(7004, "该用户禁止修改密码"),
    CAN_NOT_UPDATE_ROLE(7005, "该角色不可以修改"),
    CAN_NOT_DELETE_ROLE(7006, "该角色不可以删除"),
    CAN_NOT_SET_MENU(7007, "该角色禁止设置权限");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

}
