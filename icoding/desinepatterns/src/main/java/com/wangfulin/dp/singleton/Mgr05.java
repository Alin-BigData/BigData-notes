package com.wangfulin.dp.singleton;

/**
 * @projectName: desinepatterns
 * @description: lazy loading
 * * 也称懒汉式
 * * 虽然达到了按需初始化的目的，但却带来线程不安全的问题
 * * 可以通过synchronized解决，但也带来效率下降
 * @author: Wangfulin
 * @create: 2020-05-10 23:06
 **/
public class Mgr05 {
    private static Mgr05 INSTANCE;

    private Mgr05() {
    }

    public static Mgr05 getInstance() {
        if (INSTANCE == null) {
            //妄图通过减小同步代码块的方式提高效率，然后不可行
            // 例如两个线程都执行到这里，其中一个线程得到锁，实例化之后，
            // 释放锁，另一个线程得到锁
            synchronized (Mgr05.class) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                INSTANCE = new Mgr05();
            }
        }
        return INSTANCE;
    }

    public void m() {
        System.out.println("m");
    }

    public static void main(String[] args) {
        for(int i=0; i<100; i++) {
            new Thread(()->{
                System.out.println(Mgr05.getInstance().hashCode());
            }).start();
        }
    }
}
