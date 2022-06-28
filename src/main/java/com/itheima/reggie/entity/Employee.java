package com.itheima.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工实体
 */
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)    //插入时填充字段
    private LocalDateTime createTime;   //创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)    //插入和更新时填充字段
    private LocalDateTime updateTime;   //修改时间

    @TableField(fill = FieldFill.INSERT)    //插入时填充字段
    private Long createUser;//创建者id

    @TableField(fill = FieldFill.INSERT_UPDATE)    //修改时填充字段
    private Long updateUser;//修改者id

}
