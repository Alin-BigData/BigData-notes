package com.wangfulin.ch1;

import com.wangfulin.tools.SleepTools;

/**
 * @projectName: concurrent
 * @description: start和run方法的区别
 * @author: Wangfulin
 * @create: 2020-04-17 13:13
 **/
public class StartAndRun {
    public static class ThreadRun extends Thread {

        @Override
        public void run() {
            int i = 90;
            while (i > 0) {
                SleepTools.second(2);
                System.out.println("I am " + Thread.currentThread().getName()
                        + " and now the i=" + i--);
            }
        }
    }


    private static class User {
        public void us() {

        }
    }

    public static void main(String[] args) {
        ThreadRun beCalled = new ThreadRun();
        beCalled.setName("BeCalled");
        //beCalled.setPriority(newPriority);
        beCalled.run();

        User user = new User();
        user.us();

        //beCalled.start();
    }
}
