package com.wangfulin.dp.mediator;

/**
 * @projectName: desinepatterns
 * @description: 抽象同事类
 * @author: Wangfulin
 * @create: 2020-05-15 17:22
 **/
public abstract class Colleague {
    // 持有一个调停对象
    private Mediator mediator;

    /**
     * 构造函数
     */
    public Colleague(Mediator mediator) {
        this.mediator = mediator;
    }

    /**
     * 获取当前同事类对应的调停者对象
     */
    public Mediator getMediator() {
        return mediator;
    }


}
