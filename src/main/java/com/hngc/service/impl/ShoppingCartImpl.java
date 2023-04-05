package com.hngc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hngc.entity.ShoppingCart;
import com.hngc.mapper.ShoppingCartMapper;
import com.hngc.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
