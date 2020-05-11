package com.wangfulin.dp.strategy;

/**
 * @projectName: desinepatterns
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-11 15:51
 **/
public class Price {
    //持有一个具体的策略对象
    private MemberStrategy strategy;

    /**
    * @Name: Price
    * @Description:  传入具体的策略对象
    * @Param: [strategy]
    * @return:
    * @Author: Wangfulin
    * @Date: 2020/5/11
    */
    public Price(MemberStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * 计算图书的价格
     * @param booksPrice    图书的原价
     * @return    计算出打折后的价格
     */
    public double quote(double booksPrice){
        return this.strategy.calcPrice(booksPrice);
    }
}
