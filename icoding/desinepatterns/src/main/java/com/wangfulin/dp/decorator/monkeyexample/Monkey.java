package com.wangfulin.dp.decorator.monkeyexample;

/**
 * @projectName: desinepatterns
 * @description: 具体构件角色
 * @author: Wangfulin
 * @create: 2020-05-16 10:08
 **/
public class Monkey implements TheGreatestSage {
    @Override
    public void move() {
        //代码
        System.out.println("Monkey Move");
    }
}
