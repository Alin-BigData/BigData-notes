package com.wangfulin.ch4.template;

import java.util.Date;

/**
 * @projectName: concurrent
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-04-20 13:39
 **/
public abstract class SendCustom {

    public abstract void to();

    public abstract void from();

    public abstract void content();

    public void date() {
        System.out.println(new Date());
    }

    public abstract void send();

    // 框架方法
    public void sendMessage() {
        to();
        from();
        content();
        send();
    }
}
