package com.lujieni.hystrix.controller;

import com.lujieni.hystrix.service.Command;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @Auther ljn
 * @Date 2019/12/14
 */
@RestController
public class HystrixController {

    @GetMapping("/hystrix")
    public void hystrix(){
        List<String> list = Arrays.asList("1", "1", "2", "3");
        for(String s:list){
            Command command = new Command(s);
            command.execute();
            System.out.println(command.isResponseFromCache());
        }
    }


}
