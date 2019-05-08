package com.example.demo.biz.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeanNameAwareTest {

    @Resource
    private BizService bizService;

    @Test
    public void test1(){
        System.out.println(bizService.getBeanName());
    }

    @Test
    public void test2(){
        System.out.println(bizService.getFactory().getBean(bizService.getBeanName()));
    }
}
