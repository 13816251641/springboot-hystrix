package com.lujieni.hystrix.service;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * @Auther ljn
 * @Date 2019/12/9
 * 基于线程池的hystrix隔离策略
 */
public class Command extends HystrixCommand<String> {

    private final String name;

    public Command(String name){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Command")).
                andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(500)));
        this.name = name;
    }

    @Override
    public String run() throws Exception {
        /* hystrix提供的线程池中的线程运行下面的代码  线程名:hystrix-Command-1 */
        System.out.println("run():"+Thread.currentThread().getName());
        /* 超时立马执行回退方法并立即返回 */
        Thread.sleep(15000);
        //int i = 5/0; 模拟异常
        return "hello "+name+"!";
    }

    /**
     * 降级。Hystrix会在run()执行过程中出现错误、超时、
     * 线程池拒绝、断路器熔断等情况时，执行getFallBack()
     * 方法内的逻辑
     */
    @Override
    protected String getFallback() {
        /* hystrix提供的线程执行下面的代码 线程名:HystrixTimer-1 */
        System.out.println("getFallback():"+Thread.currentThread().getName());
        return "faild";
    }
}
