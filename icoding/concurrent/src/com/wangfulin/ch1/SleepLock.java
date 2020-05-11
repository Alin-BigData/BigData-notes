package com.wangfulin.ch1;

/**
 * @projectName: concurrent
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-04-17 22:39
 **/
public class SleepLock {
    private Object lock = new Object();

    private class ThreadSleep extends Thread {
        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " will take the lock");
            try {

                synchronized (lock) {
                    System.out.println(threadName + " taking the lock");

                    System.out.println("Finish the work: " + threadName);
                }
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }

        }
    }

    private class ThreadNotSleep extends Thread {

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " will take the lock time=" + System.currentTimeMillis());
            synchronized (lock) {
                System.out.println(threadName + " taking the lock time=" + System.currentTimeMillis());
                System.out.println("Finish the work: " + threadName);
            }
        }
    }


    public static void main(String[] args) {
        // 要让sleep线程先启动
        SleepLock sleepTest = new SleepLock();
        Thread threadA = sleepTest.new ThreadSleep();
        threadA.setName("ThreadSleep");
        Thread threadB = sleepTest.new ThreadNotSleep();
        threadB.setName("ThreadNotSleep");
        threadA.start();
        try {
            Thread.sleep(1000);
            System.out.println(" Main slept!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadB.start();
    }

}
