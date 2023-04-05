package com.hngc.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hngc.exception.CustomException;
import com.hngc.entity.Category;
import com.hngc.entity.Dish;
import com.hngc.entity.Setmeal;
import com.hngc.mapper.CategoryMapper;
import com.hngc.service.CategoryService;
import com.hngc.service.DishService;
import com.hngc.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前进行判断
     *
     * @param id id
     */
    @Override
    public void remove(Long id) {
//        查询当前分类是否已经关联菜品，如果关联则抛出业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getId, id);
        int count = dishService.count(dishLambdaQueryWrapper);
        if (count > 0) {
//            已经关联菜品，抛出业务异常
            throw new CustomException("当前分类下已经关联了菜品，不能删除！");
        }
//        查询当前分类是否已经关联套餐，如果关联则抛出业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getId, id);
        int count1 = setmealService.count(setmealLambdaQueryWrapper);
        if (count1 > 0) {
//            已经关联套餐，抛出业务异常
            throw new CustomException("当前分类下已经关联了套餐，不能删除！");
        }
//        正常删除
        super.removeById(id);

    }
}