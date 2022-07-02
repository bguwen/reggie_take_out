package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.dto.OrdersDto;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.mapper.OrderMapper;
import com.itheima.reggie.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrdersDtoService ordersDtoService;

    /**
     * 用户下单
     *
     * @param orders orders
     */
    @Override
    @Transactional
    public void submit(Orders orders) {

//        获得当前用户id
        Long currentId = BaseContext.getCurrentId();
        User user = userService.getById(currentId);

//        获得购物车数据

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(currentId != null, ShoppingCart::getUserId, currentId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);
        if (shoppingCartList == null) {
            throw new CustomException("购物车为空，不能下单");
        }
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if (addressBook == null) {
            throw new CustomException("地址信息有误，无法下单！");
        }
        long orderId = IdWorker.getId();
        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail> list = shoppingCartList.stream().map(item -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setName(item.getName());
            orderDetail.setOrderId(orderId);
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setNumber(item.getNumber());
            orderDetail.setAmount(item.getAmount());
            orderDetail.setImage(item.getImage());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
//        向订单表出入数据
        orders.setId(orderId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserId(user.getId());
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserName(user.getName());
        orders.setPhone(user.getPhone());
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName()) +
                (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        orders.setConsignee(addressBook.getConsignee());
//        向订单明细表插入数据
        this.save(orders);
//        向订单明细表插入数据
        orderDetailService.saveBatch(list);
//        清空购物车
        shoppingCartService.remove(queryWrapper);
    }

    @Override
    public Page<OrdersDto> myPage(int page, int pageSize, String number, String beginTime, String endTime) {
        LocalDateTime beginDateTime = null;
        LocalDateTime endDateTime = null;
//        解析时间
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (beginTime != null && !"".equals(beginTime)) {
            beginDateTime = LocalDateTime.parse(beginTime, pattern);
        }
        if (endTime != null && !"".equals(endTime)) {
            endDateTime = LocalDateTime.parse(endTime, pattern);
        }
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(number != null, Orders::getNumber, number);
        queryWrapper.ge(beginDateTime != null, Orders::getOrderTime, beginDateTime);
        queryWrapper.le(endDateTime != null, Orders::getOrderTime, endDateTime);

        Page<Orders> ordersPage1 = this.page(ordersPage, queryWrapper);

        List<OrdersDto> ordersDtoList = ordersPage1.getRecords().stream().map(item -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            Long userId = item.getUserId();
            User user = userService.getById(userId);
            ordersDto.setUserName(user.getName());
            return ordersDto;
        }).collect(Collectors.toList());
        ordersDtoPage.setRecords(ordersDtoList);
        return ordersDtoPage;
    }

    @Transactional
    @Override
    public Page<OrdersDto> userPage(int page, int pageSize) {
        Long currentId = BaseContext.getCurrentId();
        if (currentId == null) {
            return null;
        }
        Page<Orders> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(currentId != null, Orders::getUserId, currentId);
        Page<Orders> ordersPage = this.page(pageInfo, queryWrapper);

        List<OrdersDto> collect = ordersPage.getRecords().stream().map(item -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(item.getId() != null, OrderDetail::getOrderId, item.getId());
            List<OrderDetail> list = orderDetailService.list(lambdaQueryWrapper);

            ordersDto.setOrderDetails(list);
            return ordersDto;
        }).collect(Collectors.toList());

        Page<OrdersDto> ordersDtoPage = new Page<>();
        ordersDtoPage.setRecords(collect);
        return ordersDtoPage;
    }
}
