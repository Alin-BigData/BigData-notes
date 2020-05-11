package com.wangfulin.dp.singleton;

/**
 * @projectName: desinepatterns
 * @description: 饿汉模式
 * 类加载到内存后，就实例化一个单例，JVM保证线程安全
 * 简单实用，推荐使用！
 * 唯一缺点：不管用到与否，类装载时就完成实例化
 * @author: Wangfulin
 * @create: 2020-05-10 22:35
 **/
public class Mgr01 {
    private static final Mgr01 INSTANCE = new Mgr01();

    public static Mgr01 getInstance() {
        return INSTANCE;
    }

    public void m() {
        System.out.println("m");
    }

    public static void main(String[] args) {
        Mgr01 m1 = Mgr01.getInstance();
        Mgr01 m2 = Mgr01.getInstance();
        System.out.println(m1 == m2);
    }

}
