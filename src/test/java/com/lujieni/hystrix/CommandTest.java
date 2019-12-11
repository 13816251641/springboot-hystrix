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
public class CommandTest {

    /**
     * 测试同步执行,execute会阻塞当前线程
     */
    @Test
    public void testSynchronous() {
        /* blocking */
       System.out.println(new Command("world").execute());
       System.out.println("do other thing");

    }

    /**
     * 测试异步执行 非阻塞
     */
    @Test
    public void testAsynchronous() throws ExecutionException, InterruptedException {
        /* 得到一个future对象,command中的run方法是由别的线程来执行的*/
        Future<String> future = new Command("World").queue();
        System.out.println("do other thing");
        /*
           由于future的任务执行是由别的线程来执行的,如果
           到这里future的任务还没有完成的话get方法将阻塞
           直到任务完成。如果future的任务已经完成get直接
           返回结果。
         */
        /*  可能blocking */
        System.out.println(future.get());
    }

}
