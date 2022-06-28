package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，同时保存对应的口味数据
     *
     * @param dishDto dishDto
     */
    @Override
//    加入事务控制
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {

//        保存菜品基本信息到菜品表
        this.save(dishDto);
//        菜品id
        Long dishDtoId = dishDto.getId();

//        菜品口味
        List<DishFlavor> dishDtoFlavors = dishDto.getFlavors();
        dishDtoFlavors = dishDtoFlavors.stream().map(item -> {
            item.setDishId(dishDtoId);
            return item;
        }).collect(Collectors.toList());
//          保存菜品口味数据到菜品表
        dishFlavorService.saveBatch(dishDtoFlavors);

    }

    /**
     * 根据id查询菜品信息和口味信息
     *
     * @param id id
     * @return R
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
//        查询菜品基本信息，从dish表
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
//        查询菜品口味信息，从dish_flavor表
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(flavors);

        return dishDto;
    }

    /**
     * 修改菜品信息和口味信息
     *
     * @param dishDto dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
//        更新dish表基本信息
        this.updateById(dishDto);
//        清除口味数据，--dish_flavor

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
//        添加提交过来的口味数据
        //        菜品口味
        List<DishFlavor> dishDtoFlavors = dishDto.getFlavors();
        dishDtoFlavors = dishDtoFlavors.stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
//          保存菜品口味数据到菜品表
        dishFlavorService.saveBatch(dishDtoFlavors);


    }

    /**
     * 根据id查询菜品信息和口味信息
     *
     * @param ids ids
     */
    @Override
    @Transactional
    public void deleteByIdWithFlavor(Long[] ids) {

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        for (Long id : ids) {
            //        删除dish表基本信息
            dishLambdaQueryWrapper.eq(id != null, Dish::getId, id);
            Dish dish = this.getOne(dishLambdaQueryWrapper);
            dish.setIsDeleted(1);
            this.updateById(dish);
//            this.remove(dishLambdaQueryWrapper);
            //        清除口味数据，--dish_flavor

            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(id != null, DishFlavor::getDishId, id);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            for (DishFlavor dishFlavor : dishFlavorList) {
                dishFlavor.setIsDeleted(1);
                dishFlavorService.updateById(dishFlavor);
            }

//            dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
        }

    }
}
