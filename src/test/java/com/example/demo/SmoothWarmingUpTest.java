package com.example.demo;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.RateLimiter;
import com.google.common.util.concurrent.Uninterruptibles;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class SmoothWarmingUpTest {

    @Test
    public void acquire1() {
        RateLimiter limiter = RateLimiter.create(5, 1000, TimeUnit.MILLISECONDS);
        for (int i = 1; i < 6; i++) {
            System.out.println(limiter.acquire());
        }

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 1; i < 6; i++) {
            System.out.println(limiter.acquire());
        }
    }
}
