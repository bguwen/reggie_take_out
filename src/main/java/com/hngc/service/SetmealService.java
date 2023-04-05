package com.hngc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hngc.dto.SetmealDto;
import com.hngc.entity.Setmeal;

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
}
