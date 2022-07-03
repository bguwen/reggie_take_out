package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.OrdersDto;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {

        orderService.submit(orders);

        return R.success("下单成功！");

    }

    /**
     * 管理端地址信息分页查询
     *
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<Page<OrdersDto>> page(int page, int pageSize, String number, String beginTime, String endTime) {

        Page<OrdersDto> ordersDtoPage = orderService.myPage(page, pageSize, number, beginTime, endTime);
        return R.success(ordersDtoPage);
    }

    /**
     * 用户分页查询地址信息
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page<OrdersDto>> userPage(int page, int pageSize) {

        Page<OrdersDto> ordersPage = orderService.userPage(page, pageSize);
        return R.success(ordersPage);
    }

    /**
     * 修改订单状态 0：待派送 1：派送中
     *
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> updateStatusById(@RequestBody Orders orders) {
        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(orders.getId() != null, Orders::getId, orders.getId());
        updateWrapper.set(orders.getStatus() != null, Orders::getStatus, orders.getStatus());
        boolean flag = orderService.update(updateWrapper);
        return flag ? R.success("成功！") : R.error("失败！");
    }
}
