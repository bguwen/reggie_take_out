package com.hngc.exception;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hngc.common.R;
import com.hngc.utils.Code;
import com.hngc.utils.HttpStatus;
import com.hngc.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 授权失败
 */
@Slf4j
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws ServletException, IOException {
//            request.setAttribute("exception", accessDeniedException);
//            request.getRequestDispatcher("/error").forward(request, response);
        log.error(accessDeniedException.toString());

        String msg = "请求访问：{" + request.getRequestURI() + "}，认证失败，权限不足";
        log.error("=====================================\n" + accessDeniedException+"\n"+msg);
        ServletUtils.renderString(response, JSON.toJSONString(R.error(Code.LOGIN_AUTHENTICATION_ERR, msg)));


//
//        response.setContentType("application/json;charset=utf-8");
//        PrintWriter out = response.getWriter();
//        R respBean = R.error(Code.LOGIN_AUTHENTICATION_ERR,"权限不足");
//
//        out.write(new ObjectMapper().writeValueAsString(respBean));
//        out.flush();
//        out.close();
    }
}

