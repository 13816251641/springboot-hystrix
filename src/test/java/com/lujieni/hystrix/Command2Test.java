package com.lujieni.hystrix;

import com.lujieni.hystrix.service.Command2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 测试基于信号量隔离的command
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Command2Test {

    /**
     * 测试同步执行,基于信号量隔离策略下的execute方法
     * 会阻塞当前线程,实际执行任务的线程也是当前线程。
     * 如果run方法出现了异常会立即调用回退方法并立即
     * 返回,如果run方法出现了超时回退方法由hystrix提供
     * 的线程来执行,当前线程仍旧会执行完超时方法,不过返回
     * 值是回退方法的返回值
     *
     */
    @Test
    public void testSynchronous() {
        /* blocking */
        String result = new Command2("world").execute();
        System.out.println("do other thing:"+result+":"+Thread.currentThread().getName());
    }

    /**
     * 测试异步执行,基于信号量隔离的策略模式下
     * future中的task是由main线程来执行的,所以
     * 这里是同步执行
     */
    @Test
    public void testAsynchronous() throws ExecutionException, InterruptedException {
        /* 得到一个future对象,一定blocking*/
        Future<String> future = new Command2("World").queue();
        System.out.println("do other thing");
        /* 前面blocking过了,这里不会blocking */
        System.out.println(future.get());
    }

}
