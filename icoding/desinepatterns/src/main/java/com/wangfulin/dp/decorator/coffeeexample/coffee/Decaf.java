package com.wangfulin.dp.decorator.coffeeexample.coffee;

/**
 * @projectName: desinepatterns
 * @description: Decaf 咖啡
 * @author: Wangfulin
 * @create: 2020-05-16 15:39
 **/
public class Decaf extends Coffee  {
    public Decaf() {
        super.setDescription("Decaf");
        super.setPrice(3.0f);
    }
}
