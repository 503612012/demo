package com.oven.core.menu.vo;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 菜单实体类
 *
 * @author Oven
 */
@Data
public class Menu implements Serializable, Comparable<Menu> {

    private static final long serialVersionUID = 1L;

    @Column(name = "dbid")
    private Integer id;
    @Column(name = "menu_code")
    private String menuCode;
    @Column(name = "menu_name")
    private String menuName;
    @Column(name = "pid")
    private Integer pid;
    @Column(name = "sort")
    private Integer sort;
    @Column(name = "url")
    private String url;
    @Column(name = "iconCls")
    private String iconCls;
    @Column(name = "type")
    private Integer type;
    @Column(name = "create_id")
    private Integer createId;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "last_modify_id")
    private Integer lastModifyId;
    @Column(name = "last_modify_time")
    private String lastModifyTime;
    @Column(name = "status")
    private Integer status; // 状态，0-正常、1-删除

    // 非数据库属性
    @Column(name = "title")
    private String title;

    @Override
    public int compareTo(Menu menu) {
        return this.getSort() - menu.getSort();
    }

}
