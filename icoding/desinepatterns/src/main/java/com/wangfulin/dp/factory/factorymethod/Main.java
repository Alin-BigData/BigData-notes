package com.wangfulin.dp.factory.factorymethod;

/**
 * @projectName: desinepatterns
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-12 21:51
 **/
public class Main {
    public static void main(String[] args) {
        // 根据具体的工厂 创建工厂
        ShapeFactory circleFactory = new CircleShapeFactory();

        // 调用具体工厂的方法创建 具体的对象实例
        Shape circle = circleFactory.getShape();

        // 调用实例中的方法
        circle.draw();
    }
}
