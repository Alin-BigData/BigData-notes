package com.wangfulin.dp.factory.abstractfactory;

/**
 * @projectName: desinepatterns
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-12 23:16
 **/
public interface Factory {
    // 生产枪
    public Gun produceGun();

    // 生产子弹
    public Bullet produceBullet();
}
