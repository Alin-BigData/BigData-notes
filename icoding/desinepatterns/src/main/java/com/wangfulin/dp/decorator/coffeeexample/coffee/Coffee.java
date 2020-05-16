package com.wangfulin.dp.decorator.coffeeexample.coffee;

import com.wangfulin.dp.decorator.coffeeexample.Drink;

/**
 * @projectName: desinepatterns
 * @description: 咖啡单品，中间层，只需要返回咖啡单品的价格
 * @author: Wangfulin
 * @create: 2020-05-16 15:37
 **/
public class Coffee extends Drink {
    @Override
    public float cost() {
        return super.getPrice();
    }
}
