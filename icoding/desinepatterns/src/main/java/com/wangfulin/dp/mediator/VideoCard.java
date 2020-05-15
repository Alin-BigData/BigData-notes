package com.wangfulin.dp.mediator;

/**
 * @projectName: desinepatterns
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-15 21:23
 **/
public class VideoCard extends Colleague {
    /**
     * 构造函数
     *
     * @param mediator
     */
    public VideoCard(Mediator mediator) {
        super(mediator);
    }

    /**
     * 显示视频数据
     */
    public void showData(String data) {
        System.out.println("您正在观看的是：" + data);
    }
}
