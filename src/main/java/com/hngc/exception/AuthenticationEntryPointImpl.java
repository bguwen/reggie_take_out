package com.hngc.exception;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hngc.common.R;
import com.hngc.entity.LoginUser;
import com.hngc.utils.Code;
import com.hngc.utils.HttpStatus;
import com.hngc.utils.ServletUtils;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * 认证失败处理类 返回未授权
 */
@Slf4j
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws ServletException, IOException {
        Object user = SecurityContextHolder.getContext().getAuthentication();
        if (user==null){
            ServletUtils.renderString(response, JSON.toJSONString(R.error(Code.ERR_LOGIN, "未登录")));
            return;
        }
        String msg = "请求访问：{" + request.getRequestURI() + "}，认证失败，无法访问系统资源";
        log.error("=====================================\n" + authException+"\n"+msg);
        ServletUtils.renderString(response, JSON.toJSONString(R.error(HttpStatus.UNAUTHORIZED, msg)));


//        log.error(authException.toString());
//        log.error(request.getRequestURI());
//        response.setContentType("application/json;charset=utf-8");
//        PrintWriter out = response.getWriter();
//        R respBean = R.error(Code.LOGIN_AUTHENTICATION_ERR, "认证失败");
//
//        if (authException instanceof LockedException) {
//            respBean.setMsg("账户被锁定，请联系管理员!");
//        } else if (authException instanceof CredentialsExpiredException) {
//            respBean.setMsg("密码过期，请联系管理员!");
//        } else if (authException instanceof AccountExpiredException) {
//            respBean.setMsg("账户过期，请联系管理员!");
//        } else if (authException instanceof DisabledException) {
//            respBean.setMsg("账户被禁用，请联系管理员!");
//        } else if (authException instanceof BadCredentialsException) {
//            respBean.setMsg("用户名或者密码输入错误，请重新输入!");
//        }
//        out.write(new ObjectMapper().writeValueAsString(respBean));
//        out.flush();
//        out.close();

    }
}

