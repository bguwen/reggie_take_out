package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

//        获取请求的URI
        String requestURI = request.getRequestURI();
//            log.info("拦截到请求URI:"+request.getRequestURI());

//        定义不需要处理的路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/page/**",
                "/front/**",
                "/user/sendMsg",    //移动端发送短信
                "/user/login"   //移动端登录
        };

//        判断本次请求是否需要处理
        boolean check = check(urls, requestURI);
//        不需要处理直接放行
        if (check) {
            filterChain.doFilter(request, response);
//            log.info("本次请求不需要处理！{}",requestURI);
            return;
        }
//        判断PC端是否登录
        if (request.getSession().getAttribute("employee") != null) {
            filterChain.doFilter(request, response);
            Long id=(Long)request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(id);
//            log.info("用户已登录！id为{}",request.getSession().getAttribute("employee"));
            return;
        }
//        判断移动端是否登录
        if (request.getSession().getAttribute("user") != null) {
            filterChain.doFilter(request, response);
            Long id=(Long)request.getSession().getAttribute("user");
            BaseContext.setCurrentId(id);
//            log.info("用户已登录！id为{}",request.getSession().getAttribute("employee"));
            return;
        }

//        返回未登录结果，通过输出流方式
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
//        log.info("用户未登录！");
    }

    /**
     * 判断请求是否需要处理
     *
     * @param urls       urls
     * @param requestURI requestURI
     * @return boolean
     */
    public static boolean check(String[] urls, String requestURI) {

        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }

}
