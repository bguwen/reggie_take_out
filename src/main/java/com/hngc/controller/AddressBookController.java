package com.hngc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hngc.common.BaseContext;
import com.hngc.common.R;
import com.hngc.entity.AddressBook;
import com.hngc.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增
     *
     * @param addressBook addressBook
     * @return R
     */
    @PostMapping
    public R<AddressBook> addressBook(@RequestBody AddressBook addressBook) {
        Long currentId = BaseContext.getCurrentId();
        addressBook.setUserId(currentId);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * 设置默认地址
     *
     * @param addressBook addressBook
     * @return R
     */
    @PutMapping("/default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        if (BaseContext.getCurrentId() != null) {
            updateWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
            updateWrapper.set(AddressBook::getIsDefault, 0);
            addressBookService.update(updateWrapper);
            addressBook.setIsDefault(1);
            addressBookService.updateById(addressBook);
            return R.success(addressBook);
        }
        log.info("设置默认的id为：" + BaseContext.getCurrentId());
        return R.error("设置失败！");
    }

    /**
     * 查询默认地址
     *
     * @return R
     */
    @GetMapping("/default")
    public R<AddressBook> getDefault() {

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (addressBook != null) {
            return R.success(addressBook);
        }
        return R.error("该默认地址不存在！");
    }

    /**
     * 查询指定用户所有地址
     *
     * @param addressBook addressBook
     * @return R
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {

        addressBook.setUserId(BaseContext.getCurrentId());
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        if (addressBook.getUserId() != null) {
            queryWrapper.eq(addressBook.getUserId() != null, AddressBook::getUserId, addressBook.getUserId());
            queryWrapper.orderByDesc(AddressBook::getUpdateTime);
            List<AddressBook> addressBookList = addressBookService.list(queryWrapper);
            return R.success(addressBookList);
        }
        log.info("id为：" + addressBook.getUserId());
        return R.error("查询失败！");
    }

    /**
     * 扥局id查询
     *
     * @param id id
     * @return R
     */
    @GetMapping
    public R<AddressBook> getById(Long id) {

        if (id != null) {
            AddressBook addressBook = addressBookService.getById(id);
            return R.success(addressBook);
        }
        log.info("查询失败,id为：" + id);
        return R.error("查询失败！");

    }

    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook) {

        boolean flag = addressBookService.updateById(addressBook);
        return flag ? R.success("地址修改成功！") : R.error("地址修改失败！");

    }
}
