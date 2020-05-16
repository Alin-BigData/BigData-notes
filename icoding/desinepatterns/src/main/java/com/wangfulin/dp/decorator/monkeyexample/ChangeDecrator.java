package com.wangfulin.dp.decorator.monkeyexample;

/**
 * @projectName: desinepatterns
 * @description: 抽象装饰角色
 * @author: Wangfulin
 * @create: 2020-05-16 10:09
 **/
public class ChangeDecrator implements TheGreatestSage {
    // 持有抽象构件(Component)角色
    private TheGreatestSage sage;

    public ChangeDecrator(TheGreatestSage sage) {
        this.sage = sage;
    }

    @Override
    public void move() {
        sage.move();
    }
}
