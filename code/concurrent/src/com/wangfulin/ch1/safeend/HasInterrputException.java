package com.wangfulin.ch1.safeend;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @projectName: concurrent
 * @description: 抛出InterruptedException异常的时候，要注意中断标志位
 * @author: Wangfulin
 * @create: 2020-04-17 12:40
 **/
public class HasInterrputException {
    private static SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss_SSS");

    private static class UseThread extends Thread {

        public UseThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            while (!isInterrupted()) {
                try {
                    System.out.println("UseThread:" + formater.format(new Date()));
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    System.out.println(threadName + " catch interrput flag is "
                            + isInterrupted() + " at "
                            + (formater.format(new Date())));
                    // 注意：再interrupt()一次，才会中断
                    // 方法里如果抛出InterruptedException，
                    // 线程的中断标志位会被复位成false，如果确实是需要中断线程，
                    // 要求我们自己在catch语句块里再次调用interrupt()。
                    interrupt();
                    e.printStackTrace();
                }
                System.out.println(threadName);
            }
            System.out.println(threadName + " interrput flag is "
                    + isInterrupted());
        }

        public static void main(String[] args) throws InterruptedException {
            Thread endThread = new UseThread("HasInterrputEx");
            endThread.start();
            System.out.println("Main:" + formater.format(new Date()));
            Thread.sleep(800);
            System.out.println("Main begin interrupt thread:" + formater.format(new Date()));
            endThread.interrupt();
        }
    }
}
