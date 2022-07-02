package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetMealDishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
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

    @Override
    public SetmealDto getByIdWithDish(Long id) {
        //查询套餐基本信息，从setmeal表上查
        Setmeal setmeal = this.getById(id);
        //查询当前套餐对应的菜品信息，从setmeal_dish表上查
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id != null, SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishList = setMealDishService.list(queryWrapper);
        SetmealDto setmealDto = new SetmealDto();
//开始拷贝
        BeanUtils.copyProperties(setmeal, setmealDto);
//        设置对应的菜品信息
        setmealDto.setSetmealDishes(setmealDishList);
        return setmealDto;
    }

    /**
     * 修改套餐同时修改套餐和菜品关联关系
     *
     * @param setmealDto setmealDto
     * @return
     */
    @Transactional
    @Override
    public boolean updateWithDish(SetmealDto setmealDto) {
        setmealDto.setUpdateUser(BaseContext.getCurrentId());
        boolean flag1 = this.updateById(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes().stream().map(item->{
            item.setUpdateUser(BaseContext.getCurrentId());
            return item;
        }).collect(Collectors.toList());
        boolean flag2 = setMealDishService.updateBatchById(setmealDishes);
return flag1&&flag2;
    }
}
