
package com.ziyuan.perspective.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * PerspectiveInitFilter
 *
 * @author ziyuan
 * @since 2017-03-06
 */
public class PerspectiveInitFilter implements Filter {

    /**
     * 这里初始化一些东西
     *
     * @param filterConfig
     * @throws ServletException
     */
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    public void destroy() {
    }
}
