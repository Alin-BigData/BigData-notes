package com.wangfulin.dp.strategy;

/**
 * @projectName: desinepatterns
 * @description: 抽象折扣类
 * @author: Wangfulin
 * @create: 2020-05-11 15:45
 **/
public interface MemberStrategy {

    public double calcPrice(double booksPrice);
}
