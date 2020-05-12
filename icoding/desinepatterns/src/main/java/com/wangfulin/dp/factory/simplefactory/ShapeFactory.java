package com.wangfulin.dp.factory.simplefactory;

/**
 * @projectName: desinepatterns
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-12 19:23
 **/
public class ShapeFactory {
    public static Shape getShape(String type) {
        Shape shape = null;
        if (type.equalsIgnoreCase("circle")) {
            shape = new CircleShape();
        } else if (type.equalsIgnoreCase("rect")) {
            shape = new RectShape();
        } else if (type.equalsIgnoreCase("triangle")) {
            shape = new TriangleShape();
        }
        return shape;
    }
}
