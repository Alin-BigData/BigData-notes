package com.wangfulin.dp.singleton;

/**
 * @projectName: desinepatterns
 * @description: 枚举单例
 * 不仅可以解决线程同步，还可以防止反序列化。
 * 不能反序列化的原因是，枚举类没有构造方法
 * @author: Wangfulin
 * @create: 2020-05-11 08:38
 **/
public enum Mgr08 {

    INSTANCE;

    public void m() {}

    public static void main(String[] args) {
        for(int i=0; i<100; i++) {
            new Thread(()->{
                System.out.println(Mgr08.INSTANCE.hashCode());
            }).start();
        }
    }

}
