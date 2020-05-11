package com.wangfulin.ch2.tools;

import com.wangfulin.tools.SleepTools;

import java.util.concurrent.CountDownLatch;

/**
 * @projectName: concurrent
 * @description: 演示CountDownLatch，有5个初始化的线程，6个扣除点，
 * *扣除完毕以后，主线程和业务线程才能继续自己的工作
 * 扣减数和线程数可以不一样
 * @author: Wangfulin
 * @create: 2020-04-18 18:40
 **/
public class UseCountDownLatch {
    private static CountDownLatch latch = new CountDownLatch(6);

    //初始化线程(只有一步，有4个)
    private static class InitThread implements Runnable {
        @Override
        public void run() {
            System.out.println("Thread_" + Thread.currentThread().getId()
                    + " ready init work......");
            latch.countDown();//初始化线程完成工作了，countDown方法只扣减一次；
            for (int i = 0; i < 2; i++) {
                System.out.println("Thread_" + Thread.currentThread().getId()
                        + " ........continue do its work");
            }
        }
    }

    //业务线程
    private static class BusiThread implements Runnable {

        @Override
        public void run() {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 3; i++) {
                System.out.println("BusiThread_" + Thread.currentThread().getId()
                        + " do business-----");
            }
        }
    }


    // 只有扣减完成 主线程 和业务线程 才开始工作
    public static void main(String[] args) throws InterruptedException {
        //单独的初始化线程,初始化分为2步，需要扣减两次
        new Thread(new Runnable() {
            @Override
            public void run() {
                SleepTools.ms(1);
                System.out.println("Thread_" + Thread.currentThread().getId()
                        + " ready init work step 1st......");
                latch.countDown();//每完成一步初始化工作，扣减一次
                System.out.println("begin step 2nd.......");
                SleepTools.ms(1);
                System.out.println("Thread_" + Thread.currentThread().getId()
                        + " ready init work step 2nd......");
                latch.countDown();//每完成一步初始化工作，扣减一次
            }
        }).start();
        // 业务线程也调用await()，等待扣减完成
        new Thread(new BusiThread()).start();
        // 启动四个初始化线程  加上前面的两个线程 总共六个。
        for (int i = 0; i <= 3; i++) {
            Thread thread = new Thread(new InitThread());
            thread.start();
        }

        latch.await();
        System.out.println("Main do ites work........");
    }
}
