package com.hngc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hngc.entity.AddressBook;
import com.hngc.mapper.AddressBookMapper;
import com.hngc.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
