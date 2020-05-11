package com.wangfulin.dp.strategy;

/**
 * @projectName: desinepatterns
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-11 15:57
 **/
public class Main {
    public static void main(String[] args) {
        //选择并创建需要使用的策略对象
        MemberStrategy strategy = new AdvancedMemberStrategy();

        //创建环境
        Price price = new Price(strategy);
        // 计算价格
        double quote = price.quote(300);
        System.out.println("图书的最终价格为："  + quote);
    }
}
