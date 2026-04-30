package com.example.demopet.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("menu")
public class Menu {
    private Long id;
    private String menuName;
    private String menuCode;
    private Long parentId;
    private String path;
    private String component;
    private Integer sort;
    private String icon;
    private Integer menuType; // 1-目录, 2-菜单, 3-按钮
    private String permission;
    private Integer status; // 0-禁用, 1-启用
}
