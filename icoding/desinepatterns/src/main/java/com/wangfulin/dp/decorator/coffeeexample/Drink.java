package com.wangfulin.dp.decorator.coffeeexample;

/**
 * @projectName: desinepatterns
 * @description: 超类 抽象类
 * 分为两个分支 一个是 咖啡单品 一个是调料
 * @author: Wangfulin
 * @create: 2020-05-16 15:32
 **/
public abstract class Drink {
    public String description = "";
    private float price = 0f;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    // 抽象方法，主体单品只要返回价格 调料的返回要加主体单品价格
    public abstract float cost();
}
