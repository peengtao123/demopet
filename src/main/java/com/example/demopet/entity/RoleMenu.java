package com.example.demopet.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("role_menu")
public class RoleMenu {
    private Long id;
    private Long roleId;
    private Long menuId;
    private LocalDateTime createTime;
}
