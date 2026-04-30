package com.example.demopet.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("menu")
public class Menu {
    private Long id;
    private String menuName;
    private String menuCode;
    private String parentId;
    private String path;
    private Integer sort;
    private String icon;
}
