package com.hngc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hngc.entity.Employee;
import com.hngc.entity.LoginUser;
import com.hngc.exception.MyBusinessException;
import com.hngc.mapper.EmployeeMapper;
import com.hngc.utils.Code;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 用户验证处理
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, username);
        Employee employee = employeeMapper.selectOne(wrapper);
        //如果查询不到数据就通过抛出异常来给出提示
        if (null == employee) {
            throw new MyBusinessException(Code.USERNAME_ERR, "用户名错误");
        }
//        else if (Code.DELETED_EMPLOYEE.equals(employee.getIsDelete())) {
//            log.info("登录用户：{} 已被删除.", username);
//            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
//        }
        else if (Code.ERR_LOGIN.equals(employee.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new MyBusinessException(Code.USERNAME_ERR, "对不起，您的账号：" + username + " 已停用");
        }
        //查询权限信息
        List<String> permission = employeeMapper.getPermission(employee.getId());
        //封装成UserDetails对象返回 
        return new LoginUser(employee, permission);
    }
}
