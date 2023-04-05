package com.hngc.utils;

import org.springframework.beans.BeanMetadataAttribute;

public interface Code {
    //未知错误
    Integer SYSTEM_UNKNOWN_ERR = 0;
//    Integer SYSTEM_UNKNOWN_ERR = 50001;
    //token非法
    Integer TOKEN_ERR = 0;
//    Integer TOKEN_ERR = 60001;
    //权限不足
    Integer PERMISSION_ERR = 0;
//    Integer PERMISSION_ERR = 60001;
    //登录已过期
    Integer LOGIN_TIMEOUT = 0;
//    Integer LOGIN_TIMEOUT = 60002;
    //登录认证失败
    Integer LOGIN_AUTHENTICATION_ERR = 0;
//    Integer LOGIN_AUTHENTICATION_ERR = 60003;
    //用户名或密码错误
    Integer USERNAME_OR_PASSWORD_ERR = 0;
//    Integer USERNAME_OR_PASSWORD_ERR = 60004;
    //用户名错误
    Integer USERNAME_ERR = 0;
    Long DELETED_EMPLOYEE = 0L;
    Integer STATUS_OK = 1;
    Integer ERR_STATUS = 0;
    //登录已过期
    Integer EXPIRED_LOGIN = 30002;
    Integer ERR_LOGIN = 30001;
//    Integer USERNAME_ERR = 60005;
}
