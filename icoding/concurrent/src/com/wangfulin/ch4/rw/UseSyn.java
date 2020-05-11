package com.wangfulin.ch4.rw;

import com.wangfulin.tools.SleepTools;

/**
 * @projectName: concurrent
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-04-20 17:17
 **/
public class UseSyn implements GoodsService {

    private GoodsInfo goodsInfo;

    public UseSyn(GoodsInfo goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    @Override
    public synchronized GoodsInfo getNum() {
        SleepTools.ms(5);
        return this.goodsInfo;
    }

    @Override
    public synchronized void setNum(int number) {
        SleepTools.ms(5);
        goodsInfo.changeNumber(number);

    }
}
