package com.example.demo.effective.enumDemo;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

@SpringBootTest
public class EnumTest {

    // num << x,相当于num * 2^x,
    //|或运算符：即两个二进制数同位中，只要有一个为1则结果为1，若两个都为1其结果也为1
    public static final int STYLE_BOLD = 1 << 0;
    public static final int STYLE_ITALIC = 1 << 1;
    public static final int STYLE_UNDERLINE = 1 << 2;
    public static final int STYLE_STRIKETHROUGH = 1 << 3;

    @Test
    public void test1(){
        StatusEnum status = StatusEnum.ACTIVE;
        System.out.println(status);
    }

    @Test
    public void test2(){
        StatusEnum.ACTIVE.method();
    }


    @Test
    public void test3(){
        System.out.println(STYLE_UNDERLINE|STYLE_STRIKETHROUGH);//10
    }

    @Test
    public void test4(){
        //用EnumSet代替位域
        //位域表示法允许利用位操作，有效地执行先 union（联合）和 intersection（交集）这样的集合操作 如：test3()
        EnumSet<StatusEnum> status1 = EnumSet.of(StatusEnum.APPLY, StatusEnum.ACTIVE);
        System.out.println(status1);

        //确定，EnumSet可变，用unmodifiableSet 封装
        Set<StatusEnum> unmodifiableStyle = Collections.unmodifiableSet(status1);
        unmodifiableStyle.add(StatusEnum.FREE); //运行时：UnsupportedOperationException
    }

}
