package com.wangfulin.dp.factory.abstractfactory;

/**
 * @projectName: desinepatterns
 * @description: AK类
 * @author: Wangfulin
 * @create: 2020-05-12 23:12
 **/
public class AKM implements Gun {

    @Override
    public void shooting() {
        System.out.println("shooting with AKM");
    }
}
