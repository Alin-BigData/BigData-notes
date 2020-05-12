package com.wangfulin.dp.factory.factorymethod;


/**
 * @projectName: desinepatterns
 * @description: 三角形
 * @author: Wangfulin
 * @create: 2020-05-12 19:22
 **/
public class TriangleShape implements Shape {

    public TriangleShape() {
        System.out.println("TriangleShape: created");
    }

    @Override
    public void draw() {
        System.out.println("draw: TriangleShape");
    }

}
