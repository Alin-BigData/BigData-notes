package com.wangfulin.dp.factory.simplefactory;

/**
 * @projectName: desinepatterns
 * @description: 圆
 * @author: Wangfulin
 * @create: 2020-05-12 19:20
 **/
public class CircleShape implements Shape {
    public CircleShape() {
        System.out.println("CircleShape: created");
    }

    @Override
    public void draw() {
        System.out.println("draw: CircleShape");
    }
}
