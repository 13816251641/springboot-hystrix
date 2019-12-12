package com.lujieni.hystrix;

import com.lujieni.hystrix.service.Command;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
/**
   测试基于线程池隔离的command
 */
public class CommandTest {

    /**
     * 测试同步执行,基于线程池隔离策略下的execute方法
     * 会阻塞当前线程,但实际执行任务的线程不是当前线程
     * 而是hystrix提供的线程池中的线程,并且超时或者异常
     * 会立即调用回退方法并立即返回
     */
    @Test
    public void testSynchronous() throws Exception {
        /* blocking */
        String result = new Command("world").execute();
        System.out.println("do other thing:"+result+":"+Thread.currentThread().getName());
        Thread.sleep(20000);
    }

    /**
     * 测试异步执行,不阻塞当前线程
     */
    @Test
    public void testAsynchronous() throws ExecutionException, InterruptedException {
        /*得到一个future对象,command中的run方法是由hystrix提供的线程池中的线程来执行的*/
        Future<String> future = new Command("World").queue();
        System.out.println("do other thing");
        /*
           由于future的任务执行是由别的线程来执行的,如果
           代码运行到这里future的任务还没有完成的话get方
           法将阻塞直到任务完成。如果future的任务已经完成
           get直接返回结果。
         */
        /*  可能blocking */
        System.out.println(future.get());
    }

}
