package com.oven.vo;

import lombok.Data;

/**
 * 系统级字典实体类
 *
 * @author Oven
 */
@Data
public class SysDicVo {

    private Integer id;
    private String key;
    private String value;
    private String desc;
    private String profile;

}
