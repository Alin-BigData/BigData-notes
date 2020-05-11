package com.wangfulin.ch4.rw;

/**
 * @projectName: concurrent
 * @description: 商品服务接口
 * @author: Wangfulin
 * @create: 2020-04-20 17:15
 **/
public interface GoodsService {
    public GoodsInfo getNum();//获得商品的信息

    public void setNum(int number);//设置商品的数量
}
