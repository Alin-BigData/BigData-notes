package com.wangfulin.ch1;

import java.util.concurrent.ExecutionException;

/**
 * @projectName: concurrent
 * @description: 守护线程的使用和守护线程中的finally语句块
 * @author: Wangfulin
 * @create: 2020-04-17 13:50
 **/
public class DaemonThread {

    private static class UseThread extends Thread {
        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    System.out.println(Thread.currentThread().getName()
                            + " I am extends Thread.");
                }
                System.out.println(Thread.currentThread().getName()
                        + " interrupt flag is " + isInterrupted());
            } finally {
                System.out.println("...........finally");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException,
            ExecutionException {
        UseThread useThread = new UseThread();
        useThread.setDaemon(true);
        useThread.start();
        Thread.sleep(5);
        //useThread.interrupt();
    }
}
