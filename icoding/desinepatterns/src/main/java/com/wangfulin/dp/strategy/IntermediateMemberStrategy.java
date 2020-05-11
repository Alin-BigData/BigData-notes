package com.wangfulin.dp.strategy;

/**
 * @projectName: desinepatterns
 * @description: 中级会员
 * @author: Wangfulin
 * @create: 2020-05-11 15:49
 **/
public class IntermediateMemberStrategy implements MemberStrategy {
    @Override
    public double calcPrice(double booksPrice) {
        System.out.println("对于中级会员的折扣为10%");
        return booksPrice * 0.9;
    }
}
