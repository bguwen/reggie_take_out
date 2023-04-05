package com.hngc.dto;

import com.hngc.entity.Dish;
import com.hngc.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {
//菜品口味数据
    private List<DishFlavor> flavors = new ArrayList<>();
//菜品种类id
    private String categoryName;
//份数
    private Integer copies;
}
