package com.wangfulin.dp.strategy;

/**
 * @projectName: desinepatterns
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-11 15:50
 **/
public class AdvancedMemberStrategy implements MemberStrategy {
    @Override
    public double calcPrice(double booksPrice) {
        System.out.println("对于高级会员的折扣为20%");
        return booksPrice * 0.8;
    }
}
