package com.lujieni.hystrix.service;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * @Auther ljn
 * @Date 2019/12/9
 */
public class Command extends HystrixCommand<String> {

    private final String name;

    public Command(String name){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("helloworld")).
                andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(6000)));
        this.name = name;
    }

    @Override
    public String run() throws Exception {
        System.out.println("run()");
        Thread.sleep(5000);
        return "hello "+name+"!";
    }

    /**
     * 降级。Hystrix会在run()执行过程中出现错误、超时、线程池拒绝、断路器熔断等情况时，
     * 执行getFallBack()方法内的逻辑
     */
    @Override
    protected String getFallback() {
        return "faild";
    }
}
