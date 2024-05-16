package com.oven.demo.core.system.entity;

import com.oven.basic.base.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 系统级字典实体类
 *
 * @author Oven
 */
@Data
@Table(name = "t_sys_dic")
@EqualsAndHashCode(callSuper = true)
public class SysDicEntity extends PageRequest {

    @Id
    @Column(name = "dbid")
    private Integer id;

    @Column(name = "_key")
    private String key;

    @Column(name = "_value")
    private String value;

    @Column(name = "_desc")
    private String desc; // 描述信息

    @Column(name = "_profile")
    private String profile;

    @Column(name = "_status")
    private Integer status; // 状态：0正常、1停用

}
