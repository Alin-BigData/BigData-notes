package com.wangfulin.dp.factory.factorymethod;

/**
 * @projectName: desinepatterns
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-12 21:48
 **/
public class RectShapeFactory implements ShapeFactory{
    @Override
    public Shape getShape() {
        return new RectShape();
    }
}
