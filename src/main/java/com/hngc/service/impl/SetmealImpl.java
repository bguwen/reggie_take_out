package com.hngc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hngc.exception.CustomException;
import com.hngc.dto.SetmealDto;
import com.hngc.entity.Setmeal;
import com.hngc.entity.SetmealDish;
import com.hngc.mapper.SetmealMapper;
import com.hngc.service.SetMealDishService;
import com.hngc.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetMealDishService setMealDishService;

    /**
     * 新增套餐同时保存套餐和菜品关联关系
     *
     * @param setmealDto setmealDto
     */
    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {

        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setMealDishService.saveBatch(setmealDishes);
    }

    /**
     * 根据id批量删除餐和菜品
     *
     * @param ids ids
     */
    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
//        查询套餐状态确定是否删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);
        if (count > 0) {
            throw new CustomException("套餐正在售卖中，不能删除！");
        }
        this.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(SetmealDish::getDishId, ids);
        setMealDishService.remove(dishLambdaQueryWrapper);
    }
}
