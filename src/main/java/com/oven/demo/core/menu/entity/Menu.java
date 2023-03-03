package com.oven.demo.core.menu.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 菜单实体类
 *
 * @author Oven
 */
@Data
@Table(name = "t_menu")
public class Menu implements Serializable, Comparable<Menu> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "dbid")
    private Integer id;

    @Column(name = "menu_code")
    private String menuCode; // 菜单编码

    @Column(name = "menu_name")
    private String menuName; // 菜单名称

    @Column(name = "pid")
    private Integer pid; // 父id

    @Column(name = "sort")
    private Integer sort; // 排序值

    @Column(name = "url")
    private String url; // 链接

    @Column(name = "iconCls")
    private String iconCls; // 图标

    @Column(name = "type")
    private Integer type; // 类型：1-目录、2-按钮

    @Column(name = "create_id", updatable = false)
    private Integer createId; // 创建人id

    @Column(name = "create_time", updatable = false)
    private String createTime; // 创建时间

    @Column(name = "last_modify_id")
    private Integer lastModifyId; // 最后修改人id

    @Column(name = "last_modify_time")
    private String lastModifyTime; // 最后修改时间

    @Column(name = "status")
    private Integer status; // 状态：0-正常、1-删除

    // 非数据库属性
    @Column(name = "title", insertable = false, updatable = false)
    private String title;

    @Override
    public int compareTo(Menu menu) {
        return this.getSort() - menu.getSort();
    }

}
