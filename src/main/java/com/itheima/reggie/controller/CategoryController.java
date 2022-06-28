package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     *
     * @param category category
     * @return R
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("新增菜品{}", category);
        categoryService.save(category);
        return R.success("新增分类成功！");
    }

    /**
     * 分页查询
     *
     * @param page     page
     * @param pageSize pageSize
     * @return R
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {

//        分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
//        条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
//        添加排序条件
        queryWrapper.orderByAsc(Category::getSort);
//        执行查询
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id删除分类
     *
     * @param id id
     * @return R
     */
    @DeleteMapping
    public R<String> delete(Long id) {
//        log.info("根据id分类id{}",id);
//      删除分类
        categoryService.remove(id);
        return R.success("分类信息删除成功！");
    }

    /**
     * 根据id修改分类信息
     *
     * @param category category
     * @return R
     */
    @PutMapping
    public R<String> updateById(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("修改分类信息成功！");
    }

    /**
     * 根据条件查询分类字段
     *
     * @param category category
     * @return R
     */
    @GetMapping("/list")
    public R<List<Category>> List(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
