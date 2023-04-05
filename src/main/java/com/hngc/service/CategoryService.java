package com.hngc.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hngc.entity.Category;

public interface CategoryService extends IService<Category> {

    void remove(Long id);
}

