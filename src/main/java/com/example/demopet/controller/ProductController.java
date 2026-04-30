package com.example.demopet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demopet.entity.Product;
import com.example.demopet.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "10") int pageSize,
                       @RequestParam(required = false) String keyword,
                       Model model) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Product::getProductName, keyword)
                   .or()
                   .like(Product::getProductCode, keyword);
        }
        
        wrapper.orderByDesc(Product::getCreateTime);
        productService.page(page, wrapper);
        
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        return "product/index";
    }

    @GetMapping("/form")
    public String form(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            Product product = productService.getById(id);
            model.addAttribute("product", product);
        } else {
            model.addAttribute("product", new Product());
        }
        return "product/form";
    }

    @PostMapping("/save")
    public String save(Product product) {
        if (product.getId() == null) {
            product.setCreateTime(LocalDateTime.now());
        }
        product.setUpdateTime(LocalDateTime.now());
        productService.saveOrUpdate(product);
        return "redirect:/product";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.removeById(id);
        return "redirect:/product";
    }
}