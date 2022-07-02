package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐同时保存套餐和菜品关联关系
     * @param setmealDto setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 根据id批量删除餐和菜品
     * @param ids ids
     */
    void removeWithDish(List<Long> ids);

    /**
     * 根据id查询套餐以及菜品信息
     * @param  id id
     * @return R
     */
    SetmealDto getByIdWithDish(Long id);
}
