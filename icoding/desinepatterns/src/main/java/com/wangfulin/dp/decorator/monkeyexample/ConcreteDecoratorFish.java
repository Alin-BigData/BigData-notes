package com.wangfulin.dp.decorator.monkeyexample;

/**
 * @projectName: desinepatterns
 * @description: 具体的装饰角色 鱼
 * @author: Wangfulin
 * @create: 2020-05-16 10:25
 **/
public class ConcreteDecoratorFish extends ChangeDecrator {
    public ConcreteDecoratorFish(TheGreatestSage sage) {
        super(sage);
    }

    @Override
    public void move() {
        // 代码
        super.move();
        System.out.println("Fish Move");
    }


}
