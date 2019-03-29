package com.example.demo;

import com.google.common.base.Stopwatch;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
@SpringBootTest
public class StopwatchTest {

    @Test
    public void stopwatch1() {
        Stopwatch stopwatch = Stopwatch.createStarted();

        doSomething();
        stopwatch.stop(); // optional
        long millis = stopwatch.elapsed(MILLISECONDS);
        System.out.println("time: " + stopwatch);
    }

    @Test
    public void stopwatch2() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        //doSomething();
        stopwatch.stop();
        long millis = stopwatch.elapsed(MILLISECONDS);
        System.out.println("time: " + stopwatch);

        stopwatch.reset().start();
        //doSomething();
        stopwatch.stop();
        millis = stopwatch.elapsed(MILLISECONDS);
        System.out.println("time: " + stopwatch);
    }

    @Test
    public void stopwatch3() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        doSomething();
        stopwatch.stop(); // optional
        long millis = stopwatch.elapsed(TimeUnit.MICROSECONDS);
        System.out.println("time: " + stopwatch); //time: 100.2 ms
    }

    public static void doSomething(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
