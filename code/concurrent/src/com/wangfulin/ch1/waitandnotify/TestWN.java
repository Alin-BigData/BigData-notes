package com.wangfulin.ch1.waitandnotify;

/**
 * @projectName: concurrent
 * @description: 测试wait/notify/notifyAll
 * @author: Wangfulin
 * @create: 2020-04-17 17:24
 **/
public class TestWN {
    private static Express express = new Express(0, Express.CITY);

    /*检查里程数变化的线程,不满足条件，线程一直等待*/
    private static class CheckKm extends Thread {
        @Override
        public void run() {
            express.waitKm();
        }
    }

    /*检查地点变化的线程,不满足条件，线程一直等待*/
    private static class CheckSite extends Thread {
        @Override
        public void run() {
            express.waitSite();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) { //地点变化线程
            new CheckKm().start();
        }

        for(int i=0;i<3;i++){//里程数的变化
            new CheckKm().start();
        }

        Thread.sleep(1000);
        express.changeKm();//快递地点变化
    }
}
