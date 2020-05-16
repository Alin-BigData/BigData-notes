package com.wangfulin.dp.decorator.coffeeexample;

import com.wangfulin.dp.decorator.coffeeexample.coffee.Decaf;
import com.wangfulin.dp.decorator.coffeeexample.coffee.LongBlack;
import com.wangfulin.dp.decorator.coffeeexample.decorator.Chocolate;
import com.wangfulin.dp.decorator.coffeeexample.decorator.Milk;

/**
 * @projectName: desinepatterns
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-16 15:55
 **/
public class CoffeeBar {
    public static void main(String[] args) {
        Drink order;

        order = new Decaf(); // Decaf咖啡
        System.out.println("order1 price:"+order.cost());
        System.out.println("order1 desc:"+order.getDescription());

        System.out.println("****************");
        // 加配料 递归叠加
        order=new LongBlack();
        // 包装 叠加
        order=new Milk(order);
        order=new Chocolate(order);
        System.out.println("order2 price:"+order.cost());
        System.out.println("order2 desc:"+order.getDescription());
    }
}
