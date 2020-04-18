package com.wangfulin.ch1.syn;

import com.wangfulin.tools.SleepTools;

/**
 * @projectName: concurrent
 * @description: 对象锁和类锁
 * @author: Wangfulin
 * @create: 2020-04-17 14:01
 **/
public class SynClzAndInst {

    //使用类锁的线程
    private static class SynClass extends Thread {
        @Override
        public void run() {
            System.out.println("TestClass is running...");
            synClass();
        }
    }

    //使用对象锁的线程
    private static class InstanceSyn implements Runnable {
        private SynClzAndInst synClzAndInst;

        public InstanceSyn(SynClzAndInst synClzAndInst) {
            this.synClzAndInst = synClzAndInst;
        }

        @Override
        public void run() {
            System.out.println("TestInstance is running..." + synClzAndInst);
            synClzAndInst.instance();
        }
    }

    //使用对象锁的线程
    private static class Instance2Syn implements Runnable {
        private SynClzAndInst synClzAndInst;

        public Instance2Syn(SynClzAndInst synClzAndInst) {
            this.synClzAndInst = synClzAndInst;
        }

        @Override
        public void run() {
            System.out.println("TestInstance2 is running..." + synClzAndInst);
            synClzAndInst.instance2();
        }
    }

    //锁对象
    private synchronized void instance() {
        SleepTools.second(3);
        System.out.println("synInstance is going..." + this.toString());
        SleepTools.second(3);
        System.out.println("synInstance ended " + this.toString());
    }

    //锁对象
    private synchronized void instance2() {
        SleepTools.second(3);
        System.out.println("synInstance2 is going..." + this.toString());
        SleepTools.second(3);
        System.out.println("synInstance2 ended " + this.toString());
    }

    //类锁，实际是锁类的class对象
    private static synchronized void synClass() {
        SleepTools.second(1);
        System.out.println("synClass going...");
        SleepTools.second(1);
        System.out.println("synClass end");
    }

    public static void main(String[] args) {
        // 对象锁测试
        SynClzAndInst synClzAndInst = new SynClzAndInst();
        Thread t1 = new Thread(new InstanceSyn(synClzAndInst));

        //SynClzAndInst synClzAndInst2 = new SynClzAndInst();
        //Thread t2 = new Thread(new Instance2Syn(synClzAndInst));

        t1.start();
        //t2.start();

        // 类锁测试
        SynClass synClass = new SynClass();
        synClass.start();
        SleepTools.second(1);
    }
}
