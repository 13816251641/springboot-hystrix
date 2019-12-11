package com.lujieni.hystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * @Auther ljn
 * @Date 2019/12/9
 * 利用注解接入hystrix,必须开启@EnableCircuitBreaker才能支持@HystrixCommand
 */
@Service
public class CommandWithAnno {
    /**
     * 同步的方式。
     * fallbackMethod定义降级
     */
    @HystrixCommand(fallbackMethod = "helloFallback")
    public String getUserId(String name) {
        int i = 1/0; //此处抛异常，测试服务降级
        return "你好:" + name;
    }

    public String helloFallback(String name) {
        return "error";
    }

    /*异步的执行*/
    @HystrixCommand(fallbackMethod = "testAsyncError")
    public Future<String> getUserName(Long id) {
        return new AsyncResult<String>() {
            @Override
            public String invoke() {
                int i = 1/0;//此处抛异常,测试服务降级
                return "小明:" + id;
            }
        };
    }

    public String testAsyncError(Long id) {
        return "fail";
    }


}
