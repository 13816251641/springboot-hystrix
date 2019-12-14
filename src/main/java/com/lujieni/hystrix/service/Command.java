package com.lujieni.hystrix.service;

import com.netflix.hystrix.*;

/**
 * @Auther ljn
 * @Date 2019/12/9
 * 基于线程池的hystrix隔离策略
 */
public class Command extends HystrixCommand<String> {

    private final String name;

    public Command(String name){
        /*  同一个command group key共用一个线程池 */
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CommandGroupL"))
                     .andCommandKey(HystrixCommandKey.Factory.asKey("Command"))
                     .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                                                       .withCoreSize(20) // 线程池线程个数
                                                       .withQueueSizeRejectionThreshold(10))                 // 线程池等待队列个数
                     .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(500)));//超时时间
        this.name = name;
    }

    @Override
    public String run() throws Exception {
        /* hystrix提供的线程池中的线程运行下面的代码  线程名:hystrix-Command-1 */
        System.out.println("run():"+Thread.currentThread().getName());
        /* 超时立马执行回退方法并立即返回 */
        /*
          Thread.sleep(15000);
          System.out.println("耗时完毕:"+Thread.currentThread().getName());
         */
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

    /**
     * 开启请求缓存，只需重载getCacheKey方法
     * 因为我们这里使用的是id，不同的请求来请求的时候会有不同cacheKey所以，同一请求第一次访问会调用，之后都会走缓存
     * 好处：    1.减少请求数、降低并发
     *           2.同一用户上下文数据一致
     *           3.这个方法会在run()和contruct()方法之前执行，减少线程开支
     */
    @Override
    protected String getCacheKey() {
        return name;
    }
}
