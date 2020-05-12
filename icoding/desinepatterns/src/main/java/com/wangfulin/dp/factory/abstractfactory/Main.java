package com.wangfulin.dp.factory.abstractfactory;

/**
 * @projectName: desinepatterns
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-12 23:19
 **/
public class Main {
    public static void main(String[] args) {
        Factory factory;
        Gun gun;
        Bullet bullet;

        // 创建AKM工厂实例
        factory = new AKMFactory();
        // 创建子弹实例
        bullet = factory.produceBullet();
        bullet.load();

        // 创建枪实例
        gun = factory.produceGun();
        gun.shooting();
    }
}
