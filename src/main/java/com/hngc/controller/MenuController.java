package com.hngc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hngc.common.R;
import com.hngc.entity.Menu;
import com.hngc.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("getMenuList")
    public R<List<Menu>> getMenuList() {
        return R.success(menuService.getMentList());
    }
}
