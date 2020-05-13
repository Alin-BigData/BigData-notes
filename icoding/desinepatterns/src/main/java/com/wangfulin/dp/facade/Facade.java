package com.wangfulin.dp.facade;

/**
 * @projectName: desinepatterns
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-13 22:50
 **/
public class Facade {
    private ModuleA moduleA;
    private ModuleB moduleB;
    private ModuleC moduleC;

    public Facade() {
        moduleA = new ModuleA();
        moduleB = new ModuleB();
        moduleC = new ModuleC();
    }

    public void a1() {
        moduleA.a1();
    }

    public void b1() {
        moduleB.b1();
    }

    public void c1() {
        moduleC.c1();
    }
}
