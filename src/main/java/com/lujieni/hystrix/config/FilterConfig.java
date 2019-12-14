package com.lujieni.hystrix.config;

import com.lujieni.hystrix.filters.HystrixRequestContextServletFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther ljn
 * @Date 2019/12/14
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean registerMyFilter(HystrixRequestContextServletFilter filter){
        FilterRegistrationBean<HystrixRequestContextServletFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.addUrlPatterns("/*");
        return  filterRegistrationBean;
    }

}
