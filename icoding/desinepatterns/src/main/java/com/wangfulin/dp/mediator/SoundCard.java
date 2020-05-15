package com.wangfulin.dp.mediator;

/**
 * @projectName: desinepatterns
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-15 21:23
 **/
public class SoundCard extends Colleague {
    /**
     * 构造函数
     *
     * @param mediator
     */
    public SoundCard(Mediator mediator) {
        super(mediator);
    }

    /**
     * 按照声频数据发出声音
     */
    public void soundData(String data) {
        System.out.println("画外音：" + data);
    }


}
