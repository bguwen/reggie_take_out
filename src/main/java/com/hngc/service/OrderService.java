package com.hngc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hngc.entity.Orders;


public interface OrderService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders orders
     */
    void submit(Orders orders);
}
