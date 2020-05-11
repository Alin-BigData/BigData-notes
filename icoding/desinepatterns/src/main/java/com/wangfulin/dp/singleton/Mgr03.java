package com.wangfulin.dp.singleton;

/**
 * @projectName: desinepatterns
 * @description: lazy loading也称懒汉式
 * 虽然达到了按需初始化的目的，
 * 多个线程访问的时候，带来线程不安全
 * @author: Wangfulin
 * @create: 2020-05-10 22:46
 **/
public class Mgr03 {
    private static Mgr03 INSTANCE;

    private Mgr03() {
    }
    public static Mgr03 getInstance() {
        if (INSTANCE == null) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            INSTANCE = new Mgr03();
        }
        return INSTANCE;
    }
    public void m() {
        System.out.println("m");
    }

    public static void main(String[] args) {
        for(int i=0; i<100; i++) {
            new Thread(()->
                    System.out.println(Mgr03.getInstance().hashCode())
            ).start();
        }
    }
}
