package com.skyer.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 菜单实体类
 *
 * @author SKYER
 */
@Data
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String menuCode;
    private String menuName;
    private Integer pid;
    private Integer sort;
    private String url;
    private String iconCls;
    private Integer type;
    private Integer createId;
    private String createTime;
    private Integer lastModifyId;
    private String lastModifyTime;
    private Integer status; // 状态，0-正常、1-删除

    // 非数据库属性
    private String title;

}
