package com.lujieni.hystrix.service;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * @Auther ljn
 * @Date 2019/12/9
 * 基于信号量的hystrix隔离
 *
 * 我发现run方法在基于信号量的隔离策略下不是由
 * hystrix提供的线程池里的线程来执行的而是由main
 * 线程来执行,回退方法在超时的情况下是由hystrix
 * 提供的线程来执行;一旦方法超时了回退方法可以马
 * 上执行但main线程仍旧卡住,直到执行完超时的run方法
 * 才有返回值,不过返回值是回退方法的返回值;在方法出现
 * 异常的情况下回退方法也是main线程来执行的且能够立
 * 马返回,run方法里的逻辑就不会在执行了!!!
 */
public class Command2 extends HystrixCommand<String> {

    private final String name;

    public Command2(String name){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Command2")).
                andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(500)
                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)));
        this.name = name;
    }

    @Override
    public String run() throws Exception {
        /* 基于信号量的策略下是tomcat线程执行下面的代码 线程名:main */
        System.out.println("run():"+Thread.currentThread().getName());
        /* 模拟超时 */
        Thread.sleep(10000);
        //int i = 5/0; //模拟异常
        System.out.println("耗时完毕:"+Thread.currentThread().getName());
        return "hello "+name+"!";
    }

    /**
     * 降级。Hystrix会在run()执行过程中出现错误、超时、
     * 线程池拒绝、断路器熔断等情况时，执行getFallBack()
     * 方法内的逻辑
     */
    @Override
    protected String getFallback() {
        /*
         * 如果run方法出现了异常,回退方法由main线程来执行
         * 如果run方法出现了超时回退方法由HystrixTimer-1线程
         * 来执行,由于是基于信号量来隔离的,那么main线程在超时
         * 的情况下仍旧会等待,不过返回的值的确是回退方法返回的值而已
         */
        System.out.println("getFallback():"+Thread.currentThread().getName());
        return "faild";
    }
}
