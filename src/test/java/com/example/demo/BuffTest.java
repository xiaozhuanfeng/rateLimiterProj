package com.example.demo;

import java.nio.IntBuffer;
import java.security.SecureRandom;

public class BuffTest {

    public static void main(String[] args) {
        /* capacity(容量)、limit(限制)和position(位置)
         * capacity >= 0  始终不变
         * 0 <= position <= limit <= capacity
         * 下面方法：
         * clear():position = 0,limit = capacity
         * flip(): limit = position,position=0
         * */
        IntBuffer buffer = IntBuffer.allocate(10);

        System.out.println("capacity:" + buffer.capacity());

        for (int i = 0; i < 5; ++i) {
            int randomNumber = new SecureRandom().nextInt(20);
            buffer.put(randomNumber);
        }

        System.out.println("before flip limit:" + buffer.limit());

        buffer.flip();

        System.out.println("after flip limit:" + buffer.limit());

        System.out.println("enter while loop");

        while (buffer.hasRemaining()) {
            System.out.println("position:" + buffer.position());
            System.out.println("limit:" + buffer.limit());
            System.out.println("capacity:" + buffer.capacity());
            System.out.println("元素:" + buffer.get());
        }
    }
}
