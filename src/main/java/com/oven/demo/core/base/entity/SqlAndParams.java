package com.oven.demo.core.base.entity;

import lombok.Data;

/**
 * 自动化sql语句实体类
 *
 * @author Oven
 */
@Data
public class SqlAndParams {

    private String sql;
    private Object[] params;

    public static SqlAndParams build(String sql, Object[] params) {
        SqlAndParams result = new SqlAndParams();
        result.setSql(sql);
        result.setParams(params);
        return result;
    }

    public static SqlAndParams empty() {
        return SqlAndParams.build("", new Object[]{});
    }
    
}
