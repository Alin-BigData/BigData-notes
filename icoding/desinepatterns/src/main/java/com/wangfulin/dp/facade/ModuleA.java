package com.wangfulin.dp.facade;

/**
 * @projectName: desinepatterns
 * @description: ModuleA
 * @author: Wangfulin
 * @create: 2020-05-13 22:47
 **/
public class ModuleA {

    public void a1() {
        System.out.println("外部调用a1");
    }

    protected void a2() {
        System.out.println("内部调用a2");
    }

    protected void a3() {
        System.out.println("内部调用a3");
    }
}
