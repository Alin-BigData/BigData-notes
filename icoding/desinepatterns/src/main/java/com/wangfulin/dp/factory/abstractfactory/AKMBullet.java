package com.wangfulin.dp.factory.abstractfactory;

/**
 * @projectName: desinepatterns
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-12 23:14
 **/
public class AKMBullet implements Bullet{
    @Override
    public void load() {
        System.out.println("Load bullets with AKM");
    }
}
