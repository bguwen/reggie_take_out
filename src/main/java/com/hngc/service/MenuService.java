package com.hngc.service;

import com.hngc.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @description 针对表【menu】的数据库操作Service
 * @createDate 2023-04-05 19:01:32
 */
public interface MenuService extends IService<Menu> {

    /**
     * 查询菜单路由
     *
     * @return
     */
    List<Menu> getMentList();
}
