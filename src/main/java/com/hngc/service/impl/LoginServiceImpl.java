package com.hngc.service.impl;

import com.hngc.common.R;
import com.hngc.config.RedisCache;
import com.hngc.entity.Employee;
import com.hngc.entity.LoginUser;
import com.hngc.exception.MyBusinessException;
import com.hngc.service.LoginService;
import com.hngc.utils.Code;
import com.hngc.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RedisCache redisCache;
    @Override
    public R<Map<String,Object>> login(Employee user) {
        //3使用ProviderManager auth方法进行验证
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());

        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        //校验失败了
        if(Objects.isNull(authenticate)){
            throw new MyBusinessException(Code.USERNAME_OR_PASSWORD_ERR,"用户名或密码错误！");
        }
        LoginUser loginUser= (LoginUser)(authenticate.getPrincipal());
        ////          状态比对
        if (loginUser.getEmployee().getStatus() == 0) {
            return R.error("该账号已被禁用！");
        }
        //4自己生成jwt给前端

        String userId = loginUser.getEmployee().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        Map<String,Object> map=new HashMap<>();
        map.put("token",jwt);
        map.put("userInfo",loginUser.getEmployee());
        //5系统用户相关所有信息放入redis
        redisCache.setCacheObject("login:"+userId,loginUser,60*60, TimeUnit.SECONDS);

        return R.success(map,"登陆成功");
    }

    @Override
    public R<String> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getEmployee().getId();
        redisCache.deleteObject("login:"+userId);

        return  R.success("退出成功！");
    }
}

