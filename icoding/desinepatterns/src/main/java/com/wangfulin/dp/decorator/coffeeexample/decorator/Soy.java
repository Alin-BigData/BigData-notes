package com.wangfulin.dp.decorator.coffeeexample.decorator;

import com.wangfulin.dp.decorator.coffeeexample.Drink;

/**
 * @projectName: desinepatterns
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-16 15:54
 **/
public class Soy extends Decorator {
    public Soy(Drink obj) {
        super(obj);

        super.setDescription("Soy");
        super.setPrice(1.5f);
    }
}
