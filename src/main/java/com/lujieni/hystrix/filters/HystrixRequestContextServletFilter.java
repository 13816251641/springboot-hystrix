package com.lujieni.hystrix.filters;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * @Auther ljn
 * @Date 2019/12/14
 */
@Component
public class HystrixRequestContextServletFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //初始化Hystrix请求上下文
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            //请求正常通过
            chain.doFilter(request, response);
        } finally {
            /*关闭Hystrix请求上下文,不管会不会有内存泄漏的问题呢?
            * 其他还好,因为每次都是new的
            * */
            context.shutdown();
        }
    }


}
