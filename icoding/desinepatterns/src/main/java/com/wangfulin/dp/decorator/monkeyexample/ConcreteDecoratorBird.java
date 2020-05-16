package com.wangfulin.dp.decorator.monkeyexample;

/**
 * @projectName: desinepatterns
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-16 10:28
 **/
public class ConcreteDecoratorBird extends ChangeDecrator {
    public ConcreteDecoratorBird(TheGreatestSage sage) {
        super(sage);
    }

    @Override
    public void move() {
        // 代码
        super.move();
        System.out.println("Bird Move");
    }


}
