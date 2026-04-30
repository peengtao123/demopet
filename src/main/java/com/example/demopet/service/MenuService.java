package com.example.demopet.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demopet.entity.Menu;
import com.example.demopet.mapper.MenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService extends ServiceImpl<MenuMapper, Menu> {
    
    @Transactional(readOnly = true)
    public List<Menu> listWithSort() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Menu::getSort);
        return this.list(wrapper);
    }
    
    @Transactional(readOnly = true)
    public List<Menu> search(String keyword) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.like(Menu::getMenuName, keyword)
                   .or()
                   .like(Menu::getMenuCode, keyword);
        }
        wrapper.orderByAsc(Menu::getSort);
        return this.list(wrapper);
    }
}
