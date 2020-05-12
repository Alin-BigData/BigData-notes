package com.wangfulin.dp.factory.abstractfactory;

/**
 * @projectName: desinepatterns
 * @description: AKM 工厂 生产 AKM + 子弹
 * @author: Wangfulin
 * @create: 2020-05-12 23:17
 **/
public class AKMFactory implements Factory {
    @Override
    public Gun produceGun() {
        return new AKM();
    }

    @Override
    public Bullet produceBullet() {
        return new AKMBullet();
    }
}
