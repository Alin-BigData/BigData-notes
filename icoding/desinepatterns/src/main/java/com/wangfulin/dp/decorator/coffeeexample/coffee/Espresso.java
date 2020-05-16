package com.wangfulin.dp.decorator.coffeeexample.coffee;

/**
 * @projectName: desinepatterns
 * @description: Espresso 咖啡
 * @author: Wangfulin
 * @create: 2020-05-16 15:39
 **/
public class Espresso extends Coffee {
    public Espresso() {
        super.setDescription("Espresso");
        super.setPrice(4.0f);
    }
}
