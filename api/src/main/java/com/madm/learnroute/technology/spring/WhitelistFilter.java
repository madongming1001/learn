package com.madm.learnroute.technology.spring;

import javax.servlet.*;
import java.io.IOException;

/**
 * @Author MaDongMing
 * @Date 2022/3/30 7:01 PM
 */
public class WhitelistFilter implements javax.servlet.Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化后被调用一次
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 判断是否需要拦截
        filterChain.doFilter(servletRequest, servletResponse); // 请求通过要显示调用
    }

    @Override
    public void destroy() {
        // 被销毁时调用一次

    }
}
