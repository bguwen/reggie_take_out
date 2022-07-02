package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.OrdersDto;
import com.itheima.reggie.mapper.OrdersDtoMapper;
import com.itheima.reggie.service.OrdersDtoService;
import org.springframework.stereotype.Service;

@Service
public class OrdersDtoServiceImpl extends ServiceImpl<OrdersDtoMapper, OrdersDto> implements OrdersDtoService {
}
