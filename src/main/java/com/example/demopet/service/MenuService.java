package com.example.demopet.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demopet.entity.Menu;
import com.example.demopet.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    
    @Autowired
    private MenuMapper menuMapper;
    
    @Transactional(readOnly = true)
    public List<Menu> list() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Menu::getSort);
        return menuMapper.selectList(wrapper);
    }
    
    @Transactional(readOnly = true)
    public Menu getById(Long id) {
        return menuMapper.selectById(id);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void save(Menu menu) {
        menuMapper.insert(menu);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void update(Menu menu) {
        menuMapper.updateById(menu);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        menuMapper.deleteById(id);
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
        return menuMapper.selectList(wrapper);
    }
}
