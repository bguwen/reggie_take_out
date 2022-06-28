package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;


public interface OrderService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders orders
     */
    void submit(Orders orders);
}
