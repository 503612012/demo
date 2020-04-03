package com.oven.core.sysDic.vo;

import lombok.Data;

import javax.persistence.Column;

/**
 * 系统级字典实体类
 *
 * @author Oven
 */
@Data
public class SysDicVo {

    @Column(name = "dbid")
    private Integer id;
    @Column(name = "_key")
    private String key;
    @Column(name = "_value")
    private String value;
    @Column(name = "_desc")
    private String desc;
    @Column(name = "_profile")
    private String profile;

}
