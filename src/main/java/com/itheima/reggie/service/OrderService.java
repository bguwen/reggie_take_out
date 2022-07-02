package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.OrdersDto;
import com.itheima.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;


public interface OrderService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders orders
     */
    void submit(Orders orders);

    /**
     * 管理端订单分页查询
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    Page<OrdersDto> myPage(int page, int pageSize, String number, String beginTime, String endTime);
}
