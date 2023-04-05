package com.hngc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hngc.entity.Menu;
import com.hngc.service.MenuService;
import com.hngc.mapper.MenuMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 郭
 * @description 针对表【menu】的数据库操作Service实现
 * @createDate 2023-04-05 19:01:32
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
        implements MenuService {
    @Override
    public List<Menu> getMentList() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Menu::getId, Menu::getName, Menu::getUrl, Menu::getIcon);
        return baseMapper.selectList(queryWrapper);
    }
}




