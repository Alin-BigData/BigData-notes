package com.wangfulin.dp.decorator.coffeeexample.decorator;

import com.wangfulin.dp.decorator.coffeeexample.Drink;

/**
 * @projectName: desinepatterns
 * @description: 调料中间层
 * @author: Wangfulin
 * @create: 2020-05-16 15:42
 **/
public class Decorator extends Drink {
    // 持有超类对象实例 可以是单品 或是被包装过的单品
    private Drink Obj;

    public Decorator(Drink obj) {
        Obj = obj;
    }

    @Override
    public float cost() {
        // 自己的价格 + 被装饰主体的价格
        // 主体可以是单品价格 或是 被装饰过的单品价格
        return super.getPrice() + Obj.cost();
    }

    @Override
    public String getDescription() {
        // 除了要知道自己的价格 还要以
        // 递归的方式知道被装饰的单品的价格
        return super.description + "-" + super.getPrice() + "&&" + Obj.getDescription();
    }


}
