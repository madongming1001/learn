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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    }


    @Override
    public void destroy() {
        // 被销毁时调用一次

    }
}