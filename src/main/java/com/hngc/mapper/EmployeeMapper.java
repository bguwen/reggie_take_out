package com.hngc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hngc.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    /**
     * 查询权限信息
     * @return
     */
    List<String> getPermission(Long id);

}
