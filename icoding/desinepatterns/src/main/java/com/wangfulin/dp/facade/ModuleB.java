package com.wangfulin.dp.facade;

/**
 * @projectName: desinepatterns
 * @description: ModuleA
 * @author: Wangfulin
 * @create: 2020-05-13 22:47
 **/
public class ModuleB {
    public void b1() {
        System.out.println("外部调用b1");
    }

    // 仅允许 包内  或子类 调用
    protected void b2() {
        System.out.println("内部调用b2");
    }

    protected void b3() {
        System.out.println("内部调用b3");
    }
}
