package com.hngc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hngc.dto.DishDto;
import com.hngc.entity.Dish;

public interface DishService extends IService<Dish> {
    //    新增菜品同事插入口味数据，dish和dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //    根据id查询菜品信息和口味信息
    DishDto getByIdWithFlavor(Long id);

    //修改
    void updateWithFlavor(DishDto dishDto);
//
//    根据id删除菜品信息和口味信息
    void deleteByIdWithFlavor(Long[] ids);
}
