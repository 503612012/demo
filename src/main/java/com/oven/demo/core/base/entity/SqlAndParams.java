package com.oven.demo.core.base.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自动化sql语句实体类
 *
 * @author Oven
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SqlAndParams {

    private String sql;
    private Object[] params;

}
