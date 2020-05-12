package com.wangfulin.dp.factory.simplefactory;

/**
 * @projectName: desinepatterns
 * @description: 根据传入的参数 创建shape
 * @author: Wangfulin
 * @create: 2020-05-12 19:26
 **/
public class Main {
    public static void main(String[] args) {
        // 画圆形

        Shape shape = ShapeFactory.getShape("circle");
        shape.draw();
        // 画正方形

//        Shape shape = ShapeFactory.getShape("rect");
//        shape.draw();
        // 画三角形

//        Shape shape = ShapeFactory.getShape("triangle");
//        shape.draw();
    }
}
