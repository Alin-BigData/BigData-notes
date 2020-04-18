package com.wangfulin.ch1.safeend;

/**
 * @projectName: concurrent
 * @description: 如何安全的中断线程
 * @author: Wangfulin
 * @create: 2020-04-17 12:40
 **/
public class EndThread {
    private static class UseThread extends Thread{
        public UseThread(String name) {
            super(name);
        }

        public void run() {
            String threadName = Thread.currentThread().getName();
            // 只有在自己的线程中处理中断，才有用
            while(!isInterrupted()) {
                System.out.println(threadName+" is run!");
            }
            System.out.println("interrupt flag is " + isInterrupted());
        }

        public static void main(String[] args) throws InterruptedException {
            Thread endThread = new UseThread("endThread");
            endThread.start();
            Thread.sleep(1);

            endThread.interrupt();
        }

    }
}
