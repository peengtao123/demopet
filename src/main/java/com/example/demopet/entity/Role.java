package com.example.demopet.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("role")
public class Role {
    private Long id;
    private String roleName;
    private String roleCode;
    private String description;
}
