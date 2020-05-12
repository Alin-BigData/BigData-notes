package com.wangfulin.dp.factory.factorymethod;

/**
 * @projectName: desinepatterns
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-12 21:50
 **/
public class TriangleShapeFactory implements ShapeFactory {
    @Override
    public Shape getShape() {
        return new TriangleShape();
    }
}
