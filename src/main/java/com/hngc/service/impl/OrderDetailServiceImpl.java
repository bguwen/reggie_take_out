package com.hngc.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hngc.entity.OrderDetail;
import com.hngc.mapper.OrderDetailMapper;
import com.hngc.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
