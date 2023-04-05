package com.hngc.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hngc.common.R;
import com.hngc.config.RedisCache;
import com.hngc.entity.LoginUser;
import com.hngc.exception.MyBusinessException;
import com.hngc.utils.Code;
import com.hngc.utils.JwtUtil;
import com.hngc.utils.ServletUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //1获取token  header的token
        String token = request.getHeader("accessToken");
        if (!StringUtils.hasText(token)) {
            //放行，让后面的过滤器执行
            try {
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                log.error(e.toString());
                ServletUtils.renderString(response, JSON.toJSONString(R.error(Code.LOGIN_AUTHENTICATION_ERR, e.getMessage())));
            }
            return;
        }
        log.info("token为：\n" + token);
        //2解析token
        String userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (ExpiredJwtException e) {
            log.error(e.toString());
            ServletUtils.renderString(response, JSON.toJSONString(R.error(Code.EXPIRED_LOGIN, "登录已过期")));
            return;
        } catch (Exception e) {
            log.error(e.toString());
            ServletUtils.renderString(response, JSON.toJSONString(R.error(Code.TOKEN_ERR, "NOTLOGIN")));
            return;
        }

        //3获取userId, redis获取用户信息
        LoginUser loginUser = redisCache.getCacheObject("login:" + userId);
        if (Objects.isNull(loginUser)) {
            ServletUtils.renderString(response, JSON.toJSONString(R.error(Code.LOGIN_TIMEOUT, "登录已过期!")));
            return;
        }

        //4封装Authentication
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());

        //5存入SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        //放行，让后面的过滤器执行
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error(e.toString());
            ServletUtils.renderString(response, JSON.toJSONString(R.error(Code.SYSTEM_UNKNOWN_ERR, e.getMessage())));
        }
    }
}

