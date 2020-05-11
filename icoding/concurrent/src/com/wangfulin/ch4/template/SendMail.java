package com.wangfulin.ch4.template;

/**
 * @projectName: concurrent
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-04-20 13:45
 **/
public class SendMail extends SendCustom{
    @Override
    public void to() {
        System.out.println("Mark");

    }

    @Override
    public void from() {
        System.out.println("Bill");

    }

    @Override
    public void content() {
        System.out.println("Hello world");

    }

    @Override
    public void send() {
        System.out.println("Send mail");

    }

    public static void main(String[] args) {
        // 向上造型
        SendCustom sendC = new SendMail();
        sendC.sendMessage();
    }
}
