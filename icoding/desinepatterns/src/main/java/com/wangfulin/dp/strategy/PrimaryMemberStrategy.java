package com.wangfulin.dp.strategy;

/**
 * @projectName: desinepatterns
 * @description: 初级会员
 * @author: Wangfulin
 * @create: 2020-05-11 15:47
 **/
public class PrimaryMemberStrategy implements MemberStrategy{
    @Override
    public double calcPrice(double booksPrice) {
        System.out.println("初级会员没有折扣");
        return booksPrice;
    }
}
