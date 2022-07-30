package com.oven.demo.core.base.entity;

import lombok.Data;

/**
 * 自动化sql语句实体类
 *
 * @author Oven
 */
@Data
public class ConditionAndParams {

    private String condition;
    private Object[] params;

    public static ConditionAndParams build() {
        return ConditionAndParams.build("");
    }

    public static ConditionAndParams build(String condition) {
        return ConditionAndParams.build(condition, new Object[]{});
    }

    public static ConditionAndParams build(Object[] params) {
        return ConditionAndParams.build("", params);
    }

    public static ConditionAndParams build(String condition, Object[] params) {
        ConditionAndParams result = new ConditionAndParams();
        result.setCondition(condition);
        result.setParams(params);
        return result;
    }

}
