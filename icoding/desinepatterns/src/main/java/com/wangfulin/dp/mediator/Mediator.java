package com.wangfulin.dp.mediator;

/**
 * @projectName: desinepatterns
 * @description: 抽象调停者类
 * @author: Wangfulin
 * @create: 2020-05-15 17:20
 **/
public interface Mediator {
    /**
     * 同事对象在自身改变的时候来通知调停者方法
     * 让调停者去负责相应的与其他同事对象的交互
     */
    public void changed(Colleague c);
}
