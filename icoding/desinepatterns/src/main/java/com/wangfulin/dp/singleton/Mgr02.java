package com.wangfulin.dp.singleton;

/**
 * @projectName: desinepatterns
 * @description: 和Mgr01一个意思
 * @author: Wangfulin
 * @create: 2020-05-10 22:43
 **/
public class Mgr02 {
    private static final Mgr02 INSTANCE;
    static {
        INSTANCE = new Mgr02();
    }

    public Mgr02() {
    }

    public static Mgr02 getInstance() {
        return INSTANCE;
    }

    public void m() {
        System.out.println("m");
    }

    public static void main(String[] args) {
        Mgr02 m1 = Mgr02.getInstance();
        Mgr02 m2 = Mgr02.getInstance();
        System.out.println(m1 == m2);
    }
}
