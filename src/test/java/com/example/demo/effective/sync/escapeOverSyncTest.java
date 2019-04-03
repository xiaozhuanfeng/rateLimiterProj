package com.example.demo.effective.sync;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class escapeOverSyncTest {

    @Test
    public void test1(){
        God god = new God();
        god.addBeliever(new Believer() {
            @Override
            public void confide() {
                System.out.println("I love my son....");
            }
        });
        god.listenEveryBeliever();
    }

    @Test
    public void test2(){
        God god = new God();

        god.addBeliever(new Believer() {
            @Override
            public void confide() {
                System.out.println("I love my son....");
            }
        });

        //现在出现一个亵神者，放弃信仰了
        god.addBeliever(new Believer() {
            @Override
            public void confide() {
                //god.removeBeliever(this);
                god.getBeliever(this);
            }
        });
        god.listenEveryBeliever();
    }

    @Test
    public void test3(){
        God god = new God();

        god.addBeliever(new Believer() {
            @Override
            public void confide() {
                System.out.println("I love my son....");
            }
        });

        //现在出现一个亵神者，放弃信仰了
        god.addBeliever(new Believer() {
            @Override
            public void confide() {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                try {
                    executor.submit(()->{god.getBeliever(this);}).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }finally {
                    executor.shutdown();
                }
            }
        });
        god.listenEveryBeliever();
    }
}
