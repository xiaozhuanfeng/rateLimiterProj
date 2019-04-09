package com.example.demo.seria;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
public class SerializableTest {

    @Test
    public void test1(){
        Box box = new Box(10,20);
        String s = SerializableUtils.getClassName(box);
        System.out.println(s);
    }

    @Test
    public void test2(){
        //序列化
        Box box = new Box(10,20);
        SerializableUtils.doSerializable(box);
    }

    @Test
    public void test3(){
        //反序列化
        Box box = SerializableUtils.doDeserialization("seria/Box.out");
        System.out.println(box);
    }

    @Test
    public void test4(){
        //序列化 + transient
        Box box = new Box(10,20);
        box.setTexture("icon");  //铁
        SerializableUtils.doSerializable(box);

        Box box1 = new Box(1,2);
        box1 = SerializableUtils.doDeserialization("seria/Box.out");
        System.out.println(box1);//Box{color=red,width=10, height=20, texture='null'}
    }


    @Test
    public void test5(){
        //序列化 + transient
        Ball ball = new Ball();

        ball.setRadius(1.0);
        ball.setClour("blue");

        SerializableUtils.doSerializable(ball);
        Ball ball1 = SerializableUtils.doDeserialization("seria/Ball.out");
        System.out.println(ball1);
    }
}
