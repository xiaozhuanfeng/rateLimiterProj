package com.example.demo.concurrentDemo;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

public class CasTest {

    private static int count = 0;

    @Test
    public void test1() {
        for (int j = 0; j < 2; j++) {
            new Thread(() -> {
                for (int i = 0; i < 10000; i++) {
                    count++;
                }
            }).start();
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //结果必定  count <= 20000
        System.out.println(count);
    }

    @Test
    public void test2() {
        for (int j = 0; j < 2; j++) {
            new Thread(() -> {
                for (int i = 0; i < 10000; i++) {
                    synchronized (this) {
                        count++;
                    }
                }
            }).start();
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //synchronized  类似于悲观锁
        //synchronized关键字会让没有得到锁资源的线程进入BLOCKED状态，而后在争夺到锁资源后恢复为RUNNABLE状态
        //这个过程中涉及到操作系统用户模式和内核模式的转换，代价比较高
        System.out.println(count);
    }

    private static AtomicInteger atoCount = new AtomicInteger(0);

    @Test
    public void test3() {
        for (int j = 0; j < 2; j++) {
            new Thread(() -> {
                for (int i = 0; i < 10000; i++) {
                    atoCount.incrementAndGet();
                }
            }).start();
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Atomic操作类的底层正是用到了“CAS机制”
        System.out.println(atoCount);
    }

    private static AtomicStampedReference<Integer> atoReferenceCount = new AtomicStampedReference<Integer>(0, 0);

    @Test
    public void test4() {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                boolean f = atoReferenceCount.compareAndSet(atoReferenceCount.getReference(),
                        atoReferenceCount.getReference() + 1, atoReferenceCount.getStamp(),
                        atoReferenceCount.getStamp() + 1);

                System.out.println("线程"+Thread.currentThread()+"result="+f);
            }, "线程："+i).start();
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(atoReferenceCount.getReference());
    }
    @Test
    public void test5() {
        for (int i = 0; i < 4; i++) {
            new Thread(() -> {
                for (int j = 0; j < 500; j++) {
                    boolean f = atoReferenceCount.compareAndSet(atoReferenceCount.getReference(),
                            atoReferenceCount.getReference() + 1, atoReferenceCount.getStamp(),
                            atoReferenceCount.getStamp() + 1);

                    System.out.println("线程"+Thread.currentThread()+">>j="+j+",result="+f);
                }
            }, "线程："+i).start();
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(atoReferenceCount.getReference());
    }

    @Test
    public void test6(){
        System.out.println("ABCDEa123abc".hashCode());  // 165374702
        System.out.println("ABCDFB123abc".hashCode()); //  165374702

        Map<String,String> map = new HashMap<String,String>();
        map.put("ABCDEa123abc","123");
        map.put("ABCDFB123abc","321");

        System.out.println(map.get("ABCDEa123abc"));
        System.out.println(map.get("ABCDFB123abc"));

    }



}
