package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetMealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetMealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetMealDishService setMealDishService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐
     *
     * @param setmealDto setmealDto
     * @return R
     */
    @CacheEvict(value = "setmealCache", allEntries = true) //删除缓存数据
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功！");
    }

    /**
     * 分页查询
     *
     * @param page     page
     * @param pageSize pageSize
     * @param name     name
     * @return R
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(int page, int pageSize, String name) {

        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPageInfo = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByAsc(Setmeal::getStatus).orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, queryWrapper);
        BeanUtils.copyProperties(pageInfo, dtoPageInfo, "records");

        List<Setmeal> recordsList = pageInfo.getRecords();
        List<SetmealDto> records = recordsList.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPageInfo.setRecords(records);
        return R.success(dtoPageInfo);

    }

    /**
     * 根据id删除套餐
     *
     * @param ids ids
     * @return R
     */
    @CacheEvict(value = "setmealCache", allEntries = true) //删除缓存数据
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        setmealService.removeWithDish(ids);
        return R.success("删除成功！");
    }

    /**
     * 根据条件查询套餐数据
     *
     * @param setmeal setmeal
     * @return R
     */
    @Cacheable(value = "setmealCache", key = "#setmeal.getCategoryId+'_'+#setmeal.getStatus") //缓存注解
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(Setmeal::getStatus, 1);
        List<Setmeal> setmealList = setmealService.list(queryWrapper);
        log.info("套餐数据：" + setmealList);
        return setmealList.size() != 0 ? R.success(setmealList) : R.error("暂无数据");
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id) {
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        return R.success(setmealDto);
    }

    /**
     * 修改套餐同时修改套餐和菜品关联关系
     * @param setmealDto
     * @return
     */
    @CacheEvict(value = "setmealCache", allEntries = true) //删除缓存数据
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        boolean flag = setmealService.updateWithDish(setmealDto);
        return flag?R.success("修改成功！"):R.error("修改失败！");
    }

    @CacheEvict(value = "setmealCache", allEntries = true) //删除缓存数据
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable  int status ,Long[] ids){

        if (ids==null||ids.length==0){
            return R.error("参数有误！");
        }
        LambdaUpdateWrapper<Setmeal> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.set(Setmeal::getStatus,status).in(Setmeal::getId, (Object[]) ids);
        boolean flag = setmealService.update(updateWrapper);
        return flag?R.success("状态修改成功！"):R.error("状态修改失败！");
    }
}
