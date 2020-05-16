package com.wangfulin.dp.decorator.coffeeexample.decorator;

import com.wangfulin.dp.decorator.coffeeexample.Drink;

/**
 * @projectName: desinepatterns
 * @description: 调料 巧克力
 * @author: Wangfulin
 * @create: 2020-05-16 15:42
 **/
public class Chocolate extends Decorator {
    public Chocolate(Drink obj) {
        super(obj);

        // 设置描述和价格
        super.setDescription("Chocolate");
        super.setPrice(3.0f);
    }
}
