package com.wangfulin.dp.factory.abstractfactory;

/**
 * @projectName: desinepatterns
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-12 23:18
 **/
public class M416Factory implements Factory{
    @Override
    public Gun produceGun() {
        return new M416();
    }

    @Override
    public Bullet produceBullet() {
        return new M416Bullet();
    }
}
