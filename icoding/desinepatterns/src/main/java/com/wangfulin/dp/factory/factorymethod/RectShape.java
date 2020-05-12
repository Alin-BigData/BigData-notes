package com.wangfulin.dp.factory.factorymethod;


/**
 * @projectName: desinepatterns
 * @description: 正方形
 * @author: Wangfulin
 * @create: 2020-05-12 19:21
 **/
public class RectShape implements Shape {
    public RectShape() {
        System.out.println("RectShape: created");
    }

    @Override
    public void draw() {
        System.out.println("draw: RectShape");
    }
}
