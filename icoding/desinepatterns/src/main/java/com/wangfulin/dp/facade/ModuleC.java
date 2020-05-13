package com.wangfulin.dp.facade;

/**
 * @projectName: desinepatterns
 * @description: ModuleA
 * @author: Wangfulin
 * @create: 2020-05-13 22:47
 **/
public class ModuleC {
    public void c1() {
        System.out.println("外部调用c1");
    }

    // 仅允许 包内  或子类 调用
    protected void c2() {
        System.out.println("内部调用c2");
    }

    protected void c3() {
        System.out.println("内部调用c3");
    }
}
