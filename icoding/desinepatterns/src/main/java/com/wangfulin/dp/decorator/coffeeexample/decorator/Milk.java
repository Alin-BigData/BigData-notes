package com.wangfulin.dp.decorator.coffeeexample.decorator;

import com.wangfulin.dp.decorator.coffeeexample.Drink;

/**
 * @projectName: desinepatterns
 * @description: 牛奶 调料
 * @author: Wangfulin
 * @create: 2020-05-16 15:54
 **/
public class Milk extends Decorator {
    public Milk(Drink obj) {
        super(obj);
        super.setDescription("Milk");
        super.setPrice(2.0f);
    }
}
