package com.oven.util;

import lombok.Data;

import java.util.List;

/**
 * layui分页获取返回封装
 *
 * @author Oven
 */
@Data
public class LayuiPager<T> {

    private Integer code;
    private String msg;
    private List<T> data;
    private Integer count;

}
