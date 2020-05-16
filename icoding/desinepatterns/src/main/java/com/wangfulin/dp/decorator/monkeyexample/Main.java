package com.wangfulin.dp.decorator.monkeyexample;

/**
 * @projectName: desinepatterns
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-16 10:29
 **/
public class Main {
    public static void main(String[] args) {
        TheGreatestSage sage = new Monkey();

        /// 第一种写法
        TheGreatestSage bird = new ConcreteDecoratorBird(sage);
        TheGreatestSage fish = new ConcreteDecoratorFish(bird);

        // 第二种写法
        //TheGreatestSage fish = new Fish(new Bird(sage));

        fish.move();
        bird.move();
    }
}
