package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        Long currentId = BaseContext.getCurrentId();
        if (currentId == null) {
            return R.error("currentId为空,操作失败！");
        }
        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCart.setUserId(currentId);
        Long dishId = shoppingCart.getDishId();
//        查询当前菜品火套是否在购物车中
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
//          判断添加的是菜品还是套餐
        if (dishId != null) {
//              是菜品

            queryWrapper.eq(ShoppingCart::getDishId, dishId);

        } else {
//              是套餐
            queryWrapper.eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCartOne = shoppingCartService.getOne(queryWrapper);
//        if (shoppingCartOne!=null&& Objects.equals(shoppingCartOne.getDishFlavor(), shoppingCart.getDishFlavor())){
        if (shoppingCartOne != null) {
            shoppingCartOne.setNumber(shoppingCartOne.getNumber() + 1);
            shoppingCartService.updateById(shoppingCartOne);
        } else {
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            shoppingCartOne = shoppingCart;
        }
        return R.success(shoppingCartOne);
    }

    /**
     * 购物车展示
     *
     * @return R
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        if (BaseContext.getCurrentId() == null) {
            return R.error("UserId为空！");
        }
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(BaseContext.getCurrentId() != null, ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);
        return R.success(shoppingCartList);
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        if (BaseContext.getCurrentId() == null) {
            return R.error("UserId为空!");
        }
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());

        queryWrapper.eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId());
        queryWrapper.eq(BaseContext.getCurrentId() != null, ShoppingCart::getUserId, BaseContext.getCurrentId());

        ShoppingCart shoppingCartOne = shoppingCartService.getOne(queryWrapper);
        shoppingCartOne.setNumber(shoppingCartOne.getNumber() - 1);
        boolean flag = shoppingCartService.updateById(shoppingCartOne);

        return flag? R.success(shoppingCartOne):R.error("修改失败！");
    }

    @DeleteMapping("/clean")
    public R<String> clean() {

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        boolean flag = shoppingCartService.remove(queryWrapper);
        return flag ? R.success("清空成功！") : R.error("清空失败！");
    }
}
