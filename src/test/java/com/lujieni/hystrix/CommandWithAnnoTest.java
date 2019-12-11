package com.lujieni.hystrix;

import com.lujieni.hystrix.service.CommandWithAnno;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

/**
 * @Auther ljn
 * @Date 2019/12/9
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommandWithAnnoTest {

    @Autowired
    private CommandWithAnno commandWithAnno;

    /**
     * 测试同步
     */
    @Test
    public void testGetUserId() {
        System.out.println("=================" + commandWithAnno.getUserId("lisi"));
    }

    /**
     * 测试异步
     */
    @Test
    public void testGetUserName() throws ExecutionException, InterruptedException {
        System.out.println("=================" + commandWithAnno.getUserName(30L).get());
    }


}
