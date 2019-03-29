package com.example.demo;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
public class SmoothBurstyTest {
    @Test
    public void testAcquire1() {
        // acquire(i); 获取令牌，返回阻塞的时间,支持预消费.
        RateLimiter limiter = RateLimiter.create(1);

        for (int i = 1; i < 10; i++) {
            double waitTime = limiter.acquire();
            System.out.println("cutTime=" + longToDate(System.currentTimeMillis()) + " acq:" + i + " waitTime:" + waitTime);
        }
    }

    @Test
    public void test() {
        System.out.println(1.0D / 0.0);
    }

    @Test
    public void testAcquire2() {
        // 请求突发
        RateLimiter limiter = RateLimiter.create(5);

        for (int i = 1; i < 5; i++) {
            double waitTime = 0;
            if (i == 2) {
                waitTime = limiter.acquire(10);
            } else {
                waitTime = limiter.acquire(1);
            }

            System.out.println("cutTime=" + longToDate(System.currentTimeMillis()) + " acq:" + i + " waitTime:" + waitTime);
        }
    }

    public static String longToDate(long lo) {
        Date date = new Date(lo);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sd.format(date);
    }
}
