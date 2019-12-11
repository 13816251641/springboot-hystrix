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
     * 测试同步执行,execute方法会阻塞当前线程,但实际执行
     * 的任务线程不是当前线程而是别的
     */
    @Test
    public void testSynchronous() {
        /* blocking */
        String result = new Command2("world").execute();
        System.out.println("do other thing:"+result+":"+Thread.currentThread().getName());
    }

    /**
     * 测试异步执行,基于信号量隔离的策略模式下
     * future中的task是由main线程来执行的
     */
    @Test
    public void testAsynchronous() throws ExecutionException, InterruptedException {
        /* 得到一个future对象,blocking*/
        Future<String> future = new Command2("World").queue();
        System.out.println("do other thing");
        /* 前面blocking过了,这里不会blocking */
        System.out.println(future.get());
    }

}
