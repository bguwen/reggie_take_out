package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.OrdersDto;
import com.itheima.reggie.entity.Orders;
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
public R<String> submit(@RequestBody Orders orders){

        orderService.submit(orders);

        return R.success("下单成功！");

}
@GetMapping("/page")
public R<Page<OrdersDto>> page(int page, int pageSize, String number, String beginTime, String endTime){

    Page<OrdersDto> ordersDtoPage=orderService.myPage(page,  pageSize,  number,  beginTime,  endTime);
        return R.success(ordersDtoPage);
}
}
