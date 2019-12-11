package com.lujieni.hystrix.service;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * @Auther ljn
 * @Date 2019/12/9
 * 基于线程池的hystrix隔离
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
        /* hystrix线程池的线程运行下面的代码 */
        System.out.println("run():"+Thread.currentThread().getName());
        Thread.sleep(5000);
        int i = 5/0;
        return "hello "+name+"!";
    }

    /**
     * 降级。Hystrix会在run()执行过程中出现错误、超时、
     * 线程池拒绝、断路器熔断等情况时，执行getFallBack()
     * 方法内的逻辑
     */
    @Override
    protected String getFallback() {
        /* hystrix线程池的线程运行下面的代码 */
        System.out.println("getFallback():"+Thread.currentThread().getName());
        return "faild";
    }
}
