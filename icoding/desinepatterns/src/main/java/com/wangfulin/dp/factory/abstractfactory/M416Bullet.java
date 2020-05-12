package com.wangfulin.dp.factory.abstractfactory;

/**
 * @projectName: desinepatterns
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-12 23:15
 **/
public class M416Bullet implements Bullet{
    @Override
    public void load() {
        System.out.println("Load bullets with M416");
    }
}
