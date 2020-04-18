package com.wangfulin.ch1.syn;

/**
 * @projectName: concurrent
 * @description:
 * @author: Wangfulin
 * @create: 2020-04-17 14:10
 **/
public class SynTest {

    // 并不能保证原子性
    private volatile int age = 100000;//初始100000


    public void setAge() {
        age = age + 20;
    }

    private static class TestThread extends Thread {

        private SynTest synTest;

        public TestThread(SynTest synTest, String name) {
            super(name);
            this.synTest = synTest;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100000; i++) {//递增100000
                synTest.test();
            }
            System.out.println(Thread.currentThread().getName()
                    + " age =  " + synTest.getAge());
        }
    }

    public synchronized void test() {
        age++;
        test2();
    }

    public synchronized void test2() {
        {
            age--;
        }
    }

    public int getAge() {
        return age;
    }


    public static void main(String[] args) throws InterruptedException {
        SynTest synTest = new SynTest();
        Thread endThread = new TestThread(synTest, "endThread");
        endThread.start();
        for (int i = 0; i < 100000; i++) {//递减100000
            synTest.test2();
        }
        System.out.println(Thread.currentThread().getName()
                + " age =  " + synTest.getAge());

    }
}
