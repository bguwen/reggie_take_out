package com.hngc.entity;


import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LoginUser implements UserDetails {

    private Employee employee;

    private List<String> permission;

    @JSONField(serialize = false)
    List<SimpleGrantedAuthority> authorities;

    public LoginUser() {
    }

    public LoginUser(Employee employee, List<String> permission) {
        this.employee = employee;
        this.permission = permission;
    }

    public LoginUser(Employee employee, List<String> permission, List<SimpleGrantedAuthority> authorities) {
        this.employee = employee;
        this.permission = permission;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities != null) {
            return authorities;
        }
        authorities = permission.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    /**
     * 获取
     * @return employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * 设置
     * @param employee
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * 获取
     * @return permission
     */
    public List<String> getPermission() {
        return permission;
    }

    /**
     * 设置
     * @param permission
     */
    public void setPermission(List<String> permission) {
        this.permission = permission;
    }

    /**
     * 设置
     * @param authorities
     */
    public void setAuthorities(List<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public String toString() {
        return "LoginUser{employee = " + employee + ", permission = " + permission + ", authorities = " + authorities + "}";
    }
}
